// ===============================================
//  PRODUCTOS / VISTA POR MARCA (SPA)
//  - helpers de imagen
//  - grid de productos por marca
//  - navegación marcas ↔ vista marca
// ===============================================


// ---------- HELPERS DE IMAGEN ----------

// Normaliza el color a "slug" de archivo: "Azul Marino" -> "azulmarino"
function slugifyColor(color) {
    return (color || '')
        .toLowerCase()
        .normalize('NFD').replace(/[\u0300-\u036f]/g, '') // quita acentos
        .replace(/\s+/g, ''); // quita espacios
}

// Construye la ruta del archivo en base a convención: img/p<ID>_<color>.png
function pathImgProducto(id, color) {
    const slug = slugifyColor(color);
    return `img/p${id}_${slug}.png`;
}

// Devuelve la src según convención; el fallback real se gestiona con onerror del <img>
function obtenerSrcImagenProducto(id, color) {
    return pathImgProducto(id, color);
}


// ---------- CACHE DEL DOM ----------

const seccionesMarcas = document.querySelectorAll('.seccion-deportivas'); // Nike, Adidas, etc.
const vistaMarca = document.getElementById('vista-marca');
const tituloMarca = document.getElementById('titulo-marca');
const gridProductos = document.getElementById('grid-productos');
const btnVolver = document.getElementById('btn-volver');


// ---------- MAPA ID -> NOMBRE MARCA ----------

const mapaMarcas = {
    'nike': 'Nike',
    'adidas': 'Adidas',
    'puma': 'Puma',
    'reebok': 'Reebok',
    'new-balance': 'New Balance'
};


// ---------- CONEXIÓN MARCAS (HOME) → GRID ----------

// Cada sección de marca actúa como “botón” que abre su grid
seccionesMarcas.forEach(sec => {
    const nombreMarca = mapaMarcas[sec.id];
    if (!nombreMarca) return;
    sec.style.cursor = 'pointer';
    sec.addEventListener('click', () => {
        mostrarGridMarca(nombreMarca);
    });
});

// Delegación de eventos dentro del grid para botones “Añadir al carrito”
if (gridProductos) {
    gridProductos.addEventListener('click', (e) => {
        const btn = e.target.closest('.btn-add-carrito');
        if (!btn) return;
        const id = parseInt(btn.dataset.id, 10);
        if (typeof agregarAlCarrito === 'function') {
            agregarAlCarrito(id);
        } else {
            console.warn('agregarAlCarrito no está disponible todavía');
        }
    });
}

// Botón “Volver a la página principal”
if (btnVolver) {
    btnVolver.addEventListener('click', volverInicio);
}


// ---------- RENDER DEL GRID DE UNA MARCA ----------

function mostrarGridMarca(nombreMarca) {
    // Si por lo que sea el catálogo aún no está cargado
    if (!Array.isArray(catalogo) || catalogo.length === 0) {
        console.warn('Catálogo aún no disponible o vacío.');
        return;
    }

    // Ocultar secciones de marcas (home)
    seccionesMarcas.forEach(sec => sec.classList.add('oculto'));

    // Título de la cabecera
    tituloMarca.textContent = `Modelos ${nombreMarca}`;

    // Filtrar catálogo por marca
    const productosMarca = catalogo.filter(p => p.marca === nombreMarca);

    // Limpiar grid
    gridProductos.innerHTML = '';

    // Renderizar tarjetas
    productosMarca.forEach(prod => {
        const card = document.createElement('div');
        card.className = 'tarjeta-zapatilla';

        // Imagen inicial: primer color disponible
        const colorInicial = prod.colores && prod.colores.length ? prod.colores[0] : null;
        const srcInicial = colorInicial ? obtenerSrcImagenProducto(prod.id, colorInicial) : '';

        card.innerHTML = `
            <div class="img-zapatilla">
                ${srcInicial
                ? `<img id="img-${prod.id}" src="${srcInicial}" alt="${prod.nombre}">`
                : `<img id="img-${prod.id}" src="" alt="${prod.nombre}" style="display:none">`}
            </div>
            <h4>${prod.nombre}</h4>
            <div class="precio">${prod.precio.toFixed(2)} €</div>

            <label for="talla-${prod.id}">Talla</label>
            <select id="talla-${prod.id}">
                ${prod.tallas.map(t => `<option value="${t}">${t}</option>`).join('')}
            </select>

            <label for="color-${prod.id}">Color</label>
            <select id="color-${prod.id}">
                ${prod.colores.map(c => `<option value="${c}">${c}</option>`).join('')}
            </select>

            <button class="btn-add-carrito" data-id="${prod.id}">Añadir al carrito</button>
        `;

        gridProductos.appendChild(card);

        const imgZapa = card.querySelector(`#img-${prod.id}`);
        if (imgZapa) {
            // Fallback visual si no existe la imagen pedida
            imgZapa.onerror = () => {
                const fallback = `img/p${prod.id}_default.png`;
                if (!imgZapa.dataset.fallbackTried) {
                    imgZapa.dataset.fallbackTried = '1';
                    imgZapa.src = fallback;
                } else {
                    imgZapa.style.display = 'none';
                }
            };
        }

        // Cambio de color → actualizar imagen
        const selectColor = card.querySelector(`#color-${prod.id}`);
        if (selectColor && imgZapa) {
            selectColor.addEventListener('change', (e) => {
                const nueva = e.target.value;
                imgZapa.style.display = '';
                imgZapa.dataset.fallbackTried = '';
                imgZapa.src = obtenerSrcImagenProducto(prod.id, nueva);
            });
        }
    });

    // Mostrar la vista de marca
    vistaMarca.classList.remove('oculto');
    vistaMarca.scrollIntoView({ behavior: 'smooth', block: 'start' });
}


// ---------- VOLVER A LA PANTALLA PRINCIPAL ----------

function volverInicio() {
    vistaMarca.classList.add('oculto');
    seccionesMarcas.forEach(sec => sec.classList.remove('oculto'));
}
