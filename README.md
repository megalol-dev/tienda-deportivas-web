# 🛒 Tienda Online de Deportivas

Proyecto Full Stack desarrollado como práctica de desarrollo web utilizando **HTML, CSS, JavaScript, Java y Spring Boot**.

El objetivo del proyecto es construir una tienda online completa siguiendo una arquitectura similar a la utilizada en aplicaciones reales, separando claramente el **Frontend** de la **lógica de negocio del Backend**.

---

# 📌 Estado actual del proyecto

Actualmente la aplicación permite recorrer el flujo completo de compra:

- Consultar el catálogo de productos.
- Navegar por marcas.
- Añadir productos al carrito.
- Gestionar el carrito.
- Completar el formulario de envío.
- Realizar un pedido.
- Obtener un resumen del pedido generado por el servidor.

El pago online y la persistencia en base de datos todavía no están implementados y forman parte de las siguientes fases del proyecto.

---

# 🧩 Funcionalidades implementadas

## Frontend

- 🛍️ Catálogo dinámico de deportivas.
- 👟 Cambio de color de las zapatillas en tiempo real.
- 📏 Selección de talla.
- 🛒 Carrito de compra interactivo.
- 🧾 Checkout con validación completa del formulario.
- 📱 Diseño responsive.
- 🔔 Sistema de notificaciones (Toast).

---

## Backend (Spring Boot)

- 🌐 API REST desarrollada con Spring Boot.
- 📦 Catálogo servido desde el backend mediante JSON.
- 🛒 Gestión del carrito en el servidor.
- ➕ Añadir productos al carrito.
- ❌ Eliminar productos.
- 🗑️ Vaciar carrito.
- 📋 Creación de pedidos.
- 🧮 Cálculo del subtotal.
- 💰 Cálculo automático del IVA.
- 🚚 Cálculo de gastos de envío.
- 🧾 Generación automática del identificador del pedido.

Toda la lógica crítica de negocio se realiza en el backend para evitar manipulaciones desde el navegador.

---

# 🧠 Tecnologías utilizadas

### Frontend

- HTML5
- CSS3
- JavaScript (ES6)

### Backend

- Java 21
- Spring Boot
- Maven
- REST API
- Jackson (JSON)

---

# 🏗️ Arquitectura

El proyecto está dividido en dos aplicaciones independientes.

## Frontend

```
frontend/
│
├── css/
├── js/
├── img/
└── tienda.html
```

## Backend

```
backend/
│
├── controller/
├── model/
├── service/
└── resources/
```

La comunicación entre ambas partes se realiza mediante peticiones HTTP utilizando **Fetch API** y una **API REST** desarrollada con Spring Boot.

---

# 🚀 Cómo ejecutar el proyecto

## 1. Clonar el repositorio

```bash
git clone https://github.com/megalol-dev/tienda-deportivas-web.git
```

---

## 2. Iniciar el backend

Desde la carpeta `backend`:

```bash
./mvnw spring-boot:run
```

El servidor quedará disponible en:

```
http://localhost:8080
```

---

## 3. Iniciar el frontend

Abrir `tienda.html` utilizando **Live Server** de Visual Studio Code.

---

# 📈 Próximas mejoras

- Base de datos MySQL.
- Persistencia de pedidos.
- Gestión de usuarios.
- Login y registro.
- Panel de administración.
- Gestión de stock.
- Historial de pedidos.
- Integración de pasarela de pago.
