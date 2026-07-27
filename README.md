# 🛒 UrbanSneakers — Tienda Online de Deportivas

Proyecto **Full Stack** desarrollado con **HTML, CSS, JavaScript, Java y Spring Boot**.

El objetivo del proyecto es construir una tienda online completa siguiendo una arquitectura similar a la utilizada en aplicaciones reales, separando claramente el **Frontend**, la **API REST**, la **lógica de negocio** y la **persistencia de datos**.

El proyecto se desarrolla progresivamente incorporando seguridad, autenticación, gestión de usuarios, pedidos y otras funcionalidades propias de un e-commerce real.

---

# 📌 Estado actual del proyecto

Actualmente la aplicación permite realizar el flujo completo desde el registro de un usuario hasta la creación de un pedido asociado a su cuenta:

- Consultar el catálogo de productos.
- Navegar por marcas.
- Seleccionar talla y color.
- Añadir productos al carrito.
- Gestionar el carrito.
- Registrarse como usuario.
- Iniciar y cerrar sesión.
- Mantener una sesión mediante cookies.
- Completar el formulario de envío.
- Realizar pedidos únicamente como usuario autenticado.
- Persistir usuarios, pedidos y líneas de pedido en MariaDB.
- Asociar cada pedido con el usuario que lo realizó.
- Obtener el resumen del pedido generado por el servidor.

---

# 🧩 Funcionalidades implementadas

## 🎨 Frontend

- 🛍️ Catálogo dinámico de deportivas.
- 👟 Cambio de color de las zapatillas en tiempo real.
- 📏 Selección de talla.
- 🛒 Carrito de compra interactivo.
- 🧾 Checkout con validación del formulario.
- 👤 Registro de usuarios.
- 🔐 Inicio de sesión.
- 🚪 Cierre de sesión.
- 👤 Visualización del usuario autenticado.
- 🔔 Sistema de notificaciones Toast.
- 📱 Diseño responsive.
- 📄 Páginas legales de términos, privacidad y cookies.

---

## ⚙️ Backend — Spring Boot

- 🌐 API REST desarrollada con Spring Boot.
- 📦 Catálogo servido desde el backend.
- 🛒 Gestión del carrito.
- ➕ Añadir productos al carrito.
- ❌ Eliminar productos.
- 🗑️ Vaciar carrito.
- 📋 Creación y persistencia de pedidos.
- 🧮 Cálculo del subtotal.
- 💰 Cálculo automático del IVA.
- 🚚 Cálculo de gastos de envío.
- 🧾 Generación automática del identificador del pedido.
- 👤 Gestión y persistencia de usuarios.
- 🔗 Asociación entre usuarios y pedidos.

La lógica crítica de negocio se realiza en el backend para evitar que datos como precios, totales o el propietario de un pedido puedan ser manipulados desde el navegador.

---

# 🔐 Seguridad y autenticación

La aplicación utiliza **Spring Security** para gestionar la autenticación.

Actualmente están implementados:

- Registro de usuarios.
- Contraseñas cifradas mediante **BCrypt**.
- Login mediante email y contraseña.
- Autenticación basada en sesión.
- Cookies de sesión mediante `JSESSIONID`.
- Protección **CSRF**.
- Logout e invalidación de sesión.
- Endpoint para consultar el usuario autenticado.
- Protección de la creación de pedidos.
- Asociación del pedido al usuario obtenida desde la sesión del servidor.

El identificador del usuario propietario de un pedido **no se envía desde el frontend**. Spring Security obtiene la identidad desde la sesión autenticada y el backend realiza la asociación.

---

# 🗄️ Persistencia y base de datos

La aplicación utiliza **MariaDB** junto con **Spring Data JPA / Hibernate**.

Actualmente se persisten:

- Usuarios.
- Pedidos.
- Líneas de pedido.
- Relaciones entre usuarios y pedidos.

Relación principal:

```
Usuario
   │
   │ 1:N
   ▼
Pedido
   │
   │ 1:N
   ▼
PedidoItem

---

# 🧠 Tecnologías utilizadas

Frontend
HTML5
CSS3
JavaScript (ES6)
Fetch API

Backend
Java 21
Spring Boot
Spring Web
Spring Security
Spring Data JPA
Bean Validation
Maven
REST API
Jackson
Base de datos
MariaDB
Hibernate / JPA

---

# 🏗️ Arquitectura

El proyecto está dividido principalmente en frontend y backend.

frontend/
│
├── css/
├── img/
├── js/
├── legal/
├── usuario/
│   ├── login.html
│   ├── login.js
│   ├── registro.html
│   └── registro.js
│
└── tienda.html


backend/
│
└── src/main/java/com/tiendadeportivas/backend/
    │
    ├── config/
    ├── controller/
    ├── model/
    ├── repository/
    ├── security/
    └── service/

---

La comunicación entre ambas partes se realiza mediante peticiones HTTP utilizando Fetch API y la API REST desarrollada con Spring Boot.

---

# 🔄 Flujo actual de compra

Registro / Login
       ↓
Spring Security
       ↓
Sesión + JSESSIONID
       ↓
Catálogo
       ↓
Carrito
       ↓
Checkout
       ↓
Validación Backend
       ↓
Creación del Pedido
       ↓
Asociación con Usuario
       ↓
MariaDB

---

<Tiena aún sin terminar>