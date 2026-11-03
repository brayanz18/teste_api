
package api_teste.ds.exceptions;

// Importa as exceções personalizadas utilizadas pelos serviços.
import api_teste.ds.services.exceptions.DataBindingViolationException;
import api_teste.ds.services.exceptions.ObjectNotFoundException;

// Importa PrintWriter para escrever o rastreamento do erro.
import java.io.PrintWriter;

// Importa StringWriter para armazenar o rastreamento do erro em texto.
import java.io.StringWriter;

// Importa Value para ler configurações do application.properties.
import org.springframework.beans.factory.annotation.Value;

// Importa a exceção lançada quando ocorre violação de integridade no banco.
import org.springframework.dao.DataIntegrityViolationException;

// Importa HttpHeaders para trabalhar com os cabeçalhos da resposta HTTP.
import org.springframework.http.HttpHeaders;

// Importa HttpStatus para utilizar os códigos de resposta HTTP.
import org.springframework.http.HttpStatus;

// Importa HttpStatusCode para receber o código HTTP da validação.
import org.springframework.http.HttpStatusCode;

// Importa a exceção personalizada de autorização.
import api_teste.ds.exceptions.AuthorizationException;

// Importa ResponseEntity para construir respostas HTTP.
import org.springframework.http.ResponseEntity;

// Importa ExceptionHandler para definir quais exceções cada método tratará.
import org.springframework.web.bind.annotation.ExceptionHandler;

// Importa ResponseStatus para associar um código HTTP ao tratamento da exceção.
import org.springframework.web.bind.annotation.ResponseStatus;

// Importa RestControllerAdvice para centralizar o tratamento de exceções
// de todos os controllers da aplicação.
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Importa WebRequest para representar a requisição HTTP recebida.
import org.springframework.web.context.request.WebRequest;

// Importa MethodArgumentNotValidException, lançada quando uma validação
// de dados recebidos por @Valid não é atendida.
import org.springframework.web.bind.MethodArgumentNotValidException;

// Importa a classe que permite personalizar o tratamento
// das exceções relacionadas às requisições HTTP.
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// Importa ConstraintViolationException para tratar erros de validação
// de parâmetros e restrições.
import jakarta.validation.ConstraintViolationException;


// Indica que esta classe será responsável por tratar exceções
// de todos os controllers REST da aplicação.
// Também permite retornar respostas diretamente no formato HTTP.
@RestControllerAdvice

// Herda ResponseEntityExceptionHandler para permitir personalizar
// o tratamento de algumas exceções do Spring.
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {

    // Lê a configuração server.error.include-exception do application.properties.
    // Se não existir, utiliza false como valor padrão.
    // Essa variável controla se o Stack Trace será incluído na resposta.
    @Value("${server.error.include-exception:false}")
    private boolean printStackTrace;


    // Define o tratamento para quando um objeto não for encontrado.
    // Exemplo: tentar buscar uma tarefa que não existe.
    @ExceptionHandler(ObjectNotFoundException.class)

    // Define o código HTTP 404 (Not Found).
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleObjectNotFoundException(
            ObjectNotFoundException objectNotFoundException,
            WebRequest request) {

        // Monta e retorna uma resposta de erro informando
        // que o objeto solicitado não foi encontrado.
        return buildErrorResponse(
                objectNotFoundException,
                objectNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND,
                request
        );
    }


    // Define o tratamento para erros relacionados à vinculação
    // ou integridade dos dados.
    @ExceptionHandler(DataBindingViolationException.class)

    // Define o código HTTP 409 (Conflict).
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataBindingViolation(
            DataBindingViolationException dataBindingViolationException,
            WebRequest request) {

        // Retorna uma mensagem informando que ocorreu
        // uma violação de integridade dos dados.
        return buildErrorResponse(
                dataBindingViolationException,
                "Violação de integridade dos dados no banco",
                HttpStatus.CONFLICT,
                request
        );
    }


    // Define o tratamento para erros de integridade do banco de dados.
    // Exemplo: tentar excluir um registro que ainda possui relacionamentos.
    @ExceptionHandler(DataIntegrityViolationException.class)

    // Define o código HTTP 409 (Conflict).
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleDataIntegrityViolation(
            DataIntegrityViolationException dataIntegrityViolationException,
            WebRequest request) {

        // Retorna uma resposta informando que houve
        // uma violação de integridade dos dados no banco.
        return buildErrorResponse(
                dataIntegrityViolationException,
                "Violação de integridade dos dados no banco.",
                HttpStatus.CONFLICT,
                request
        );
    }


    // Sobrescreve o método original do Spring para personalizar
    // o tratamento de erros de validação dos objetos recebidos por @Valid.
    // Exemplo: enviar um campo obrigatório vazio.
    // O retorno utilizado neste projeto é HTTP 422 (Unprocessable Content).
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        // Cria um objeto ErrorResponse com o código HTTP 422
        // e uma mensagem informando que os dados são inválidos.
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_CONTENT.value(),
                "Os dados enviados são inválidos.");

        // Percorre todos os campos que apresentaram erros de validação.
        exception.getBindingResult().getFieldErrors().forEach(error ->

                // Adiciona à resposta o nome do campo e a mensagem
                // explicando o motivo da falha na validação.
                errorResponse.addValidationError(
                        error.getField(), error.getDefaultMessage()));

        // Retorna a resposta HTTP 422 contendo os erros encontrados.
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(errorResponse);
    }


    // Define o tratamento para erros de validação de restrições.
    // Exemplo: quando um parâmetro não atende às regras definidas
    // por anotações como @Min, @Max ou @NotNull.
    @ExceptionHandler(ConstraintViolationException.class)

    // Define o código HTTP 422 (Unprocessable Content).
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ResponseEntity<Object> handleConstraintViolation(
            ConstraintViolationException constraintViolationException,
            WebRequest request) {

        // Retorna a mensagem da exceção e o código HTTP 422.
        return buildErrorResponse(
                constraintViolationException,
                constraintViolationException.getMessage(),
                HttpStatus.UNPROCESSABLE_CONTENT,
                request
        );
    }


    // Define o tratamento para erros de autorização.
    // Exemplo: quando um usuário tenta acessar uma tarefa
    // que pertence a outro usuário.
    @ExceptionHandler(AuthorizationException.class)

    // Define o código HTTP 403 (Forbidden).
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationException(
            AuthorizationException authorizationException,
            WebRequest request) {

        // Retorna a mensagem informando que o acesso foi negado.
        return buildErrorResponse(
                authorizationException,
                authorizationException.getMessage(),
                HttpStatus.FORBIDDEN,
                request
        );
    }


    // Define um tratamento geral para exceções que não foram
    // capturadas pelos métodos específicos desta classe.
    @ExceptionHandler(Exception.class)

    // Define o código HTTP 500 (Internal Server Error).
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> HandleAllUncaughtException(
            Exception exception,
            WebRequest request) {

        // Retorna uma mensagem genérica informando
        // que ocorreu um erro interno no servidor.
        return buildErrorResponse(
                exception,
                "Ocorreu um erro no servidor.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    // Método auxiliar responsável por construir as respostas de erro.
    // Ele é reutilizado pelos métodos de tratamento de exceções acima.
    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request) {

        // Cria um objeto ErrorResponse com o código HTTP
        // e a mensagem que serão enviados ao cliente.
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(), message);

        // Verifica se a configuração permite incluir o Stack Trace na resposta.
        if (printStackTrace) {

            // Cria um objeto para armazenar o rastreamento do erro em texto.
            StringWriter stringWriter = new StringWriter();

            // Cria um escritor para registrar os detalhes da exceção.
            PrintWriter printWriter = new PrintWriter(stringWriter);

            // Registra os detalhes técnicos de onde o erro aconteceu.
            exception.printStackTrace(printWriter);

            // Adiciona o Stack Trace ao objeto ErrorResponse.
            errorResponse.setStackTrace(stringWriter.toString());
        }

        // Monta e retorna a resposta HTTP utilizando o código recebido
        // e o objeto ErrorResponse como conteúdo da resposta.
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
