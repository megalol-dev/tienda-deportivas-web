// Convierte excepciones comunes en respuestas HTTP.
package com.tiendadeportivas.backend.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import com.tiendadeportivas.backend.exception.PedidoConcurrenteException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Devuelve una respuesta de acceso denegado.
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Map<String, String>> manejarSecurityException(
            SecurityException ex) {

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(respuesta);
    }

    // Devuelve una petición inválida.
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarIllegalArgumentException(
            IllegalArgumentException ex) {

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    // Devuelve los errores de validación por campo.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidaciones(
                    MethodArgumentNotValidException ex) {

            Map<String, String> errores = new HashMap<>();

            ex.getBindingResult()
                            .getFieldErrors()
                            .forEach(error -> errores.put(
                                            error.getField(),
                                            error.getDefaultMessage()));

            return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(errores);
    }
    
    // Devuelve conflicto cuando el pedido cambió mientras otro usuario lo
    // gestionaba.
    @ExceptionHandler(PedidoConcurrenteException.class)
    public ResponseEntity<Map<String, String>> manejarPedidoConcurrente(
                    PedidoConcurrenteException ex) {

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", ex.getMessage());

            return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(respuesta);
    }

    // Devuelve conflicto cuando una Idempotency-Key ya pertenece
    // a una solicitud lógica diferente.
    @ExceptionHandler(ConflictoIdempotenciaException.class)
    public ResponseEntity<Map<String, String>> manejarConflictoIdempotencia(
                    ConflictoIdempotenciaException ex) {

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put("error", ex.getMessage());

            return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(respuesta);
    }

    // Devuelve conflicto cuando Hibernate detecta una actualización concurrente.
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Map<String, String>> manejarOptimisticLock(
                    ObjectOptimisticLockingFailureException ex) {

            Map<String, String> respuesta = new HashMap<>();
            respuesta.put(
                            "error",
                            "El pedido ha sido modificado por otro usuario.");

            return ResponseEntity
                            .status(HttpStatus.CONFLICT)
                            .body(respuesta);
    }
}
