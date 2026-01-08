package com.UU.UUPokedexSeptiembre2025Service.RestController;

import com.UU.UUPokedexSeptiembre2025Service.DTO.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Manejador global de excepciones que centraliza la conversión de excepciones Java
 * a respuestas HTTP uniformes con código de estado y mensaje descriptivo.
 * 
 * Elimina la necesidad de try/catch repetidos en cada controlador.
 * Garantiza respuestas consistentes en toda la API.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura: IllegalArgumentException
     * Causa: Validación fallida (ej: parámetros inválidos)
     * Status HTTP: 400 Bad Request
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Captura: MethodArgumentTypeMismatchException, HttpMessageNotReadableException, MethodArgumentNotValidException
     * Causa: Parámetros con tipos incorrectos o formato inválido en la solicitud
     * Status HTTP: 400 Bad Request
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Captura: NoSuchElementException
     * Causa: Recurso solicitado no existe (ej: Pokémon no encontrado)
     * Status HTTP: 404 Not Found
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Captura: RuntimeException
     * Causa: Error no controlado en tiempo de ejecución (ej: timeout en API, conexión fallida)
     * Status HTTP: 500 Internal Server Error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Captura: Exception (genérica)
     * Causa: Cualquier otra excepción no específica
     * Status HTTP: 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req.getRequestURI());
    }

    /**
     * Construye la respuesta de error estándar con todos los detalles.
     * 
     * @param status Código HTTP
     * @param message Mensaje de error descriptivo
     * @param path Ruta del endpoint donde ocurrió el error
     * @return ResponseEntity con ErrorResponse y status asignado
     */
    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        // Crear DTO con información del error
        ErrorResponse body = new ErrorResponse(
                status.value(),              // Código numérico (400, 404, 500, etc)
                status.getReasonPhrase(),    // Descripción del status ("Bad Request", "Not Found", etc)
                message == null ? "" : message,  // Mensaje de error (vacío si es null)
                Instant.now().toEpochMilli(),    // Timestamp en milisegundos
                path                         // Ruta del endpoint
        );
        return ResponseEntity.status(status).body(body);
    }
}
