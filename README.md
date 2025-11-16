# UrbanSneakers 👟  
Tienda online de zapatillas deportivas (prototipo Front-End)

Proyecto realizado como práctica para FP, simulando una tienda online de deportivas con carrito de compra, resumen de pedido y formulario de checkout.  
No hay backend real ni pasarela de pago: **todas las compras están simuladas en el navegador** usando JavaScript y `localStorage`.

---

## 🧱 Funcionalidades principales

- **Catálogo de productos desde JSON**
  - Archivo `catalogo.json` con la lista de deportivas (id, marca, nombre, precio, tallas, colores).
  - Carga dinámica mediante `fetch` en `catalogo.js`.
- **SPA por marcas**
  - Pantalla de inicio con tarjetas de marca: Nike, Adidas, Puma, Reebok, New Balance.
  - Al hacer clic en una marca se muestra una vista tipo SPA con el grid de productos de esa marca.
- **Tarjetas de producto con opciones**
  - Imagen de la deportiva (por convención `img/p<ID>_<color>.png` con fallback a `p<ID>_default.png`).
  - Selección de talla y color.
  - Botón **“Añadir al carrito”** que respeta talla y color elegidos.

- **Carrito de la compra (modal)**
  - Modal flotante con listado de productos añadidos.
  - Agrupación por producto + talla + color (si añades dos veces la misma combinación, aumenta la cantidad).
  - Botón **“Quitar producto”** por línea.
  - Botón **“Cancelar todos los pedidos”** que vacía el carrito.
  - Botón **“Pagar ahora”** que lleva al checkout.
  - Badge en el header que muestra el número total de unidades.

- **Persistencia con `localStorage`**
  - El carrito se guarda en `localStorage` bajo la clave `urbanSneakersCarrito`.
  - Al recargar la página, se reconstruye el carrito y se actualiza el badge automáticamente.

- **Checkout y pedido simulado**
  - Formulario de datos de envío (nombre, apellidos, email, teléfono, dirección, ciudad, provincia, CP, país, notas).
  - Resumen del pedido con subtotales, gastos de envío y cálculo de IVA.
  - Validación de formulario:
    - Campos nativos requeridos (`required`, tipo `email`, etc.).
    - Validación adicional del **código postal** (5 dígitos).
    - Check de aceptar **Términos y Condiciones** obligatorio.
    - Mensajes de error específicos por campo.
  - Al enviar el formulario correctamente:
    - Se genera un **ID de pedido simulado** (por ejemplo: `PED-<timestamp>`).
    - Se muestra una pantalla de **“✅ Pedido recibido”** con el ID.
    - Se vacía el carrito, se actualiza el badge y se limpia el resumen.
    - El usuario debe pulsar **“Volver a la tienda”** para regresar a la vista principal.

- **Control de navegación en checkout**
  - Cuando se entra al checkout:
    - Se ocultan las secciones de marcas y vistas de producto.
    - Se desactivan los enlaces del header (`Usuario`, `Carrito`, etc.).
  - Hasta que no se cancela el checkout o se vuelve desde la pantalla de confirmación, el usuario **no puede seguir “trasteando” la tienda**, evitando estados raros de pedido.

- **Toasts / notificaciones**
  - Notificación flotante centrada en pantalla.
  - Usada para:
    - Confirmar que se ha añadido un producto al carrito.
    - Mostrar errores de validación del formulario (campo incorrecto, CP no válido, etc.).
  - Versiones:
    - Verde (éxito).
    - Roja (error).

- **Diseño responsive**
  - Adaptación para **PC y móvil** (principalmente).
  - Uso de `@media` para:
    - Reorganizar el grid de productos (de 5 columnas en desktop a 2 columnas en móvil).
    - Ajustar secciones de marcas a ancho completo en pantallas pequeñas.
    - Colocar de forma más legible:
      - Formulario y resumen del checkout (1 columna en móvil).
      - Filas del carrito (imagen + info + botón) en un layout más cómodo en móvil.
  - Botones suficientemente grandes y separados en móvil para evitar toques erróneos.

---

## 🗂️ Estructura del proyecto

```text
/
├── index.html              # Página principal de la tienda
├── tiendaEstilos.css       # Hoja de estilos principal (con variables CSS y media queries)
├── catalogo.json           # Datos del catálogo de zapatillas
├── img/                    # Imágenes (logo, marcas y productos)
│   ├── logotipo.png
│   ├── nike.png
│   ├── adidas.png
│   ├── puma.png
│   ├── reebok.png
│   ├── newBalance.png
│   └── p<ID>_<color>.png   # Imágenes de productos (convención usada en JS)
└── app.js/                 # Lógica JavaScript separada por responsabilidad
    ├── catalogo.js         # Carga del JSON y creación de la variable global `catalogo`
    ├── productos.js        # Vista por marcas, grid de productos y selects de talla/color
    ├── carrito.js          # Lógica del carrito, modal, badge y localStorage
    └── checkout.js         # Checkout, validación del formulario y pedido simulado
```

---

## ⚙️ Tecnologías utilizadas

- **HTML5** – estructura de la página y del formulario.
- **CSS3** – estilos, layout, diseño responsive y variables CSS (`:root`).
- **JavaScript (ES6+)** – lógica de la aplicación:
  - Fetch de `catalogo.json`
  - Gestión del carrito
  - SPA de marcas
  - Validación de formulario
  - `localStorage` para persistencia del carrito


## 🚀 Cómo ejecutar el proyecto

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/megalol-dev/tienda-deportivas-web.git
   cd tienda-deportivas-web
