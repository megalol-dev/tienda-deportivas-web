// Valida y envía el formulario de registro.
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

// Muestra un error de validación.
function mostrarError(input, error, mensaje) {
  input.classList.add("campo-invalido");

  error.textContent = mensaje;
}

// Limpia un error de validación.
function limpiarError(input, error) {
  input.classList.remove("campo-invalido");

  error.textContent = "";
}

// Valida el nombre del registro.
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

// Valida el email del registro.
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

// Valida la contraseña del registro.
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

// Valida la confirmación de contraseña.
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

  const nombre = document.getElementById("registro-nombre").value.trim();
  const email = document.getElementById("registro-email").value.trim();
  const password = document.getElementById("registro-password").value;
  const confirmarPassword = document.getElementById(
    "registro-confirmar-password",
  ).value;

  const datosRegistro = {
    nombre: nombre,
    email: email,
    password: password,
    confirmarPassword: confirmarPassword,
  };

  try {

    const respuestaCsrf = await fetch("http://localhost:8080/auth/csrf", {
      method: "GET",
      credentials: "include",
    });

    if (!respuestaCsrf.ok) {
      throw new Error("No se pudo obtener el token CSRF.");
    }

    const csrf = await respuestaCsrf.json();

    console.log("CSRF obtenido:", csrf);

    const respuesta = await fetch("http://localhost:8080/auth/registro", {
      method: "POST",

      headers: {
        "Content-Type": "application/json",

        [csrf.headerName]: csrf.token,
      },

      credentials: "include",

      body: JSON.stringify(datosRegistro),
    });

    if (!respuesta.ok) {
      let mensaje = "No se ha podido crear la cuenta.";

      try {
        const error = await respuesta.json();

        if (error.message) {
          mensaje = error.message;
        }
      } catch {

      }

      alert(mensaje);

      return;
    }

    const usuario = await respuesta.json();

    console.log("Usuario registrado:", usuario);

    alert("Cuenta creada correctamente.");

    window.location.href = "login.html";
  } catch (error) {
    console.error("Error durante el registro:", error);

    alert("No se ha podido conectar con el servidor.");
  }
});
