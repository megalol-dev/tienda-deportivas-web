// ===============================================
//  CARRITO URBANSNEAKERS
//  - estado del carrito
//  - localStorage
//  - badge del header
//  - modal del carrito
//  - toasts
// ===============================================

// ----- ESTADO DEL CARRITO -----
let carrito = [];
let totalProductos = 0;
let totalPrecio = 0;


// ----- TOAST (notificación flotante) -----
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


// ----- PERSISTENCIA EN LOCALSTORAGE -----
const STORAGE_KEY = 'urbanSneakersCarrito';

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

function cargarCarritoDeStorage() {
    try {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (!raw) return;
        const data = JSON.parse(raw);

        if (!data || !Array.isArray(data.items)) return;

        carrito = data.items
            .map(it => {
                const prod = catalogo.find(p => p.id === it.id);
                if (!prod) return null;
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

function recalcularTotales() {
    totalProductos = carrito.reduce((acc, it) => acc + it.cantidad, 0);
    totalPrecio = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);
}


// ----- BADGE DEL CARRITO EN EL HEADER -----
function actualizarBadge() {
    const badge = document.getElementById('badge');
    if (!badge) return;
    const unidades = carrito.reduce((acc, item) => acc + item.cantidad, 0);
    badge.textContent = unidades;
}


// ----- AÑADIR PRODUCTOS AL CARRITO -----
function agregarAlCarrito(idProducto) {
    const prod = catalogo.find(p => p.id === idProducto);
    if (!prod) return;

    // Leemos talla y color seleccionados en la tarjeta correspondiente
    const selectTalla = document.getElementById(`talla-${idProducto}`);
    const selectColor = document.getElementById(`color-${idProducto}`);
    const talla = selectTalla ? selectTalla.value : null;
    const color = selectColor ? selectColor.value : null;

    // Clave para agrupar mismo modelo + talla + color
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


// ----- DOM DEL MODAL DE CARRITO -----
const btnCarrito = document.getElementById('btn-carrito');
const modalCarrito = document.getElementById('modal-carrito');
const modalOverlay = modalCarrito.querySelector('.modal-overlay');
const btnCerrarCarrito = document.getElementById('btn-cerrar-carrito');
const listaCarrito = document.getElementById('carrito-items');
const totalCarritoEl = document.getElementById('carrito-total');
const btnVaciar = document.getElementById('btn-vaciar');
const btnPagar = document.getElementById('btn-pagar'); // llama a abrirCheckout()


// ----- FUNCIONES MODAL -----
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


// ----- LISTENERS DEL CARRITO -----
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

// Delegación: quitar producto
if (listaCarrito) {
    listaCarrito.addEventListener('click', (e) => {
        const btn = e.target.closest('.btn-quitar');
        if (!btn) return;
        const clave = btn.dataset.clave;

        carrito = carrito.filter(it => it.clave !== clave);

        guardarCarritoEnStorage();
        recalcularTotales();
        actualizarBadge();
        renderizarCarritoModal();
    });
}

// Vaciar carrito
if (btnVaciar) {
    btnVaciar.addEventListener('click', () => {
        carrito = [];
        guardarCarritoEnStorage();
        recalcularTotales();
        actualizarBadge();
        renderizarCarritoModal();
    });
}

// "Pagar ahora" → abre la pantalla de checkout
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


// ----- INICIALIZACIÓN -----
cargarCarritoDeStorage();
recalcularTotales();
actualizarBadge();
