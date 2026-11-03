
package api_teste.ds.exceptions;

// Importa ArrayList para criar uma lista de erros de validação.
import java.util.ArrayList;

// Importa List para trabalhar com uma coleção de erros.
import java.util.List;

// Importa Objects para verificar se um objeto é nulo.
import java.util.Objects;

// Importa JsonInclude para controlar quais informações serão incluídas no JSON.
import com.fasterxml.jackson.annotation.JsonInclude;

// Importa Getter para gerar automaticamente os métodos de leitura.
import lombok.Getter;

// Importa RequiredArgsConstructor para gerar um construtor
// com os atributos obrigatórios (campos final).
import lombok.RequiredArgsConstructor;

// Importa Setter para gerar automaticamente os métodos de alteração.
import lombok.Setter;


// Gera automaticamente os métodos Getters para os atributos da classe.
@Getter

// Gera automaticamente os métodos Setters para os atributos não finais.
@Setter

// Gera um construtor que recebe os atributos obrigatórios da classe.
@RequiredArgsConstructor

// Faz com que os atributos com valor null não apareçam no JSON da resposta.
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    // Armazena o código HTTP do erro.
    private final int status;

    // Armazena a mensagem explicando o erro ocorrido.
    private final String message;

    // Armazena o rastreamento técnico do erro (Stack Trace).
    // Pode ser utilizado para identificar onde o erro aconteceu.
    private String stackTrace;

    // Armazena uma lista com os erros encontrados durante as validações.
    private List<ValidationError> errors;


    // Classe interna responsável por representar cada erro de validação.
    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError {

        // Guarda o nome do campo que apresentou erro.
        private final String field;

        // Guarda a mensagem explicando o erro encontrado naquele campo.
        private final String message;
    }


    // Método responsável por adicionar um novo erro de validação à lista.
    public void addValidationError(String field, String message) {

        // Verifica se a lista de erros ainda não foi criada.
        if (Objects.isNull(this.errors)) {

            // Cria uma nova lista para armazenar os erros.
            this.errors = new ArrayList<>();
        }

        // Adiciona um novo erro contendo o nome do campo e sua mensagem.
        this.errors.add(new ValidationError(field, message));
    }
}
