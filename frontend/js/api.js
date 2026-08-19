// Centraliza las peticiones con protección CSRF.
const API_URL = "http://localhost:8080";

// Obtiene el token CSRF de la sesión.
async function obtenerCsrfToken() {
  const respuesta = await fetch(`${API_URL}/auth/csrf`, {
    method: "GET",
    credentials: "include",
  });

  if (!respuesta.ok) {
    throw new Error("No se pudo obtener el token CSRF");
  }

  const csrf = await respuesta.json();

  return csrf.token;
}

// Realiza una petición autenticada con CSRF.
async function fetchConCsrf(url, opciones = {}) {
  const token = await obtenerCsrfToken();

  const headers = {
    ...(opciones.headers || {}),
    "X-XSRF-TOKEN": token,
  };

  return fetch(url, {
    ...opciones,
    headers,
    credentials: "include",
  });
}
