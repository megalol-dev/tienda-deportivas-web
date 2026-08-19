// Gestiona la sesión mostrada en la tienda.
document.addEventListener("DOMContentLoaded", comprobarUsuario);

// Actualiza la cabecera según la sesión.
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

    if (!respuesta.ok) {
      mostrarUsuarioAnonimo(nombreUsuario, btnAuth);
      return;
    }

    const usuario = await respuesta.json();

    nombreUsuario.textContent = usuario.nombre;

    btnPerfil.hidden = false;

    btnAuth.textContent = "Cerrar sesión";

    btnAuth.href = "#";

    btnAuth.dataset.accion = "logout";

    btnAuth.removeEventListener("click", cerrarSesion);
    btnAuth.addEventListener("click", cerrarSesion);
  } catch (error) {
    console.error("No se pudo comprobar la sesión:", error);

    mostrarUsuarioAnonimo(nombreUsuario, btnAuth);
  }
}

// Restaura la cabecera para un visitante.
function mostrarUsuarioAnonimo(nombreUsuario, btnAuth) {
  const btnPerfil = document.getElementById("btn-perfil");

  if (btnPerfil) {
    btnPerfil.hidden = true;
  }

  nombreUsuario.textContent = "Usuario";

  btnAuth.textContent = "Registrarse / Iniciar sesión";

  btnAuth.removeEventListener("click", cerrarSesion);

  btnAuth.href = "usuario/login.html";

  btnAuth.dataset.accion = "login";
}

// Cierra la sesión desde la tienda.
async function cerrarSesion(event) {
  event.preventDefault();

  try {

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF");
    }

    const csrf = await respuestaCsrf.json();

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

    if (typeof limpiarCarritoFrontend === "function") {
      limpiarCarritoFrontend();
    }

    const nombreUsuario = document.getElementById("nombre-usuario");

    const btnAuth = document.getElementById("btn-auth");

    mostrarUsuarioAnonimo(nombreUsuario, btnAuth);
  } catch (error) {
    console.error("Error cerrando sesión:", error);

    alert("No se ha podido cerrar la sesión.");
  }
}
