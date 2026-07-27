// =====================================================
// LOGIN DE USUARIO
// =====================================================

const formLogin = document.getElementById("form-login");

formLogin.addEventListener("submit", async (event) => {
  event.preventDefault();

  // =================================================
  // RECOGER DATOS
  // =================================================

  const email = document.getElementById("login-email").value.trim();
  const password = document.getElementById("login-password").value;

  const datosLogin = {
    email: email,
    password: password,
  };

  try {
    // =============================================
    // OBTENER TOKEN CSRF
    // =============================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    // =============================================
    // LOGIN
    // =============================================

    const respuesta = await fetch("http://localhost:8080/auth/login", {
      method: "POST",

      credentials: "include",

      headers: {
        "Content-Type": "application/json",
        [csrf.headerName]: csrf.token,
      },

      body: JSON.stringify(datosLogin),
    });

    // =============================================
    // LOGIN INCORRECTO
    // =============================================

    if (!respuesta.ok) {
      let mensaje = "Email o contraseña incorrectos.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {
        // Dejamos el mensaje genérico
      }

      alert(mensaje);

      return;
    }

    // =============================================
    // LOGIN CORRECTO
    // =============================================

    const usuario = await respuesta.json();

    console.log("Usuario autenticado:", usuario);

    // Volvemos a la tienda.
    //
    // NO guardamos el usuario en localStorage.
    // Spring mantiene la autenticación mediante
    // la cookie JSESSIONID.

    window.location.href = "../tienda.html";
  } catch (error) {
    console.error("Error durante el login:", error);

    alert("No se ha podido conectar con el servidor.");
  }
});
