// Gestiona la cuenta, los pedidos y las facturas del cliente.
document.addEventListener("DOMContentLoaded", iniciarPerfil);

let usuarioActual = null;

// Inicializa los datos y eventos del perfil.
async function iniciarPerfil() {
  await cargarPerfil();

  prepararNavegacion();

  prepararModales();

  prepararCerrarSesion();
}

// Carga los datos del cliente autenticado.
async function cargarPerfil() {
  try {
    const respuesta = await fetch("http://localhost:8080/auth/me", {
      method: "GET",
      credentials: "include",
    });

    if (!respuesta.ok) {
      console.log("No existe una sesión activa.");

      window.location.href = "../tienda.html";

      return;
    }

    usuarioActual = await respuesta.json();

    console.log("Usuario del perfil:", usuarioActual);

    mostrarDatosCuenta(usuarioActual);
  } catch (error) {
    console.error("Error comprobando la sesión:", error);

    window.location.href = "../tienda.html";
  }
}

// Muestra los datos de la cuenta.
function mostrarDatosCuenta(usuario) {
  const perfilNombre = document.getElementById("perfil-nombre");

  const cuentaNombre = document.getElementById("cuenta-nombre");

  const cuentaEmail = document.getElementById("cuenta-email");

  const cuentaRol = document.getElementById("cuenta-rol");

  const cuentaFechaAlta = document.getElementById("cuenta-fecha-alta");

  if (perfilNombre) {
    perfilNombre.textContent = usuario.nombre;
  }

  if (cuentaNombre) {
    cuentaNombre.textContent = usuario.nombre;
  }

  if (cuentaEmail) {
    cuentaEmail.textContent = usuario.email;
  }

  if (cuentaRol) {
    if (usuario.rol === "CLIENTE") {
      cuentaRol.textContent = "Cliente";
    } else {
      cuentaRol.textContent = usuario.rol;
    }
  }

  if (cuentaFechaAlta && usuario.fechaAlta) {
    const fecha = new Date(usuario.fechaAlta);

    cuentaFechaAlta.textContent = fecha.toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });
  }
}

// Configura la navegación del perfil.
function prepararNavegacion() {
  const btnMiCuenta = document.getElementById("btn-mi-cuenta");

  const btnMisPedidos = document.getElementById("btn-mis-pedidos");

  const seccionMiCuenta = document.getElementById("seccion-mi-cuenta");

  const seccionMisPedidos = document.getElementById("seccion-mis-pedidos");

  if (btnMiCuenta && seccionMiCuenta && seccionMisPedidos) {
    btnMiCuenta.addEventListener("click", () => {
      seccionMiCuenta.hidden = false;

      seccionMisPedidos.hidden = true;
    });
  }

  if (btnMisPedidos && seccionMiCuenta && seccionMisPedidos) {
    btnMisPedidos.addEventListener("click", async () => {
      seccionMiCuenta.hidden = true;

      seccionMisPedidos.hidden = false;

      await cargarPedidos();
    });
  }
}

// Carga los pedidos disponibles.
async function cargarPedidos() {
  const listaPedidos = document.getElementById("lista-pedidos");

  if (!listaPedidos) {
    return;
  }

  listaPedidos.innerHTML = "<p>Cargando pedidos...</p>";

  try {
    const respuesta = await fetch(
      "http://localhost:8080/cliente/perfil/pedidos",
      {
        method: "GET",
        credentials: "include",
      },
    );

    if (respuesta.status === 401 || respuesta.status === 403) {
      window.location.href = "../tienda.html";

      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudieron cargar los pedidos.");
    }

    const pedidos = await respuesta.json();

    console.log("Pedidos del cliente:", pedidos);

    mostrarPedidos(pedidos);
  } catch (error) {
    console.error("Error cargando los pedidos:", error);

    listaPedidos.innerHTML = "<p>No se han podido cargar tus pedidos.</p>";
  }
}

// Muestra los pedidos creando nodos seguros para los datos recibidos del backend.
function mostrarPedidos(pedidos) {
  const listaPedidos = document.getElementById("lista-pedidos");

  if (!listaPedidos) {
    return;
  }

  listaPedidos.replaceChildren();

  if (!Array.isArray(pedidos) || pedidos.length === 0) {
    const mensaje = document.createElement("p");

    mensaje.textContent = "Todavía no has realizado ningún pedido.";

    listaPedidos.appendChild(mensaje);

    return;
  }

  pedidos.forEach((pedido) => {
    const tarjetaPedido = document.createElement("article");

    tarjetaPedido.className = "tarjeta-pedido";

    const fecha = pedido.fechaPedido
      ? new Date(pedido.fechaPedido).toLocaleDateString("es-ES", {
          day: "2-digit",
          month: "2-digit",
          year: "numeric",
        })
      : "-";

    const estado = formatearEstadoPedido(pedido.estado);

    const cabecera = document.createElement("div");
    cabecera.className = "pedido-cabecera";

    const datosCabecera = document.createElement("div");

    const tituloPedido = document.createElement("h4");
    tituloPedido.textContent = `Pedido ${pedido.idPedido || ""}`;

    const fechaPedido = document.createElement("p");
    fechaPedido.textContent = fecha;

    datosCabecera.appendChild(tituloPedido);
    datosCabecera.appendChild(fechaPedido);

    const estadoPedido = document.createElement("span");

    estadoPedido.className = "estado-pedido";

    const estadoSeguro = String(pedido.estado || "").toLowerCase();

    if (
      ["preparando", "enviado", "entregado", "devuelto", "cancelado"].includes(
        estadoSeguro,
      )
    ) {
      estadoPedido.classList.add(`estado-${estadoSeguro}`);
    }

    estadoPedido.textContent = estado;

    cabecera.appendChild(datosCabecera);
    cabecera.appendChild(estadoPedido);

    const datosEnvioCabecera = document.createElement("div");
    datosEnvioCabecera.className = "pedido-datos-cabecera";

    const tituloEnvio = document.createElement("h4");
    tituloEnvio.textContent = "Datos de envío";

    const btnFactura = document.createElement("button");

    btnFactura.type = "button";
    btnFactura.className = "btn-descargar-factura";
    btnFactura.dataset.idPedido = String(pedido.idPedido || "");
    btnFactura.textContent = "Descargar factura";

    datosEnvioCabecera.appendChild(tituloEnvio);
    datosEnvioCabecera.appendChild(btnFactura);

    const crearDato = (etiqueta, valor) => {
      const parrafo = document.createElement("p");

      const strong = document.createElement("strong");
      strong.textContent = `${etiqueta}: `;

      const texto = document.createTextNode(valor || "");

      parrafo.appendChild(strong);
      parrafo.appendChild(texto);

      return parrafo;
    };

    const destinatario =
      `${pedido.nombre || ""} ${pedido.apellidos || ""}`.trim();

    const localidad =
      `${pedido.cp || ""} ${pedido.ciudad || ""}, ${pedido.provincia || ""}`.trim();

    const datoDestinatario = crearDato("Destinatario", destinatario);
    const datoDireccion = crearDato("Dirección", pedido.direccion);
    const datoLocalidad = crearDato("Localidad", localidad);
    const datoPais = crearDato("País", pedido.pais);
    const datoTelefono = crearDato("Teléfono", pedido.telefono);

    const productosPedido = document.createElement("div");
    productosPedido.className = "pedido-productos";

    const tituloProductos = document.createElement("h4");
    tituloProductos.textContent = "Productos";

    const contenedorProductos = document.createElement("div");
    contenedorProductos.className = "lista-productos-pedido";

    productosPedido.appendChild(tituloProductos);
    productosPedido.appendChild(contenedorProductos);

    if (Array.isArray(pedido.items)) {
      pedido.items.forEach((item) => {
        const producto = crearProductoPedido(item);

        contenedorProductos.appendChild(producto);
      });
    }

    const pedidoTotal = document.createElement("div");
    pedidoTotal.className = "pedido-total";

    const textoTotal = crearDato("Total", formatearPrecio(pedido.total));

    pedidoTotal.appendChild(textoTotal);

    btnFactura.addEventListener("click", () => {
      descargarFactura(pedido.idPedido);
    });

    tarjetaPedido.appendChild(cabecera);
    tarjetaPedido.appendChild(datosEnvioCabecera);
    tarjetaPedido.appendChild(datoDestinatario);
    tarjetaPedido.appendChild(datoDireccion);
    tarjetaPedido.appendChild(datoLocalidad);
    tarjetaPedido.appendChild(datoPais);
    tarjetaPedido.appendChild(datoTelefono);
    tarjetaPedido.appendChild(productosPedido);
    tarjetaPedido.appendChild(pedidoTotal);

    listaPedidos.appendChild(tarjetaPedido);
  });
}

// Descarga la factura de un pedido.
async function descargarFactura(idPedido) {
  try {
    const respuesta = await fetch(
      `http://localhost:8080/cliente/perfil/pedidos/${encodeURIComponent(idPedido)}/factura`,
      {
        method: "GET",
        credentials: "include",
      },
    );

    if (respuesta.status === 401 || respuesta.status === 403) {
      window.location.href = "../tienda.html";

      return;
    }

    if (!respuesta.ok) {
      throw new Error("No se pudo descargar la factura.");
    }

    const pdf = await respuesta.blob();

    const url = URL.createObjectURL(pdf);

    const enlace = document.createElement("a");

    enlace.href = url;

    enlace.download = `factura-${idPedido}.pdf`;

    document.body.appendChild(enlace);

    enlace.click();

    enlace.remove();

    URL.revokeObjectURL(url);
  } catch (error) {
    console.error("Error descargando la factura:", error);

    alert("No se ha podido descargar la factura.");
  }
}

// Crea la vista de una línea de pedido sin interpretar sus datos como HTML.
function crearProductoPedido(item) {
  const producto = document.createElement("div");

  producto.className = "producto-pedido";

  const rutaImagen = obtenerImagenPedido(item.productoId, item.color);

  const contenedorImagen = document.createElement("div");
  contenedorImagen.className = "producto-pedido-imagen";

  const imagen = document.createElement("img");

  imagen.src = rutaImagen;
  imagen.alt = item.nombreProducto || "";

  contenedorImagen.appendChild(imagen);

  const informacion = document.createElement("div");
  informacion.className = "producto-pedido-info";

  const nombre = document.createElement("h5");
  nombre.textContent = item.nombreProducto || "";

  informacion.appendChild(nombre);

  const crearDatoProducto = (etiqueta, valor) => {
    const parrafo = document.createElement("p");

    const strong = document.createElement("strong");
    strong.textContent = `${etiqueta}: `;

    const texto = document.createTextNode(String(valor ?? ""));

    parrafo.appendChild(strong);
    parrafo.appendChild(texto);

    return parrafo;
  };

  informacion.appendChild(crearDatoProducto("Talla", item.talla));

  informacion.appendChild(crearDatoProducto("Color", item.color));

  informacion.appendChild(crearDatoProducto("Cantidad", item.cantidad));

  informacion.appendChild(
    crearDatoProducto("Precio", formatearPrecio(item.precioUnitario)),
  );

  producto.appendChild(contenedorImagen);
  producto.appendChild(informacion);

  imagen.addEventListener("error", () => {
    if (!imagen.dataset.fallback) {
      imagen.dataset.fallback = "true";

      imagen.src = `../img/p${item.productoId}_default.png`;
    } else {
      imagen.style.display = "none";
    }
  });

  return producto;
}

// Construye la ruta de imagen de un pedido.
function obtenerImagenPedido(productoId, color) {
  const colorArchivo = normalizarColorImagen(color);

  return `../img/p${productoId}_${colorArchivo}.png`;
}

// Normaliza un color para la imagen.
function normalizarColorImagen(color) {
  return (color || "")
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .replace(/\s+/g, "");
}

// Formatea un importe en euros.
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

// Convierte un estado en texto legible.
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

// Abre un modal del perfil.
function abrirModal(modal) {
  if (!modal) {
    return;
  }

  modal.hidden = false;

  document.body.classList.add("modal-abierto");
}

// Cierra un modal del perfil.
function cerrarModal(modal) {
  if (!modal) {
    return;
  }

  modal.hidden = true;

  document.body.classList.remove("modal-abierto");
}

// Configura los modales del perfil.
function prepararModales() {
  prepararModalNombre();

  prepararModalEmail();

  prepararModalPassword();
}

// Configura la edición del nombre.
function prepararModalNombre() {
  const btnEditar = document.getElementById("btn-editar-nombre");
  const modal = document.getElementById("modal-editar-nombre");
  const input = document.getElementById("input-editar-nombre");
  const btnGuardar = document.getElementById("btn-guardar-nombre");
  const btnCancelar = document.getElementById("btn-cancelar-nombre");

  if (!btnEditar || !modal || !input) {
    return;
  }

  btnEditar.addEventListener("click", () => {
    if (usuarioActual) {
      input.value = usuarioActual.nombre;
    }

    input.classList.remove("campo-invalido");

    const errorNombre = document.getElementById("error-editar-nombre");

    if (errorNombre) {
      errorNombre.textContent = "";
    }

    abrirModal(modal);

    input.focus();
  });

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarNombre);
  }

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      cerrarModal(modal);
    });
  }
}

// Configura la edición del email.
function prepararModalEmail() {
  const btnEditar = document.getElementById("btn-editar-email");
  const modal = document.getElementById("modal-editar-email");
  const input = document.getElementById("input-editar-email");
  const btnGuardar = document.getElementById("btn-guardar-email");
  const btnCancelar = document.getElementById("btn-cancelar-email");
  const inputPasswordActual = document.getElementById(
    "input-password-actual-email",
  );
  const errorPasswordActual = document.getElementById(
    "error-password-actual-email",
  );

  if (!btnEditar || !modal || !input) {
    return;
  }

  btnEditar.addEventListener("click", () => {
    if (usuarioActual) {
      input.value = usuarioActual.email;
    }

    if (inputPasswordActual) {
      inputPasswordActual.value = "";
      inputPasswordActual.classList.remove("campo-invalido");
    }

    if (errorPasswordActual) {
      errorPasswordActual.textContent = "";
    }

    const errorEmail = document.getElementById("error-editar-email");

    input.classList.remove("campo-invalido");

    if (errorEmail) {
      errorEmail.textContent = "";
    }

    abrirModal(modal);

    input.focus();
  });

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarEmail);
  }

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      cerrarModal(modal);
    });
  }

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

// Configura el cambio de contraseña.
function prepararModalPassword() {
  const btnCambiar = document.getElementById("btn-cambiar-password");
  const modal = document.getElementById("modal-cambiar-password");
  const inputPasswordActual = document.getElementById("input-password-actual");
  const inputPassword = document.getElementById("input-nueva-password");
  const inputConfirmar = document.getElementById("input-confirmar-password");
  const btnCancelar = document.getElementById("btn-cancelar-password");
  const btnGuardar = document.getElementById("btn-guardar-password");

  if (
    !btnCambiar ||
    !modal ||
    !inputPasswordActual ||
    !inputPassword ||
    !inputConfirmar
  ) {
    return;
  }

  btnCambiar.addEventListener("click", () => {
    inputPasswordActual.value = "";
    inputPassword.value = "";
    inputConfirmar.value = "";

    inputPasswordActual.classList.remove("campo-invalido");
    inputPassword.classList.remove("campo-invalido");
    inputConfirmar.classList.remove("campo-invalido");

    const errorPassword = document.getElementById("error-nueva-password");

    const errorPasswordActual = document.getElementById(
      "error-password-actual",
    );

    const errorConfirmar = document.getElementById("error-confirmar-password");

    if (errorPasswordActual) {
      errorPasswordActual.textContent = "";
    }

    if (errorPassword) {
      errorPassword.textContent = "";
    }

    if (errorConfirmar) {
      errorConfirmar.textContent = "";
    }

    abrirModal(modal);

    inputPasswordActual.focus();
  });

  if (btnGuardar) {
    btnGuardar.addEventListener("click", actualizarPassword);
  }

  if (btnCancelar) {
    btnCancelar.addEventListener("click", () => {
      inputPasswordActual.value = "";
      inputPassword.value = "";
      inputConfirmar.value = "";

      inputPassword.classList.remove("campo-invalido");
      inputConfirmar.classList.remove("campo-invalido");
      inputPasswordActual.classList.remove("campo-invalido");

      const errorPassword = document.getElementById("error-nueva-password");

      const errorPasswordActual = document.getElementById(
        "error-password-actual",
      );

      if (errorPasswordActual) {
        errorPasswordActual.textContent = "";
      }

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

    if (confirmarPassword.length < 8 || confirmarPassword.length > 72) {
      inputConfirmar.classList.add("campo-invalido");

      if (errorConfirmar) {
        errorConfirmar.textContent =
          "La confirmación debe tener entre 8 y 72 caracteres.";
      }

      return;
    }

    if (confirmarPassword !== inputPassword.value) {
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

// Configura el botón de cierre de sesión.
function prepararCerrarSesion() {
  const btnCerrarSesion = document.getElementById("btn-cerrar-sesion-perfil");

  if (!btnCerrarSesion) {
    return;
  }

  btnCerrarSesion.addEventListener("click", cerrarSesionPerfil);
}

// Cierra la sesión desde el perfil.
async function cerrarSesionPerfil() {
  try {
    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

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

    console.log("Sesión cerrada correctamente.");

    window.location.href = "../tienda.html";
  } catch (error) {
    console.error("Error cerrando sesión:", error);

    alert("No se ha podido cerrar la sesión.");
  }
}

// Valida y actualiza el nombre del cliente.
async function actualizarNombre() {
  const inputNombre = document.getElementById("input-editar-nombre");

  const errorNombre = document.getElementById("error-editar-nombre");

  if (!inputNombre) {
    return;
  }

  const nuevoNombre = inputNombre.value.trim();

  inputNombre.classList.remove("campo-invalido");

  if (errorNombre) {
    errorNombre.textContent = "";
  }

  if (nuevoNombre.length < 2 || nuevoNombre.length > 100) {
    inputNombre.classList.add("campo-invalido");

    if (errorNombre) {
      errorNombre.textContent = "Nombre inválido.";
    }

    return;
  }

  const patronNombre = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patronNombre.test(nuevoNombre)) {
    inputNombre.classList.add("campo-invalido");

    if (errorNombre) {
      errorNombre.textContent = "Nombre inválido.";
    }

    return;
  }

  try {
    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

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

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar el nombre.";

      try {
        const error = await respuesta.json();

        if (error.error) {
          mensaje = error.error;
        } else if (error.message) {
          mensaje = error.message;
        }
      } catch {}

      alert(mensaje);

      return;
    }

    const usuarioActualizado = await respuesta.json();

    const cuentaNombre = document.getElementById("cuenta-nombre");

    if (cuentaNombre) {
      cuentaNombre.textContent = usuarioActualizado.nombre;
    }

    const perfilNombre = document.getElementById("perfil-nombre");

    if (perfilNombre) {
      perfilNombre.textContent = usuarioActualizado.nombre;
    }

    if (typeof usuarioActual !== "undefined" && usuarioActual) {
      usuarioActual.nombre = usuarioActualizado.nombre;
    }

    const modalNombre = document.getElementById("modal-editar-nombre");

    cerrarModal(modalNombre);

    alert("Nombre actualizado correctamente.");
  } catch (error) {
    console.error("Error actualizando el nombre:", error);

    alert("No se ha podido actualizar el nombre.");
  }
}

// Valida y actualiza el email del cliente.
async function actualizarEmail() {
  const inputEmail = document.getElementById("input-editar-email");
  const errorEmail = document.getElementById("error-editar-email");
  const inputPasswordActual = document.getElementById(
    "input-password-actual-email",
  );
  const errorPasswordActual = document.getElementById(
    "error-password-actual-email",
  );

  if (!inputEmail || !inputPasswordActual) {
    return;
  }

  const nuevoEmail = inputEmail.value.trim().toLowerCase();
  const passwordActual = inputPasswordActual.value;

  if (errorEmail) {
    errorEmail.textContent = "";
  }

  inputEmail.classList.remove("campo-invalido");
  inputPasswordActual.classList.remove("campo-invalido");

  if (errorPasswordActual) {
    errorPasswordActual.textContent = "";
  }

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

  if (passwordActual.length === 0) {
    inputPasswordActual.classList.add("campo-invalido");

    if (errorPasswordActual) {
      errorPasswordActual.textContent =
        "Debes introducir tu contraseña actual.";
    }

    return;
  }

  try {
    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

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
          passwordActual: passwordActual,
        }),
      },
    );

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar el correo electrónico.";

      try {
        const error = await respuesta.json();

        if (error.error) {
          mensaje = error.error;
        } else if (error.message) {
          mensaje = error.message;
        }
      } catch {}

      if (mensaje === "La contraseña actual no es correcta.") {
        if (errorPasswordActual) {
          errorPasswordActual.textContent = mensaje;
        }

        inputPasswordActual.classList.add("campo-invalido");
      } else {
        if (errorEmail) {
          errorEmail.textContent = mensaje;
        }

        inputEmail.classList.add("campo-invalido");
      }

      return;
    }

    const usuarioActualizado = await respuesta.json();

    console.log("Email actualizado correctamente:", usuarioActualizado.email);

    alert(
      "Correo electrónico actualizado correctamente. " +
        "Por seguridad debes iniciar sesión de nuevo.",
    );

    // El backend ya ha invalidado la sesión tras completar el cambio sensible.
    window.location.href = "login.html";
  } catch (error) {
    console.error("Error actualizando el email:", error);

    alert("No se ha podido actualizar el correo electrónico.");
  }
}

// Valida y actualiza la contraseña del cliente.
async function actualizarPassword() {
  const inputPasswordActual = document.getElementById("input-password-actual");
  const inputPassword = document.getElementById("input-nueva-password");

  const inputConfirmar = document.getElementById("input-confirmar-password");

  const errorPasswordActual = document.getElementById("error-password-actual");

  const errorPassword = document.getElementById("error-nueva-password");

  const errorConfirmar = document.getElementById("error-confirmar-password");

  if (!inputPasswordActual || !inputPassword || !inputConfirmar) {
    return;
  }

  const passwordActual = inputPasswordActual.value;
  const password = inputPassword.value;
  const confirmarPassword = inputConfirmar.value;

  inputPasswordActual.classList.remove("campo-invalido");
  inputPassword.classList.remove("campo-invalido");
  inputConfirmar.classList.remove("campo-invalido");

  if (errorPasswordActual) {
    errorPasswordActual.textContent = "";
  }

  if (errorPassword) {
    errorPassword.textContent = "";
  }

  if (errorConfirmar) {
    errorConfirmar.textContent = "";
  }

  // La contraseña actual es obligatoria.
  if (passwordActual.length === 0) {
    inputPasswordActual.classList.add("campo-invalido");

    if (errorPasswordActual) {
      errorPasswordActual.textContent =
        "Debes introducir tu contraseña actual.";
    }

    return;
  }

  if (password.length < 8 || password.length > 72) {
    inputPassword.classList.add("campo-invalido");

    if (errorPassword) {
      errorPassword.textContent =
        "La contraseña debe tener entre 8 y 72 caracteres.";
    }

    return;
  }

  const patronPassword = /^(?=.*\p{L})(?=.*\d).+$/u;

  if (!patronPassword.test(password)) {
    inputPassword.classList.add("campo-invalido");

    if (errorPassword) {
      errorPassword.textContent =
        "La contraseña debe contener al menos una letra y un número.";
    }

    return;
  }

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
    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

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
          passwordActual: passwordActual,
          password: password,
          confirmarPassword: confirmarPassword,
        }),
      },
    );

    if (!respuesta.ok) {
      let mensaje = "No se ha podido actualizar la contraseña.";

      try {
        const error = await respuesta.json();

        if (error.error) {
          mensaje = error.error;
        }
      } catch {}

      if (mensaje === "La contraseña actual no es correcta.") {
        if (errorPasswordActual) {
          errorPasswordActual.textContent = mensaje;
        }

        inputPasswordActual.classList.add("campo-invalido");
      } else {
        if (errorPassword) {
          errorPassword.textContent = mensaje;
        }

        inputPassword.classList.add("campo-invalido");
      }

      return;
    }

    console.log("Contraseña actualizada correctamente.");

    await cerrarSesionTrasCambiarPassword();
  } catch (error) {
    console.error("Error actualizando la contraseña:", error);

    alert("No se ha podido actualizar la contraseña.");
  }
}

// Cierra la sesión tras cambiar la contraseña.
async function cerrarSesionTrasCambiarPassword() {
  try {
    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

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


