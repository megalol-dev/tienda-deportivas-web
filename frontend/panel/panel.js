// =====================================================
// PANEL DE ADMINISTRACIÓN
// -----------------------------------------------------
// Comprueba:
// • Que exista una sesión.
// • Que el usuario tenga un rol autorizado.
// • Muestra nombre y rol.
// • Gestiona pedidos.
// • Gestiona productos.
// • Permite cerrar sesión.
// =====================================================

let usuarioActual = null;

document.addEventListener("DOMContentLoaded", comprobarAccesoPanel);

// =====================================================
// ELEMENTOS GENERALES DEL PANEL
// =====================================================

const btnLogout = document.getElementById("btn-logout-panel");
const btnGestionPedidos = document.getElementById("btn-gestion-pedidos");
const btnGestionProductos = document.getElementById("btn-gestion-productos");
const btnGestionUsuarios = document.getElementById("btn-gestion-usuarios");
const seccionPedidos = document.getElementById("seccion-pedidos");
const seccionProductos = document.getElementById("seccion-productos");
const seccionUsuarios = document.getElementById("seccion-usuarios");
const camposPasswordEmpleado = document.getElementById(
  "campos-password-empleado",
);

// =====================================================
// COMPROBAR ACCESO
// =====================================================

async function comprobarAccesoPanel() {
  try {
    const respuesta = await fetch(`${API_URL}/auth/me`, {
      method: "GET",
      credentials: "include",
    });

    // =============================================
    // NO HAY SESIÓN
    // =============================================

    if (!respuesta.ok) {
      window.location.href = "../usuario/login.html";

      return;
    }

    const usuario = await respuesta.json();
    usuarioActual = usuario;

    // =============================================
    // COMPROBAR ROL
    // =============================================

    const rolesPermitidos = ["TRABAJADOR", "JEFE", "ADMIN"];

    if (!rolesPermitidos.includes(usuario.rol)) {
      window.location.href = "../tienda.html";

      return;
    }

    // =============================================
    // MOSTRAR INFORMACIÓN
    // =============================================

    const nombrePanel = document.getElementById("nombre-panel");
    const rolPanel = document.getElementById("rol-panel");

    if (nombrePanel) {
      nombrePanel.textContent = usuario.nombre;
    }

    if (rolPanel) {
      rolPanel.textContent = usuario.rol;
    }

    // =============================================
    // PERMISOS PARA GESTIÓN DE EMPLEADOS
    // ---------------------------------------------
    // Solamente JEFE y ADMIN pueden acceder
    // a la gestión del personal.
    // =============================================

    if (btnGestionUsuarios) {
      if (usuario.rol === "JEFE" || usuario.rol === "ADMIN") {
        btnGestionUsuarios.classList.remove("oculto");
      } else {
        btnGestionUsuarios.classList.add("oculto");
      }
    }

    // =============================================
    // PERMISOS PARA CREAR EMPLEADOS
    // ---------------------------------------------
    // Solamente JEFE y ADMIN pueden ver el botón
    // para crear nuevos empleados.
    // =============================================

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

// =====================================================
// LOGOUT
// =====================================================

if (btnLogout) {
  btnLogout.addEventListener("click", cerrarSesionPanel);
}

async function cerrarSesionPanel() {
  try {
    const respuesta = await fetchConCsrf(`${API_URL}/auth/logout`, {
      method: "POST",
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo cerrar la sesión.");
    }

    window.location.href = "../usuario/login.html";
  } catch (error) {
    console.error("Error cerrando sesión:", error);
  }
}

// =====================================================
// MODAL DE MENSAJES DEL PANEL
// -----------------------------------------------------
// Modal reutilizable para:
// • Pedidos
// • Productos
// • Usuarios
// =====================================================

const modalPanel = document.getElementById("modal-panel");

const modalPanelTitulo = document.getElementById("modal-panel-titulo");

const modalPanelMensaje = document.getElementById("modal-panel-mensaje");

const btnCerrarModalPanel = document.getElementById("btn-cerrar-modal-panel");

// =====================================================
// MOSTRAR MODAL
// =====================================================

function mostrarModalPanel(titulo, mensaje) {
  if (!modalPanel || !modalPanelTitulo || !modalPanelMensaje) {
    return;
  }

  modalPanelTitulo.textContent = titulo;

  modalPanelMensaje.textContent = mensaje;

  modalPanel.classList.remove("oculto");
}

// =====================================================
// CERRAR MODAL
// =====================================================

function cerrarModalPanel() {
  if (!modalPanel) {
    return;
  }

  modalPanel.classList.add("oculto");
}

if (btnCerrarModalPanel) {
  btnCerrarModalPanel.addEventListener("click", cerrarModalPanel);
}

// =====================================================
// =====================================================
// GESTIÓN DE PEDIDOS
// =====================================================
// =====================================================

const tablaPedidosBody = document.getElementById("tabla-pedidos-body");

// =====================================================
// BOTÓN GESTIÓN DE PEDIDOS
// =====================================================

if (btnGestionPedidos) {
  btnGestionPedidos.addEventListener("click", mostrarGestionPedidos);
}

// =====================================================
// MOSTRAR GESTIÓN DE PEDIDOS
// =====================================================

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

  seccionPedidos.classList.remove("oculto");

  await cargarPedidos();

  seccionPedidos.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// CARGAR PEDIDOS
// =====================================================

async function cargarPedidos() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/pedidos`, {
      method: "GET",
      credentials: "include",
    });

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

// =====================================================
// RENDERIZAR PEDIDOS
// =====================================================

function renderizarPedidos(pedidos) {
  if (!tablaPedidosBody) {
    return;
  }

  tablaPedidosBody.innerHTML = "";

  // =============================================
  // NO EXISTEN PEDIDOS
  // =============================================

  if (!Array.isArray(pedidos) || pedidos.length === 0) {
    tablaPedidosBody.innerHTML = `
      <tr>
        <td colspan="6">
          No existen pedidos.
        </td>
      </tr>
    `;

    return;
  }

  // =============================================
  // MOSTRAR PEDIDOS
  // =============================================

  pedidos.forEach((pedido) => {
    const fila = document.createElement("tr");

    const fecha = pedido.fechaPedido
      ? new Date(pedido.fechaPedido).toLocaleString("es-ES")
      : "---";

    const total = Number(pedido.total || 0).toFixed(2);

    fila.innerHTML = `

      <td>
        ${pedido.idPedido}
      </td>

      <td>
        ${pedido.nombre}
        ${pedido.apellidos}
      </td>

      <td>
        ${fecha}
      </td>

      <td>
        ${total} €
      </td>

      <td>

        <select
          id="estado-pedido-${pedido.id}"
        >

          ${crearOpcionesEstado(pedido.estado)}

        </select>

      </td>

      <td>

        <button
          type="button"
          class="btn-guardar-estado"
          data-id="${pedido.id}"
        >
          Guardar
        </button>

      </td>

    `;

    tablaPedidosBody.appendChild(fila);
  });
}

// =====================================================
// OPCIONES DE ESTADO
// =====================================================

function crearOpcionesEstado(estadoActual) {
  const estados = [
    "PENDIENTE",
    "PAGADO",
    "PREPARANDO",
    "ENVIADO",
    "ENTREGADO",
    "CANCELADO",
  ];

  return estados
    .map((estado) => {
      const seleccionado = estado === estadoActual ? "selected" : "";

      return `
        <option
          value="${estado}"
          ${seleccionado}
        >
          ${estado}
        </option>
      `;
    })
    .join("");
}

// =====================================================
// BOTÓN GUARDAR ESTADO
// =====================================================

if (tablaPedidosBody) {
  tablaPedidosBody.addEventListener("click", async (event) => {
    const boton = event.target.closest(".btn-guardar-estado");

    if (!boton) {
      return;
    }

    const pedidoId = boton.dataset.id;

    const select = document.getElementById(`estado-pedido-${pedidoId}`);

    if (!select) {
      return;
    }

    await cambiarEstadoPedido(pedidoId, select.value);
  });
}

// =====================================================
// CAMBIAR ESTADO DEL PEDIDO
// =====================================================

async function cambiarEstadoPedido(pedidoId, nuevoEstado) {
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
        }),
      },
    );

    if (!respuesta.ok) {
      throw new Error("No se pudo modificar el estado del pedido.");
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

// =====================================================
// =====================================================
// GESTIÓN DE PRODUCTOS
// =====================================================
// =====================================================

const tablaProductosBody = document.getElementById("tabla-productos-body");

const btnNuevoProducto = document.getElementById("btn-nuevo-producto");

const formularioProductoContenedor = document.getElementById(
  "formulario-producto-contenedor",
);

const btnCancelarProducto = document.getElementById("btn-cancelar-producto");

// =====================================================
// ELEMENTOS DEL FORMULARIO DE PRODUCTOS
// =====================================================

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

// =====================================================
// PRODUCTOS CARGADOS
// -----------------------------------------------------
// Guardamos temporalmente los productos recibidos
// para poder localizar el producto que queremos editar.
// =====================================================

let productosCargados = [];

// =====================================================
// BOTÓN GESTIÓN DE PRODUCTOS
// =====================================================

if (btnGestionProductos) {
  btnGestionProductos.addEventListener("click", mostrarGestionProductos);
}

// =====================================================
// MOSTRAR GESTIÓN DE PRODUCTOS
// =====================================================

async function mostrarGestionProductos() {
  if (!seccionProductos) {
    return;
  }

  // Ocultamos pedidos.

  if (seccionPedidos) {
    seccionPedidos.classList.add("oculto");
  }

  if (seccionUsuarios) {
    seccionUsuarios.classList.add("oculto");
  }

  // Mostramos productos.

  seccionProductos.classList.remove("oculto");

  await cargarProductos();

  seccionProductos.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// CARGAR PRODUCTOS
// =====================================================

async function cargarProductos() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/productos`, {
      method: "GET",
      credentials: "include",
    });

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

// =====================================================
// RENDERIZAR PRODUCTOS
// =====================================================

function renderizarProductos(productos) {
  if (!tablaProductosBody) {
    return;
  }

  tablaProductosBody.innerHTML = "";

  // =============================================
  // NO EXISTEN PRODUCTOS
  // =============================================

  if (!Array.isArray(productos) || productos.length === 0) {
    tablaProductosBody.innerHTML = `
      <tr>
        <td colspan="8">
          No existen productos.
        </td>
      </tr>
    `;

    return;
  }

  // =============================================
  // MOSTRAR PRODUCTOS
  // =============================================

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

    fila.innerHTML = `

      <td>
        ${producto.id}
      </td>

      <td>
        ${producto.marca}
      </td>

      <td>
        ${producto.nombre}
      </td>

      <td>
        ${precio} €
      </td>

      <td>
        ${tallas}
      </td>

      <td>
        ${colores}
      </td>

      <td>
        ${estado}
      </td>

      <td>

        <button
          type="button"
          class="btn-editar-producto"
          data-id="${producto.id}"
        >
          Editar
        </button>

      </td>

    `;

    tablaProductosBody.appendChild(fila);
  });
}

// =====================================================
// BOTÓN CREAR NUEVO PRODUCTO
// =====================================================

if (btnNuevoProducto) {
  btnNuevoProducto.addEventListener("click", prepararNuevoProducto);
}

// =====================================================
// PREPARAR NUEVO PRODUCTO
// =====================================================

function prepararNuevoProducto() {
  if (!formProducto || !formularioProductoContenedor) {
    return;
  }

  // Limpiamos todos los campos.

  formProducto.reset();

  // Un producto nuevo todavía no tiene ID.

  if (productoId) {
    productoId.value = "";
  }

  // Cambiamos el título.

  if (tituloFormularioProducto) {
    tituloFormularioProducto.textContent = "Crear producto";
  }

  // Los productos nuevos estarán activos
  // por defecto.

  if (productoActivo) {
    productoActivo.value = "true";
  }

  // Mostramos el formulario.

  formularioProductoContenedor.classList.remove("oculto");

  formularioProductoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// BOTÓN EDITAR PRODUCTO
// =====================================================

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

// =====================================================
// PREPARAR EDICIÓN DE PRODUCTO
// =====================================================

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

  // =============================================
  // RELLENAR FORMULARIO
  // =============================================

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
  
  // =============================================
  // CAMBIAR TÍTULO
  // =============================================

  if (tituloFormularioProducto) {
    tituloFormularioProducto.textContent = "Editar producto";
  }

  // =============================================
  // MOSTRAR FORMULARIO
  // =============================================

  if (formularioProductoContenedor) {
    formularioProductoContenedor.classList.remove("oculto");

    formularioProductoContenedor.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  }
}

// =====================================================
// GUARDAR PRODUCTO
// -----------------------------------------------------
// Si tiene ID:
//      PUT -> modificar producto.
//
// Si no tiene ID:
//      POST -> crear producto.
// =====================================================

if (formProducto) {
  formProducto.addEventListener("submit", guardarProducto);
}

// =====================================================
// MOSTRAR ERROR EN UN CAMPO DE PRODUCTO
// =====================================================

function mostrarErrorCampoProducto(input, elementoError, mensaje) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = mensaje;

  input.classList.add("campo-invalido");
}


// =====================================================
// LIMPIAR ERROR DE UN CAMPO DE PRODUCTO
// =====================================================

function limpiarErrorCampoProducto(input, elementoError) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = "";

  input.classList.remove("campo-invalido");
}

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


// =====================================================
// VALIDACIÓN AL SALIR DE CADA CAMPO
// =====================================================

if (productoMarca) {
  productoMarca.addEventListener(
    "blur",
    validarCampoMarcaProducto,
  );
}

if (productoNombre) {
  productoNombre.addEventListener(
    "blur",
    validarCampoNombreProducto,
  );
}

if (productoPrecio) {
  productoPrecio.addEventListener(
    "blur",
    validarCampoPrecioProducto,
  );
}

if (productoTallas) {
  productoTallas.addEventListener(
    "blur",
    validarCampoTallasProducto,
  );
}

if (productoColores) {
  productoColores.addEventListener(
    "blur",
    validarCampoColoresProducto,
  );
}

// =====================================================
// VALIDAR DATOS DEL PRODUCTO
// =====================================================

function validarProducto() {
  const marca = productoMarca.value.trim();
  const nombre = productoNombre.value.trim();
  const precio = Number(productoPrecio.value);

  // =============================================
  // MARCA
  // =============================================

  if (marca.length < 2 || marca.length > 100) {
    return "La marca debe tener entre 2 y 100 caracteres.";
  }

  // =============================================
  // NOMBRE
  // =============================================

  if (nombre.length < 2 || nombre.length > 150) {
    return "El nombre debe tener entre 2 y 150 caracteres.";
  }

  // =============================================
  // PRECIO
  // =============================================

  if (
    !Number.isFinite(precio) ||
    precio < 0.01 ||
    precio > 99999999.99
  ) {
    return "El precio debe estar entre 0,01 € y 99.999.999,99 €.";
  }

  // =============================================
  // TALLAS
  // =============================================

  const textosTallas = productoTallas.value
    .split(",")
    .map((talla) => talla.trim());

  if (
    textosTallas.length === 0 ||
    textosTallas.some((talla) => talla === "")
  ) {
    return "Debes introducir al menos una talla.";
  }

  const tallasValidas = textosTallas.every((talla) =>
    /^\d+$/.test(talla),
  );

  if (!tallasValidas) {
    return "Las tallas deben ser números enteros separados por comas.";
  }

  const tallas = textosTallas.map(Number);

  if (tallas.some((talla) => talla < 1 || talla > 100)) {
    return "Las tallas deben estar entre 1 y 100.";
  }

  // =============================================
  // COLORES
  // =============================================

  const colores = productoColores.value
    .split(",")
    .map((color) => color.trim());

  if (
    colores.length === 0 ||
    colores.some((color) => color === "")
  ) {
    return "Debes introducir al menos un color.";
  }

  if (colores.some((color) => color.length > 50)) {
    return "Cada color puede tener como máximo 50 caracteres.";
  }

  return null;
}

async function guardarProducto(event) {
  event.preventDefault();

  const errorValidacion = validarProducto();

  if (errorValidacion) {
    mostrarModalPanel("Datos incorrectos", errorValidacion);

    return;
  }

  try {
    // =============================================
    // CONVERTIR TALLAS
    // =============================================

    const tallas = productoTallas.value
      .split(",")
      .map((talla) => Number(talla.trim()))
      .filter((talla) => !Number.isNaN(talla));

    // =============================================
    // CONVERTIR COLORES
    // =============================================

    const colores = productoColores.value
      .split(",")
      .map((color) => color.trim())
      .filter((color) => color !== "");

    // =============================================
    // DATOS DEL PRODUCTO
    // =============================================

    const datosProducto = {
      marca: productoMarca.value.trim(),

      nombre: productoNombre.value.trim(),

      precio: Number(productoPrecio.value),

      tallas: tallas,

      colores: colores,

      activo: productoActivo.value === "true",
    };

    // =============================================
    // COMPROBAR SI CREAMOS O EDITAMOS
    // =============================================

    const id = productoId.value;

    let respuesta;

    // =============================================
    // EDITAR PRODUCTO
    // =============================================

    if (id) {
      respuesta = await fetchConCsrf(`${API_URL}/admin/productos/${id}`, {
        method: "PUT",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(datosProducto),
      });
    }

    // =============================================
    // CREAR PRODUCTO
    // =============================================
    else {
      respuesta = await fetchConCsrf(`${API_URL}/admin/productos`, {
        method: "POST",

        headers: {
          "Content-Type": "application/json",
        },

        body: JSON.stringify(datosProducto),
      });
    }

    // =============================================
    // COMPROBAR RESPUESTA
    // =============================================

    if (!respuesta.ok) {
      let mensaje = "No se pudo guardar el producto.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {
        // Conservamos el mensaje genérico.
      }

      mostrarModalPanel("Datos incorrectos", mensaje);

      return;
    }

    const productoGuardado = await respuesta.json();

    console.log("Producto guardado:", productoGuardado);

    // =============================================
    // RECARGAR TABLA
    // =============================================

    await cargarProductos();

    // =============================================
    // OCULTAR Y LIMPIAR FORMULARIO
    // =============================================

    if (formularioProductoContenedor) {
      formularioProductoContenedor.classList.add("oculto");
    }

    formProducto.reset();

    productoId.value = "";

    // =============================================
    // MOSTRAR MODAL
    // =============================================

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

// =====================================================
// CANCELAR FORMULARIO DE PRODUCTOS
// =====================================================

if (btnCancelarProducto) {
  btnCancelarProducto.addEventListener("click", cerrarFormularioProducto);
}

function cerrarFormularioProducto() {
  if (!formularioProductoContenedor) {
    return;
  }

  formularioProductoContenedor.classList.add("oculto");

  // Dejamos el formulario limpio para
  // la próxima operación.

  if (formProducto) {
    formProducto.reset();
  }

  if (productoId) {
    productoId.value = "";
  }
}

// =====================================================
// GESTIÓN DE EMPLEADOS
// =====================================================

// =====================================================
// ELEMENTOS DE LA SECCIÓN
// =====================================================

const tablaUsuariosBody = document.getElementById("tabla-usuarios-body");
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

// MENSAJES DE ERROR DEL FORMULARIO DE EMPLEADOS
const errorEmpleadoNombre = document.getElementById(
  "error-empleado-nombre",
);
const errorEmpleadoEmail = document.getElementById(
  "error-empleado-email",
);
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

// =====================================================
// EMPLEADOS CARGADOS
// -----------------------------------------------------
// Guardamos los empleados recibidos del servidor
// para poder recuperar sus datos al pulsar Editar.
// =====================================================

let empleadosCargados = [];

// =====================================================
// BOTÓN GESTIÓN DE EMPLEADOS
// =====================================================

if (btnGestionUsuarios) {
  btnGestionUsuarios.addEventListener("click", mostrarGestionUsuarios);
}

// =====================================================
// MOSTRAR GESTIÓN DE EMPLEADOS
// =====================================================

async function mostrarGestionUsuarios() {
  if (!seccionUsuarios) {
    return;
  }

  // Ocultamos las otras secciones.

  if (seccionPedidos) {
    seccionPedidos.classList.add("oculto");
  }

  if (seccionProductos) {
    seccionProductos.classList.add("oculto");
  }

  // Mostramos empleados.

  seccionUsuarios.classList.remove("oculto");

  // Obtenemos los empleados reales desde MySQL.

  await cargarEmpleados();

  seccionUsuarios.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// CARGAR EMPLEADOS
// =====================================================

async function cargarEmpleados() {
  try {
    const respuesta = await fetch(`${API_URL}/admin/usuarios`, {
      method: "GET",
      credentials: "include",
    });

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

// =====================================================
// RENDERIZAR EMPLEADOS
// =====================================================

function renderizarEmpleados(empleados) {
  if (!tablaUsuariosBody) {
    return;
  }

  tablaUsuariosBody.innerHTML = "";

  // =============================================
  // NO HAY EMPLEADOS
  // =============================================

  if (!Array.isArray(empleados) || empleados.length === 0) {
    tablaUsuariosBody.innerHTML = `
      <tr>
        <td colspan="7">
          No existen empleados.
        </td>
      </tr>
    `;

    return;
  }

  // =============================================
  // MOSTRAR EMPLEADOS
  // =============================================

  empleados.forEach((empleado) => {
    const fila = document.createElement("tr");

    const esUsuarioActual = usuarioActual && empleado.id === usuarioActual.id;

    // Un JEFE solamente puede editar TRABAJADORES.
    //
    // No puede editar:
    // • ADMIN
    // • otros JEFES
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

    fila.innerHTML = `

      <td>
        ${empleado.id}
      </td>

      <td>
        ${empleado.nombre}
      </td>

      <td>
        ${empleado.email}
      </td>

      <td>
        ${empleado.rol}
      </td>

      <td>
        ${estado}
      </td>

      <td>
        ${fechaAlta}
      </td>

      <td>

  ${
    esUsuarioActual
      ? "<strong>Sesión actual</strong>"
      : esEmpleadoProtegidoParaJefe
        ? "<strong>Empleado protegido</strong>"
        : `
          <button
            type="button"
            class="btn-editar-empleado"
            data-id="${empleado.id}"
          >
            Editar
          </button>
        `
  }

</td>

    `;

    tablaUsuariosBody.appendChild(fila);
  });
}

// =====================================================
// CREAR NUEVO EMPLEADO
// =====================================================

if (btnNuevoEmpleado) {
  btnNuevoEmpleado.addEventListener("click", prepararNuevoEmpleado);
}

function prepararNuevoEmpleado() {
  if (!formEmpleado || !formularioEmpleadoContenedor) {
    return;
  }

  // =============================================
  // LIMPIAR FORMULARIO
  // =============================================

  formEmpleado.reset();

  empleadoId.value = "";

  // =============================================
  // CONFIGURAR MODO CREAR
  // =============================================

  if (tituloFormularioEmpleado) {
    tituloFormularioEmpleado.textContent = "Crear empleado";
  }

  if (btnGuardarEmpleado) {
    btnGuardarEmpleado.textContent = "Guardar empleado";
  }

  // =============================================
  // MOSTRAR CAMPOS DE CONTRASEÑA
  // ---------------------------------------------
  // Al crear un empleado sí necesitamos
  // introducir una contraseña.
  // =============================================

  if (camposPasswordEmpleado) {
    camposPasswordEmpleado.classList.remove("oculto");
  }

  // =============================================
  // CONTRASEÑA OBLIGATORIA AL CREAR
  // =============================================

  empleadoPassword.required = true;

  empleadoConfirmarPassword.required = true;

  // =============================================
  // VALORES POR DEFECTO
  // =============================================

  empleadoRol.value = "TRABAJADOR";

  // =============================================
  // JERARQUÍA DE ROLES AL CREAR
  // ---------------------------------------------
  // JEFE:
  // solamente puede crear TRABAJADORES.
  //
  // ADMIN:
  // puede crear TRABAJADORES y JEFES.
  // =============================================

  const opcionJefe = empleadoRol.querySelector('option[value="JEFE"]');

  if (opcionJefe) {
    opcionJefe.disabled = usuarioActual?.rol === "JEFE";

    opcionJefe.hidden = usuarioActual?.rol === "JEFE";
  }

  empleadoActivo.value = "true";

  // =============================================
  // MOSTRAR FORMULARIO
  // =============================================

  formularioEmpleadoContenedor.classList.remove("oculto");

  formularioEmpleadoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// BOTÓN EDITAR EMPLEADO
// =====================================================

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

// =====================================================
// PREPARAR EDICIÓN DE EMPLEADO
// =====================================================

function prepararEdicionEmpleado(id) {
  const empleado = empleadosCargados.find((empleado) => empleado.id === id);

  if (!empleado) {
    console.error("Empleado no encontrado:", id);

    return;
  }

  // =============================================
  // IMPEDIR EDICIÓN VISUAL DEL ADMIN POR UN JEFE
  // ---------------------------------------------
  // Esta comprobación mejora la interfaz.
  // La protección real continúa en Spring.
  // =============================================

  // =============================================
  // JERARQUÍA VISUAL DE EDICIÓN
  // ---------------------------------------------
  // Un JEFE solamente puede editar TRABAJADORES.
  //
  // Esto mejora la UX.
  // La seguridad real está en Spring.
  // =============================================

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

  // =============================================
  // RELLENAR DATOS
  // =============================================

  empleadoId.value = empleado.id;
  empleadoNombre.value = empleado.nombre;
  empleadoEmail.value = empleado.email;
  empleadoRol.value = empleado.rol;
  // =============================================
  // JERARQUÍA DE ROLES AL EDITAR
  // ---------------------------------------------
  // Un JEFE no puede ascender un trabajador
  // al rol JEFE.
  // =============================================

  const opcionJefe = empleadoRol.querySelector('option[value="JEFE"]');

  if (opcionJefe) {
    opcionJefe.disabled = usuarioActual?.rol === "JEFE";

    opcionJefe.hidden = usuarioActual?.rol === "JEFE";
  }
  empleadoActivo.value = String(empleado.activo);

  // =============================================
  // CONTRASEÑA AL EDITAR
  // ---------------------------------------------
  // Los campos se muestran, pero son opcionales.
  //
  // Vacíos:
  //      conserva la contraseña actual.
  //
  // Rellenos:
  //      cambia la contraseña.
  // =============================================

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

  // =============================================
  // CAMBIAR FORMULARIO A MODO EDICIÓN
  // =============================================

  if (tituloFormularioEmpleado) {
    tituloFormularioEmpleado.textContent = "Editar empleado";
  }

  if (btnGuardarEmpleado) {
    btnGuardarEmpleado.textContent = "Guardar cambios";
  }

  // =============================================
  // MOSTRAR FORMULARIO
  // =============================================

  formularioEmpleadoContenedor.classList.remove("oculto");

  formularioEmpleadoContenedor.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// =====================================================
// GUARDAR EMPLEADO
// -----------------------------------------------------
// Sin ID:
//      POST → crear.
//
// Con ID:
//      PUT → editar.
// =====================================================

if (formEmpleado) {
  formEmpleado.addEventListener("submit", guardarEmpleado);
}


// =====================================================
// MOSTRAR ERROR EN UN CAMPO DE EMPLEADO
// =====================================================

function mostrarErrorCampoEmpleado(input, elementoError, mensaje) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = mensaje;

  input.classList.add("campo-invalido");
}


// =====================================================
// LIMPIAR ERROR DE UN CAMPO DE EMPLEADO
// =====================================================

function limpiarErrorCampoEmpleado(input, elementoError) {
  if (!input || !elementoError) {
    return;
  }

  elementoError.textContent = "";

  input.classList.remove("campo-invalido");
}

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

// =====================================================
// VALIDACIÓN AL SALIR DE CADA CAMPO DE EMPLEADO
// =====================================================

if (empleadoNombre) {
  empleadoNombre.addEventListener(
    "blur",
    validarCampoNombreEmpleado,
  );
}

if (empleadoEmail) {
  empleadoEmail.addEventListener(
    "blur",
    validarCampoEmailEmpleado,
  );
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

// =====================================================
// VALIDAR DATOS DEL EMPLEADO
// =====================================================

function validarEmpleado(esNuevo) {
  const nombre = empleadoNombre.value.trim();

  const email = empleadoEmail.value.trim().toLowerCase();

  // =============================================
  // NOMBRE
  // =============================================

  if (nombre.length < 2 || nombre.length > 100) {
    return "El nombre debe tener entre 2 y 100 caracteres.";
  }

  const patronNombre = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patronNombre.test(nombre)) {
    return "El nombre contiene caracteres no válidos.";
  }

  // =============================================
  // EMAIL
  // =============================================

  if (email.length === 0 || email.length > 150) {
    return "El email no es válido.";
  }

  const patronEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patronEmail.test(email)) {
    return "Introduce un correo electrónico válido.";
  }

  // =============================================
  // CONTRASEÑA
  // ---------------------------------------------
  // Solo existe al crear.
  // =============================================

  const password = empleadoPassword.value;
  const confirmarPassword = empleadoConfirmarPassword.value;

  // Al crear siempre exigimos contraseña.
  // Al editar solamente la validamos si se ha escrito una nueva.

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

  // =============================================
  // ROL
  // =============================================

  if (empleadoRol.value !== "TRABAJADOR" && empleadoRol.value !== "JEFE") {
    return "El rol seleccionado no es válido.";
  }

  // =============================================
  // JERARQUÍA DE ROLES
  // =============================================

  if (usuarioActual?.rol === "JEFE" && empleadoRol.value !== "TRABAJADOR") {
    return "Un jefe solamente puede gestionar trabajadores.";
  }

  return null;
}

async function guardarEmpleado(event) {
  event.preventDefault();

  const id = empleadoId.value;

  const errorValidacion = validarEmpleado(!id);

  if (errorValidacion) {
    mostrarModalPanel("Datos incorrectos", errorValidacion);

    return;
  }

  try {
    // =================================================
    // CREAR EMPLEADO
    // =================================================

    if (!id) {
      const datosEmpleado = {
        nombre: empleadoNombre.value.trim(),
        email: empleadoEmail.value.trim().toLowerCase(),
        rol: empleadoRol.value,
        activo: empleadoActivo.value === "true",
      };

      // =============================================
      // NUEVA CONTRASEÑA OPCIONAL
      // ---------------------------------------------
      // Solo la enviamos si el jefe ha introducido
      // una nueva contraseña.
      // =============================================

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

      if (!respuesta.ok) {
        let mensaje = "No se pudo crear el empleado.";

        try {
          const error = await respuesta.json();

          if (error.message && error.message !== "No message available") {
            mensaje = error.message;
          }
        } catch {
          // Conservamos mensaje genérico.
        }

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

    // =================================================
    // EDITAR EMPLEADO
    // =================================================

    const datosEmpleado = {
      nombre: empleadoNombre.value.trim(),
      email: empleadoEmail.value.trim().toLowerCase(),
      rol: empleadoRol.value,
      activo: empleadoActivo.value === "true",
    };

    // =================================================
    // NUEVA CONTRASEÑA OPCIONAL
    // -------------------------------------------------
    // Si los campos están vacíos, no enviamos password
    // y el backend conserva la contraseña actual.
    //
    // Si se ha escrito una nueva contraseña,
    // enviamos ambos campos al backend.
    // =================================================

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

// =====================================================
// CANCELAR FORMULARIO
// =====================================================

if (btnCancelarEmpleado) {
  btnCancelarEmpleado.addEventListener("click", cerrarFormularioEmpleado);
}

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

  // Dejamos el formulario preparado para
  // una futura creación.

  empleadoPassword.required = false;

  empleadoConfirmarPassword.required = false;
}
