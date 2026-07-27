// =====================================================
// REGISTRO DE USUARIO
// -----------------------------------------------------
// Gestiona:
// • Lectura del formulario.
// • Validación de contraseñas.
// • Obtención del token CSRF.
// • Envío del registro al backend.
// =====================================================

const formRegistro = document.getElementById("form-registro");

formRegistro.addEventListener("submit", async (event) => {
  event.preventDefault();

  // =================================================
  // 1. OBTENER DATOS DEL FORMULARIO
  // =================================================

  const nombre = document.getElementById("registro-nombre").value.trim();

  const email = document.getElementById("registro-email").value.trim();

  const password = document.getElementById("registro-password").value;

  const confirmarPassword = document.getElementById(
    "registro-confirmar-password",
  ).value;

  // =================================================
  // 2. COMPROBAR CONTRASEÑAS
  // =================================================

  if (password !== confirmarPassword) {
    alert("Las contraseñas no coinciden.");

    return;
  }

  // =================================================
  // 3. DATOS PARA EL BACKEND
  // =================================================

  const datosRegistro = {
    nombre: nombre,
    email: email,
    password: password,
    confirmarPassword: confirmarPassword,
  };

  try {
    // =================================================
    // 4. OBTENER TOKEN CSRF
    // =================================================

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    console.log("CSRF obtenido:", csrf);

    // =================================================
    // 5. ENVIAR REGISTRO
    // =================================================

    const respuesta = await fetch("http://localhost:8080/auth/registro", {
      method: "POST",

      headers: {
        "Content-Type": "application/json",

        // Spring nos dice qué nombre de cabecera usar
        [csrf.headerName]: csrf.token,
      },

      // Envía también la cookie de sesión
      credentials: "include",

      body: JSON.stringify(datosRegistro),
    });

    // =================================================
    // 6. COMPROBAR RESPUESTA
    // =================================================

    if (!respuesta.ok) {
      let mensaje = "No se ha podido crear la cuenta.";

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

    // =================================================
    // 7. REGISTRO CORRECTO
    // =================================================

    const usuario = await respuesta.json();

    console.log("Usuario registrado:", usuario);

    alert("Cuenta creada correctamente.");

    window.location.href = "login.html";
  } catch (error) {
    console.error("Error durante el registro:", error);

    alert("No se ha podido conectar con el servidor.");
  }
});
