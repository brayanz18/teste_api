
package api_teste.ds.exceptions;

import api_teste.ds.services.exceptions.DataBindingViolationException;
import api_teste.ds.services.exceptions.ObjectNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.validation.ConstraintViolationException;


// Centraliza o tratamento de exceções dos controllers REST
@RestControllerAdvice

public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {

    // Controla se o Stack Trace será exibido na resposta
    @Value("${server.error.include-exception:false}")
    private boolean printStackTrace;


    // Trata quando um objeto não é encontrado
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


    // Trata erros de vinculação ou integridade dos dados
    @ExceptionHandler(DataBindingViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)

    public ResponseEntity<Object> handleDataBindingViolation(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {

        return buildErrorResponse(
                dataBindingViolationException,
                dataBindingViolationException.getMessage(),
                HttpStatus.CONFLICT,
                request
        );
    }


    // Trata erros de integridade do banco de dados
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


    // Trata erros de validação dos dados recebidos por @Valid
    @Override

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Os dados enviados são inválidos."
        );

        exception.getBindingResult().getFieldErrors().forEach(error ->

                errorResponse.addValidationError(
                        error.getField(),
                        error.getDefaultMessage()
                )
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(errorResponse);
    }


    // Trata erros de validação de parâmetros
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)

    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {

        return buildErrorResponse(
                constraintViolationException,
                constraintViolationException.getMessage(),
                HttpStatus.UNPROCESSABLE_CONTENT,
                request
        );
    }


    // Trata erros de autorização
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)

    public ResponseEntity<Object> handleAuthorizationException(
            AuthorizationException authorizationException,
            WebRequest request) {

        return buildErrorResponse(
                authorizationException,
                authorizationException.getMessage(),
                HttpStatus.FORBIDDEN,
                request
        );
    }


    // Trata exceções não capturadas pelos métodos anteriores
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


    // Método auxiliar responsável por construir as respostas de erro
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                message
        );

        // Verifica se o Stack Trace deve ser incluído
        if (printStackTrace) {

            StringWriter stringWriter = new StringWriter();

            PrintWriter printWriter = new PrintWriter(stringWriter);

            exception.printStackTrace(printWriter);

            errorResponse.setStackTrace(stringWriter.toString());
        }

        return ResponseEntity
                .status(httpStatus)
                .body(errorResponse);
    }
}
