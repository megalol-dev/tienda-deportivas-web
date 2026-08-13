// =====================================================
// LOGIN DE USUARIO
// =====================================================

const formLogin = document.getElementById("form-login");

const loginEmail = document.getElementById("login-email");
const loginPassword = document.getElementById("login-password");

const errorLoginEmail = document.getElementById("error-login-email");
const errorLoginPassword = document.getElementById("error-login-password");

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
// VALIDAR EMAIL
// =====================================================

function validarEmailLogin() {
  const email = loginEmail.value.trim();

  if (email.length === 0 || email.length > 150) {
    mostrarError(
      loginEmail,
      errorLoginEmail,
      "Introduce un correo electrónico válido.",
    );
    return false;
  }

  const patron = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

  if (!patron.test(email)) {
    mostrarError(
      loginEmail,
      errorLoginEmail,
      "Introduce un correo electrónico válido.",
    );
    return false;
  }

  limpiarError(loginEmail, errorLoginEmail);

  return true;
}

// =====================================================
// VALIDAR PASSWORD
// =====================================================

function validarPasswordLogin() {
  const password = loginPassword.value;

  if (password.length < 8 || password.length > 72) {
    mostrarError(
      loginPassword,
      errorLoginPassword,
      "La contraseña debe tener entre 8 y 72 caracteres.",
    );
    return false;
  }

  limpiarError(loginPassword, errorLoginPassword);

  return true;
}

// =====================================================
// VALIDACIÓN EN VIVO
// =====================================================

loginEmail.addEventListener("blur", validarEmailLogin);

loginPassword.addEventListener("blur", validarPasswordLogin);

// =====================================================
// LOGIN
// =====================================================

formLogin.addEventListener("submit", async (event) => {
  event.preventDefault();

  if (!validarEmailLogin() || !validarPasswordLogin()) {
    return;
  }

  // =================================================
  // RECOGER DATOS
  // =================================================

  const datosLogin = {
    email: loginEmail.value.trim(),
    password: loginPassword.value,
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
      } catch {}

      alert(mensaje);

      return;
    }

    // =============================================
    // LOGIN CORRECTO
    // =============================================

    const usuario = await respuesta.json();

    console.log("Usuario autenticado:", usuario);

    if (usuario.rol === "CLIENTE") {
      window.location.href = "../tienda.html";
    } else if (
      usuario.rol === "TRABAJADOR" ||
      usuario.rol === "JEFE" ||
      usuario.rol === "ADMIN"
    ) {
      window.location.href = "../panel/panel.html";
    } else {
      console.error("Rol desconocido:", usuario.rol);
    }
  } catch (error) {
    console.error("Error durante el login:", error);

    alert("No se ha podido conectar con el servidor.");
  }
});
