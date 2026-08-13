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
const registroNombre = document.getElementById("registro-nombre");
const registroEmail = document.getElementById("registro-email");
const registroPassword = document.getElementById("registro-password");
const registroConfirmarPassword = document.getElementById(
  "registro-confirmar-password",
);

const errorRegistroNombre = document.getElementById("error-registro-nombre");
const errorRegistroEmail = document.getElementById("error-registro-email");
const errorRegistroPassword = document.getElementById(
  "error-registro-password",
);

const errorRegistroConfirmarPassword = document.getElementById(
  "error-registro-confirmar-password",
);

// =====================================================
// MOSTRAR ERROR
// =====================================================

function mostrarError(input, error, mensaje) {
  input.classList.add("campo-invalido");

  error.textContent = mensaje;
}

// =====================================================
// LIMPIAR ERROR
// =====================================================

function limpiarError(input, error) {
  input.classList.remove("campo-invalido");

  error.textContent = "";
}

// =====================================================
// VALIDAR NOMBRE
// =====================================================

function validarNombreRegistro() {
  const nombre = registroNombre.value.trim();

  if (nombre.length < 2 || nombre.length > 100) {
    mostrarError(
      registroNombre,
      errorRegistroNombre,
      "El nombre debe tener entre 2 y 100 caracteres.",
    );

    return false;
  }

  const patron = /^[\p{L}][\p{L} .'-]*$/u;

  if (!patron.test(nombre)) {
    mostrarError(
      registroNombre,
      errorRegistroNombre,
      "El nombre contiene caracteres no válidos.",
    );

    return false;
  }

  limpiarError(
    registroNombre,
    errorRegistroNombre,
  );

  return true;
}

// =====================================================
// VALIDAR EMAIL
// =====================================================

function validarEmailRegistro() {
  const email = registroEmail.value.trim();

  if (email.length === 0 || email.length > 150) {
    mostrarError(
      registroEmail,
      errorRegistroEmail,
      "Introduce un correo electrónico válido.",
    );

    return false;
  }

  const patron = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patron.test(email)) {
    mostrarError(
      registroEmail,
      errorRegistroEmail,
      "Introduce un correo electrónico válido.",
    );

    return false;
  }

  limpiarError(
    registroEmail,
    errorRegistroEmail,
  );

  return true;
}

// =====================================================
// VALIDAR PASSWORD
// =====================================================

function validarPasswordRegistro() {
  const password = registroPassword.value;

  if (
    password.length < 8 ||
    password.length > 72
  ) {
    mostrarError(
      registroPassword,
      errorRegistroPassword,
      "La contraseña debe tener entre 8 y 72 caracteres.",
    );

    return false;
  }

  const patron =
    /^(?=.*\p{L})(?=.*\d).+$/u;

  if (!patron.test(password)) {
    mostrarError(
      registroPassword,
      errorRegistroPassword,
      "Debe contener al menos una letra y un número.",
    );

    return false;
  }

  limpiarError(
    registroPassword,
    errorRegistroPassword,
  );

  return true;
}

// =====================================================
// VALIDAR CONFIRMAR PASSWORD
// =====================================================

function validarConfirmarPasswordRegistro() {

  const confirmar =
    registroConfirmarPassword.value;

  if (
    confirmar.length < 8 ||
    confirmar.length > 72
  ) {
    mostrarError(
      registroConfirmarPassword,
      errorRegistroConfirmarPassword,
      "Debe tener entre 8 y 72 caracteres.",
    );

    return false;
  }

  if (
    confirmar !==
    registroPassword.value
  ) {
    mostrarError(
      registroConfirmarPassword,
      errorRegistroConfirmarPassword,
      "Las contraseñas no coinciden.",
    );

    return false;
  }

  limpiarError(
    registroConfirmarPassword,
    errorRegistroConfirmarPassword,
  );

  return true;
}

// =====================================================
// VALIDACIÓN EN VIVO
// =====================================================

registroNombre.addEventListener(
  "blur",
  validarNombreRegistro,
);

registroEmail.addEventListener(
  "blur",
  validarEmailRegistro,
);

registroPassword.addEventListener(
  "blur",
  validarPasswordRegistro,
);

registroConfirmarPassword.addEventListener(
  "blur",
  validarConfirmarPasswordRegistro,
);

formRegistro.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (
    !validarNombreRegistro() ||
    !validarEmailRegistro() ||
    !validarPasswordRegistro() ||
    !validarConfirmarPasswordRegistro()
  ) {
    return;
  }

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
