// =====================================================
// PRODUCTOS
// -----------------------------------------------------
// Este archivo gestiona:
//
// • La navegación entre marcas.
// • El renderizado de los productos.
// • La actualización de imágenes según el color.
// • La comunicación con carrito.js mediante
//   agregarAlCarrito().
//
// NO guarda datos.
// NO gestiona el carrito.
// Solo muestra información.
// =====================================================

// =====================================================
// HELPERS DE IMAGEN
// =====================================================

// Convierte un color en un nombre válido para un archivo.
//
// Ejemplo:
//
// "Azul Marino"
// ↓
// "azulmarino"
//
function slugifyColor(color) {
  return (color || "")
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .replace(/\s+/g, "");
}

// Construye la ruta de la imagen.
//
// Ejemplo:
//
// id = 4
// color = Negro
//
// ↓
//
// img/p4_negro.png
//
function pathImgProducto(id, color) {
  return `img/p${id}_${slugifyColor(color)}.png`;
}

// Devuelve la imagen correspondiente
function obtenerSrcImagenProducto(id, color) {
  return pathImgProducto(id, color);
}

// =====================================================
// ELEMENTOS DEL DOM
// =====================================================

const seccionesMarcas = document.querySelectorAll(".seccion-deportivas");
const vistaMarca = document.getElementById("vista-marca");
const tituloMarca = document.getElementById("titulo-marca");
const gridProductos = document.getElementById("grid-productos");
const btnVolver = document.getElementById("btn-volver");

// =====================================================
// RELACIÓN ID HTML -> MARCA
// =====================================================

const mapaMarcas = {
  nike: "Nike",
  adidas: "Adidas",
  puma: "Puma",
  reebok: "Reebok",
  "new-balance": "New Balance",
};

// =====================================================
// EVENTOS DE LAS MARCAS
// =====================================================

seccionesMarcas.forEach((sec) => {
  const nombreMarca = mapaMarcas[sec.id];

  if (!nombreMarca) return;

  sec.style.cursor = "pointer";

  sec.addEventListener("click", () => {
    mostrarGridMarca(nombreMarca);
  });
});

// =====================================================
// BOTÓN AÑADIR AL CARRITO
// =====================================================

if (gridProductos) {
  gridProductos.addEventListener("click", (e) => {
    const btn = e.target.closest(".btn-add-carrito");

    if (!btn) return;

    const id = Number(btn.dataset.id);

    if (typeof agregarAlCarrito === "function") {
      agregarAlCarrito(id);
    }
  });
}

// =====================================================
// BOTÓN VOLVER
// =====================================================

if (btnVolver) {
  btnVolver.addEventListener("click", volverInicio);
}

// =====================================================
// RENDER DEL CATÁLOGO POR MARCA
// =====================================================

function mostrarGridMarca(nombreMarca) {
  if (!Array.isArray(catalogo) || catalogo.length === 0) {
    console.warn("Catálogo vacío.");

    return;
  }

  seccionesMarcas.forEach((sec) => sec.classList.add("oculto"));

  tituloMarca.textContent = `Modelos ${nombreMarca}`;

  gridProductos.innerHTML = "";

  const productosMarca = catalogo.filter((p) => p.marca === nombreMarca);

  productosMarca.forEach((prod) => {
    const card = document.createElement("div");

    card.className = "tarjeta-zapatilla";

    const colorInicial = prod.colores?.[0];

    const srcInicial = colorInicial
      ? obtenerSrcImagenProducto(prod.id, colorInicial)
      : "";

    card.innerHTML = `

            <div class="img-zapatilla">

                ${
                  srcInicial
                    ? `<img id="img-${prod.id}" src="${srcInicial}" alt="${prod.nombre}">`
                    : `<img id="img-${prod.id}" src="" alt="${prod.nombre}" style="display:none">`
                }

            </div>

            <h4>${prod.nombre}</h4>

            <div class="precio">${prod.precio.toFixed(2)} €</div>

            <label>Talla</label>

            <select id="talla-${prod.id}">

                ${prod.tallas.map((t) => `<option>${t}</option>`).join("")}

            </select>

            <label>Color</label>

            <select id="color-${prod.id}">

                ${prod.colores.map((c) => `<option>${c}</option>`).join("")}

            </select>

            <button
                class="btn-add-carrito"
                data-id="${prod.id}">

                Añadir al carrito

            </button>

        `;

    gridProductos.appendChild(card);

    configurarImagen(card, prod);
  });

  vistaMarca.classList.remove("oculto");

  vistaMarca.scrollIntoView({
    behavior: "smooth",

    block: "start",
  });
}

// =====================================================
// CONFIGURAR IMAGEN DEL PRODUCTO
// =====================================================

function configurarImagen(card, prod) {
  const img = card.querySelector(`#img-${prod.id}`);

  if (!img) return;

  img.onerror = () => {
    const fallback = `img/p${prod.id}_default.png`;

    if (!img.dataset.fallbackTried) {
      img.dataset.fallbackTried = "1";

      img.src = fallback;
    } else {
      img.style.display = "none";
    }
  };

  const selectColor = card.querySelector(`#color-${prod.id}`);

  if (!selectColor) return;

  selectColor.addEventListener("change", (e) => {
    img.dataset.fallbackTried = "";

    img.style.display = "";

    img.src = obtenerSrcImagenProducto(
      prod.id,

      e.target.value,
    );
  });
}

// =====================================================
// VOLVER A LA PANTALLA PRINCIPAL
// =====================================================

function volverInicio() {
  vistaMarca.classList.add("oculto");

  seccionesMarcas.forEach((sec) => sec.classList.remove("oculto"));
}
