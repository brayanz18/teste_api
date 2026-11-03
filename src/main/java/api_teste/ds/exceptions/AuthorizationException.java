
package api_teste.ds.exceptions;

// Importa HttpStatus para utilizar os códigos de resposta HTTP.
import org.springframework.http.HttpStatus;

// Importa ResponseStatus para definir o código HTTP associado à exceção.
import org.springframework.web.bind.annotation.ResponseStatus;


// Define que, quando essa exceção acontecer,
// a API deverá retornar o código HTTP 403 (Forbidden).
@ResponseStatus(HttpStatus.FORBIDDEN)

// Cria uma exceção personalizada chamada AuthorizationException.
// Ela herda RuntimeException, permitindo lançar erros durante a execução do programa.
public class AuthorizationException extends RuntimeException {

    // Construtor que recebe uma mensagem explicando o motivo do erro.
    public AuthorizationException(String message) {

        // Envia a mensagem para o construtor da classe RuntimeException.
        super(message);
    }

    // Construtor que recebe uma mensagem e a causa original do erro.
    public AuthorizationException(String message, Throwable cause) {

        // Encaminha a mensagem e a causa para a classe RuntimeException.
        // Isso permite preservar a origem do erro.
        super(message, cause);
    }
}
