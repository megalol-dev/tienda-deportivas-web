/* =======================================================
   DATOS PRINCIPALES DE LA TIENDA URBANSNEAKERS
   ======================================================= */

/*
   - catálogos de productos divididos por marca
   - cada producto tiene:
       nombre → nombre comercial del modelo
       precio → en euros
       talla  → se elegirá luego (aquí guardamos las disponibles)
       color  → opciones disponibles para seleccionar en el checkout
*/

// ======== CATÁLOGO GENERAL ========
const catalogo = [
    // ===== NIKE =====
    { id: 1, marca: "Nike", nombre: "Nike Air Max 90", precio: 119.99, tallas: [38, 39, 40, 41, 42, 43], colores: ["Blanco", "Negro", "Rojo"] },
    { id: 2, marca: "Nike", nombre: "Nike Air Force 1", precio: 109.99, tallas: [37, 38, 39, 40, 41, 42], colores: ["Blanco", "Azul", "Gris"] },
    { id: 3, marca: "Nike", nombre: "Nike Zoom Fly", precio: 129.99, tallas: [39, 40, 41, 42, 43, 44], colores: ["Negro", "Rojo", "Verde"] },
    { id: 4, marca: "Nike", nombre: "Nike Revolution 6", precio: 89.99, tallas: [36, 37, 38, 39, 40, 41], colores: ["Negro", "Blanco", "Rosa"] },
    { id: 5, marca: "Nike", nombre: "Nike Air Jordan 1", precio: 149.99, tallas: [40, 41, 42, 43, 44], colores: ["Rojo", "Blanco", "Negro"] },
    { id: 6, marca: "Nike", nombre: "Nike Pegasus Trail", precio: 134.99, tallas: [38, 39, 40, 41, 42, 43], colores: ["Verde", "Negro", "Gris"] },
    { id: 7, marca: "Nike", nombre: "Nike Waffle Trainer", precio: 99.99, tallas: [37, 38, 39, 40, 41, 42], colores: ["Amarillo", "Blanco", "Negro"] },
    { id: 8, marca: "Nike", nombre: "Nike Blazer Mid", precio: 114.99, tallas: [39, 40, 41, 42, 43], colores: ["Blanco", "Negro", "Verde"] },
    { id: 9, marca: "Nike", nombre: "Nike Air Huarache", precio: 124.99, tallas: [38, 39, 40, 41, 42], colores: ["Azul", "Negro", "Gris"] },
    { id: 10, marca: "Nike", nombre: "Nike Free Run 5.0", precio: 99.99, tallas: [36, 37, 38, 39, 40], colores: ["Negro", "Rosa", "Blanco"] },

    // ===== ADIDAS =====
    { id: 11, marca: "Adidas", nombre: "Adidas Ultraboost", precio: 139.99, tallas: [38, 39, 40, 41, 42], colores: ["Blanco", "Negro", "Azul"] },
    { id: 12, marca: "Adidas", nombre: "Adidas Superstar", precio: 99.99, tallas: [37, 38, 39, 40, 41], colores: ["Blanco", "Negro", "Dorado"] },
    { id: 13, marca: "Adidas", nombre: "Adidas Stan Smith", precio: 89.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Verde", "Azul"] },
    { id: 14, marca: "Adidas", nombre: "Adidas NMD R1", precio: 129.99, tallas: [38, 39, 40, 41, 42], colores: ["Negro", "Gris", "Rojo"] },
    { id: 15, marca: "Adidas", nombre: "Adidas Forum Low", precio: 109.99, tallas: [37, 38, 39, 40, 41], colores: ["Blanco", "Azul", "Rojo"] },
    { id: 16, marca: "Adidas", nombre: "Adidas Gazelle", precio: 94.99, tallas: [38, 39, 40, 41, 42], colores: ["Verde", "Azul", "Negro"] },
    { id: 17, marca: "Adidas", nombre: "Adidas ZX 2K Boost", precio: 124.99, tallas: [39, 40, 41, 42, 43], colores: ["Negro", "Gris", "Naranja"] },
    { id: 18, marca: "Adidas", nombre: "Adidas OZWEEGO", precio: 114.99, tallas: [37, 38, 39, 40, 41], colores: ["Verde", "Gris", "Negro"] },
    { id: 19, marca: "Adidas", nombre: "Adidas Continental 80", precio: 84.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Rosa", "Negro"] },
    { id: 20, marca: "Adidas", nombre: "Adidas 4DFWD", precio: 159.99, tallas: [39, 40, 41, 42, 43], colores: ["Negro", "Blanco", "Gris"] },

    // ===== PUMA =====
    { id: 21, marca: "Puma", nombre: "Puma Suede Classic", precio: 89.99, tallas: [37, 38, 39, 40, 41], colores: ["Negro", "Rojo", "Azul"] },
    { id: 22, marca: "Puma", nombre: "Puma RS-X", precio: 119.99, tallas: [38, 39, 40, 41, 42], colores: ["Blanco", "Negro", "Verde"] },
    { id: 23, marca: "Puma", nombre: "Puma Cali", precio: 99.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Rosa", "Negro"] },
    { id: 24, marca: "Puma", nombre: "Puma Future Rider", precio: 109.99, tallas: [38, 39, 40, 41, 42], colores: ["Azul", "Gris", "Rojo"] },
    { id: 25, marca: "Puma", nombre: "Puma Mirage Sport", precio: 124.99, tallas: [37, 38, 39, 40, 41], colores: ["Negro", "Naranja", "Gris"] },
    { id: 26, marca: "Puma", nombre: "Puma Carina Street", precio: 94.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Rosa", "Negro"] },
    { id: 27, marca: "Puma", nombre: "Puma X-Ray 2", precio: 114.99, tallas: [39, 40, 41, 42, 43], colores: ["Verde", "Azul", "Negro"] },
    { id: 28, marca: "Puma", nombre: "Puma R78", precio: 89.99, tallas: [37, 38, 39, 40, 41], colores: ["Gris", "Rojo", "Azul"] },
    { id: 29, marca: "Puma", nombre: "Puma Smash 3.0", precio: 79.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Negro", "Azul"] },
    { id: 30, marca: "Puma", nombre: "Puma Mayze Stack", precio: 129.99, tallas: [37, 38, 39, 40, 41], colores: ["Negro", "Rosa", "Gris"] },

    // ===== REEBOK =====
    { id: 31, marca: "Reebok", nombre: "Reebok Classic Leather", precio: 94.99, tallas: [37, 38, 39, 40, 41], colores: ["Blanco", "Beige", "Negro"] },
    { id: 32, marca: "Reebok", nombre: "Reebok Club C 85", precio: 99.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Verde", "Gris"] },
    { id: 33, marca: "Reebok", nombre: "Reebok Zig Dynamica", precio: 114.99, tallas: [38, 39, 40, 41, 42], colores: ["Negro", "Naranja", "Gris"] },
    { id: 34, marca: "Reebok", nombre: "Reebok Nano X3", precio: 129.99, tallas: [39, 40, 41, 42, 43], colores: ["Gris", "Verde", "Negro"] },
    { id: 35, marca: "Reebok", nombre: "Reebok Floatride", precio: 124.99, tallas: [38, 39, 40, 41, 42], colores: ["Azul", "Negro", "Blanco"] },
    { id: 36, marca: "Reebok", nombre: "Reebok Royal Glide", precio: 89.99, tallas: [36, 37, 38, 39, 40], colores: ["Blanco", "Rosa", "Negro"] },
    { id: 37, marca: "Reebok", nombre: "Reebok DMX Trail", precio: 139.99, tallas: [39, 40, 41, 42, 43], colores: ["Negro", "Verde", "Gris"] },
    { id: 38, marca: "Reebok", nombre: "Reebok Energen Lite", precio: 104.99, tallas: [37, 38, 39, 40, 41], colores: ["Blanco", "Azul", "Negro"] },
    { id: 39, marca: "Reebok", nombre: "Reebok Classic Nylon", precio: 84.99, tallas: [36, 37, 38, 39, 40], colores: ["Azul", "Gris", "Negro"] },
    { id: 40, marca: "Reebok", nombre: "Reebok Zig Kinetica 2", precio: 149.99, tallas: [39, 40, 41, 42, 43], colores: ["Negro", "Rojo", "Gris"] },

    // ===== NEW BALANCE =====
    { id: 41, marca: "New Balance", nombre: "New Balance 574", precio: 99.99, tallas: [37, 38, 39, 40, 41], colores: ["Gris", "Negro", "Azul"] },
    { id: 42, marca: "New Balance", nombre: "New Balance 327", precio: 119.99, tallas: [38, 39, 40, 41, 42], colores: ["Beige", "Verde", "Blanco"] },
    { id: 43, marca: "New Balance", nombre: "New Balance 550", precio: 124.99, tallas: [39, 40, 41, 42, 43], colores: ["Blanco", "Rojo", "Azul"] },
    { id: 44, marca: "New Balance", nombre: "New Balance Fresh Foam", precio: 129.99, tallas: [38, 39, 40, 41, 42], colores: ["Gris", "Azul", "Negro"] },
    { id: 45, marca: "New Balance", nombre: "New Balance 997H", precio: 114.99, tallas: [37, 38, 39, 40, 41], colores: ["Verde", "Gris", "Blanco"] },
    { id: 46, marca: "New Balance", nombre: "New Balance 1080v12", precio: 139.99, tallas: [39, 40, 41, 42, 43], colores: ["Negro", "Azul", "Blanco"] },
    { id: 47, marca: "New Balance", nombre: "New Balance 237", precio: 109.99, tallas: [37, 38, 39, 40, 41], colores: ["Gris", "Negro", "Rojo"] },
    { id: 48, marca: "New Balance", nombre: "New Balance XC-72", precio: 129.99, tallas: [38, 39, 40, 41, 42], colores: ["Azul", "Beige", "Negro"] },
    { id: 49, marca: "New Balance", nombre: "New Balance 2002R", precio: 134.99, tallas: [39, 40, 41, 42, 43], colores: ["Gris", "Negro", "Verde"] },
    { id: 50, marca: "New Balance", nombre: "New Balance 990v6", precio: 159.99, tallas: [38, 39, 40, 41, 42], colores: ["Blanco", "Gris", "Negro"] }
];

/* =======================================================
   VARIABLES DE CONTROL DEL CARRITO
   ======================================================= */

// Carrito vacío que se llenará al pulsar “Añadir”
let carrito = [];

// Contadores que cambiarán durante la sesión
let totalProductos = 0;
let totalPrecio = 0;

/* =======================================================
   UTILIDADES DEL CARRITO (badge y alta)
   ======================================================= */

// sección: función que actualiza el badge del carrito
function actualizarBadge() {
    const badge = document.getElementById('badge');
    const unidades = carrito.reduce((acc, item) => acc + item.cantidad, 0);
    badge.textContent = unidades;
}

// sección: función que agrega (o acumula) productos al carrito
function agregarAlCarrito(idProducto) {
    const prod = catalogo.find(p => p.id === idProducto);
    if (!prod) return;

    // Leemos talla y color seleccionados en la tarjeta correspondiente
    const selectTalla = document.getElementById(`talla-${idProducto}`);
    const selectColor = document.getElementById(`color-${idProducto}`);
    const talla = selectTalla ? selectTalla.value : null;
    const color = selectColor ? selectColor.value : null;

    // Clave compuesta para agrupar mismo modelo + talla + color
    const clave = `${idProducto}-${talla}-${color}`;
    const existente = carrito.find(it => it.clave === clave);

    if (existente) {
        existente.cantidad += 1;
    } else {
        carrito.push({
            clave,
            id: prod.id,
            marca: prod.marca,
            nombre: prod.nombre,
            precio: prod.precio,
            talla,
            color,
            cantidad: 1
        });
    }

    guardarCarritoEnStorage();
    recalcularTotales();
    actualizarBadge();
    mostrarToast(`🛒 Añadido: ${prod.nombre}`, 'success');
}


/* =======================================================
   TOAST (notificación flotante)
   ======================================================= */

const TOAST_DURATION_MS = 2000;

// Crea (si no existe) y devuelve el contenedor
function getToastContainer() {
    let container = document.getElementById('toast-container');
    if (!container) {
        // fallback por si alguien olvidó el div en HTML
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

    // Eliminarlo del DOM tras la duración + margen de animación
    setTimeout(() => {
        toast.remove();
    }, TOAST_DURATION_MS + 400);
}


/* =======================================================
   PERSISTENCIA DEL CARRITO(localStorage)
    ======================================================= */

// Clave única para almacenar el carrito
const STORAGE_KEY = 'urbanSneakersCarrito';

// sección: guarda el carrito actual en localStorage
function guardarCarritoEnStorage() {
    try {
        const payload = {
            items: carrito.map(it => ({
                clave: it.clave,
                id: it.id,
                talla: it.talla,
                color: it.color,
                cantidad: it.cantidad
            }))
        };
        localStorage.setItem(STORAGE_KEY, JSON.stringify(payload));
    } catch (e) {
        console.warn('No se pudo guardar el carrito en localStorage:', e);
    }
}

// sección: carga el carrito desde localStorage (si existe y es válido)
function cargarCarritoDeStorage() {
    try {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (!raw) return;
        const data = JSON.parse(raw);

        if (!data || !Array.isArray(data.items)) return;

        // Reconstruimos el carrito validando contra el catálogo
        carrito = data.items
            .map(it => {
                const prod = catalogo.find(p => p.id === it.id);
                if (!prod) return null; // ignora items de productos que ya no existen
                return {
                    clave: it.clave || `${prod.id}-${it.talla}-${it.color}`,
                    id: prod.id,
                    marca: prod.marca,
                    nombre: prod.nombre,
                    precio: prod.precio,
                    talla: it.talla ?? null,
                    color: it.color ?? null,
                    cantidad: Number(it.cantidad) > 0 ? Number(it.cantidad) : 1
                };
            })
            .filter(Boolean);
    } catch (e) {
        console.warn('No se pudo cargar el carrito de localStorage:', e);
    }
}

// sección: recalcula totales a partir del carrito (evita desajustes)
function recalcularTotales() {
    totalProductos = carrito.reduce((acc, it) => acc + it.cantidad, 0);
    totalPrecio = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
}


/* =======================================================
   RENDERIZADO LISTA GENERAL (desactivada en home)
   ======================================================= */

// sección: render de lista completa (NO se llama al cargar)
function renderizarProductos() {
    const contenedorLista = document.getElementById("lista");
    contenedorLista.innerHTML = "<h2>Productos disponibles</h2>";

    catalogo.forEach((producto) => {
        const productoHTML = `
      <div class="tarjeta-producto">
        <h3>${producto.nombre}</h3>
        <p><strong>Marca:</strong> ${producto.marca}</p>
        <p><strong>Precio:</strong> ${producto.precio.toFixed(2)} €</p>
        <button class="boton-agregar" data-id="${producto.id}">
          Añadir al carrito
        </button>
      </div>
    `;
        contenedorLista.innerHTML += productoHTML;
    });
}

// NO renderizar en la carga
// renderizarProductos();


/* =======================================================
   HELPERS DE IMAGEN (convención p<ID>_<color>.png)
   ======================================================= */

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


/* =======================================================
   CACHE DEL DOM Y SPA DE MARCAS
   ======================================================= */

// sección: cache de elementos del DOM
const seccionesMarcas = document.querySelectorAll('.seccion-deportivas'); // Nike, Adidas, etc.
const vistaMarca = document.getElementById('vista-marca');
const tituloMarca = document.getElementById('titulo-marca');
const gridProductos = document.getElementById('grid-productos');
const btnVolver = document.getElementById('btn-volver');

// Carrito modal
const btnCarrito = document.getElementById('btn-carrito');
const modalCarrito = document.getElementById('modal-carrito');
const modalOverlay = modalCarrito.querySelector('.modal-overlay');
const btnCerrarCarrito = document.getElementById('btn-cerrar-carrito');
const listaCarrito = document.getElementById('carrito-items');
const totalCarritoEl = document.getElementById('carrito-total');
const btnVaciar = document.getElementById('btn-vaciar');
const btnPagar = document.getElementById('btn-pagar'); // placeholder


// sección: mapa ID de sección -> nombre de marca
const mapaMarcas = {
    'nike': 'Nike',
    'adidas': 'Adidas',
    'puma': 'Puma',
    'reebok': 'Reebok',
    'new-balance': 'New Balance'
};

// sección que conecta las tarjetas de marcas (home) con su grid
seccionesMarcas.forEach(sec => {
    const nombreMarca = mapaMarcas[sec.id];
    if (!nombreMarca) return; // por si hay otras secciones
    sec.style.cursor = 'pointer';
    sec.addEventListener('click', () => {
        mostrarGridMarca(nombreMarca);
    });
});

// sección: delegación de eventos en el grid (para botones dinámicos)
gridProductos.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-add-carrito');
    if (!btn) return;
    const id = parseInt(btn.dataset.id, 10);
    agregarAlCarrito(id);
});

// sección: botón Volver (regresa a las marcas)
btnVolver.addEventListener('click', volverInicio);

// Abrir carrito
btnCarrito.addEventListener('click', (e) => {
    e.preventDefault();
    abrirModalCarrito();
});

// Cerrar carrito (botón X y clic fuera)
btnCerrarCarrito.addEventListener('click', cerrarModalCarrito);
modalOverlay.addEventListener('click', cerrarModalCarrito);

// Cerrar con ESC
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') cerrarModalCarrito();
});

// Delegación: quitar producto
listaCarrito.addEventListener('click', (e) => {
    const btn = e.target.closest('.btn-quitar');
    if (!btn) return;
    const clave = btn.dataset.clave;

    // Eliminar línea completa del carrito
    carrito = carrito.filter(it => it.clave !== clave);

    guardarCarritoEnStorage();
    recalcularTotales();
    actualizarBadge();
    renderizarCarritoModal();
});

// Vaciar carrito por completo
btnVaciar.addEventListener('click', () => {
    carrito = [];
    guardarCarritoEnStorage();
    recalcularTotales();
    actualizarBadge();
    renderizarCarritoModal();
});

// Pagar ahora (placeholder sin lógica, lo implementaremos después)
// Abrir checkout (desde modal)
btnPagar.addEventListener('click', (e) => {
    e.preventDefault();
    abrirCheckout();
});


/* =======================================================
   SECCIÓN QUE MUESTRA LOS GRID (por marca)
   ======================================================= */

// sección: render del grid de una marca concreta
function mostrarGridMarca(nombreMarca) {
    // Ocultar secciones de marcas (home)
    seccionesMarcas.forEach(sec => sec.classList.add('oculto'));

    // Preparar cabecera de la vista de marca
    tituloMarca.textContent = `Modelos ${nombreMarca}`;

    // Filtrar catálogo por marca
    const productosMarca = catalogo.filter(p => p.marca === nombreMarca);

    // Limpiar grid
    gridProductos.innerHTML = '';

    // Renderizar tarjetas (5 columnas gracias a .rejilla-5)
    productosMarca.forEach(prod => {
        const card = document.createElement('div');
        card.className = 'tarjeta-zapatilla';

        // Imagen inicial: primer color disponible (si existe)
        const colorInicial = prod.colores && prod.colores.length ? prod.colores[0] : null;
        const srcInicial = colorInicial ? obtenerSrcImagenProducto(prod.id, colorInicial) : '';

        // Tarjeta con imagen, datos, selects y botón de carrito
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

        // 👉 Fallback visual si no existe la imagen pedida: intenta default o esconde img
        const imgZapa = card.querySelector(`#img-${prod.id}`);
        imgZapa.onerror = () => {
            const fallback = `img/p${prod.id}_default.png`;
            if (!imgZapa.dataset.fallbackTried) {
                imgZapa.dataset.fallbackTried = '1';
                imgZapa.src = fallback;
            } else {
                imgZapa.style.display = 'none';
            }
        };

        // Cambio de color -> actualizar imagen
        const selectColor = card.querySelector(`#color-${prod.id}`);
        selectColor.addEventListener('change', (e) => {
            const nueva = e.target.value;
            imgZapa.style.display = '';         // por si se ocultó antes
            imgZapa.dataset.fallbackTried = ''; // resetea intento de fallback
            imgZapa.src = obtenerSrcImagenProducto(prod.id, nueva);
        });
    });

    // Mostrar la vista de marca
    vistaMarca.classList.remove('oculto');

    // Llevar la vista hacia arriba (opcional)
    vistaMarca.scrollIntoView({ behavior: 'smooth', block: 'start' });
}


/* =======================================================
   SECCIÓN BOTÓN VOLVER (restaurar home)
   ======================================================= */

// sección: función volver a la pantalla principal
function volverInicio() {
    vistaMarca.classList.add('oculto'); // ocultar grid de marca
    seccionesMarcas.forEach(sec => sec.classList.remove('oculto')); // mostrar marcas
}


// Abre el modal del carrito
function abrirModalCarrito() {
    renderizarCarritoModal();
    modalCarrito.classList.add('modal-visible');
}

// Cierra el modal del carrito
function cerrarModalCarrito() {
    modalCarrito.classList.remove('modal-visible');
}

// Pinta las líneas del carrito en el modal
function renderizarCarritoModal() {
    if (!carrito.length) {
        listaCarrito.innerHTML = `
      <div style="padding:18px; text-align:center; color:#666;">
        Tu carrito está vacío.
      </div>`;
        totalCarritoEl.textContent = '0,00 €';
        return;
    }

    // Construimos cada fila
    listaCarrito.innerHTML = '';
    carrito.forEach(item => {
        // Importe por línea
        const importe = (item.precio * item.cantidad).toFixed(2).replace('.', ',');
        const precioU = item.precio.toFixed(2).replace('.', ',');

        // Miniatura basada en convención p<ID>_<color>.png con fallback
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

        // manejar fallback por JS (si falla la imagen, probar default y luego ocultar)
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

    // Total
    const total = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
    totalCarritoEl.textContent = `${total.toFixed(2).replace('.', ',')} €`;
}


/* =======================================================
   CHECKOUT (resumen + formulario simulado)
   ======================================================= */

const sectionCheckout = document.getElementById('checkout');
const resumenLineas = document.getElementById('resumen-lineas');
const resumenSubtotalEl = document.getElementById('resumen-subtotal');
const resumenEnvioEl = document.getElementById('resumen-envio');
const resumenIvaEl = document.getElementById('resumen-iva');
const resumenTotalEl = document.getElementById('resumen-total');
const formularioCheckout = document.getElementById('formulario-checkout');
const btnCancelarCheckout = document.getElementById('btn-cancelar-checkout');
const confirmacionSection = document.getElementById('checkout-confirmacion');
const confirmIdEl = document.getElementById('confirm-id');
const btnConfirmVolver = document.getElementById('confirm-volver');

// Abrir pantalla de checkout (desde modal Pagar ahora)
function abrirCheckout() {
    // Cerramos modal si está abierto
    try { modalCarrito.classList.remove('modal-visible'); } catch (e) { }
    renderizarResumenPedido();
    sectionCheckout.classList.remove('oculto');
    sectionCheckout.scrollIntoView({ behavior: 'smooth', block: 'start' });
}

// Cerrar checkout y volver a la tienda (sin vaciar carrito)
function cerrarCheckout() {
    sectionCheckout.classList.add('oculto');
    confirmacionSection.classList.add('oculto');
}

// Render del resumen del pedido (con cálculos)
function renderizarResumenPedido() {
    resumenLineas.innerHTML = '';
    if (!carrito.length) {
        resumenLineas.innerHTML = `<div style="padding:18px; color:#666; text-align:center;">Tu carrito está vacío.</div>`;
        resumenSubtotalEl.textContent = '0,00 €';
        resumenEnvioEl.textContent = '0,00 €';
        resumenIvaEl.textContent = '0,00 €';
        resumenTotalEl.textContent = '0,00 €';
        return;
    }

    // Subtotal
    const subtotal = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);

    carrito.forEach(item => {
        const importe = (item.precio * item.cantidad).toFixed(2).replace('.', ',');
        const precioU = item.precio.toFixed(2).replace('.', ',');
        const srcImg = item.color ? obtenerSrcImagenProducto(item.id, item.color) : '';

        const div = document.createElement('div');
        div.className = 'linea-resumen';
        div.innerHTML = `
      <div class="thumb-zapa">
        ${srcImg ? `<img class="thumb-img" src="${srcImg}" alt="${item.marca} ${item.nombre}">` : `Foto`}
      </div>
      <div class="info">
        <strong>${item.marca} — ${item.nombre}</strong>
        <small>Talla: ${item.talla ?? '-'}</small>
        <small>Color: ${item.color ?? '-'}</small>
        <small>Precio: ${precioU} € · Cantidad: ${item.cantidad}</small>
      </div>
      <div class="importe">${importe} €</div>
    `;

        const img = div.querySelector('.thumb-img');
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

        resumenLineas.appendChild(div);
    });

    // Gastos de envío (regla simple)
    const envio = subtotal > 0 ? 5.00 : 0.00; // puedes ajustar o calcular según peso/ubicación
    // IVA
    const iva = subtotal * 0.21;
    const total = subtotal + envio + iva;

    resumenSubtotalEl.textContent = `${subtotal.toFixed(2).replace('.', ',')} €`;
    resumenEnvioEl.textContent = `${envio.toFixed(2).replace('.', ',')} €`;
    resumenIvaEl.textContent = `${iva.toFixed(2).replace('.', ',')} €`;
    resumenTotalEl.textContent = `${total.toFixed(2).replace('.', ',')} €`;
}

// Validación básica del formulario (devuelve true si OK)
function validarFormularioCheckout() {
    // usar el constraint validation API cuando sea posible
    if (!formularioCheckout.checkValidity()) {
        mostrarToast('Rellena los campos obligatorios correctamente.', 'error');
        return false;
    }
    // chequeo adicional: CP 5 dígitos
    const cp = document.getElementById('cp-c').value.trim();
    if (!/^\d{5}$/.test(cp)) {
        mostrarToast('El código postal debe tener 5 dígitos.', 'error');
        return false;
    }
    // checkbox términos
    const acepto = document.getElementById('acepto-terminos').checked;
    if (!acepto) {
        mostrarToast('Debes aceptar los Términos y Condiciones.', 'error');
        return false;
    }
    return true;
}

// Manejo del envío (simulado)
formularioCheckout.addEventListener('submit', (e) => {
    e.preventDefault();
    if (!validarFormularioCheckout()) return;

    // crear objeto pedido simulado
    const pedido = {
        id: `PED-${Date.now()}`,
        fecha: new Date().toISOString(),
        items: carrito.map(it => ({ id: it.id, nombre: it.nombre, talla: it.talla, color: it.color, cantidad: it.cantidad, precio: it.precio })),
        nombres: document.getElementById('nombre-c').value.trim(),
        apellidos: document.getElementById('apellidos-c').value.trim(),
        email: document.getElementById('email-c').value.trim(),
        telefono: document.getElementById('telefono-c').value.trim(),
        direccion: document.getElementById('direccion1-c').value.trim(),
        ciudad: document.getElementById('ciudad-c').value.trim(),
        provincia: document.getElementById('provincia-c').value.trim(),
        cp: document.getElementById('cp-c').value.trim(),
        pais: document.getElementById('pais-c').value
    };

    // Simulación: "procesamos" el pedido
    mostrarToast(`✅ Pedido simulado: ${pedido.id}`, 'success');

    // Mostramos confirmación en la UI con ID
    confirmIdEl.textContent = pedido.id;
    confirmacionSection.classList.remove('oculto');

    // Vaciamos carrito y persistimos
    carrito = [];
    guardarCarritoEnStorage();
    recalcularTotales();
    actualizarBadge();

    // refrescar resumen (mostrará vacío)
    renderizarResumenPedido();

    // opcional: limpiar formulario
    formularioCheckout.reset();

    // ocultar la parte del formulario/resumen principal
    sectionCheckout.classList.add('oculto');
});

// cancelar checkout (volver a tienda)
btnCancelarCheckout.addEventListener('click', () => {
    cerrarCheckout();
});

// volver desde confirmación a la tienda (vuelve al home)
btnConfirmVolver.addEventListener('click', () => {
    confirmacionSection.classList.add('oculto');
    cerrarCheckout();
    volverInicio();
});


/* =======================================================
   INICIALIZACIONES FINALES
   ======================================================= */

// Cargar carrito guardado, recalcular y pintar badge
cargarCarritoDeStorage();
recalcularTotales();
actualizarBadge();




/* =======================================================
   INICIALIZACIONES FINALES
   ======================================================= */

// Cargar carrito guardado, recalcular y pintar badge
cargarCarritoDeStorage();
recalcularTotales();
actualizarBadge();
