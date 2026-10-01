package api_teste.ds.exceptions;

import api_teste.ds.services.exceptions.DataBindingViolationException;
import api_teste.ds.services.exceptions.ObjectNotFoundException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {

    @Value("${server.error.include-exception:false}")
    private boolean printStackTrace;

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {

        return buildErrorResponse(
                objectNotFoundException,
                objectNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolation(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {

        return buildErrorResponse(
                dataBindingViolationException,
                "Violação de integridade dos dados no banco",
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {

        return buildErrorResponse(
                dataIntegrityViolationException,
                "Violação de integridade dos dados no banco.",
                HttpStatus.CONFLICT,
                request
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {

        return buildErrorResponse(
                constraintViolationException,
                constraintViolationException.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> HandleAllUncaughtException(
            Exception exception,
            WebRequest request) {

        return buildErrorResponse(
                exception,
                "Ocorreu um erro no servidor.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }
  
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if (printStackTrace) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    
}