const API_URL = "http://localhost:8080";

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
