// =====================================================
// PANEL DEL CLIENTE
// -----------------------------------------------------
// • Comprueba que existe una sesión activa.
// • Recupera los datos del usuario.
// • Muestra Mi cuenta.
// • Permite cambiar entre Mi cuenta / Mis pedidos.
// • Gestiona la apertura y cierre de los modales.
// • Permite cerrar sesión.
// =====================================================

document.addEventListener("DOMContentLoaded", iniciarPerfil);

// =====================================================
// USUARIO ACTUAL
// -----------------------------------------------------
// Guardamos temporalmente los datos recibidos de /auth/me
// para poder utilizarlos en los modales.
// =====================================================

let usuarioActual = null;

// =====================================================
// INICIAR PERFIL
// =====================================================

async function iniciarPerfil() {
  await cargarPerfil();

  prepararNavegacion();

  prepararModales();

  prepararCerrarSesion();
}

// =====================================================
// CARGAR PERFIL
// =====================================================

async function cargarPerfil() {
  try {
    // =============================================
    // COMPROBAR SESIÓN
    // =============================================

    const respuesta = await fetch("http://localhost:8080/auth/me", {
      method: "GET",
      credentials: "include",
    });

    // =============================================
    // NO HAY SESIÓN
    // =============================================

    if (!respuesta.ok) {
      console.log("No existe una sesión activa.");

      window.location.href = "../tienda.html";

      return;
    }

    // =============================================
    // USUARIO AUTENTICADO
    // =============================================

    usuarioActual = await respuesta.json();

    console.log("Usuario del perfil:", usuarioActual);

    // =============================================
    // MOSTRAR DATOS
    // =============================================

    mostrarDatosCuenta(usuarioActual);
  } catch (error) {
    console.error("Error comprobando la sesión:", error);

    // Si no podemos comprobar la sesión,
    // no permitimos permanecer en el perfil.

    window.location.href = "../tienda.html";
  }
}

// =====================================================
// MOSTRAR DATOS DE MI CUENTA
// =====================================================

function mostrarDatosCuenta(usuario) {
  const perfilNombre = document.getElementById("perfil-nombre");

  const cuentaNombre = document.getElementById("cuenta-nombre");

  const cuentaEmail = document.getElementById("cuenta-email");

  const cuentaRol = document.getElementById("cuenta-rol");

  const cuentaFechaAlta = document.getElementById("cuenta-fecha-alta");

  // =============================================
  // NOMBRE DEL SALUDO
  // =============================================

  if (perfilNombre) {
    perfilNombre.textContent = usuario.nombre;
  }

  // =============================================
  // NOMBRE
  // =============================================

  if (cuentaNombre) {
    cuentaNombre.textContent = usuario.nombre;
  }

  // =============================================
  // EMAIL
  // =============================================

  if (cuentaEmail) {
    cuentaEmail.textContent = usuario.email;
  }

  // =============================================
  // ROL
  // =============================================

  if (cuentaRol) {
    if (usuario.rol === "CLIENTE") {
      cuentaRol.textContent = "Cliente";
    } else {
      cuentaRol.textContent = usuario.rol;
    }
  }

  // =============================================
  // FECHA DE ALTA
  // =============================================

  if (cuentaFechaAlta && usuario.fechaAlta) {
    const fecha = new Date(usuario.fechaAlta);

    cuentaFechaAlta.textContent = fecha.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  }
}

// =====================================================
// PREPARAR NAVEGACIÓN
// =====================================================

function prepararNavegacion() {
  const btnMiCuenta = document.getElementById("btn-mi-cuenta");

  const btnMisPedidos = document.getElementById("btn-mis-pedidos");

  const seccionMiCuenta = document.getElementById("seccion-mi-cuenta");

  const seccionMisPedidos = document.getElementById("seccion-mis-pedidos");

  // =============================================
  // MOSTRAR MI CUENTA
  // =============================================

  if (btnMiCuenta && seccionMiCuenta && seccionMisPedidos) {
    btnMiCuenta.addEventListener("click", () => {
      seccionMiCuenta.hidden = false;

      seccionMisPedidos.hidden = true;
    });
  }

  // =============================================
  // MOSTRAR MIS PEDIDOS
  // =============================================

  if (btnMisPedidos && seccionMiCuenta && seccionMisPedidos) {
    btnMisPedidos.addEventListener("click", async () => {
      seccionMiCuenta.hidden = true;

      seccionMisPedidos.hidden = false;

      await cargarPedidos();
    });
  }
}

// =====================================================
// CARGAR PEDIDOS DEL CLIENTE
// =====================================================

async function cargarPedidos() {
  const listaPedidos = document.getElementById("lista-pedidos");

  if (!listaPedidos) {
    return;
  }

  // Mensaje temporal mientras responde el backend.
  listaPedidos.innerHTML = "<p>Cargando pedidos...</p>";

  try {
    const respuesta = await fetch(
      "http://localhost:8080/cliente/perfil/pedidos",
      {
        method: "GET",
        credentials: "include",
      },
    );

    // =================================================
    // SESIÓN NO VÁLIDA
    // =================================================

    if (respuesta.status === 401 || respuesta.status === 403) {
      window.location.href = "../tienda.html";

      return;
    }

    // =================================================
    // ERROR
    // =================================================

    if (!respuesta.ok) {
      throw new Error("No se pudieron cargar los pedidos.");
    }

    // =================================================
    // PEDIDOS RECIBIDOS
    // =================================================

    const pedidos = await respuesta.json();

    console.log("Pedidos del cliente:", pedidos);

    mostrarPedidos(pedidos);
  } catch (error) {
    console.error("Error cargando los pedidos:", error);

    listaPedidos.innerHTML = "<p>No se han podido cargar tus pedidos.</p>";
  }
}

// =====================================================
// MOSTRAR PEDIDOS
// =====================================================

function mostrarPedidos(pedidos) {
  const listaPedidos = document.getElementById("lista-pedidos");

  if (!listaPedidos) {
    return;
  }

  listaPedidos.innerHTML = "";

  // =================================================
  // CLIENTE SIN PEDIDOS
  // =================================================

  if (!Array.isArray(pedidos) || pedidos.length === 0) {
    listaPedidos.innerHTML = "<p>Todavía no has realizado ningún pedido.</p>";

    return;
  }

  // =================================================
  // CREAR TARJETA POR CADA PEDIDO
  // =================================================

  pedidos.forEach((pedido) => {
    const tarjetaPedido = document.createElement("article");

    tarjetaPedido.className = "tarjeta-pedido";

    // ===============================================
    // FECHA
    // ===============================================

    const fecha = pedido.fechaPedido
      ? new Date(pedido.fechaPedido).toLocaleDateString("es-ES", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
        })
      : "-";

    // ===============================================
    // ESTADO
    // ===============================================

    const estado = formatearEstadoPedido(pedido.estado);

    // ===============================================
    // HTML PRINCIPAL DEL PEDIDO
    // ===============================================

    tarjetaPedido.innerHTML = `
      <div class="pedido-cabecera">

        <div>
          <h4>Pedido ${pedido.idPedido}</h4>
          <p>${fecha}</p>
        </div>

        <span class="estado-pedido estado-${pedido.estado?.toLowerCase()}">
          ${estado}
        </span>

      </div>

      <div class="pedido-datos-cabecera">

        <h4>Datos de envío</h4>

        <button
            type="button"
            class="btn-descargar-factura"
            data-id-pedido="${pedido.idPedido}">
            Descargar factura
        </button>

      </div>

        <p>
          <strong>Destinatario:</strong>
          ${pedido.nombre} ${pedido.apellidos}
        </p>

        <p>
          <strong>Dirección:</strong>
          ${pedido.direccion}
        </p>

        <p>
          <strong>Localidad:</strong>
          ${pedido.cp} ${pedido.ciudad}, ${pedido.provincia}
        </p>

        <p>
          <strong>País:</strong>
          ${pedido.pais}
        </p>

        <p>
          <strong>Teléfono:</strong>
          ${pedido.telefono}
        </p>

      </div>

      <div class="pedido-productos">

        <h4>Productos</h4>

        <div class="lista-productos-pedido"></div>

      </div>

      <div class="pedido-total">

        <p>
          <strong>Total:</strong>
          ${formatearPrecio(pedido.total)}
        </p>

      </div>
    `;

    // ===============================================
    // PRODUCTOS DEL PEDIDO
    // ===============================================

    const contenedorProductos = tarjetaPedido.querySelector(
      ".lista-productos-pedido",
    );

    if (contenedorProductos && Array.isArray(pedido.items)) {
      pedido.items.forEach((item) => {
        const producto = crearProductoPedido(item);

        contenedorProductos.appendChild(producto);
      });
    }
    // ===============================================
    // DESCARGAR FACTURA
    // ===============================================

    const btnFactura = tarjetaPedido.querySelector(".btn-descargar-factura");

    if (btnFactura) {
      btnFactura.addEventListener("click", () => {
        descargarFactura(pedido.idPedido);
      });
    }

    listaPedidos.appendChild(tarjetaPedido);
  });
}

// =====================================================
// DESCARGAR FACTURA
// -----------------------------------------------------
// Solicita al backend el PDF correspondiente al pedido.
//
// El backend comprueba que el pedido pertenece al
// usuario autenticado antes de devolver la factura.
// =====================================================

async function descargarFactura(idPedido) {
  try {
    const respuesta = await fetch(
      `http://localhost:8080/cliente/perfil/pedidos/${encodeURIComponent(idPedido)}/factura`,
      {
        method: "GET",
        credentials: "include",
      },
    );

    // =================================================
    // SESIÓN NO VÁLIDA
    // =================================================

    if (respuesta.status === 401 || respuesta.status === 403) {
      window.location.href = "../tienda.html";

      return;
    }

    // =================================================
    // ERROR
    // =================================================

    if (!respuesta.ok) {
      throw new Error(
        "No se pudo descargar la factura.",
      );
    }

    // =================================================
    // CONVERTIR RESPUESTA EN PDF
    // =================================================

    const pdf = await respuesta.blob();

    const url = URL.createObjectURL(pdf);

    // =================================================
    // CREAR DESCARGA TEMPORAL
    // =================================================

    const enlace = document.createElement("a");

    enlace.href = url;

    enlace.download =
      `factura-${idPedido}.pdf`;

    document.body.appendChild(enlace);

    enlace.click();

    enlace.remove();

    // =================================================
    // LIBERAR MEMORIA
    // =================================================

    URL.revokeObjectURL(url);
  } catch (error) {
    console.error(
      "Error descargando la factura:",
      error,
    );

    alert(
      "No se ha podido descargar la factura.",
    );
  }
}
// =====================================================
// CREAR PRODUCTO DEL PEDIDO
// =====================================================

function crearProductoPedido(item) {
  const producto = document.createElement("div");

  producto.className = "producto-pedido";

  const rutaImagen = obtenerImagenPedido(item.productoId, item.color);

  producto.innerHTML = `
    <div class="producto-pedido-imagen">

      <img
        src="${rutaImagen}"
        alt="${item.nombreProducto}"
      />

    </div>

    <div class="producto-pedido-info">

      <h5>${item.nombreProducto}</h5>

      <p>
        <strong>Talla:</strong>
        ${item.talla}
      </p>

      <p>
        <strong>Color:</strong>
        ${item.color}
      </p>

      <p>
        <strong>Cantidad:</strong>
        ${item.cantidad}
      </p>

      <p>
        <strong>Precio:</strong>
        ${formatearPrecio(item.precioUnitario)}
      </p>

    </div>
  `;

  // =================================================
  // FALLBACK SI NO EXISTE LA IMAGEN
  // =================================================

  const imagen = producto.querySelector("img");

  if (imagen) {
    imagen.addEventListener("error", () => {
      if (!imagen.dataset.fallback) {
        imagen.dataset.fallback = "true";

        imagen.src = `../img/p${item.productoId}_default.png`;
      } else {
        imagen.style.display = "none";
      }
    });
  }

  return producto;
}

// =====================================================
// OBTENER IMAGEN DEL PRODUCTO DEL PEDIDO
// =====================================================

function obtenerImagenPedido(productoId, color) {
  const colorArchivo = normalizarColorImagen(color);

  return `../img/p${productoId}_${colorArchivo}.png`;
}

// =====================================================
// NORMALIZAR COLOR PARA EL NOMBRE DEL ARCHIVO
// -----------------------------------------------------
// Ejemplo:
// "Azul Marino" -> "azulmarino"
// =====================================================

function normalizarColorImagen(color) {
  return (color || "")
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .replace(/\s+/g, "");
}

// =====================================================
// FORMATEAR PRECIO
// =====================================================

function formatearPrecio(precio) {
  const numero = Number(precio);

  if (Number.isNaN(numero)) {
    return "-";
  }

  return numero.toLocaleString("es-ES", {
    style: "currency",
    currency: "EUR",
  });
}

// =====================================================
// FORMATEAR ESTADO DEL PEDIDO
// =====================================================

// =====================================================
// FORMATEAR ESTADO DEL PAGO
// -----------------------------------------------------
// En el área del cliente mostramos únicamente
// el estado económico del pedido.
//
// El estado logístico (PREPARANDO, ENVIADO, etc.)
// pertenece al panel de gestión.
// =====================================================

// =====================================================
// FORMATEAR ESTADO DEL PEDIDO
// -----------------------------------------------------
// En el área del cliente mostramos el estado logístico
// de su pedido.
//
// El estado del pago se gestiona internamente y no
// necesita mostrarse al cliente.
// =====================================================

function formatearEstadoPedido(estadoPedido) {
  const estados = {
    PREPARANDO: "Preparando",
    ENVIADO: "Enviado",
    ENTREGADO: "Entregado",
    DEVUELTO: "Devuelto",
    CANCELADO: "Cancelado",
  };

  return estados[estadoPedido] || estadoPedido || "-";
}

// =====================================================
// ABRIR MODAL
// -----------------------------------------------------
// • Muestra el modal.
// • Bloquea el scroll de la página.
// =====================================================

function abrirModal(modal) {
  if (!modal) {
    return;
  }

  modal.hidden = false;

  document.body.classList.add("modal-abierto");
}

// =====================================================
// CERRAR MODAL
// -----------------------------------------------------
// • Oculta el modal.
// • Devuelve el scroll a la página.
// =====================================================

function cerrarModal(modal) {
  if (!modal) {
    return;
  }

  modal.hidden = true;

  document.body.classList.remove("modal-abierto");
}

// =====================================================
// PREPARAR MODALES
// =====================================================

function prepararModales() {
  prepararModalNombre();

  prepararModalEmail();

  prepararModalPassword();
}

// =====================================================
// MODAL NOMBRE
// =====================================================

function prepararModalNombre() {
  const btnEditar = document.getElementById("btn-editar-nombre");
  const modal = document.getElementById("modal-editar-nombre");
  const input = document.getElementById("input-editar-nombre");
  const btnGuardar = document.getElementById("btn-guardar-nombre");
  const btnCancelar = document.getElementById("btn-cancelar-nombre");

  if (!btnEditar || !modal || !input) {
    return;
  }

  // =============================================
  // ABRIR
  // =============================================

  btnEditar.addEventListener("click", () => {
    if (usuarioActual) {
      input.value = usuarioActual.nombre;
    }

    // Limpiamos posibles errores anteriores.
    input.classList.remove("campo-invalido");

    const errorNombre = document.getElementById("error-editar-nombre");

    if (errorNombre) {
      errorNombre.textContent = "";
    }

    abrirModal(modal);

    input.focus();
  });

  // =============================================
  // GUARDAR
  // =============================================

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarNombre);
  }

  // =============================================
  // CANCELAR
  // =============================================

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      cerrarModal(modal);
    });
  }
}

// =====================================================
// MODAL EMAIL
// =====================================================

function prepararModalEmail() {
  const btnEditar = document.getElementById("btn-editar-email");
  const modal = document.getElementById("modal-editar-email");
  const input = document.getElementById("input-editar-email");
  const btnGuardar = document.getElementById("btn-guardar-email");
  const btnCancelar = document.getElementById("btn-cancelar-email");

  if (!btnEditar || !modal || !input) {
    return;
  }

  // =============================================
  // ABRIR
  // =============================================

  btnEditar.addEventListener("click", () => {
    if (usuarioActual) {
      input.value = usuarioActual.email;
    }

    abrirModal(modal);

    input.focus();
  });

  // =============================================
  // GUARDAR
  // =============================================

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarEmail);
  }

  // =============================================
  // CANCELAR
  // =============================================

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      cerrarModal(modal);
    });
  }

  // Comprovación final
  input.addEventListener("blur", () => {
    const email = input.value.trim();

    const errorEmail = document.getElementById("error-editar-email");

    const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (email.length === 0 || email.length > 150 || !patronEmail.test(email)) {
      input.classList.add("campo-invalido");

      if (errorEmail) {
        errorEmail.textContent = "Introduce un correo electrónico válido.";
      }

      return;
    }

    input.classList.remove("campo-invalido");

    if (errorEmail) {
      errorEmail.textContent = "";
    }
  });
}

// =====================================================
// MODAL CONTRASEÑA
// =====================================================

function prepararModalPassword() {
  const btnCambiar = document.getElementById("btn-cambiar-password");
  const modal = document.getElementById("modal-cambiar-password");
  const inputPassword = document.getElementById("input-nueva-password");
  const inputConfirmar = document.getElementById("input-confirmar-password");
  const btnCancelar = document.getElementById("btn-cancelar-password");
  const btnGuardar = document.getElementById("btn-guardar-password");

  if (!btnCambiar || !modal || !inputPassword || !inputConfirmar) {
    return;
  }

  // =============================================
  // ABRIR
  // =============================================

  btnCambiar.addEventListener("click", () => {
    inputPassword.value = "";
    inputConfirmar.value = "";

    // Limpiamos posibles errores anteriores.

    inputPassword.classList.remove("campo-invalido");
    inputConfirmar.classList.remove("campo-invalido");

    const errorPassword = document.getElementById("error-nueva-password");

    const errorConfirmar = document.getElementById("error-confirmar-password");

    if (errorPassword) {
      errorPassword.textContent = "";
    }

    if (errorConfirmar) {
      errorConfirmar.textContent = "";
    }

    abrirModal(modal);

    inputPassword.focus();
  });

  // =============================================
  // GUARDAR
  // =============================================

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarPassword);
  }

  // =============================================
  // CANCELAR
  // =============================================

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      inputPassword.value = "";
      inputConfirmar.value = "";

      inputPassword.classList.remove("campo-invalido");
      inputConfirmar.classList.remove("campo-invalido");

      const errorPassword = document.getElementById("error-nueva-password");

      const errorConfirmar = document.getElementById(
        "error-confirmar-password",
      );

      if (errorPassword) {
        errorPassword.textContent = "";
      }

      if (errorConfirmar) {
        errorConfirmar.textContent = "";
      }

      cerrarModal(modal);
    });
  }

  inputConfirmar.addEventListener("blur", () => {
    const confirmarPassword = inputConfirmar.value;

    const errorConfirmar = document.getElementById("error-confirmar-password");

    // =============================================
    // VALIDAR LONGITUD
    // =============================================

    if (confirmarPassword.length < 8 || confirmarPassword.length > 72) {
      inputConfirmar.classList.add("campo-invalido");

      if (errorConfirmar) {
        errorConfirmar.textContent =
          "La confirmación debe tener entre 8 y 72 caracteres.";
      }

      return;
    }

    // =============================================
    // VALIDAR COINCIDENCIA
    // =============================================

    if (confirmarPassword !== inputPassword.value) {
      inputConfirmar.classList.add("campo-invalido");

      if (errorConfirmar) {
        errorConfirmar.textContent = "Las contraseñas no coinciden.";
      }

      return;
    }

    // =============================================
    // CORRECTO
    // =============================================

    inputConfirmar.classList.remove("campo-invalido");

    if (errorConfirmar) {
      errorConfirmar.textContent = "";
    }
  });

  inputConfirmar.addEventListener("blur", () => {
    const errorConfirmar = document.getElementById("error-confirmar-password");

    if (inputConfirmar.value !== inputPassword.value) {
      inputConfirmar.classList.add("campo-invalido");

      if (errorConfirmar) {
        errorConfirmar.textContent = "Las contraseñas no coinciden.";
      }

      return;
    }

    inputConfirmar.classList.remove("campo-invalido");

    if (errorConfirmar) {
      errorConfirmar.textContent = "";
    }
  });
}

// =====================================================
// PREPARAR BOTÓN CERRAR SESIÓN
// =====================================================

function prepararCerrarSesion() {
  const btnCerrarSesion = document.getElementById("btn-cerrar-sesion-perfil");

  if (!btnCerrarSesion) {
    return;
  }

  btnCerrarSesion.addEventListener("click", cerrarSesionPerfil);
}

// =====================================================
// CERRAR SESIÓN
// =====================================================

async function cerrarSesionPerfil() {
  try {
    // =============================================
    // OBTENER TOKEN CSRF
    // =============================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =============================================
    // CERRAR SESIÓN
    // =============================================

    const respuesta = await fetch("http://localhost:8080/auth/logout", {
      method: "POST",

      credentials: "include",

      headers: {
        [csrf.headerName]: csrf.token,
      },
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo cerrar la sesión.");
    }

    // =============================================
    // VOLVER A LA TIENDA
    // =============================================

    console.log("Sesión cerrada correctamente.");

    window.location.href = "../tienda.html";
  } catch (error) {
    console.error("Error cerrando sesión:", error);

    alert("No se ha podido cerrar la sesión.");
  }
}

// =====================================================
// ACTUALIZAR NOMBRE DEL CLIENTE
// =====================================================

async function actualizarNombre() {
  const inputNombre = document.getElementById("input-editar-nombre");

  const errorNombre = document.getElementById("error-editar-nombre");

  if (!inputNombre) {
    return;
  }

  const nuevoNombre = inputNombre.value.trim();

  // =================================================
  // LIMPIAR ERROR ANTERIOR
  // =================================================

  inputNombre.classList.remove("campo-invalido");

  if (errorNombre) {
    errorNombre.textContent = "";
  }

  // =================================================
  // VALIDAR LONGITUD
  // =================================================

  if (nuevoNombre.length < 2 || nuevoNombre.length > 100) {
    inputNombre.classList.add("campo-invalido");

    if (errorNombre) {
      errorNombre.textContent = "Nombre inválido.";
    }

    return;
  }

  // =================================================
  // VALIDAR CARACTERES
  // =================================================

  const patronNombre = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patronNombre.test(nuevoNombre)) {
    inputNombre.classList.add("campo-invalido");

    if (errorNombre) {
      errorNombre.textContent = "Nombre inválido.";
    }

    return;
  }

  try {
    // =================================================
    // OBTENER TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =================================================
    // ACTUALIZAR NOMBRE
    // =================================================

    const respuesta = await fetch(
      "http://localhost:8080/cliente/perfil/nombre",
      {
        method: "PUT",

        credentials: "include",

        headers: {
          "Content-Type": "application/json",
          [csrf.headerName]: csrf.token,
        },

        body: JSON.stringify({
          nombre: nuevoNombre,
        }),
      },
    );

    // =================================================
    // ERROR DEL BACKEND
    // =================================================

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar el nombre.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {
        // Conservamos el mensaje genérico.
      }

      alert(mensaje);

      return;
    }

    // =================================================
    // ACTUALIZACIÓN CORRECTA
    // =================================================

    const usuarioActualizado = await respuesta.json();

    // Actualizamos el nombre de la tabla.
    const cuentaNombre = document.getElementById("cuenta-nombre");

    if (cuentaNombre) {
      cuentaNombre.textContent = usuarioActualizado.nombre;
    }

    // Actualizamos también:
    // "Bienvenido, Luis"
    const perfilNombre = document.getElementById("perfil-nombre");

    if (perfilNombre) {
      perfilNombre.textContent = usuarioActualizado.nombre;
    }

    // Si utilizas una variable global usuarioActual,
    // mantenemos también su información sincronizada.
    if (typeof usuarioActual !== "undefined" && usuarioActual) {
      usuarioActual.nombre = usuarioActualizado.nombre;
    }

    // =================================================
    // CERRAR MODAL
    // =================================================

    const modalNombre = document.getElementById("modal-editar-nombre");

    cerrarModal(modalNombre);

    alert("Nombre actualizado correctamente.");
  } catch (error) {
    console.error("Error actualizando el nombre:", error);

    alert("No se ha podido actualizar el nombre.");
  }
}

// =====================================================
// ACTUALIZAR EMAIL DEL CLIENTE
// =====================================================

async function actualizarEmail() {
  const inputEmail = document.getElementById("input-editar-email");

  const errorEmail = document.getElementById("error-editar-email");

  if (!inputEmail) {
    return;
  }

  const nuevoEmail = inputEmail.value.trim().toLowerCase();

  // =================================================
  // LIMPIAR ERROR ANTERIOR
  // =================================================

  if (errorEmail) {
    errorEmail.textContent = "";
  }

  inputEmail.classList.remove("campo-invalido");

  // =================================================
  // VALIDAR EMAIL
  // =================================================

  if (nuevoEmail.length === 0 || nuevoEmail.length > 150) {
    if (errorEmail) {
      errorEmail.textContent = "Introduce un correo electrónico válido.";
    }

    inputEmail.classList.add("campo-invalido");

    return;
  }

  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(nuevoEmail)) {
    if (errorEmail) {
      errorEmail.textContent = "Introduce un correo electrónico válido.";
    }

    inputEmail.classList.add("campo-invalido");

    return;
  }

  try {
    // =================================================
    // OBTENER TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =================================================
    // ACTUALIZAR EMAIL
    // =================================================

    const respuesta = await fetch(
      "http://localhost:8080/cliente/perfil/email",
      {
        method: "PUT",

        credentials: "include",

        headers: {
          "Content-Type": "application/json",
          [csrf.headerName]: csrf.token,
        },

        body: JSON.stringify({
          email: nuevoEmail,
        }),
      },
    );

    // =================================================
    // ERROR DEL BACKEND
    // =================================================

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar el correo electrónico.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {
        // Conservamos el mensaje genérico.
      }

      if (errorEmail) {
        errorEmail.textContent = mensaje;
      }

      inputEmail.classList.add("campo-invalido");

      return;
    }

    // =================================================
    // EMAIL ACTUALIZADO CORRECTAMENTE
    // =================================================

    const usuarioActualizado = await respuesta.json();

    console.log("Email actualizado correctamente:", usuarioActualizado.email);

    // =================================================
    // CERRAR SESIÓN
    // -------------------------------------------------
    // El SecurityContext todavía fue creado utilizando
    // el email anterior.
    //
    // Por seguridad cerramos la sesión y obligamos
    // al usuario a autenticarse con el nuevo email.
    // =================================================

    await cerrarSesionTrasCambiarEmail();
  } catch (error) {
    console.error("Error actualizando el email:", error);

    alert("No se ha podido actualizar el correo electrónico.");
  }
}

// =====================================================
// ACTUALIZAR CONTRASEÑA DEL CLIENTE
// =====================================================

async function actualizarPassword() {
  const inputPassword = document.getElementById("input-nueva-password");

  const inputConfirmar = document.getElementById("input-confirmar-password");

  const errorPassword = document.getElementById("error-nueva-password");

  const errorConfirmar = document.getElementById("error-confirmar-password");

  if (!inputPassword || !inputConfirmar) {
    return;
  }

  const password = inputPassword.value;
  const confirmarPassword = inputConfirmar.value;

  // =================================================
  // LIMPIAR ERRORES ANTERIORES
  // =================================================

  inputPassword.classList.remove("campo-invalido");
  inputConfirmar.classList.remove("campo-invalido");

  if (errorPassword) {
    errorPassword.textContent = "";
  }

  if (errorConfirmar) {
    errorConfirmar.textContent = "";
  }

  // =================================================
  // VALIDAR LONGITUD
  // =================================================

  if (password.length < 8 || password.length > 72) {
    inputPassword.classList.add("campo-invalido");

    if (errorPassword) {
      errorPassword.textContent =
        "La contraseña debe tener entre 8 y 72 caracteres.";
    }

    return;
  }

  // =================================================
  // VALIDAR LETRA + NÚMERO
  // =================================================

  const patronPassword = /^(?=.*\p{L})(?=.*\d).+$/u;

  if (!patronPassword.test(password)) {
    inputPassword.classList.add("campo-invalido");

    if (errorPassword) {
      errorPassword.textContent =
        "La contraseña debe contener al menos una letra y un número.";
    }

    return;
  }

  // =================================================
  // VALIDAR CONFIRMACIÓN
  // =================================================

  if (confirmarPassword.length < 8 || confirmarPassword.length > 72) {
    inputConfirmar.classList.add("campo-invalido");

    if (errorConfirmar) {
      errorConfirmar.textContent =
        "La confirmación debe tener entre 8 y 72 caracteres.";
    }

    return;
  }

  if (password !== confirmarPassword) {
    inputConfirmar.classList.add("campo-invalido");

    if (errorConfirmar) {
      errorConfirmar.textContent = "Las contraseñas no coinciden.";
    }

    return;
  }

  try {
    // =================================================
    // OBTENER TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =================================================
    // ACTUALIZAR CONTRASEÑA
    // =================================================

    const respuesta = await fetch(
      "http://localhost:8080/cliente/perfil/password",
      {
        method: "PUT",

        credentials: "include",

        headers: {
          "Content-Type": "application/json",
          [csrf.headerName]: csrf.token,
        },

        body: JSON.stringify({
          password: password,
          confirmarPassword: confirmarPassword,
        }),
      },
    );

    // =================================================
    // ERROR DEL BACKEND
    // =================================================

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar la contraseña.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {
        // Conservamos el mensaje genérico.
      }

      if (errorPassword) {
        errorPassword.textContent = mensaje;
      }

      inputPassword.classList.add("campo-invalido");

      return;
    }

    // =================================================
    // CONTRASEÑA ACTUALIZADA
    // =================================================

    console.log("Contraseña actualizada correctamente.");

    // Cerramos la sesión por seguridad.

    await cerrarSesionTrasCambiarPassword();
  } catch (error) {
    console.error("Error actualizando la contraseña:", error);

    alert("No se ha podido actualizar la contraseña.");
  }
}

// =====================================================
// CERRAR SESIÓN TRAS CAMBIAR CONTRASEÑA
// =====================================================

async function cerrarSesionTrasCambiarPassword() {
  try {
    // =================================================
    // OBTENER TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =================================================
    // LOGOUT
    // =================================================

    const respuestaLogout = await fetch("http://localhost:8080/auth/logout", {
      method: "POST",

      credentials: "include",

      headers: {
        [csrf.headerName]: csrf.token,
      },
    });

    if (!respuestaLogout.ok) {
      throw new Error("No se pudo cerrar la sesión.");
    }

    // =================================================
    // INFORMAR AL USUARIO
    // =================================================

    alert(
      "Contraseña actualizada correctamente. " +
        "Por seguridad debes iniciar sesión de nuevo.",
    );

    window.location.href = "login.html";
  } catch (error) {
    console.error(
      "Error cerrando sesión después de cambiar la contraseña:",
      error,
    );

    alert(
      "La contraseña se ha actualizado, pero no se pudo " +
        "cerrar la sesión correctamente. Vuelve a iniciar sesión.",
    );

    window.location.href = "login.html";
  }
}

// =====================================================
// CERRAR SESIÓN TRAS CAMBIAR EMAIL
// =====================================================

async function cerrarSesionTrasCambiarEmail() {
  try {
    // =================================================
    // OBTENER NUEVO TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =================================================
    // LOGOUT
    // =================================================

    const respuestaLogout = await fetch("http://localhost:8080/auth/logout", {
      method: "POST",

      credentials: "include",

      headers: {
        [csrf.headerName]: csrf.token,
      },
    });

    if (!respuestaLogout.ok) {
      throw new Error("No se pudo cerrar la sesión.");
    }

    // =================================================
    // INFORMAR Y VOLVER AL LOGIN
    // =================================================

    alert(
      "Correo electrónico actualizado correctamente. " +
        "Por seguridad debes iniciar sesión de nuevo.",
    );

    window.location.href = "login.html";
  } catch (error) {
    console.error("Error cerrando sesión después de cambiar el email:", error);

    // Aunque falle el logout visualmente, no dejamos
    // al usuario trabajando normalmente con una sesión
    // cuyo identificador ha cambiado.

    alert(
      "El correo se ha actualizado, pero no se pudo cerrar " +
        "la sesión correctamente. Vuelve a iniciar sesión.",
    );

    window.location.href = "login.html";
  }
}
