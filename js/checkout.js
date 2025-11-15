// ===============================================
//  CHECKOUT / FORMULARIO DE ENVÍO
//  - pantalla de checkout
//  - resumen del pedido
//  - validación del formulario
//  - confirmación de pedido simulado
// ===============================================

const sectionCheckout = document.getElementById('checkout');
const checkoutContainer = document.querySelector('.checkout-container');
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


// ======================================================
//  BLOQUEO / DESBLOQUEO DE NAVEGACIÓN DURANTE CHECKOUT
// ======================================================

function bloquearNavegacionCheckout() {
    // Ocultar secciones de marcas (home)
    document.querySelectorAll('.seccion-deportivas').forEach(sec => {
        sec.classList.add('oculto');
    });

    // Ocultar vista de marca si estaba abierta
    const vistaMarca = document.getElementById('vista-marca');
    if (vistaMarca) vistaMarca.classList.add('oculto');

    // Desactivar todos los enlaces del header (Usuario, Carrito, etc.)
    document.querySelectorAll('header a').forEach(a => {
        a.style.pointerEvents = 'none';
        a.style.opacity = '0.4';
    });
}

function desbloquearNavegacionCheckout() {
    // Mostrar secciones de marcas (home)
    document.querySelectorAll('.seccion-deportivas').forEach(sec => {
        sec.classList.remove('oculto');
    });

    // Reactivar enlaces del header
    document.querySelectorAll('header a').forEach(a => {
        a.style.pointerEvents = 'auto';
        a.style.opacity = '1';
    });
}


// ---------- ABRIR / CERRAR CHECKOUT ----------

// Se llama desde el botón "Pagar ahora" (carrito.js)
function abrirCheckout() {
    bloquearNavegacionCheckout();

    // Cerramos modal del carrito si está abierto
    try {
        if (typeof modalCarrito !== 'undefined') {
            modalCarrito.classList.remove('modal-visible');
        }
    } catch (e) {
        console.warn('No se pudo cerrar el modal del carrito al abrir checkout:', e);
    }

    // Aseguramos que el formulario/resumen esté visible
    if (checkoutContainer) {
        checkoutContainer.classList.remove('oculto');
        checkoutContainer.style.display = ''; // por si quedó en none
    }

    // Y la confirmación oculta al entrar
    if (confirmacionSection) {
        confirmacionSection.classList.add('oculto');
    }

    renderizarResumenPedido();

    if (sectionCheckout) {
        sectionCheckout.classList.remove('oculto');
        sectionCheckout.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
}

// Cerrar checkout y dejarlo listo para la próxima vez
function cerrarCheckout() {
    if (sectionCheckout) {
        sectionCheckout.classList.add('oculto');
    }
    if (checkoutContainer) {
        checkoutContainer.classList.remove('oculto');
        checkoutContainer.style.display = ''; // restauramos el display
    }
    if (confirmacionSection) {
        confirmacionSection.classList.add('oculto');
    }
}


// ---------- RESUMEN DEL PEDIDO ----------

function renderizarResumenPedido() {
    if (!resumenLineas ||
        !resumenSubtotalEl || !resumenEnvioEl ||
        !resumenIvaEl || !resumenTotalEl) return;

    resumenLineas.innerHTML = '';

    if (!carrito.length) {
        resumenLineas.innerHTML = `
            <div style="padding:18px; color:#666; text-align:center;">
                Tu carrito está vacío.
            </div>`;
        resumenSubtotalEl.textContent = '0,00 €';
        resumenEnvioEl.textContent = '0,00 €';
        resumenIvaEl.textContent = '0,00 €';
        resumenTotalEl.textContent = '0,00 €';
        return;
    }

    const subtotal = carrito.reduce((acc, it) => acc + it.precio * it.cantidad, 0);

    carrito.forEach(item => {
        const importe = (item.precio * item.cantidad).toFixed(2).replace('.', ',');
        const precioU = item.precio.toFixed(2).replace('.', ',');
        const srcImg = item.color ? obtenerSrcImagenProducto(item.id, item.color) : '';

        const div = document.createElement('div');
        div.className = 'linea-resumen';
        div.innerHTML = `
            <div class="thumb-zapa">
                ${srcImg
                ? `<img class="thumb-img" src="${srcImg}" alt="${item.marca} ${item.nombre}">`
                : `Foto`}
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

    const envio = subtotal > 0 ? 5.00 : 0.00;
    const iva = subtotal * 0.21;
    const total = subtotal + envio + iva;

    resumenSubtotalEl.textContent = `${subtotal.toFixed(2).replace('.', ',')} €`;
    resumenEnvioEl.textContent = `${envio.toFixed(2).replace('.', ',')} €`;
    resumenIvaEl.textContent = `${iva.toFixed(2).replace('.', ',')} €`;
    resumenTotalEl.textContent = `${total.toFixed(2).replace('.', ',')} €`;
}


// ---------- VALIDACIÓN DEL FORMULARIO ----------

function validarFormularioCheckout() {
    if (!formularioCheckout) return false;

    function mostrarErrorCampo(input, mensaje) {
        if (typeof mostrarToast === 'function') {
            mostrarToast(mensaje, 'error');
        }
        if (input && typeof input.focus === 'function') {
            input.focus();
            try {
                input.scrollIntoView({ behavior: 'smooth', block: 'center' });
            } catch (e) { }
        }
        return false;
    }

    const nombre = document.getElementById('nombre-c');
    const apellidos = document.getElementById('apellidos-c');
    const email = document.getElementById('email-c');
    const telefono = document.getElementById('telefono-c');
    const dir1 = document.getElementById('direccion1-c');
    const ciudad = document.getElementById('ciudad-c');
    const provincia = document.getElementById('provincia-c');
    const cpInput = document.getElementById('cp-c');
    const paisSelect = document.getElementById('pais-c');
    const acepto = document.getElementById('acepto-terminos');

    // Nombre
    if (!nombre || !nombre.value.trim()) {
        return mostrarErrorCampo(nombre, 'El campo "Nombre" es obligatorio.');
    }

    // Apellidos
    if (!apellidos || !apellidos.value.trim()) {
        return mostrarErrorCampo(apellidos, 'El campo "Apellidos" es obligatorio.');
    }

    // Email
    const emailVal = email ? email.value.trim() : '';
    if (!emailVal) {
        return mostrarErrorCampo(email, 'El campo "Correo electrónico" es obligatorio.');
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(emailVal)) {
        return mostrarErrorCampo(email, 'Introduce un correo electrónico válido (debe contener "@" y un dominio).');
    }

    // Teléfono
    const telVal = telefono ? telefono.value.trim() : '';
    if (!telVal) {
        return mostrarErrorCampo(telefono, 'El campo "Teléfono" es obligatorio.');
    }
    const soloDigitosTel = telVal.replace(/\D/g, '');
    if (soloDigitosTel.length < 9) {
        return mostrarErrorCampo(telefono, 'El teléfono debe tener al menos 9 dígitos numéricos.');
    }

    // Dirección 1
    if (!dir1 || !dir1.value.trim()) {
        return mostrarErrorCampo(dir1, 'El campo "Dirección (línea 1)" es obligatorio.');
    }

    // Ciudad
    if (!ciudad || !ciudad.value.trim()) {
        return mostrarErrorCampo(ciudad, 'El campo "Ciudad" es obligatorio.');
    }

    // Provincia
    if (!provincia || !provincia.value.trim()) {
        return mostrarErrorCampo(provincia, 'El campo "Provincia" es obligatorio.');
    }

    // Código postal
    const cp = cpInput ? cpInput.value.trim() : '';
    if (!cp) {
        return mostrarErrorCampo(cpInput, 'El campo "Código postal" es obligatorio.');
    }
    if (!/^\d{5}$/.test(cp)) {
        return mostrarErrorCampo(cpInput, 'El código postal debe tener exactamente 5 dígitos numéricos.');
    }

    // País
    if (!paisSelect || !paisSelect.value) {
        return mostrarErrorCampo(paisSelect, 'Selecciona un país para el envío.');
    }

    // Aceptación de términos
    if (!acepto || !acepto.checked) {
        return mostrarErrorCampo(acepto, 'Debes aceptar los Términos y Condiciones antes de continuar.');
    }

    return true;
}


// ---------- ENVÍO SIMULADO DEL FORMULARIO ----------

if (formularioCheckout) {
    formularioCheckout.addEventListener('submit', (e) => {
        e.preventDefault();
        if (!validarFormularioCheckout()) return;

        const pedido = {
            id: `PED-${Date.now()}`,
            fecha: new Date().toISOString(),
            items: carrito.map(it => ({
                id: it.id,
                nombre: it.nombre,
                talla: it.talla,
                color: it.color,
                cantidad: it.cantidad,
                precio: it.precio
            })),
            nombres: document.getElementById('nombre-c')?.value.trim(),
            apellidos: document.getElementById('apellidos-c')?.value.trim(),
            email: document.getElementById('email-c')?.value.trim(),
            telefono: document.getElementById('telefono-c')?.value.trim(),
            direccion: document.getElementById('direccion1-c')?.value.trim(),
            ciudad: document.getElementById('ciudad-c')?.value.trim(),
            provincia: document.getElementById('provincia-c')?.value.trim(),
            cp: document.getElementById('cp-c')?.value.trim(),
            pais: document.getElementById('pais-c')?.value
        };

        if (typeof mostrarToast === 'function') {
            mostrarToast(`✅ Pedido simulado: ${pedido.id}`, 'success');
        }

        // Mostrar confirmación con ID
        if (confirmIdEl && confirmacionSection) {
            confirmIdEl.textContent = pedido.id;
            confirmacionSection.classList.remove('oculto');
        }

        // Vaciar carrito y actualizar
        carrito = [];
        if (typeof guardarCarritoEnStorage === 'function') {
            guardarCarritoEnStorage();
        }
        if (typeof recalcularTotales === 'function') {
            recalcularTotales();
        }
        if (typeof actualizarBadge === 'function') {
            actualizarBadge();
        }

        // Refrescar resumen (queda vacío)
        renderizarResumenPedido();

        // Limpiar formulario
        formularioCheckout.reset();

        // 🔴 Forzar que el formulario + resumen DESAPAREZCAN
        if (checkoutContainer) {
            checkoutContainer.classList.add('oculto');
            checkoutContainer.style.display = 'none';
        }

        // Mantenemos visible la sección checkout solo para mostrar la confirmación
        if (sectionCheckout) {
            sectionCheckout.classList.remove('oculto');
        }
    });
}


// ---------- BOTONES: CANCELAR Y VOLVER ----------

if (btnCancelarCheckout) {
    btnCancelarCheckout.addEventListener('click', () => {
        cerrarCheckout();
        desbloquearNavegacionCheckout();
        if (typeof volverInicio === 'function') {
            volverInicio();
        }
    });
}

if (btnConfirmVolver) {
    btnConfirmVolver.addEventListener('click', () => {
        if (confirmacionSection) {
            confirmacionSection.classList.add('oculto');
        }
        cerrarCheckout();
        desbloquearNavegacionCheckout();
        if (typeof volverInicio === 'function') {
            volverInicio();
        }
    });
}





