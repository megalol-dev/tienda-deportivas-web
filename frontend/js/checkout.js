// Gestiona el checkout y el regreso desde Stripe.
const sectionCheckout = document.getElementById("checkout");
const checkoutContainer = document.querySelector(".checkout-container");
const resumenLineas = document.getElementById("resumen-lineas");
const resumenSubtotalEl = document.getElementById("resumen-subtotal");
const resumenEnvioEl = document.getElementById("resumen-envio");
const resumenIvaEl = document.getElementById("resumen-iva");
const resumenTotalEl = document.getElementById("resumen-total");
const formularioCheckout = document.getElementById("formulario-checkout");
const btnCancelarCheckout = document.getElementById("btn-cancelar-checkout");
const confirmacionSection = document.getElementById("checkout-confirmacion");
const confirmIdEl = document.getElementById("confirm-id");
const btnConfirmVolver = document.getElementById("confirm-volver");

// Oculta el catálogo durante el checkout.
function bloquearNavegacionCheckout() {

  document.querySelectorAll(".seccion-deportivas").forEach((sec) => {
    sec.classList.add("oculto");
  });

  const vistaMarca = document.getElementById("vista-marca");
  if (vistaMarca) vistaMarca.classList.add("oculto");

  document.querySelectorAll("header a").forEach((a) => {
    a.style.pointerEvents = "none";
    a.style.opacity = "0.4";
  });
}

// Restaura el catálogo tras el checkout.
function desbloquearNavegacionCheckout() {

  document.querySelectorAll(".seccion-deportivas").forEach((sec) => {
    sec.classList.remove("oculto");
  });

  document.querySelectorAll("header a").forEach((a) => {
    a.style.pointerEvents = "auto";
    a.style.opacity = "1";
  });
}

// Abre el formulario y el resumen de compra.
function abrirCheckout() {
  bloquearNavegacionCheckout();

  try {
    if (typeof modalCarrito !== "undefined") {
      modalCarrito.classList.remove("modal-visible");
    }
  } catch (e) {
    console.warn(
      "No se pudo cerrar el modal del carrito al abrir checkout:",
      e,
    );
  }

  if (checkoutContainer) {
    checkoutContainer.classList.remove("oculto");
    checkoutContainer.style.display = ""; // Garantiza que sea visible.
  }

  if (confirmacionSection) {
    confirmacionSection.classList.add("oculto");
  }

    renderizarResumenPedido();
    cargarResumenBackend();

  if (sectionCheckout) {
    sectionCheckout.classList.remove("oculto");
    sectionCheckout.scrollIntoView({ behavior: "smooth", block: "start" });
  }
}

// Cierra y reinicia la vista del checkout.
function cerrarCheckout() {
  if (sectionCheckout) {
    sectionCheckout.classList.add("oculto");
  }
  if (checkoutContainer) {
    checkoutContainer.classList.remove("oculto");
    checkoutContainer.style.display = ""; // Restaura su visualización.
  }
  if (confirmacionSection) {
    confirmacionSection.classList.add("oculto");
  }
}

// Renderiza los productos del resumen.
function renderizarResumenPedido() {
  if (
    !resumenLineas ||
    !resumenSubtotalEl ||
    !resumenEnvioEl ||
    !resumenIvaEl ||
    !resumenTotalEl
  )
    return;

  resumenLineas.innerHTML = "";

  if (!carrito.length) {
    resumenLineas.innerHTML = `
            <div style="padding:18px; color:#666; text-align:center;">
                Tu carrito está vacío.
            </div>`;
    resumenSubtotalEl.textContent = "0,00 €";
    resumenEnvioEl.textContent = "0,00 €";
    resumenIvaEl.textContent = "0,00 €";
    resumenTotalEl.textContent = "0,00 €";
    return;
  }

  carrito.forEach((item) => {
    const importe = (item.precio * item.cantidad).toFixed(2).replace(".", ",");
    const precioU = item.precio.toFixed(2).replace(".", ",");
    const srcImg = item.color
      ? obtenerSrcImagenProducto(item.id, item.color)
      : "";

    const div = document.createElement("div");
    div.className = "linea-resumen";
    div.innerHTML = `
            <div class="thumb-zapa">
                ${
                  srcImg
                    ? `<img class="thumb-img" src="${srcImg}" alt="${item.marca} ${item.nombre}">`
                    : `Foto`
                }
            </div>
            <div class="info">
                <strong>${item.marca} — ${item.nombre}</strong>
                <small>Talla: ${item.talla ?? "-"}</small>
                <small>Color: ${item.color ?? "-"}</small>
                <small>Precio: ${precioU} € · Cantidad: ${item.cantidad}</small>
            </div>
            <div class="importe">${importe} €</div>
        `;

    const img = div.querySelector(".thumb-img");
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

    resumenLineas.appendChild(div);
  });
}

// Carga los importes calculados por el backend.
async function cargarResumenBackend() {
  try {
    const respuesta = await fetch(`${API_URL}/pedido/resumen`, {
      credentials: "include",
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo obtener el resumen.");
    }

    const resumen = await respuesta.json();

    resumenSubtotalEl.textContent = `${resumen.subtotal.toFixed(2).replace(".", ",")} €`;

    resumenEnvioEl.textContent = `${resumen.envio.toFixed(2).replace(".", ",")} €`;

    resumenIvaEl.textContent = `${resumen.iva.toFixed(2).replace(".", ",")} €`;

    resumenTotalEl.textContent = `${resumen.total.toFixed(2).replace(".", ",")} €`;
  } catch (error) {
    console.error(error);
  }
}

// Valida los datos de envío del checkout.
function validarFormularioCheckout() {
  if (!formularioCheckout) return false;

  // Muestra el error de un campo y lo enfoca.
  function mostrarErrorCampo(input, mensaje) {
    if (typeof mostrarToast === "function") {
      mostrarToast(mensaje, "error");
    }
    if (input && typeof input.focus === "function") {
      input.focus();
      try {
        input.scrollIntoView({ behavior: "smooth", block: "center" });
      } catch (e) {}
    }
    return false;
  }

  const nombre = document.getElementById("nombre-c");
  const apellidos = document.getElementById("apellidos-c");
  const email = document.getElementById("email-c");
  const telefono = document.getElementById("telefono-c");
  const dir1 = document.getElementById("direccion1-c");
  const ciudad = document.getElementById("ciudad-c");
  const provincia = document.getElementById("provincia-c");
  const cpInput = document.getElementById("cp-c");
  const paisSelect = document.getElementById("pais-c");
  const acepto = document.getElementById("acepto-terminos");

  if (!nombre || !nombre.value.trim()) {
    return mostrarErrorCampo(nombre, 'El campo "Nombre" es obligatorio.');
  }

  if (!apellidos || !apellidos.value.trim()) {
    return mostrarErrorCampo(apellidos, 'El campo "Apellidos" es obligatorio.');
  }

  const emailVal = email ? email.value.trim() : "";
  if (!emailVal) {
    return mostrarErrorCampo(
      email,
      'El campo "Correo electrónico" es obligatorio.',
    );
  }
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(emailVal)) {
    return mostrarErrorCampo(
      email,
      'Introduce un correo electrónico válido (debe contener "@" y un dominio).',
    );
  }

  const telVal = telefono ? telefono.value.trim() : "";
  if (!telVal) {
    return mostrarErrorCampo(telefono, 'El campo "Teléfono" es obligatorio.');
  }
  const soloDigitosTel = telVal.replace(/\D/g, "");
  if (soloDigitosTel.length < 9) {
    return mostrarErrorCampo(
      telefono,
      "El teléfono debe tener al menos 9 dígitos numéricos.",
    );
  }

  if (!dir1 || !dir1.value.trim()) {
    return mostrarErrorCampo(dir1, 'El campo "Dirección" es obligatorio.');
  }

  if (!ciudad || !ciudad.value.trim()) {
    return mostrarErrorCampo(ciudad, 'El campo "Ciudad" es obligatorio.');
  }

  if (!provincia || !provincia.value.trim()) {
    return mostrarErrorCampo(provincia, 'El campo "Provincia" es obligatorio.');
  }

  const cp = cpInput ? cpInput.value.trim() : "";
  if (!cp) {
    return mostrarErrorCampo(
      cpInput,
      'El campo "Código postal" es obligatorio.',
    );
  }
  if (!/^\d{5}$/.test(cp)) {
    return mostrarErrorCampo(
      cpInput,
      "El código postal debe tener exactamente 5 dígitos numéricos.",
    );
  }

  if (!paisSelect || !paisSelect.value) {
    return mostrarErrorCampo(paisSelect, "Selecciona un país para el envío.");
  }

  if (!acepto || !acepto.checked) {
    return mostrarErrorCampo(
      acepto,
      "Debes aceptar los Términos y Condiciones antes de continuar.",
    );
  }

  return true;
}

// Solicita la URL de pago de un pedido.
async function crearCheckoutStripe(idPedido) {

  const respuesta = await fetchConCsrf(
    `${API_URL}/api/stripe/checkout/pedido?idPedido=${encodeURIComponent(idPedido)}`,
    {
      method: "POST",
    },
  );

  if (respuesta.status === 401 || respuesta.status === 403) {
    throw new Error(
      "Debes iniciar sesión como cliente para realizar el pago.",
    );
  }

  if (!respuesta.ok) {
    throw new Error(
      "No se pudo crear la sesión de pago con Stripe.",
    );
  }

  const datos = await respuesta.json();

  if (!datos.url) {
    throw new Error(
      "Stripe no devolvió una URL de pago.",
    );
  }

  return datos.url;
}

if (formularioCheckout) {
  formularioCheckout.addEventListener("submit", async (e) => {
    e.preventDefault();
    if (!validarFormularioCheckout()) return;

    const pedidoRequest = {
      nombre: document.getElementById("nombre-c")?.value.trim(),

      apellidos: document.getElementById("apellidos-c")?.value.trim(),

      email: document.getElementById("email-c")?.value.trim(),

      telefono: document.getElementById("telefono-c")?.value.trim(),

      direccion: document.getElementById("direccion1-c")?.value.trim(),

      ciudad: document.getElementById("ciudad-c")?.value.trim(),

      provincia: document.getElementById("provincia-c")?.value.trim(),

      cp: document.getElementById("cp-c")?.value.trim(),

      pais: document.getElementById("pais-c")?.value,

      aceptaTerminos: document.getElementById("acepto-terminos")?.checked,
    };

    try {

      const respuesta = await fetchConCsrf(`${API_URL}/pedido`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(pedidoRequest),
      });

      if (respuesta.status === 401) {
        mostrarToast(
          "Debes iniciar sesión para poder realizar una compra.",
          "error",
        );
        return;
      }

      if (!respuesta.ok) {
        throw new Error("No se pudo crear el pedido.");
      }

      const resumen = await respuesta.json();

      const urlStripe = await crearCheckoutStripe(resumen.idPedido);

      console.log("Pedido creado:", resumen.idPedido);

      console.log("URL Stripe creada:", urlStripe);

      window.location.href = urlStripe;

    } catch (error) {
      console.error("Error al crear el pedido:", error);

      mostrarToast("No se pudo realizar el pedido.", "error");
    }
  });
}

if (btnCancelarCheckout) {
  btnCancelarCheckout.addEventListener("click", () => {
    cerrarCheckout();
    desbloquearNavegacionCheckout();

    if (typeof volverInicio === "function") {
      volverInicio();
    }
  });
}

if (btnConfirmVolver) {
  btnConfirmVolver.addEventListener("click", () => {
    if (confirmacionSection) {
      confirmacionSection.classList.add("oculto");
    }

    cerrarCheckout();
    desbloquearNavegacionCheckout();

    if (typeof volverInicio === "function") {
      volverInicio();
    }
  });
}

document.addEventListener("DOMContentLoaded", async () => {

  const parametros = new URLSearchParams(window.location.search);

  const estadoPago = parametros.get("pago");
  const idPedido = parametros.get("idPedido");

  if (!estadoPago) {
    return;
  }

  if (estadoPago === "exito") {
    bloquearNavegacionCheckout();

    if (checkoutContainer) {
      checkoutContainer.classList.add("oculto");
      checkoutContainer.style.display = "none";
    }

    if (sectionCheckout) {
      sectionCheckout.classList.remove("oculto");

      sectionCheckout.scrollIntoView({
        behavior: "smooth",
        block: "start",
      });
    }

    if (confirmacionSection) {
      confirmacionSection.classList.remove("oculto");
    }

    if (confirmIdEl && idPedido) {
      confirmIdEl.textContent = idPedido;
    }

    if (typeof cargarCarritoDesdeBackend === "function") {
      await cargarCarritoDesdeBackend();
    }

    if (typeof renderizarCarrito === "function") {
      renderizarCarrito();
    }

    if (typeof mostrarToast === "function") {
      mostrarToast(
        `Pago realizado correctamente${idPedido ? ": " + idPedido : ""}`,
        "success",
      );
    }
  }

  if (estadoPago === "cancelado") {

    if (typeof mostrarToast === "function") {
      mostrarToast(
        "El pago ha sido cancelado. Tu pedido no ha sido cobrado.",
        "error",
      );
    }
  }

  window.history.replaceState(
    {},
    document.title,
    window.location.pathname,
  );
});

