// Gestiona pedidos, productos y empleados del panel.
let usuarioActual = null;

document.addEventListener("DOMContentLoaded", comprobarAccesoPanel);
window.addEventListener("focus", comprobarAccesoPanel);
document.addEventListener(
  "DOMContentLoaded",
  inicializarDesplazamientoEmpleados,
);

const btnLogout = document.getElementById("btn-logout-panel");
const btnGestionPedidos = document.getElementById("btn-gestion-pedidos");
const btnGestionProductos = document.getElementById("btn-gestion-productos");
const btnGestionUsuarios = document.getElementById("btn-gestion-usuarios");
const seccionPedidos = document.getElementById("seccion-pedidos");
const seccionProductos = document.getElementById("seccion-productos");
const seccionUsuarios = document.getElementById("seccion-usuarios");
const btnMiCuenta = document.getElementById("btn-mi-cuenta");
const seccionMiCuenta = document.getElementById("seccion-mi-cuenta");
const formMiCuenta = document.getElementById("form-mi-cuenta");
const miCuentaNombre = document.getElementById("mi-cuenta-nombre");
const miCuentaEmail = document.getElementById("mi-cuenta-email");
const miCuentaRol = document.getElementById("mi-cuenta-rol");
const errorMiCuentaNombre = document.getElementById("error-mi-cuenta-nombre");
const errorMiCuentaEmail = document.getElementById("error-mi-cuenta-email");
const miCuentaPasswordEmailContenedor = document.getElementById(
  "mi-cuenta-password-email-contenedor",
);
const miCuentaPasswordActual = document.getElementById(
  "mi-cuenta-password-actual",
);
const errorMiCuentaPasswordActual = document.getElementById(
  "error-mi-cuenta-password-actual",
);
const camposPasswordEmpleado = document.getElementById(
  "campos-password-empleado",
);
const avisoOrientacionSalida = document.getElementById(
  "aviso-orientacion-salida",
);
const btnCambiarMiPassword = document.getElementById("btn-cambiar-mi-password");
const modalMiPassword = document.getElementById("modal-mi-password");
const inputMiPasswordActual = document.getElementById("mi-password-actual");
const inputMiPasswordNueva = document.getElementById("mi-password-nueva");
const inputMiPasswordConfirmar = document.getElementById(
  "mi-password-confirmar",
);
const errorMiPasswordActual = document.getElementById(
  "error-mi-password-actual",
);
const errorMiPasswordNueva = document.getElementById("error-mi-password-nueva");
const errorMiPasswordConfirmar = document.getElementById(
  "error-mi-password-confirmar",
);
const btnGuardarMiPassword = document.getElementById("btn-guardar-mi-password");
const btnCancelarMiPassword = document.getElementById(
  "btn-cancelar-mi-password",
);

if (btnCambiarMiPassword) {
  btnCambiarMiPassword.addEventListener("click", abrirModalMiPassword);
}

if (btnCancelarMiPassword) {
  btnCancelarMiPassword.addEventListener("click", cerrarModalMiPassword);
}

if (btnGuardarMiPassword) {
  btnGuardarMiPassword.addEventListener("click", actualizarMiPassword);
}

if (formMiCuenta) {
  formMiCuenta.addEventListener("submit", guardarMiCuenta);
}

if (miCuentaEmail) {
  miCuentaEmail.addEventListener("input", () => {
    if (!usuarioActual || !miCuentaPasswordEmailContenedor) {
      return;
    }

    const emailIntroducido = miCuentaEmail.value.trim().toLowerCase();

    const emailActual = usuarioActual.email.trim().toLowerCase();

    const emailCambiado = emailIntroducido !== emailActual;

    miCuentaPasswordEmailContenedor.hidden = !emailCambiado;

    if (!emailCambiado && miCuentaPasswordActual) {
      miCuentaPasswordActual.value = "";
      miCuentaPasswordActual.classList.remove("campo-invalido");

      if (errorMiCuentaPasswordActual) {
        errorMiCuentaPasswordActual.textContent = "";
      }
    }
  });
}

function abrirModalMiPassword() {
  limpiarFormularioMiPassword();

  if (modalMiPassword) {
    modalMiPassword.hidden = false;
  }

  inputMiPasswordActual?.focus();
}

function cerrarModalMiPassword() {
  if (modalMiPassword) {
    modalMiPassword.hidden = true;
  }

  limpiarFormularioMiPassword();
}

function limpiarFormularioMiPassword() {
  if (inputMiPasswordActual) {
    inputMiPasswordActual.value = "";
    inputMiPasswordActual.classList.remove("campo-invalido");
  }

  if (inputMiPasswordNueva) {
    inputMiPasswordNueva.value = "";
    inputMiPasswordNueva.classList.remove("campo-invalido");
  }

  if (inputMiPasswordConfirmar) {
    inputMiPasswordConfirmar.value = "";
    inputMiPasswordConfirmar.classList.remove("campo-invalido");
  }

  if (errorMiPasswordActual) {
    errorMiPasswordActual.textContent = "";
  }

  if (errorMiPasswordNueva) {
    errorMiPasswordNueva.textContent = "";
  }

  if (errorMiPasswordConfirmar) {
    errorMiPasswordConfirmar.textContent = "";
  }
}

async function actualizarMiPassword() {
  const passwordActual = inputMiPasswordActual?.value || "";
  const password = inputMiPasswordNueva?.value || "";
  const confirmarPassword = inputMiPasswordConfirmar?.value || "";

  limpiarErroresMiPassword();

  if (passwordActual.length === 0) {
    mostrarErrorMiPassword(
      inputMiPasswordActual,
      errorMiPasswordActual,
      "Debes introducir tu contraseña actual.",
    );

    return;
  }

  if (password.length < 8 || password.length > 72) {
    mostrarErrorMiPassword(
      inputMiPasswordNueva,
      errorMiPasswordNueva,
      "La contraseña debe tener entre 8 y 72 caracteres.",
    );

    return;
  }

  if (!/\p{L}/u.test(password) || !/\d/.test(password)) {
    mostrarErrorMiPassword(
      inputMiPasswordNueva,
      errorMiPasswordNueva,
      "La contraseña debe contener al menos una letra y un número.",
    );

    return;
  }

  if (password !== confirmarPassword) {
    mostrarErrorMiPassword(
      inputMiPasswordConfirmar,
      errorMiPasswordConfirmar,
      "Las contraseñas no coinciden.",
    );

    return;
  }

  try {
    const respuesta = await fetchConCsrf(`${API_URL}/admin/mi-password`, {
      method: "PUT",

      headers: {
        "Content-Type": "application/json",
      },

      body: JSON.stringify({
        passwordActual: passwordActual,
        password: password,
        confirmarPassword: confirmarPassword,
      }),
    });

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar la contraseña.";

      try {
        const error = await respuesta.json();

        if (error.error) {
          mensaje = error.error;
        } else if (error.message) {
          mensaje = error.message;
        }
      } catch {}

      if (mensaje === "La contraseña actual no es correcta.") {
        mostrarErrorMiPassword(
          inputMiPasswordActual,
          errorMiPasswordActual,
          mensaje,
        );
      } else {
        mostrarErrorMiPassword(
          inputMiPasswordNueva,
          errorMiPasswordNueva,
          mensaje,
        );
      }

      return;
    }

    await cerrarSesionPanel();
  } catch (error) {
    console.error("Error actualizando contraseña:", error);

    mostrarModalPanel("Error", "No se ha podido actualizar la contraseña.");
  }
}

function mostrarErrorMiPassword(input, elementoError, mensaje) {
  if (input) {
    input.classList.add("campo-invalido");
  }

  if (elementoError) {
    elementoError.textContent = mensaje;
  }
}

function limpiarErroresMiPassword() {
  inputMiPasswordActual?.classList.remove("campo-invalido");
  inputMiPasswordNueva?.classList.remove("campo-invalido");
  inputMiPasswordConfirmar?.classList.remove("campo-invalido");

  if (errorMiPasswordActual) {
    errorMiPasswordActual.textContent = "";
  }

  if (errorMiPasswordNueva) {
    errorMiPasswordNueva.textContent = "";
  }

  if (errorMiPasswordConfirmar) {
    errorMiPasswordConfirmar.textContent = "";
  }
}

// Comprueba la sesión y adapta el panel al rol devuelto por el backend.
async function comprobarAccesoPanel() {
  try {
    const respuesta = await fetch(`${API_URL}/auth/me`, {
      method: "GET",
      credentials: "include",
    });

    if (!respuesta.ok) {
      window.location.href = "../usuario/login.html";

      return;
    }

    const usuario = await respuesta.json();
    usuarioActual = usuario;

    const rolesPermitidos = ["TRABAJADOR", "JEFE", "ADMIN"];

    if (!rolesPermitidos.includes(usuario.rol)) {
      window.location.href = "../tienda.html";

      return;
    }

    document.body.classList.add("panel-gestion-autorizado");

    const nombrePanel = document.getElementById("nombre-panel");
    const rolPanel = document.getElementById("rol-panel");

    if (nombrePanel) {
      nombrePanel.textContent = usuario.nombre;
    }

    if (rolPanel) {
      rolPanel.textContent = usuario.rol;
    }

    if (btnGestionUsuarios) {
      if (usuario.rol === "JEFE" || usuario.rol === "ADMIN") {
        btnGestionUsuarios.classList.remove("oculto");
      } else {
        btnGestionUsuarios.classList.add("oculto");
      }
    }

    const contenedorNuevoEmpleado = document.getElementById(
      "contenedor-nuevo-empleado",
    );

    if (contenedorNuevoEmpleado) {
      if (usuario.rol === "JEFE" || usuario.rol === "ADMIN") {
        contenedorNuevoEmpleado.classList.remove("oculto");
      } else {
        contenedorNuevoEmpleado.classList.add("oculto");
      }
    }

    console.log("Acceso al panel:", usuario);
  } catch (error) {
    console.error("Error comprobando acceso al panel:", error);

    window.location.href = "../usuario/login.html";
  }
}

// Comprueba si una respuesta indica que la sesión ya no es válida.
async function comprobarSesionExpirada(respuesta) {
  if (respuesta.status !== 401 && respuesta.status !== 403) {
    return false;
  }

  try {
    const comprobacion = await fetch(`${API_URL}/auth/me`, {
      method: "GET",
      credentials: "include",
    });

    if (comprobacion.ok) {
      return false;
    }
  } catch (error) {
    console.error("Error comprobando la sesión:", error);
  }

  window.location.href = "../usuario/login.html";

  return true;
}

if (btnLogout) {
  btnLogout.addEventListener("click", cerrarSesionPanel);
}

// Cierra la sesión desde el panel.
async function cerrarSesionPanel() {
  try {
    const respuesta = await fetchConCsrf(`${API_URL}/auth/logout`, {
      method: "POST",
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo cerrar la sesión.");
    }

    prepararSalidaResponsivePanel();
  } catch (error) {
    console.error("Error cerrando sesión:", error);
  }
}

// Espera a que el móvil vuelva a vertical antes de salir del panel.
function prepararSalidaResponsivePanel() {
  const esMovilHorizontal = window.matchMedia(
    "(max-width: 1024px) and (max-height: 600px) and (orientation: landscape)",
  ).matches;

  if (!esMovilHorizontal || !avisoOrientacionSalida) {
    window.location.href = "../usuario/login.html";

    return;
  }

  avisoOrientacionSalida.classList.add("activo");

  const redirigirAlVolverAVertical = () => {
    if (!window.matchMedia("(orientation: portrait)").matches) {
      return;
    }

    window.removeEventListener("resize", redirigirAlVolverAVertical);
    window.removeEventListener("orientationchange", redirigirAlVolverAVertical);

    window.location.href = "../usuario/login.html";
  };

  window.addEventListener("resize", redirigirAlVolverAVertical);
  window.addEventListener("orientationchange", redirigirAlVolverAVertical);
}

const modalPanel = document.getElementById("modal-panel");

const modalPanelTitulo = document.getElementById("modal-panel-titulo");

const modalPanelMensaje = document.getElementById("modal-panel-mensaje");

const btnCerrarModalPanel = document.getElementById("btn-cerrar-modal-panel");

// Muestra un mensaje en el panel.
function mostrarModalPanel(titulo, mensaje) {
  if (!modalPanel || !modalPanelTitulo || !modalPanelMensaje) {
    return;
  }

  modalPanelTitulo.textContent = titulo;

  modalPanelMensaje.textContent = mensaje;

  modalPanel.classList.remove("oculto");
}

// Cierra el mensaje del panel.
function cerrarModalPanel() {
  if (!modalPanel) {
    return;
  }

  modalPanel.classList.add("oculto");
}

if (btnCerrarModalPanel) {
  btnCerrarModalPanel.addEventListener("click", cerrarModalPanel);
}

const tablaPedidosBody = document.getElementById("tabla-pedidos-body");

if (btnGestionPedidos) {
  btnGestionPedidos.addEventListener("click", mostrarGestionPedidos);
}

if (btnMiCuenta) {
  btnMiCuenta.addEventListener("click", mostrarMiCuenta);
}

// Muestra los datos de la cuenta del usuario autenticado.
function mostrarMiCuenta() {
  if (!seccionMiCuenta || !usuarioActual) {
    return;
  }

  if (seccionPedidos) {
    seccionPedidos.classList.add("oculto");
  }

  if (seccionProductos) {
    seccionProductos.classList.add("oculto");
  }

  if (seccionUsuarios) {
    seccionUsuarios.classList.add("oculto");
  }

  seccionMiCuenta.classList.remove("oculto");

  if (miCuentaNombre) {
    miCuentaNombre.value = usuarioActual.nombre || "";
  }

  if (miCuentaEmail) {
    miCuentaEmail.value = usuarioActual.email || "";
  }

  if (miCuentaRol) {
    miCuentaRol.value = usuarioActual.rol || "";
  }

  seccionMiCuenta.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// Guarda el nuevo nombre del usuario autenticado.
// Guarda los datos personales del usuario autenticado.
async function guardarMiCuenta(event) {
  event.preventDefault();

  if (!miCuentaNombre || !miCuentaEmail || !usuarioActual) {
    return;
  }

  const nombre = miCuentaNombre.value.trim();
  const email = miCuentaEmail.value.trim().toLowerCase();
  const passwordActual = miCuentaPasswordActual?.value || "";

  limpiarErroresMiCuenta();

  if (nombre.length < 2 || nombre.length > 100) {
    mostrarErrorMiCuenta(
      miCuentaNombre,
      errorMiCuentaNombre,
      "El nombre debe tener entre 2 y 100 caracteres.",
    );

    return;
  }

  if (!/^[\p{L}][\p{L} .'-]*$/u.test(nombre)) {
    mostrarErrorMiCuenta(
      miCuentaNombre,
      errorMiCuentaNombre,
      "El nombre contiene caracteres no válidos.",
    );

    return;
  }

  if (email.length === 0 || email.length > 150) {
    mostrarErrorMiCuenta(
      miCuentaEmail,
      errorMiCuentaEmail,
      "El email debe tener entre 1 y 150 caracteres.",
    );

    return;
  }

  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(email)) {
    mostrarErrorMiCuenta(
      miCuentaEmail,
      errorMiCuentaEmail,
      "Introduce un correo electrónico válido.",
    );

    return;
  }

  const nombreCambiado = nombre !== usuarioActual.nombre;

  const emailCambiado = email !== usuarioActual.email.toLowerCase();

  if (emailCambiado && passwordActual.length === 0) {
    mostrarErrorMiCuenta(
      miCuentaPasswordActual,
      errorMiCuentaPasswordActual,
      "Debes introducir tu contraseña actual para cambiar el email.",
    );

    return;
  }

  if (!nombreCambiado && !emailCambiado) {
    mostrarModalPanel("Sin cambios", "No has modificado ningún dato.");

    return;
  }

  try {
    if (nombreCambiado) {
      const respuestaNombre = await fetchConCsrf(
        `${API_URL}/admin/mi-cuenta/nombre`,
        {
          method: "PUT",

          headers: {
            "Content-Type": "application/json",
          },

          body: JSON.stringify({
            nombre: nombre,
          }),
        },
      );

      if (await comprobarSesionExpirada(respuestaNombre)) {
        return;
      }

      if (!respuestaNombre.ok) {
        let mensaje = "No se ha podido actualizar el nombre.";

        try {
          const error = await respuestaNombre.json();

          if (error.error) {
            mensaje = error.error;
          } else if (error.message) {
            mensaje = error.message;
          }
        } catch {}

        mostrarErrorMiCuenta(miCuentaNombre, errorMiCuentaNombre, mensaje);

        return;
      }

      const usuarioActualizado = await respuestaNombre.json();

      usuarioActual.nombre = usuarioActualizado.nombre;

      miCuentaNombre.value = usuarioActualizado.nombre;

      const nombrePanel = document.getElementById("nombre-panel");

      if (nombrePanel) {
        nombrePanel.textContent = usuarioActualizado.nombre;
      }
    }

    if (emailCambiado) {
      const respuestaEmail = await fetchConCsrf(
        `${API_URL}/admin/mi-cuenta/email`,
        {
          method: "PUT",

          headers: {
            "Content-Type": "application/json",
          },

          body: JSON.stringify({
            email: email,
            passwordActual: passwordActual,
          }),
        },
      );

      if (!respuestaEmail.ok) {
        let mensaje = "No se ha podido actualizar el email.";

        try {
          const error = await respuestaEmail.json();

          if (error.error) {
            mensaje = error.error;
          } else if (error.message) {
            mensaje = error.message;
          }
        } catch {}

        if (mensaje === "La contraseña actual no es correcta.") {
          mostrarErrorMiCuenta(
            miCuentaPasswordActual,
            errorMiCuentaPasswordActual,
            mensaje,
          );
        } else {
          mostrarErrorMiCuenta(miCuentaEmail, errorMiCuentaEmail, mensaje);
        }

        return;
      }

      // El backend invalida la sesión y el frontend conduce al nuevo login.
      prepararSalidaResponsivePanel();

      return;
    }

    mostrarModalPanel(
      "Datos actualizados",
      "Tus datos se han actualizado correctamente.",
    );
  } catch (error) {
    console.error("Error actualizando los datos de la cuenta:", error);

    mostrarModalPanel("Error", "No se han podido actualizar tus datos.");
  }
}

// Muestra un error de validación de Mi cuenta.
function mostrarErrorMiCuenta(input, elementoError, mensaje) {
  if (input) {
    input.classList.add("campo-invalido");
  }

  if (elementoError) {
    elementoError.textContent = mensaje;
  }
}

// Limpia los errores del formulario Mi cuenta.
function limpiarErroresMiCuenta() {
  miCuentaNombre?.classList.remove("campo-invalido");
  miCuentaEmail?.classList.remove("campo-invalido");
  miCuentaPasswordActual?.classList.remove("campo-invalido");

  if (errorMiCuentaNombre) {
    errorMiCuentaNombre.textContent = "";
  }

  if (errorMiCuentaEmail) {
    errorMiCuentaEmail.textContent = "";
  }

  if (errorMiCuentaPasswordActual) {
    errorMiCuentaPasswordActual.textContent = "";
  }
}

// Muestra la gestión de pedidos.
async function mostrarGestionPedidos() {
  if (!seccionPedidos) {
    return;
  }

  if (seccionProductos) {
    seccionProductos.classList.add("oculto");
  }

  if (seccionUsuarios) {
    seccionUsuarios.classList.add("oculto");
  }

  if (seccionMiCuenta) {
    seccionMiCuenta.classList.add("oculto");
  }

  seccionPedidos.classList.remove("oculto");

  await cargarPedidos();

  seccionPedidos.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// Carga los pedidos disponibles.
async function cargarPedidos() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/pedidos`, {
      method: "GET",
      credentials: "include",
    });

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudieron cargar los pedidos.");
    }

    const pedidos = await respuesta.json();

    console.log("Pedidos recibidos:", pedidos);

    renderizarPedidos(pedidos);
  } catch (error) {
    console.error("Error cargando pedidos:", error);
  }
}

// Renderiza los pedidos con nodos DOM para no interpretar datos dinámicos como HTML.
function renderizarPedidos(pedidos) {
  if (!tablaPedidosBody) {
    return;
  }

  tablaPedidosBody.replaceChildren();

  if (!Array.isArray(pedidos) || pedidos.length === 0) {
    const fila = document.createElement("tr");

    const celda = document.createElement("td");

    celda.colSpan = 6;
    celda.textContent = "No existen pedidos.";

    fila.appendChild(celda);

    tablaPedidosBody.appendChild(fila);

    return;
  }

  pedidos.forEach((pedido) => {
    const fila = document.createElement("tr");

    const fecha = pedido.fechaPedido
      ? new Date(pedido.fechaPedido).toLocaleString("es-ES")
      : "---";

    const total = Number(pedido.total || 0).toFixed(2);

    const celdaIdPedido = document.createElement("td");
    celdaIdPedido.textContent = pedido.idPedido;

    const celdaCliente = document.createElement("td");
    celdaCliente.textContent =
      `${pedido.nombre || ""} ${pedido.apellidos || ""}`.trim();

    const celdaFecha = document.createElement("td");
    celdaFecha.textContent = fecha;

    const celdaTotal = document.createElement("td");
    celdaTotal.textContent = `${total} €`;

    const celdaEstado = document.createElement("td");

    const selectEstado = document.createElement("select");

    selectEstado.id = `estado-pedido-${pedido.id}`;

    const tieneTransiciones = crearOpcionesEstado(selectEstado, pedido.estado);

    selectEstado.disabled = !tieneTransiciones;

    celdaEstado.appendChild(selectEstado);

    const celdaAcciones = document.createElement("td");

    const botonGuardar = document.createElement("button");

    botonGuardar.type = "button";
    botonGuardar.className = "btn-guardar-estado";
    botonGuardar.dataset.id = String(pedido.id);
    botonGuardar.dataset.version = String(pedido.version);
    botonGuardar.textContent = "Guardar";
    botonGuardar.disabled = !tieneTransiciones;

    celdaAcciones.appendChild(botonGuardar);

    fila.appendChild(celdaIdPedido);
    fila.appendChild(celdaCliente);
    fila.appendChild(celdaFecha);
    fila.appendChild(celdaTotal);
    fila.appendChild(celdaEstado);
    fila.appendChild(celdaAcciones);

    tablaPedidosBody.appendChild(fila);
  });
}

// Crea las transiciones de estado permitidas.
function crearOpcionesEstado(select, estadoActual) {
  const transicionesPermitidas = {
    PENDIENTE: [],
    PREPARANDO: ["ENVIADO"],
    ENVIADO: ["ENTREGADO", "DEVUELTO_A_TIENDA"],
    DEVUELTO_A_TIENDA: ["PREPARANDO"],
    ENTREGADO: [],
  };

  const nombresEstado = {
    PENDIENTE: "PENDIENTE",
    PREPARANDO: "PREPARANDO",
    ENVIADO: "ENVIADO",
    ENTREGADO: "ENTREGADO",
    DEVUELTO_A_TIENDA: "DEVUELTO A TIENDA",
  };

  const estadosDisponibles = [
    estadoActual,
    ...(transicionesPermitidas[estadoActual] || []),
  ];

  estadosDisponibles.forEach((estado) => {
    const opcion = document.createElement("option");

    opcion.value = estado;
    opcion.textContent = nombresEstado[estado] || estado;
    opcion.selected = estado === estadoActual;

    select.appendChild(opcion);
  });

  return estadosDisponibles.length > 1;
}

if (tablaPedidosBody) {
  tablaPedidosBody.addEventListener("click", async (event) => {
    const boton = event.target.closest(".btn-guardar-estado");

    if (!boton) {
      return;
    }

    const pedidoId = boton.dataset.id;
    const version = Number(boton.dataset.version);

    const select = document.getElementById(`estado-pedido-${pedidoId}`);

    if (!select) {
      return;
    }

    await cambiarEstadoPedido(pedidoId, select.value, version);
  });
}

// Guarda el nuevo estado de un pedido.
async function cambiarEstadoPedido(pedidoId, nuevoEstado, version) {
  try {
    const respuesta = await fetchConCsrf(
      `${API_URL}/admin/pedidos/${pedidoId}/estado`,
      {
        method: "PATCH",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify({
          estado: nuevoEstado,
          version,
        }),
      },
    );

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      if (respuesta.status === 409) {
        mostrarModalPanel(
          "Pedido actualizado por otro usuario",
          "Otro trabajador ha modificado este pedido antes que tú. Se recargarán los datos actualizados.",
        );
      } else {
        mostrarModalPanel(
          "Cambio no permitido",
          "No se ha podido realizar el cambio de estado.",
        );
      }

      await cargarPedidos();
      return;
    }

    const pedidoActualizado = await respuesta.json();

    console.log("Pedido actualizado:", pedidoActualizado);

    await cargarPedidos();

    mostrarModalPanel(
      "Estado actualizado",
      "El estado del pedido se ha modificado correctamente.",
    );
  } catch (error) {
    console.error("Error modificando pedido:", error);
  }
}

const tablaProductosBody = document.getElementById("tabla-productos-body");

const btnNuevoProducto = document.getElementById("btn-nuevo-producto");

const formularioProductoContenedor = document.getElementById(
  "formulario-producto-contenedor",
);

const btnCancelarProducto = document.getElementById("btn-cancelar-producto");

const formProducto = document.getElementById("form-producto");

const productoId = document.getElementById("producto-id");

const productoMarca = document.getElementById("producto-marca");

const productoNombre = document.getElementById("producto-nombre");

const productoPrecio = document.getElementById("producto-precio");

const productoTallas = document.getElementById("producto-tallas");

const productoColores = document.getElementById("producto-colores");

const productoActivo = document.getElementById("producto-activo");

const errorProductoMarca = document.getElementById("error-producto-marca");

const errorProductoNombre = document.getElementById("error-producto-nombre");

const errorProductoPrecio = document.getElementById("error-producto-precio");

const errorProductoTallas = document.getElementById("error-producto-tallas");

const errorProductoColores = document.getElementById("error-producto-colores");

const tituloFormularioProducto = document.getElementById(
  "titulo-formulario-producto",
);

let productosCargados = [];

if (btnGestionProductos) {
  btnGestionProductos.addEventListener("click", mostrarGestionProductos);
}

// Muestra la gestión de productos.
async function mostrarGestionProductos() {
  if (!seccionProductos) {
    return;
  }

  if (seccionPedidos) {
    seccionPedidos.classList.add("oculto");
  }

  if (seccionUsuarios) {
    seccionUsuarios.classList.add("oculto");
  }

  if (seccionMiCuenta) {
    seccionMiCuenta.classList.add("oculto");
  }

  seccionProductos.classList.remove("oculto");

  await cargarProductos();

  seccionProductos.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// Carga los productos del panel.
async function cargarProductos() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/productos`, {
      method: "GET",
      credentials: "include",
    });

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudieron cargar los productos.");
    }

    const productos = await respuesta.json();

    productosCargados = productos;

    console.log("Productos recibidos:", productos);

    renderizarProductos(productos);
  } catch (error) {
    console.error("Error cargando productos:", error);
  }
}

// Renderiza los productos con textContent y propiedades DOM seguras.
function renderizarProductos(productos) {
  if (!tablaProductosBody) {
    return;
  }

  tablaProductosBody.replaceChildren();

  if (!Array.isArray(productos) || productos.length === 0) {
    const fila = document.createElement("tr");

    const celda = document.createElement("td");

    celda.colSpan = 8;
    celda.textContent = "No existen productos.";

    fila.appendChild(celda);

    tablaProductosBody.appendChild(fila);

    return;
  }

  productos.forEach((producto) => {
    const fila = document.createElement("tr");

    const tallas = Array.isArray(producto.tallas)
      ? producto.tallas.join(", ")
      : "---";

    const colores = Array.isArray(producto.colores)
      ? producto.colores.join(", ")
      : "---";

    const precio = Number(producto.precio || 0).toFixed(2);

    const estado = producto.activo ? "ACTIVO" : "INACTIVO";

    const celdaId = document.createElement("td");
    celdaId.textContent = String(producto.id);

    const celdaMarca = document.createElement("td");
    celdaMarca.textContent = producto.marca || "";

    const celdaNombre = document.createElement("td");
    celdaNombre.textContent = producto.nombre || "";

    const celdaPrecio = document.createElement("td");
    celdaPrecio.textContent = `${precio} €`;

    const celdaTallas = document.createElement("td");
    celdaTallas.textContent = tallas;

    const celdaColores = document.createElement("td");
    celdaColores.textContent = colores;

    const celdaEstado = document.createElement("td");
    celdaEstado.textContent = estado;

    const celdaAcciones = document.createElement("td");

    const botonEditar = document.createElement("button");

    botonEditar.type = "button";
    botonEditar.className = "btn-editar-producto";
    botonEditar.dataset.id = String(producto.id);
    botonEditar.textContent = "Editar";

    celdaAcciones.appendChild(botonEditar);

    fila.appendChild(celdaId);
    fila.appendChild(celdaMarca);
    fila.appendChild(celdaNombre);
    fila.appendChild(celdaPrecio);
    fila.appendChild(celdaTallas);
    fila.appendChild(celdaColores);
    fila.appendChild(celdaEstado);
    fila.appendChild(celdaAcciones);

    tablaProductosBody.appendChild(fila);
  });
}

if (btnNuevoProducto) {
  btnNuevoProducto.addEventListener("click", prepararNuevoProducto);
}

// Prepara el formulario de creación de producto.
function prepararNuevoProducto() {
  if (!formProducto || !formularioProductoContenedor) {
    return;
  }

  formProducto.reset();

  if (productoId) {
    productoId.value = "";
  }

  if (tituloFormularioProducto) {
    tituloFormularioProducto.textContent = "Crear producto";
  }

  if (productoActivo) {
    productoActivo.value = "true";
  }

  formularioProductoContenedor.classList.remove("oculto");

  formularioProductoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

if (tablaProductosBody) {
  tablaProductosBody.addEventListener("click", (event) => {
    const boton = event.target.closest(".btn-editar-producto");

    if (!boton) {
      return;
    }

    const id = boton.dataset.id;

    prepararEdicionProducto(id);
  });
}

// Carga un producto en el formulario de edición.
function prepararEdicionProducto(id) {
  const producto = productosCargados.find(
    (producto) => String(producto.id) === String(id),
  );

  if (!producto) {
    console.error("Producto no encontrado:", id);

    return;
  }

  if (!formProducto) {
    return;
  }

  productoId.value = producto.id;
  productoMarca.value = producto.marca;
  productoNombre.value = producto.nombre;
  productoPrecio.value = producto.precio;
  productoTallas.value = Array.isArray(producto.tallas)
    ? producto.tallas.join(", ")
    : "";

  productoColores.value = Array.isArray(producto.colores)
    ? producto.colores.join(", ")
    : "";

  productoActivo.value = String(producto.activo);

  if (tituloFormularioProducto) {
    tituloFormularioProducto.textContent = "Editar producto";
  }

  if (formularioProductoContenedor) {
    formularioProductoContenedor.classList.remove("oculto");

    formularioProductoContenedor.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  }
}

if (formProducto) {
  formProducto.addEventListener("submit", guardarProducto);
}

// Muestra un error del formulario de producto.
function mostrarErrorCampoProducto(input, elementoError, mensaje) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = mensaje;

  input.classList.add("campo-invalido");
}

// Limpia un error del formulario de producto.
function limpiarErrorCampoProducto(input, elementoError) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = "";

  input.classList.remove("campo-invalido");
}

// Valida la marca del producto.
function validarCampoMarcaProducto() {
  const marca = productoMarca.value.trim();

  if (marca.length < 2 || marca.length > 100) {
    mostrarErrorCampoProducto(
      productoMarca,
      errorProductoMarca,
      "La marca debe tener entre 2 y 100 caracteres.",
    );

    return false;
  }

  limpiarErrorCampoProducto(productoMarca, errorProductoMarca);

  return true;
}

// Valida el nombre del producto.
function validarCampoNombreProducto() {
  const nombre = productoNombre.value.trim();

  if (nombre.length < 2 || nombre.length > 150) {
    mostrarErrorCampoProducto(
      productoNombre,
      errorProductoNombre,
      "El nombre debe tener entre 2 y 150 caracteres.",
    );

    return false;
  }

  limpiarErrorCampoProducto(productoNombre, errorProductoNombre);

  return true;
}

// Valida el precio del producto.
function validarCampoPrecioProducto() {
  const precio = Number(productoPrecio.value);

  if (
    productoPrecio.value.trim() === "" ||
    !Number.isFinite(precio) ||
    precio < 0.01 ||
    precio > 99999999.99
  ) {
    mostrarErrorCampoProducto(
      productoPrecio,
      errorProductoPrecio,
      "Introduce un precio válido superior a 0 €.",
    );

    return false;
  }

  limpiarErrorCampoProducto(productoPrecio, errorProductoPrecio);

  return true;
}

// Valida las tallas del producto.
function validarCampoTallasProducto() {
  const textosTallas = productoTallas.value
    .split(",")
    .map((talla) => talla.trim());

  if (textosTallas.length === 0 || textosTallas.some((talla) => talla === "")) {
    mostrarErrorCampoProducto(
      productoTallas,
      errorProductoTallas,
      "Debes introducir al menos una talla.",
    );

    return false;
  }

  if (!textosTallas.every((talla) => /^\d+$/.test(talla))) {
    mostrarErrorCampoProducto(
      productoTallas,
      errorProductoTallas,
      "Las tallas deben ser números enteros separados por comas.",
    );

    return false;
  }

  const tallas = textosTallas.map(Number);

  if (tallas.some((talla) => talla < 1 || talla > 100)) {
    mostrarErrorCampoProducto(
      productoTallas,
      errorProductoTallas,
      "Las tallas deben estar entre 1 y 100.",
    );

    return false;
  }

  limpiarErrorCampoProducto(productoTallas, errorProductoTallas);

  return true;
}

// Valida los colores del producto.
function validarCampoColoresProducto() {
  const colores = productoColores.value.split(",").map((color) => color.trim());

  if (colores.length === 0 || colores.some((color) => color === "")) {
    mostrarErrorCampoProducto(
      productoColores,
      errorProductoColores,
      "Debes introducir al menos un color.",
    );

    return false;
  }

  if (colores.some((color) => color.length > 50)) {
    mostrarErrorCampoProducto(
      productoColores,
      errorProductoColores,
      "Cada color puede tener como máximo 50 caracteres.",
    );

    return false;
  }

  limpiarErrorCampoProducto(productoColores, errorProductoColores);

  return true;
}

if (productoMarca) {
  productoMarca.addEventListener("blur", validarCampoMarcaProducto);
}

if (productoNombre) {
  productoNombre.addEventListener("blur", validarCampoNombreProducto);
}

if (productoPrecio) {
  productoPrecio.addEventListener("blur", validarCampoPrecioProducto);
}

if (productoTallas) {
  productoTallas.addEventListener("blur", validarCampoTallasProducto);
}

if (productoColores) {
  productoColores.addEventListener("blur", validarCampoColoresProducto);
}

// Valida el formulario completo del producto.
function validarProducto() {
  const marca = productoMarca.value.trim();
  const nombre = productoNombre.value.trim();
  const precio = Number(productoPrecio.value);

  if (marca.length < 2 || marca.length > 100) {
    return "La marca debe tener entre 2 y 100 caracteres.";
  }

  if (nombre.length < 2 || nombre.length > 150) {
    return "El nombre debe tener entre 2 y 150 caracteres.";
  }

  if (!Number.isFinite(precio) || precio < 0.01 || precio > 99999999.99) {
    return "El precio debe estar entre 0,01 € y 99.999.999,99 €.";
  }

  const textosTallas = productoTallas.value
    .split(",")
    .map((talla) => talla.trim());

  if (textosTallas.length === 0 || textosTallas.some((talla) => talla === "")) {
    return "Debes introducir al menos una talla.";
  }

  const tallasValidas = textosTallas.every((talla) => /^\d+$/.test(talla));

  if (!tallasValidas) {
    return "Las tallas deben ser números enteros separados por comas.";
  }

  const tallas = textosTallas.map(Number);

  if (tallas.some((talla) => talla < 1 || talla > 100)) {
    return "Las tallas deben estar entre 1 y 100.";
  }

  const colores = productoColores.value.split(",").map((color) => color.trim());

  if (colores.length === 0 || colores.some((color) => color === "")) {
    return "Debes introducir al menos un color.";
  }

  if (colores.some((color) => color.length > 50)) {
    return "Cada color puede tener como máximo 50 caracteres.";
  }

  return null;
}

// Crea o actualiza un producto.
async function guardarProducto(event) {
  event.preventDefault();

  const errorValidacion = validarProducto();

  if (errorValidacion) {
    mostrarModalPanel("Datos incorrectos", errorValidacion);

    return;
  }

  try {
    const tallas = productoTallas.value
      .split(",")
      .map((talla) => Number(talla.trim()))
      .filter((talla) => !Number.isNaN(talla));

    const colores = productoColores.value
      .split(",")
      .map((color) => color.trim())
      .filter((color) => color !== "");

    const datosProducto = {
      marca: productoMarca.value.trim(),

      nombre: productoNombre.value.trim(),

      precio: Number(productoPrecio.value),

      tallas: tallas,

      colores: colores,

      activo: productoActivo.value === "true",
    };

    const id = productoId.value;

    let respuesta;

    if (id) {
      respuesta = await fetchConCsrf(`${API_URL}/admin/productos/${id}`, {
        method: "PUT",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(datosProducto),
      });
    } else {
      respuesta = await fetchConCsrf(`${API_URL}/admin/productos`, {
        method: "POST",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(datosProducto),
      });
    }

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      let mensaje = "No se pudo guardar el producto.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {}

      mostrarModalPanel("Datos incorrectos", mensaje);

      return;
    }

    const productoGuardado = await respuesta.json();

    console.log("Producto guardado:", productoGuardado);

    await cargarProductos();

    if (formularioProductoContenedor) {
      formularioProductoContenedor.classList.add("oculto");
    }

    formProducto.reset();

    productoId.value = "";

    if (id) {
      mostrarModalPanel(
        "Producto actualizado",
        "Los cambios del producto se han guardado correctamente.",
      );
    } else {
      mostrarModalPanel(
        "Producto creado",
        "El nuevo producto se ha creado correctamente.",
      );
    }
  } catch (error) {
    console.error("Error guardando producto:", error);
  }
}

if (btnCancelarProducto) {
  btnCancelarProducto.addEventListener("click", cerrarFormularioProducto);
}

// Cierra y reinicia el formulario de producto.
function cerrarFormularioProducto() {
  if (!formularioProductoContenedor) {
    return;
  }

  formularioProductoContenedor.classList.add("oculto");

  if (formProducto) {
    formProducto.reset();
  }

  if (productoId) {
    productoId.value = "";
  }
}

const tablaUsuariosBody = document.getElementById("tabla-usuarios-body");
const contenedorTablaEmpleados = document.querySelector(
  "#seccion-usuarios .tabla-pedidos-contenedor",
);
const controlDesplazamientoEmpleados = document.getElementById(
  "control-desplazamiento-empleados",
);
const botonesDesplazamientoEmpleados = document.querySelectorAll(
  ".btn-desplazamiento-empleados",
);
const btnNuevoEmpleado = document.getElementById("btn-nuevo-empleado");
const formularioEmpleadoContenedor = document.getElementById(
  "formulario-empleado-contenedor",
);

const formEmpleado = document.getElementById("form-empleado");
const empleadoId = document.getElementById("empleado-id");
const empleadoNombre = document.getElementById("empleado-nombre");
const empleadoEmail = document.getElementById("empleado-email");
const empleadoPassword = document.getElementById("empleado-password");
const empleadoConfirmarPassword = document.getElementById(
  "empleado-confirmar-password",
);

const errorEmpleadoNombre = document.getElementById("error-empleado-nombre");
const errorEmpleadoEmail = document.getElementById("error-empleado-email");
const errorEmpleadoPassword = document.getElementById(
  "error-empleado-password",
);
const errorEmpleadoConfirmarPassword = document.getElementById(
  "error-empleado-confirmar-password",
);

const empleadoRol = document.getElementById("empleado-rol");
const empleadoActivo = document.getElementById("empleado-activo");
const tituloFormularioEmpleado = document.getElementById(
  "titulo-formulario-empleado",
);

const btnGuardarEmpleado = document.getElementById("btn-guardar-empleado");
const btnCancelarEmpleado = document.getElementById("btn-cancelar-empleado");

let empleadosCargados = [];

if (btnGestionUsuarios) {
  btnGestionUsuarios.addEventListener("click", mostrarGestionUsuarios);
}

// Sincroniza el control inferior con la posición horizontal de la tabla.
function actualizarControlDesplazamientoEmpleados() {
  if (!contenedorTablaEmpleados || !controlDesplazamientoEmpleados) {
    return;
  }

  const desplazamientoMaximo = Math.max(
    0,
    contenedorTablaEmpleados.scrollWidth - contenedorTablaEmpleados.clientWidth,
  );

  controlDesplazamientoEmpleados.max = String(desplazamientoMaximo);
  controlDesplazamientoEmpleados.value = String(
    Math.min(contenedorTablaEmpleados.scrollLeft, desplazamientoMaximo),
  );
}

// Activa el deslizador y los botones de desplazamiento de empleados.
function inicializarDesplazamientoEmpleados() {
  if (!contenedorTablaEmpleados || !controlDesplazamientoEmpleados) {
    return;
  }

  controlDesplazamientoEmpleados.addEventListener("input", () => {
    contenedorTablaEmpleados.scrollLeft = Number(
      controlDesplazamientoEmpleados.value,
    );
  });

  contenedorTablaEmpleados.addEventListener(
    "scroll",
    actualizarControlDesplazamientoEmpleados,
    { passive: true },
  );

  botonesDesplazamientoEmpleados.forEach((boton) => {
    boton.addEventListener("click", () => {
      const direccion = Number(boton.dataset.direccion);
      const distancia = Math.max(
        200,
        contenedorTablaEmpleados.clientWidth * 0.75,
      );

      contenedorTablaEmpleados.scrollBy({
        left: direccion * distancia,
        behavior: "smooth",
      });
    });
  });

  window.addEventListener("resize", actualizarControlDesplazamientoEmpleados);
  actualizarControlDesplazamientoEmpleados();
}

// Muestra la gestión de empleados.
async function mostrarGestionUsuarios() {
  if (!seccionUsuarios) {
    return;
  }

  if (seccionPedidos) {
    seccionPedidos.classList.add("oculto");
  }

  if (seccionProductos) {
    seccionProductos.classList.add("oculto");
  }

  if (seccionMiCuenta) {
    seccionMiCuenta.classList.add("oculto");
  }

  seccionUsuarios.classList.remove("oculto");

  await cargarEmpleados();

  requestAnimationFrame(actualizarControlDesplazamientoEmpleados);

  seccionUsuarios.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// Carga los empleados del panel.
async function cargarEmpleados() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/usuarios`, {
      method: "GET",
      credentials: "include",
    });

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudieron cargar los empleados.");
    }

    const empleados = await respuesta.json();

    empleadosCargados = empleados;

    console.log("Empleados recibidos:", empleados);

    renderizarEmpleados(empleados);
  } catch (error) {
    console.error("Error cargando empleados:", error);
  }
}

// Renderiza los empleados sin interpolar los datos recibidos mediante innerHTML.
function renderizarEmpleados(empleados) {
  if (!tablaUsuariosBody) {
    return;
  }

  tablaUsuariosBody.replaceChildren();

  if (!Array.isArray(empleados) || empleados.length === 0) {
    const fila = document.createElement("tr");

    const celda = document.createElement("td");

    celda.colSpan = 7;
    celda.textContent = "No existen empleados.";

    fila.appendChild(celda);

    tablaUsuariosBody.appendChild(fila);

    return;
  }

  empleados.forEach((empleado) => {
    const fila = document.createElement("tr");

    const esUsuarioActual = usuarioActual && empleado.id === usuarioActual.id;

    const esEmpleadoProtegidoParaJefe =
      usuarioActual &&
      usuarioActual.rol === "JEFE" &&
      empleado.rol !== "TRABAJADOR";

    if (esUsuarioActual) {
      fila.classList.add("empleado-actual");
    }

    const fechaAlta = empleado.fechaAlta
      ? new Date(empleado.fechaAlta).toLocaleString("es-ES")
      : "---";

    const estado = empleado.activo ? "ACTIVO" : "INACTIVO";

    const celdaId = document.createElement("td");
    celdaId.textContent = String(empleado.id);

    const celdaNombre = document.createElement("td");
    celdaNombre.textContent = empleado.nombre || "";

    const celdaEmail = document.createElement("td");
    celdaEmail.textContent = empleado.email || "";

    const celdaRol = document.createElement("td");
    celdaRol.textContent = empleado.rol || "";

    const celdaEstado = document.createElement("td");
    celdaEstado.textContent = estado;

    const celdaFechaAlta = document.createElement("td");
    celdaFechaAlta.textContent = fechaAlta;

    const celdaAcciones = document.createElement("td");

    if (esUsuarioActual) {
      const textoSesionActual = document.createElement("strong");

      textoSesionActual.textContent = "Sesión actual";

      celdaAcciones.appendChild(textoSesionActual);
    } else if (esEmpleadoProtegidoParaJefe) {
      const textoEmpleadoProtegido = document.createElement("strong");

      textoEmpleadoProtegido.textContent = "Empleado protegido";

      celdaAcciones.appendChild(textoEmpleadoProtegido);
    } else {
      const botonEditar = document.createElement("button");

      botonEditar.type = "button";
      botonEditar.className = "btn-editar-empleado";
      botonEditar.dataset.id = String(empleado.id);
      botonEditar.textContent = "Editar";

      celdaAcciones.appendChild(botonEditar);
    }

    fila.appendChild(celdaId);
    fila.appendChild(celdaNombre);
    fila.appendChild(celdaEmail);
    fila.appendChild(celdaRol);
    fila.appendChild(celdaEstado);
    fila.appendChild(celdaFechaAlta);
    fila.appendChild(celdaAcciones);

    tablaUsuariosBody.appendChild(fila);
  });
}

if (btnNuevoEmpleado) {
  btnNuevoEmpleado.addEventListener("click", prepararNuevoEmpleado);
}

// Prepara el formulario de creación de empleado.
function prepararNuevoEmpleado() {
  if (!formEmpleado || !formularioEmpleadoContenedor) {
    return;
  }

  formEmpleado.reset();

  empleadoId.value = "";

  if (tituloFormularioEmpleado) {
    tituloFormularioEmpleado.textContent = "Crear empleado";
  }

  if (btnGuardarEmpleado) {
    btnGuardarEmpleado.textContent = "Guardar empleado";
  }

  if (camposPasswordEmpleado) {
    camposPasswordEmpleado.classList.remove("oculto");
  }

  empleadoPassword.required = true;

  empleadoConfirmarPassword.required = true;

  empleadoRol.value = "TRABAJADOR";

  const opcionJefe = empleadoRol.querySelector('option[value="JEFE"]');

  if (opcionJefe) {
    opcionJefe.disabled = usuarioActual?.rol === "JEFE";

    opcionJefe.hidden = usuarioActual?.rol === "JEFE";
  }

  empleadoActivo.value = "true";

  formularioEmpleadoContenedor.classList.remove("oculto");

  formularioEmpleadoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

if (tablaUsuariosBody) {
  tablaUsuariosBody.addEventListener("click", (event) => {
    const boton = event.target.closest(".btn-editar-empleado");

    if (!boton) {
      return;
    }

    const id = Number(boton.dataset.id);

    prepararEdicionEmpleado(id);
  });
}

// Carga un empleado en el formulario de edición.
function prepararEdicionEmpleado(id) {
  const empleado = empleadosCargados.find((empleado) => empleado.id === id);

  if (!empleado) {
    console.error("Empleado no encontrado:", id);

    return;
  }

  if (
    usuarioActual &&
    usuarioActual.rol === "JEFE" &&
    empleado.rol !== "TRABAJADOR"
  ) {
    mostrarModalPanel(
      "Acción no permitida",
      "Un jefe solamente puede modificar trabajadores.",
    );

    return;
  }

  empleadoId.value = empleado.id;
  empleadoNombre.value = empleado.nombre;
  empleadoEmail.value = empleado.email;
  empleadoRol.value = empleado.rol;

  const opcionJefe = empleadoRol.querySelector('option[value="JEFE"]');

  if (opcionJefe) {
    opcionJefe.disabled = usuarioActual?.rol === "JEFE";

    opcionJefe.hidden = usuarioActual?.rol === "JEFE";
  }
  empleadoActivo.value = String(empleado.activo);

  if (camposPasswordEmpleado) {
    camposPasswordEmpleado.classList.remove("oculto");
  }

  empleadoPassword.value = "";
  empleadoConfirmarPassword.value = "";

  empleadoPassword.required = false;
  empleadoConfirmarPassword.required = false;

  const ayudaPassword = document.getElementById("ayuda-password-empleado");

  if (ayudaPassword) {
    ayudaPassword.textContent =
      "Déjala vacía para conservar la contraseña actual. Si introduces una nueva, debe tener entre 8 y 72 caracteres, una letra y un número.";
  }

  if (tituloFormularioEmpleado) {
    tituloFormularioEmpleado.textContent = "Editar empleado";
  }

  if (btnGuardarEmpleado) {
    btnGuardarEmpleado.textContent = "Guardar cambios";
  }

  formularioEmpleadoContenedor.classList.remove("oculto");

  formularioEmpleadoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

if (formEmpleado) {
  formEmpleado.addEventListener("submit", guardarEmpleado);
}

// Muestra un error del formulario de empleado.
function mostrarErrorCampoEmpleado(input, elementoError, mensaje) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = mensaje;

  input.classList.add("campo-invalido");
}

// Limpia un error del formulario de empleado.
function limpiarErrorCampoEmpleado(input, elementoError) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = "";

  input.classList.remove("campo-invalido");
}

// Valida el nombre del empleado.
function validarCampoNombreEmpleado() {
  const nombre = empleadoNombre.value.trim();

  if (nombre.length < 2 || nombre.length > 100) {
    mostrarErrorCampoEmpleado(
      empleadoNombre,
      errorEmpleadoNombre,
      "El nombre debe tener entre 2 y 100 caracteres.",
    );

    return false;
  }

  const patronNombre = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patronNombre.test(nombre)) {
    mostrarErrorCampoEmpleado(
      empleadoNombre,
      errorEmpleadoNombre,
      "El nombre contiene caracteres no válidos.",
    );

    return false;
  }

  limpiarErrorCampoEmpleado(empleadoNombre, errorEmpleadoNombre);

  return true;
}

// Valida el email del empleado.
function validarCampoEmailEmpleado() {
  const email = empleadoEmail.value.trim().toLowerCase();

  if (email.length === 0 || email.length > 150) {
    mostrarErrorCampoEmpleado(
      empleadoEmail,
      errorEmpleadoEmail,
      "El email debe tener entre 1 y 150 caracteres.",
    );

    return false;
  }

  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(email)) {
    mostrarErrorCampoEmpleado(
      empleadoEmail,
      errorEmpleadoEmail,
      "Introduce un correo electrónico válido.",
    );

    return false;
  }

  limpiarErrorCampoEmpleado(empleadoEmail, errorEmpleadoEmail);

  return true;
}

// Valida la contraseña del empleado.
function validarCampoPasswordEmpleado() {
  const password = empleadoPassword.value;

  if (password.length < 8 || password.length > 72) {
    mostrarErrorCampoEmpleado(
      empleadoPassword,
      errorEmpleadoPassword,
      "La contraseña debe tener entre 8 y 72 caracteres.",
    );

    return false;
  }

  if (!/\p{L}/u.test(password)) {
    mostrarErrorCampoEmpleado(
      empleadoPassword,
      errorEmpleadoPassword,
      "La contraseña debe contener al menos una letra.",
    );

    return false;
  }

  if (!/\d/.test(password)) {
    mostrarErrorCampoEmpleado(
      empleadoPassword,
      errorEmpleadoPassword,
      "La contraseña debe contener al menos un número.",
    );

    return false;
  }

  limpiarErrorCampoEmpleado(empleadoPassword, errorEmpleadoPassword);

  return true;
}

// Valida la confirmación de contraseña.
function validarCampoConfirmarPasswordEmpleado() {
  const password = empleadoPassword.value;
  const confirmacion = empleadoConfirmarPassword.value;

  if (confirmacion === "") {
    mostrarErrorCampoEmpleado(
      empleadoConfirmarPassword,
      errorEmpleadoConfirmarPassword,
      "Debes confirmar la contraseña.",
    );

    return false;
  }

  if (password !== confirmacion) {
    mostrarErrorCampoEmpleado(
      empleadoConfirmarPassword,
      errorEmpleadoConfirmarPassword,
      "Las contraseñas no coinciden.",
    );

    return false;
  }

  limpiarErrorCampoEmpleado(
    empleadoConfirmarPassword,
    errorEmpleadoConfirmarPassword,
  );

  return true;
}

if (empleadoNombre) {
  empleadoNombre.addEventListener("blur", validarCampoNombreEmpleado);
}

if (empleadoEmail) {
  empleadoEmail.addEventListener("blur", validarCampoEmailEmpleado);
}

if (empleadoPassword) {
  empleadoPassword.addEventListener("blur", () => {
    const esNuevo = empleadoId.value === "";

    if (
      esNuevo ||
      empleadoPassword.value !== "" ||
      empleadoConfirmarPassword.value !== ""
    ) {
      validarCampoPasswordEmpleado();
    } else {
      limpiarErrorCampoEmpleado(empleadoPassword, errorEmpleadoPassword);
    }

    if (empleadoConfirmarPassword.value !== "") {
      validarCampoConfirmarPasswordEmpleado();
    }
  });
}

if (empleadoConfirmarPassword) {
  empleadoConfirmarPassword.addEventListener("blur", () => {
    const esNuevo = empleadoId.value === "";

    if (
      esNuevo ||
      empleadoPassword.value !== "" ||
      empleadoConfirmarPassword.value !== ""
    ) {
      validarCampoConfirmarPasswordEmpleado();
    } else {
      limpiarErrorCampoEmpleado(
        empleadoConfirmarPassword,
        errorEmpleadoConfirmarPassword,
      );
    }
  });
}

// Valida el formulario completo del empleado.
function validarEmpleado(esNuevo) {
  const nombre = empleadoNombre.value.trim();

  const email = empleadoEmail.value.trim().toLowerCase();

  if (nombre.length < 2 || nombre.length > 100) {
    return "El nombre debe tener entre 2 y 100 caracteres.";
  }

  const patronNombre = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patronNombre.test(nombre)) {
    return "El nombre contiene caracteres no válidos.";
  }

  if (email.length === 0 || email.length > 150) {
    return "El email no es válido.";
  }

  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(email)) {
    return "Introduce un correo electrónico válido.";
  }

  const password = empleadoPassword.value;
  const confirmarPassword = empleadoConfirmarPassword.value;

  if (esNuevo || password !== "" || confirmarPassword !== "") {
    if (password.length < 8 || password.length > 72) {
      return "La contraseña debe tener entre 8 y 72 caracteres.";
    }

    if (!/\p{L}/u.test(password)) {
      return "La contraseña debe contener al menos una letra.";
    }

    if (!/\d/.test(password)) {
      return "La contraseña debe contener al menos un número.";
    }

    if (password !== confirmarPassword) {
      return "Las contraseñas no coinciden.";
    }
  }

  if (empleadoRol.value !== "TRABAJADOR" && empleadoRol.value !== "JEFE") {
    return "El rol seleccionado no es válido.";
  }

  if (usuarioActual?.rol === "JEFE" && empleadoRol.value !== "TRABAJADOR") {
    return "Un jefe solamente puede gestionar trabajadores.";
  }

  return null;
}

// Crea o actualiza un empleado.
async function guardarEmpleado(event) {
  event.preventDefault();

  const id = empleadoId.value;

  const errorValidacion = validarEmpleado(!id);

  if (errorValidacion) {
    mostrarModalPanel("Datos incorrectos", errorValidacion);

    return;
  }

  try {
    if (!id) {
      const datosEmpleado = {
        nombre: empleadoNombre.value.trim(),
        email: empleadoEmail.value.trim().toLowerCase(),
        rol: empleadoRol.value,
        activo: empleadoActivo.value === "true",
      };

      if (empleadoPassword.value !== "") {
        datosEmpleado.password = empleadoPassword.value;

        datosEmpleado.confirmarPassword = empleadoConfirmarPassword.value;
      }

      const respuesta = await fetchConCsrf(`${API_URL}/admin/usuarios`, {
        method: "POST",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(datosEmpleado),
      });

      if (await comprobarSesionExpirada(respuesta)) {
        return;
      }

      if (!respuesta.ok) {
        let mensaje = "No se pudo crear el empleado.";

        try {
          const error = await respuesta.json();

          if (error.message && error.message !== "No message available") {
            mensaje = error.message;
          }
        } catch {}

        mostrarModalPanel("Datos incorrectos", mensaje);

        return;
      }

      const empleadoCreado = await respuesta.json();

      console.log("Empleado creado:", empleadoCreado);

      await cargarEmpleados();

      cerrarFormularioEmpleado();

      mostrarModalPanel(
        "Empleado creado",
        "El nuevo empleado se ha creado correctamente.",
      );

      return;
    }

    const datosEmpleado = {
      nombre: empleadoNombre.value.trim(),
      email: empleadoEmail.value.trim().toLowerCase(),
      rol: empleadoRol.value,
      activo: empleadoActivo.value === "true",
    };

    if (empleadoPassword.value !== "") {
      datosEmpleado.password = empleadoPassword.value;

      datosEmpleado.confirmarPassword = empleadoConfirmarPassword.value;
    }

    const respuesta = await fetchConCsrf(`${API_URL}/admin/usuarios/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(datosEmpleado),
    });

    if (await comprobarSesionExpirada(respuesta)) {
      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudo modificar el empleado.");
    }

    const empleadoActualizado = await respuesta.json();

    console.log("Empleado actualizado:", empleadoActualizado);

    await cargarEmpleados();

    cerrarFormularioEmpleado();

    mostrarModalPanel(
      "Empleado actualizado",
      "Los datos del empleado se han modificado correctamente.",
    );
  } catch (error) {
    console.error("Error guardando empleado:", error);
  }
}

if (btnCancelarEmpleado) {
  btnCancelarEmpleado.addEventListener("click", cerrarFormularioEmpleado);
}

// Cierra y reinicia el formulario de empleado.
function cerrarFormularioEmpleado() {
  if (!formularioEmpleadoContenedor) {
    return;
  }

  formularioEmpleadoContenedor.classList.add("oculto");

  if (formEmpleado) {
    formEmpleado.reset();
  }

  if (empleadoId) {
    empleadoId.value = "";
  }

  empleadoPassword.required = false;

  empleadoConfirmarPassword.required = false;
}
