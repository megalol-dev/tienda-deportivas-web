// Gestiona el estado y la interfaz del carrito.
let carrito = [];

let totalProductos = 0;
let totalPrecio = 0;

const TOAST_DURATION_MS = 3000;

// Obtiene o crea el contenedor de avisos.
function getToastContainer() {
  let container = document.getElementById("toast-container");
  if (!container) {
    container = document.createElement("div");
    container.id = "toast-container";
    container.setAttribute("aria-live", "polite");
    container.setAttribute("aria-atomic", "true");
    document.body.appendChild(container);
  }
  return container;
}

// Muestra una notificación temporal.
function mostrarToast(mensaje, variante = "success") {
  const container = getToastContainer();
  const toast = document.createElement("div");
  toast.className = `toast ${variante}`;
  toast.textContent = mensaje;
  container.appendChild(toast);
  setTimeout(() => {
    toast.remove();
  }, TOAST_DURATION_MS + 300);
}

// Sincroniza el carrito con el backend.
async function cargarCarritoDesdeBackend() {
  try {

    await catalogoPromise;

    const respuesta = await fetch(`${API_URL}/carrito`, {
      method: "GET",
      credentials: "include",
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo obtener el carrito del servidor.");
    }

    const itemsBackend = await respuesta.json();

    carrito = itemsBackend
      .map((itemBackend) => {
        const producto = catalogo.find(
          (prod) => prod.id === itemBackend.idProducto,
        );

        if (!producto) {
          return null;
        }

        return {
          clave: `${producto.id}-${itemBackend.talla}-${itemBackend.color}`,
          id: producto.id,
          marca: producto.marca,
          nombre: producto.nombre,
          precio: producto.precio,
          talla: itemBackend.talla,
          color: itemBackend.color,
          cantidad: itemBackend.cantidad,
        };
      })
      .filter(Boolean);

    actualizarInterfazCarrito();

    return carrito;
  } catch (error) {
    console.error("Error cargando el carrito:", error);

    carrito = [];
    actualizarInterfazCarrito();

    return [];
  }
}

// Actualiza las vistas dependientes del carrito.
function actualizarInterfazCarrito() {
  recalcularTotales();
  actualizarBadge();

  if (listaCarrito && totalCarritoEl) {
    renderizarCarritoModal();
  }
}

// Limpia el carrito visible del navegador.
function limpiarCarritoFrontend() {
  carrito = [];

  totalProductos = 0;
  totalPrecio = 0;

  actualizarInterfazCarrito();

  if (typeof cerrarModalCarrito === "function") {
    cerrarModalCarrito();
  }
}

// Recalcula unidades e importe del carrito.
function recalcularTotales() {
  totalProductos = carrito.reduce(
    (acumulado, item) => acumulado + item.cantidad,
    0,
  );

  totalPrecio = carrito.reduce(
    (acumulado, item) => acumulado + item.precio * item.cantidad,
    0,
  );
}

// Actualiza el contador del carrito.
function actualizarBadge() {
  const badge = document.getElementById("badge");
  if (!badge) return;
  const unidades = carrito.reduce((acc, item) => acc + item.cantidad, 0);
  badge.textContent = unidades;
}

const modalSesionCarrito = document.getElementById("modal-sesion-carrito");

const btnCancelarModalSesion = document.getElementById(
  "btn-cancelar-modal-sesion",
);

// Muestra el aviso de sesión necesaria.
function abrirModalSesionCarrito() {
  if (!modalSesionCarrito) return;

  modalSesionCarrito.hidden = false;

  document.body.classList.add("modal-sesion-abierto");
}

// Oculta el aviso de sesión necesaria.
function cerrarModalSesionCarrito() {
  if (!modalSesionCarrito) return;

  modalSesionCarrito.hidden = true;

  document.body.classList.remove("modal-sesion-abierto");
}

if (btnCancelarModalSesion) {
  btnCancelarModalSesion.addEventListener("click", cerrarModalSesionCarrito);
}

document.addEventListener("keydown", (event) => {
  if (
    event.key === "Escape" &&
    modalSesionCarrito &&
    !modalSesionCarrito.hidden
  ) {
    cerrarModalSesionCarrito();
  }
});

// Añade una variante al carrito del cliente.
async function agregarAlCarrito(idProducto) {

  const sesionActiva = await comprobarSesionCarrito();

  if (!sesionActiva) {

    limpiarCarritoFrontend();

    abrirModalSesionCarrito();

    return;
  }

  const producto = catalogo.find((prod) => prod.id === idProducto);

  if (!producto) {
    mostrarToast("El producto no existe.", "error");

    return;
  }

  const selectTalla = document.getElementById(`talla-${idProducto}`);

  const selectColor = document.getElementById(`color-${idProducto}`);

  const talla = Number(selectTalla?.value);
  const color = selectColor?.value;

  if (!talla || !color) {
    mostrarToast("Selecciona una talla y un color.", "error");

    return;
  }

  try {
    const respuesta = await fetchConCsrf(`${API_URL}/carrito`, {
      method: "POST",

      headers: {
        "Content-Type": "application/json",
      },

      body: JSON.stringify({
        idProducto: producto.id,
        talla: talla,
        color: color,
        cantidad: 1,
      }),
    });

    if (!respuesta.ok) {
      throw new Error("El servidor no pudo añadir el producto.");
    }

    await cargarCarritoDesdeBackend();

    mostrarToast(`🛒 Añadido: ${producto.nombre}`, "success");
  } catch (error) {
    console.error("Error añadiendo el producto:", error);

    mostrarToast("No se pudo añadir el producto.", "error");
  }
}

const btnCarrito = document.getElementById("btn-carrito");
const modalCarrito = document.getElementById("modal-carrito");
const modalOverlay = modalCarrito.querySelector(".modal-overlay");
const btnCerrarCarrito = document.getElementById("btn-cerrar-carrito");
const listaCarrito = document.getElementById("carrito-items");
const totalCarritoEl = document.getElementById("carrito-total");
const btnVaciar = document.getElementById("btn-vaciar");
const btnPagar = document.getElementById("btn-pagar"); // Abre el checkout.

// Abre el modal del carrito.
function abrirModalCarrito() {
  renderizarCarritoModal();
  modalCarrito.classList.add("modal-visible");
}

// Cierra el modal del carrito.
function cerrarModalCarrito() {
  modalCarrito.classList.remove("modal-visible");
}

// Renderiza las líneas del carrito.
function renderizarCarritoModal() {
  if (!carrito.length) {
    listaCarrito.innerHTML = `
            <div style="padding:18px; text-align:center; color:#666;">
                Tu carrito está vacío.
            </div>`;
    totalCarritoEl.textContent = "0,00 €";
    return;
  }

  listaCarrito.innerHTML = "";

  carrito.forEach((item) => {
    const importe = (item.precio * item.cantidad).toFixed(2).replace(".", ",");
    const precioU = item.precio.toFixed(2).replace(".", ",");
    const srcImg = item.color
      ? obtenerSrcImagenProducto(item.id, item.color)
      : "";

    const fila = document.createElement("div");
    fila.className = "item-carrito";

    fila.innerHTML = `
            <div class="thumb-zapa">
                ${
                  srcImg
                    ? `<img class="thumb-img" src="${srcImg}" alt="${item.marca} ${item.nombre}">`
                    : `Foto`
                }
            </div>

            <div class="info-zapa">
                <h4>${item.marca} — ${item.nombre}</h4>
                <div class="meta">Talla: ${item.talla ?? "-"}</div>
                <div class="meta">Color: ${item.color ?? "-"}</div>
                <div class="meta">Precio: ${precioU} €</div>
                <div class="meta">Cantidad: ${item.cantidad}</div>
            </div>

            <div class="importe">${importe} €</div>

            <div class="acciones-item">
                <button class="btn-quitar" data-clave="${item.clave}">Quitar producto</button>
            </div>
        `;

    const img = fila.querySelector(".thumb-img");
    if (img) {
      img.onerror = () => {
        const fallback = `img/p${item.id}_default.png`;
        if (!img.dataset.fallbackTried) {
          img.dataset.fallbackTried = "1";
          img.src = fallback;
        } else {
          img.style.display = "none";
        }
      };
    }

    listaCarrito.appendChild(fila);
  });

  const total = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
  totalCarritoEl.textContent = `${total.toFixed(2).replace(".", ",")} €`;
}

// Comprueba si hay un cliente autenticado.
async function comprobarSesionCarrito() {
  try {
    const respuesta = await fetch(`${API_URL}/auth/me`, {
      method: "GET",
      credentials: "include",
    });

    return respuesta.ok;
  } catch (error) {
    console.error("Error comprobando la sesión del carrito:", error);

    return false;
  }
}

if (btnCarrito) {
  btnCarrito.addEventListener("click", async (event) => {
    event.preventDefault();

    const sesionActiva = await comprobarSesionCarrito();

    if (!sesionActiva) {

      limpiarCarritoFrontend();

      abrirModalSesionCarrito();

      return;
    }

    await cargarCarritoDesdeBackend();

    abrirModalCarrito();
  });
}

if (btnCerrarCarrito) {
  btnCerrarCarrito.addEventListener("click", cerrarModalCarrito);
}

if (modalOverlay) {
  modalOverlay.addEventListener("click", cerrarModalCarrito);
}

document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") cerrarModalCarrito();
});

if (listaCarrito) {
  listaCarrito.addEventListener("click", async (event) => {
    const boton = event.target.closest(".btn-quitar");

    if (!boton) return;

    const clave = boton.dataset.clave;

    const item = carrito.find((producto) => producto.clave === clave);

    if (!item) return;

    try {
      const parametros = new URLSearchParams({
        idProducto: item.id,
        talla: item.talla,
        color: item.color,
      });

      const respuesta = await fetchConCsrf(`${API_URL}/carrito?${parametros}`, {
        method: "DELETE",
      });

      if (!respuesta.ok) {
        throw new Error("El servidor no pudo eliminar el producto.");
      }

      await cargarCarritoDesdeBackend();

      mostrarToast("Producto eliminado del carrito.", "success");
    } catch (error) {
      console.error("Error eliminando el producto:", error);

      mostrarToast("No se pudo eliminar el producto.", "error");
    }
  });
}

if (btnVaciar) {
  btnVaciar.addEventListener("click", async () => {
    try {
      const respuesta = await fetchConCsrf(`${API_URL}/carrito/todo`, {
        method: "DELETE",
      });

      if (!respuesta.ok) {
        throw new Error("El servidor no pudo vaciar el carrito.");
      }

      await cargarCarritoDesdeBackend();

      mostrarToast("Carrito vaciado correctamente.", "success");
    } catch (error) {
      console.error("Error vaciando el carrito:", error);

      mostrarToast("No se pudo vaciar el carrito.", "error");
    }
  });
}

if (btnPagar) {
  btnPagar.addEventListener("click", (e) => {
    e.preventDefault();

    if (!carrito.length || totalProductos === 0) {
      if (typeof mostrarToast === "function") {
        mostrarToast("No puedes hacer un pedido con 0 productos.", "error");
      } else {
        alert("No puedes hacer un pedido con 0 productos.");
      }
      return; // Mantiene cerrado el checkout.
    }

    if (typeof abrirCheckout === "function") {
      abrirCheckout();
    }
  });
}

// Carga el carrito al iniciar la página.
async function inicializarCarrito() {
  const sesionActiva = await comprobarSesionCarrito();

  if (!sesionActiva) {
    limpiarCarritoFrontend();

    return;
  }

  await cargarCarritoDesdeBackend();
}

inicializarCarrito();
