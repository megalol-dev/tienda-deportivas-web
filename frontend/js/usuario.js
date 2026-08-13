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
  const btnPerfil = document.getElementById("btn-perfil");

  if (!nombreUsuario || !btnAuth || !btnPerfil) {
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

    nombreUsuario.textContent = usuario.nombre;

    // MOSTRAR ACCESO AL PERFIL
    btnPerfil.hidden = false;

    btnAuth.textContent = "Cerrar sesión";

    // Quitamos el enlace al login
    btnAuth.href = "#";

    // Indicamos qué comportamiento tiene ahora
    btnAuth.dataset.accion = "logout";

    // Evitamos posibles listeners duplicados
    btnAuth.removeEventListener("click", cerrarSesion);
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
  const btnPerfil = document.getElementById("btn-perfil");

  // Ocultamos el acceso al perfil
  if (btnPerfil) {
    btnPerfil.hidden = true;
  }

  nombreUsuario.textContent = "Usuario";

  btnAuth.textContent = "Registrarse / Iniciar sesión";

  // Eliminamos el comportamiento anterior de cerrar sesión
  btnAuth.removeEventListener("click", cerrarSesion);

  // Restauramos el enlace normal al login
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
    // LIMPIAR CARRITO VISUAL
    // -------------------------------------------------
    // El carrito real permanece almacenado en Spring
    // asociado al usuario.
    //
    // Solo eliminamos del navegador los productos que
    // pertenecían a la sesión que acaba de cerrarse.
    // =============================================

    if (typeof limpiarCarritoFrontend === "function") {
      limpiarCarritoFrontend();
    }

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
