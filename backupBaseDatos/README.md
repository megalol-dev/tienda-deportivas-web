# Copia didáctica de la base de datos

Esta carpeta contiene una copia SQL de la base de datos local `tienda_deportivas` utilizada por UrbanSneakers.

La copia se guarda dentro del proyecto únicamente para facilitar las pruebas, la restauración del entorno local y la presentación didáctica del portfolio.

> [!WARNING]
> En una aplicación real no se debe guardar ni publicar una base de datos dentro del repositorio del proyecto. Las copias de seguridad deben almacenarse fuera del código, cifradas, protegidas mediante controles de acceso y gestionadas con una política de conservación adecuada.

Los usuarios, pedidos, direcciones y credenciales incluidos en esta copia deben ser exclusivamente datos ficticios de prueba. Nunca se deben incorporar datos personales ni credenciales reales.

## Contenido

- `tienda_deportivas.sql`: estructura y datos de la base utilizada en el entorno local.

## Restauración local

Con MySQL de XAMPP iniciado, la copia puede restaurarse desde una consola de Windows con:

```powershell
cmd /c "C:\xampp\mysql\bin\mysql.exe -u root < backupBaseDatos\tienda_deportivas.sql"
```

La restauración reemplaza las tablas incluidas en el volcado, por lo que debe realizarse únicamente en un entorno local y después de crear una copia de seguridad del estado existente.
