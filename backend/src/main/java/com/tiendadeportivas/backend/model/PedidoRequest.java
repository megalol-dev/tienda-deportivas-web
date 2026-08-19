// Transporta los datos de envío de un pedido.
package com.tiendadeportivas.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;

public class PedidoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9,15}", message = "El teléfono debe contener entre 9 y 15 dígitos")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;

    @NotBlank(message = "El código postal es obligatorio")
    @Pattern(regexp = "\\d{5}", message = "El código postal debe tener exactamente 5 dígitos")
    private String cp;

    @NotBlank(message = "El país es obligatorio")
    private String pais;

    @AssertTrue(message = "Debes aceptar los términos y condiciones")
    private boolean aceptaTerminos;

    // Crea una instancia de PedidoRequest.
    public PedidoRequest() {
    }

    // Devuelve el valor de nombre.
    public String getNombre() {
        return nombre;
    }

    // Actualiza el valor de nombre.
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Devuelve el valor de apellidos.
    public String getApellidos() {
        return apellidos;
    }

    // Actualiza el valor de apellidos.
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    // Devuelve el valor de email.
    public String getEmail() {
        return email;
    }

    // Actualiza el valor de email.
    public void setEmail(String email) {
        this.email = email;
    }

    // Devuelve el valor de telefono.
    public String getTelefono() {
        return telefono;
    }

    // Actualiza el valor de telefono.
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    // Devuelve el valor de direccion.
    public String getDireccion() {
        return direccion;
    }

    // Actualiza el valor de direccion.
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    // Devuelve el valor de ciudad.
    public String getCiudad() {
        return ciudad;
    }

    // Actualiza el valor de ciudad.
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    // Devuelve el valor de provincia.
    public String getProvincia() {
        return provincia;
    }

    // Actualiza el valor de provincia.
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    // Devuelve el código postal.
    public String getCp() {
        return cp;
    }

    // Actualiza el código postal.
    public void setCp(String cp) {
        this.cp = cp;
    }

    // Devuelve el valor de pais.
    public String getPais() {
        return pais;
    }

    // Actualiza el valor de pais.
    public void setPais(String pais) {
        this.pais = pais;
    }

    // Indica si se aceptaron los términos.
    public boolean isAceptaTerminos() {
        return aceptaTerminos;
    }

    // Actualiza el valor de acepta terminos.
    public void setAceptaTerminos(boolean aceptaTerminos) {
        this.aceptaTerminos = aceptaTerminos;
    }
}
