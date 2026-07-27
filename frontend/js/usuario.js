// =====================================================
// ESTADO DEL USUARIO EN LA TIENDA
// -----------------------------------------------------
// • Comprueba si existe una sesión.
// • Muestra el nombre del usuario.
// • Cambia Login/Registro por Cerrar sesión.
// • Permite cerrar la sesión de Spring Security.
// =====================================================

document.addEventListener("DOMContentLoaded", comprobarUsuario);

// =====================================================
// COMPROBAR USUARIO ACTUAL
// =====================================================

async function comprobarUsuario() {
  const nombreUsuario = document.getElementById("nombre-usuario");

  const btnAuth = document.getElementById("btn-auth");

  if (!nombreUsuario || !btnAuth) {
    return;
  }

  try {
    const respuesta = await fetch("http://localhost:8080/auth/me", {
      method: "GET",
      credentials: "include",
    });

    // =============================================
    // NO HAY SESIÓN
    // =============================================

    if (!respuesta.ok) {
      mostrarUsuarioAnonimo(nombreUsuario, btnAuth);

      return;
    }

    // =============================================
    // HAY SESIÓN
    // =============================================

    const usuario = await respuesta.json();

    console.log("Sesión activa:", usuario);

    nombreUsuario.textContent = usuario.nombre;

    btnAuth.textContent = "Cerrar sesión";

    // Quitamos el enlace al login
    btnAuth.href = "#";

    // Indicamos qué comportamiento tiene ahora
    btnAuth.dataset.accion = "logout";

    btnAuth.addEventListener("click", cerrarSesion);
  } catch (error) {
    console.error("No se pudo comprobar la sesión:", error);

    mostrarUsuarioAnonimo(nombreUsuario, btnAuth);
  }
}

// =====================================================
// MOSTRAR ESTADO ANÓNIMO
// =====================================================

function mostrarUsuarioAnonimo(nombreUsuario, btnAuth) {
  nombreUsuario.textContent = "Usuario";

  btnAuth.textContent = "Registrarse / Iniciar sesión";

  btnAuth.href = "usuario/login.html";

  btnAuth.dataset.accion = "login";
}

// =====================================================
// CERRAR SESIÓN
// =====================================================

async function cerrarSesion(event) {
  event.preventDefault();

  try {
    // =============================================
    // OBTENER CSRF
    // =============================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF");
    }

    const csrf = await respuestaCsrf.json();

    // =============================================
    // LOGOUT
    // =============================================

    const respuesta = await fetch("http://localhost:8080/auth/logout", {
      method: "POST",

      credentials: "include",

      headers: {
        [csrf.headerName]: csrf.token,
      },
    });

    if (!respuesta.ok) {
      throw new Error("No se pudo cerrar la sesión");
    }

    console.log("Sesión cerrada correctamente.");

    // =============================================
    // VOLVER AL ESTADO INICIAL
    // =============================================

    const nombreUsuario = document.getElementById("nombre-usuario");

    const btnAuth = document.getElementById("btn-auth");

    mostrarUsuarioAnonimo(nombreUsuario, btnAuth);
  } catch (error) {
    console.error("Error cerrando sesión:", error);

    alert("No se ha podido cerrar la sesión.");
  }
}
