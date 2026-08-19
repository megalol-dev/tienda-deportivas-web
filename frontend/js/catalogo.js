// Carga el catálogo desde el backend.
let catalogo = [];

// Carga el catálogo público desde el backend.
async function cargarCatalogo() {
  try {

    const respuesta = await fetch("http://localhost:8080/productos");

    if (!respuesta.ok) {
      throw new Error("No se pudo obtener el catálogo del servidor.");
    }

    catalogo = await respuesta.json();

    return catalogo;
  } catch (error) {
    console.error("Error cargando el catálogo:", error);

    catalogo = [];

    return catalogo;
  }
}

const catalogoPromise = cargarCatalogo();
