// Este archivo se encagar de leer el inventario desde -> catalogo.JSON

// Aquí vivirá el catálogo cargado desde el JSON
let catalogo = [];

// Función que carga el archivo catalogo.json
async function cargarCatalogo() {
    try {
        // Como este archivo está en /js y el JSON junto al HTML:
        const respuesta = await fetch('../catalogo.json');

        if (!respuesta.ok) {
            throw new Error('No se pudo cargar catalogo.json');
        }

        const datos = await respuesta.json();

        // Guardamos los datos en la variable global
        catalogo = datos;

        return catalogo;
    } catch (error) {
        console.error('Error cargando el catálogo:', error);
        return [];
    }
}

// Llamamos a la carga nada más arrancar este script
cargarCatalogo();
