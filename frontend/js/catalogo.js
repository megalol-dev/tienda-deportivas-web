// =====================================================
// CATÁLOGO DE PRODUCTOS
// -----------------------------------------------------
// Este archivo es el encargado de obtener el catálogo
// desde el backend de Spring Boot.
//
// El catálogo queda disponible en la variable global
// "catalogo" para que el resto de scripts puedan
// utilizarlo (productos.js, carrito.js, checkout.js...)
// =====================================================

// -----------------------------------------------------
// VARIABLE GLOBAL DEL CATÁLOGO
// -----------------------------------------------------

let catalogo = [];

// -----------------------------------------------------
// CARGAR CATÁLOGO DESDE EL BACKEND
// -----------------------------------------------------

async function cargarCatalogo() {
  try {
    // Solicita el catálogo al endpoint REST de Spring
    const respuesta = await fetch("http://localhost:8080/productos");

    // Si Spring responde con un error (404,500...)
    if (!respuesta.ok) {
      throw new Error("No se pudo obtener el catálogo del servidor.");
    }

    // Convierte automáticamente el JSON recibido
    // en un Array de objetos JavaScript.
    catalogo = await respuesta.json();

    return catalogo;
  } catch (error) {
    console.error("Error cargando el catálogo:", error);

    catalogo = [];

    return catalogo;
  }
}

// -----------------------------------------------------
// INICIALIZACIÓN
// -----------------------------------------------------

// Guardamos la promesa para que otros scripts puedan esperar
// hasta que el catálogo haya terminado de cargarse.
const catalogoPromise = cargarCatalogo();
