// ===============================================
//  CARRITO URBANSNEAKERS
//  - estado del carrito
//  - localStorage
//  - badge del header
//  - modal del carrito
//  - toasts
// ===============================================

// ===============================================
// CONFIGURACIÓN DEL BACKEND
// ===============================================

// Dirección del servidor Spring Boot.
// En producción únicamente habrá que cambiar esta constante.
const API_URL = "http://localhost:8080";


// ===============================================
// ESTADO DEL CARRITO
// ===============================================

// Productos añadidos al carrito.
let carrito = [];

// Totales calculados automáticamente.
let totalProductos = 0;
let totalPrecio = 0;

// ===============================================
// SISTEMA DE TOASTS
// ===============================================

// Tiempo que permanece visible un aviso.
const TOAST_DURATION_MS = 2000;



// Crea (si no existe) y devuelve el contenedor
function getToastContainer() {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        container.setAttribute('aria-live', 'polite');
        container.setAttribute('aria-atomic', 'true');
        document.body.appendChild(container);
    }
    return container;
}

// Muestra un toast con mensaje y variante (success|error)
function mostrarToast(mensaje, variante = 'success') {
    const container = getToastContainer();
    const toast = document.createElement('div');
    toast.className = `toast ${variante}`;
    toast.innerHTML = `<span class="icon">✅</span> ${mensaje}`;

    container.appendChild(toast);

    setTimeout(() => {
        toast.remove();
    }, TOAST_DURATION_MS + 400);
}


// ===============================================
// SINCRONIZACIÓN CON EL BACKEND
// ===============================================

// Obtiene el carrito almacenado en Spring Boot.
//
// Spring devuelve únicamente los datos necesarios:
// idProducto, talla, color y cantidad.
//
// Después completamos cada elemento utilizando
// la información oficial del catálogo.
async function cargarCarritoDesdeBackend() {
    try {
        // Esperamos a que catalogo.js termine de cargar los productos.
        await catalogoPromise;

        const respuesta = await fetch(`${API_URL}/carrito`);

        if (!respuesta.ok) {
            throw new Error("No se pudo obtener el carrito del servidor.");
        }

        const itemsBackend = await respuesta.json();

        carrito = itemsBackend
            .map(itemBackend => {
                const producto = catalogo.find(
                    prod => prod.id === itemBackend.idProducto
                );

                // Si el producto ya no existe en el catálogo,
                // no se muestra en el carrito.
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
                    cantidad: itemBackend.cantidad
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


// Actualiza todos los elementos visuales que dependen
// del estado actual del carrito.
function actualizarInterfazCarrito() {
    recalcularTotales();
    actualizarBadge();

    // Solo intentamos renderizar el modal si sus elementos existen.
    if (listaCarrito && totalCarritoEl) {
        renderizarCarritoModal();
    }
}


function recalcularTotales() {
    totalProductos = carrito.reduce(
        (acumulado, item) => acumulado + item.cantidad,
        0
    );

    totalPrecio = carrito.reduce(
        (acumulado, item) =>
            acumulado + item.precio * item.cantidad,
        0
    );
}

// ===============================================
// ACTUALIZACIÓN DEL HEADER
// ===============================================

function actualizarBadge() {
    const badge = document.getElementById('badge');
    if (!badge) return;
    const unidades = carrito.reduce((acc, item) => acc + item.cantidad, 0);
    badge.textContent = unidades;
}

// ===============================================
// GESTIÓN DEL CARRITO
// ===============================================

// Envía a Spring el producto seleccionado.
// Después vuelve a pedir el carrito completo para
// actualizar la interfaz con el estado real del servidor.
async function agregarAlCarrito(idProducto) {
    const producto = catalogo.find(
        prod => prod.id === idProducto
    );

    if (!producto) {
        mostrarToast("El producto no existe.", "error");
        return;
    }

    const selectTalla = document.getElementById(
        `talla-${idProducto}`
    );

    const selectColor = document.getElementById(
        `color-${idProducto}`
    );

    const talla = Number(selectTalla?.value);
    const color = selectColor?.value;

    if (!talla || !color) {
        mostrarToast(
            "Selecciona una talla y un color.",
            "error"
        );
        return;
    }

    try {
        const respuesta = await fetch(`${API_URL}/carrito`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                idProducto: producto.id,
                talla,
                color,
                cantidad: 1
            })
        });

        if (!respuesta.ok) {
            throw new Error(
                "El servidor no pudo añadir el producto."
            );
        }

        // Volvemos a leer el carrito desde Spring.
        await cargarCarritoDesdeBackend();

        mostrarToast(
            `🛒 Añadido: ${producto.nombre}`,
            "success"
        );

    } catch (error) {
        console.error(
            "Error añadiendo el producto:",
            error
        );

        mostrarToast(
            "No se pudo añadir el producto.",
            "error"
        );
    }
}


// ===============================================
// ELEMENTOS DEL MODAL
// ===============================================

const btnCarrito = document.getElementById('btn-carrito');
const modalCarrito = document.getElementById('modal-carrito');
const modalOverlay = modalCarrito.querySelector('.modal-overlay');
const btnCerrarCarrito = document.getElementById('btn-cerrar-carrito');
const listaCarrito = document.getElementById('carrito-items');
const totalCarritoEl = document.getElementById('carrito-total');
const btnVaciar = document.getElementById('btn-vaciar');
const btnPagar = document.getElementById('btn-pagar'); // llama a abrirCheckout()


// ===============================================
// FUNCIONES DEL MODAL
// ===============================================

function abrirModalCarrito() {
    renderizarCarritoModal();
    modalCarrito.classList.add('modal-visible');
}

function cerrarModalCarrito() {
    modalCarrito.classList.remove('modal-visible');
}

function renderizarCarritoModal() {
    if (!carrito.length) {
        listaCarrito.innerHTML = `
            <div style="padding:18px; text-align:center; color:#666;">
                Tu carrito está vacío.
            </div>`;
        totalCarritoEl.textContent = '0,00 €';
        return;
    }

    listaCarrito.innerHTML = '';

    carrito.forEach(item => {
        const importe = (item.precio * item.cantidad).toFixed(2).replace('.', ',');
        const precioU = item.precio.toFixed(2).replace('.', ',');
        const srcImg = item.color ? obtenerSrcImagenProducto(item.id, item.color) : '';

        const fila = document.createElement('div');
        fila.className = 'item-carrito';

        fila.innerHTML = `
            <div class="thumb-zapa">
                ${srcImg
                ? `<img class="thumb-img" src="${srcImg}" alt="${item.marca} ${item.nombre}">`
                : `Foto`}
            </div>

            <div class="info-zapa">
                <h4>${item.marca} — ${item.nombre}</h4>
                <div class="meta">Talla: ${item.talla ?? '-'}</div>
                <div class="meta">Color: ${item.color ?? '-'}</div>
                <div class="meta">Precio: ${precioU} €</div>
                <div class="meta">Cantidad: ${item.cantidad}</div>
            </div>

            <div class="importe">${importe} €</div>

            <div class="acciones-item">
                <button class="btn-quitar" data-clave="${item.clave}">Quitar producto</button>
            </div>
        `;

        const img = fila.querySelector('.thumb-img');
        if (img) {
            img.onerror = () => {
                const fallback = `img/p${item.id}_default.png`;
                if (!img.dataset.fallbackTried) {
                    img.dataset.fallbackTried = '1';
                    img.src = fallback;
                } else {
                    img.style.display = 'none';
                }
            };
        }

        listaCarrito.appendChild(fila);
    });

    const total = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
    totalCarritoEl.textContent = `${total.toFixed(2).replace('.', ',')} €`;
}


// ===============================================
// EVENTOS
// ===============================================

if (btnCarrito) {
    btnCarrito.addEventListener('click', (e) => {
        e.preventDefault();
        abrirModalCarrito();
    });
}

if (btnCerrarCarrito) {
    btnCerrarCarrito.addEventListener('click', cerrarModalCarrito);
}

if (modalOverlay) {
    modalOverlay.addEventListener('click', cerrarModalCarrito);
}

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') cerrarModalCarrito();
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

      const respuesta = await fetch(`${API_URL}/carrito?${parametros}`, {
        method: "DELETE",
      });

      if (!respuesta.ok) {
        throw new Error("El servidor no pudo eliminar el producto.");
      }

      // Spring elimina y después volvemos a leer su estado.
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
      const respuesta = await fetch(`${API_URL}/carrito/todo`, {
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
    btnPagar.addEventListener('click', (e) => {
        e.preventDefault();

        // Si el carrito está vacío o el total de unidades es 0, mostramos aviso
        if (!carrito.length || totalProductos === 0) {
            if (typeof mostrarToast === 'function') {
                mostrarToast('No puedes hacer un pedido con 0 productos.', 'error');
            } else {
                alert('No puedes hacer un pedido con 0 productos.');
            }
            return; // no abrimos el checkout
        }

        // Si hay productos, continuamos al checkout
        if (typeof abrirCheckout === 'function') {
            abrirCheckout();
        }
    });
}


// ===============================================
// INICIALIZACIÓN
// ===============================================

// Al abrir la tienda, el estado inicial del carrito
// se obtiene siempre desde Spring Boot.
cargarCarritoDesdeBackend();
