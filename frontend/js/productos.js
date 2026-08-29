// Renderiza los productos y sus variantes.
// Normaliza un color para usarlo en una ruta.
function slugifyColor(color) {
  return (color || "")
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .replace(/\s+/g, "");
}

// Construye la ruta de imagen de una variante.
function pathImgProducto(id, color) {
  return `img/p${id}_${slugifyColor(color)}.png`;
}

// Devuelve la imagen de una variante.
function obtenerSrcImagenProducto(id, color) {
  return pathImgProducto(id, color);
}

const seccionesMarcas = document.querySelectorAll(".seccion-deportivas");
const vistaMarca = document.getElementById("vista-marca");
const tituloMarca = document.getElementById("titulo-marca");
const gridProductos = document.getElementById("grid-productos");
const btnVolver = document.getElementById("btn-volver");

const mapaMarcas = {
  nike: "Nike",
  adidas: "Adidas",
  puma: "Puma",
  reebok: "Reebok",
  "new-balance": "New Balance",
};

seccionesMarcas.forEach((sec) => {
  const nombreMarca = mapaMarcas[sec.id];

  if (!nombreMarca) return;

  sec.style.cursor = "pointer";

  sec.addEventListener("click", () => {
    mostrarGridMarca(nombreMarca);
  });
});

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

if (btnVolver) {
  btnVolver.addEventListener("click", volverInicio);
}

// Prepara cada letra del título para animarla de forma independiente.
function mostrarTituloMarcaAnimado(nombreMarca) {
  const fragmento = document.createDocumentFragment();

  Array.from(`Modelos ${nombreMarca}`).forEach((letra, indice) => {
    const caracter = document.createElement("span");

    caracter.textContent = letra === " " ? "\u00a0" : letra;
    caracter.style.setProperty("--i", indice);
    fragmento.appendChild(caracter);
  });

  tituloMarca.replaceChildren(fragmento);
}

// Muestra los productos de una marca creando nodos y tratando sus datos como texto.
function mostrarGridMarca(nombreMarca) {
  if (!Array.isArray(catalogo) || catalogo.length === 0) {
    console.warn("Catálogo vacío.");

    return;
  }

  seccionesMarcas.forEach((sec) => sec.classList.add("oculto"));

  mostrarTituloMarcaAnimado(nombreMarca);

  gridProductos.replaceChildren();

  const productosMarca = catalogo.filter((p) => p.marca === nombreMarca);

  productosMarca.forEach((prod) => {
    const card = document.createElement("div");

    card.className = "tarjeta-zapatilla";

    const colorInicial = prod.colores?.[0];

    const srcInicial = colorInicial
      ? obtenerSrcImagenProducto(prod.id, colorInicial)
      : "";

    const contenedorImagen = document.createElement("div");
    contenedorImagen.className = "img-zapatilla";

    const imagen = document.createElement("img");

    imagen.id = `img-${prod.id}`;
    imagen.alt = prod.nombre || "";

    if (srcInicial) {
      imagen.src = srcInicial;
    } else {
      imagen.src = "";
      imagen.style.display = "none";
    }

    contenedorImagen.appendChild(imagen);

    const titulo = document.createElement("h4");
    titulo.textContent = prod.nombre || "";

    const precio = document.createElement("div");
    precio.className = "precio";
    precio.textContent = `${Number(prod.precio || 0).toFixed(2)} €`;

    const labelTalla = document.createElement("label");
    labelTalla.textContent = "Talla";

    const selectTalla = document.createElement("select");
    selectTalla.id = `talla-${prod.id}`;

    if (Array.isArray(prod.tallas)) {
      prod.tallas.forEach((talla) => {
        const opcion = document.createElement("option");

        opcion.value = String(talla);
        opcion.textContent = String(talla);

        selectTalla.appendChild(opcion);
      });
    }

    const labelColor = document.createElement("label");
    labelColor.textContent = "Color";

    const selectColor = document.createElement("select");
    selectColor.id = `color-${prod.id}`;

    if (Array.isArray(prod.colores)) {
      prod.colores.forEach((color) => {
        const opcion = document.createElement("option");

        opcion.value = color;
        opcion.textContent = color;

        selectColor.appendChild(opcion);
      });
    }

    const botonCarrito = document.createElement("button");

    botonCarrito.type = "button";
    botonCarrito.className = "btn-add-carrito";
    botonCarrito.dataset.id = String(prod.id);
    botonCarrito.textContent = "Añadir al carrito";

    card.appendChild(contenedorImagen);
    card.appendChild(titulo);
    card.appendChild(precio);
    card.appendChild(labelTalla);
    card.appendChild(selectTalla);
    card.appendChild(labelColor);
    card.appendChild(selectColor);
    card.appendChild(botonCarrito);

    gridProductos.appendChild(card);

    configurarImagen(card, prod);
  });

  vistaMarca.classList.remove("oculto");

  vistaMarca.scrollIntoView({
    behavior: "smooth",
    block: "start",
  });
}

// Configura el cambio y respaldo de imagen.
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

// Regresa a la portada de marcas.
function volverInicio() {
  vistaMarca.classList.add("oculto");

  seccionesMarcas.forEach((sec) => sec.classList.remove("oculto"));
}
