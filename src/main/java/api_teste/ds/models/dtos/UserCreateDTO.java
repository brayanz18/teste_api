
package api_teste.ds.models.dtos;

// Importa NotBlank para impedir que os campos sejam enviados vazios ou contendo apenas espaços.
import jakarta.validation.constraints.NotBlank;

// Importa Size para definir o tamanho mínimo e máximo dos campos.
import jakarta.validation.constraints.Size;

// Importa AllArgsConstructor para gerar um construtor com todos os atributos.
import lombok.AllArgsConstructor;

// Importa Data para gerar automaticamente Getters, Setters,
// toString, equals e hashCode.
import lombok.Data;

// Importa NoArgsConstructor para gerar um construtor sem parâmetros.
import lombok.NoArgsConstructor;


// Gera automaticamente um construtor com todos os atributos da classe.
@AllArgsConstructor

// Gera automaticamente um construtor vazio.
@NoArgsConstructor

// Gera Getters, Setters e outros métodos úteis do Java.
@Data
public class UserCreateDTO {

    // Valida que o username não seja nulo, vazio ou contenha apenas espaços.
    @NotBlank

    // Define que o username deve possuir no mínimo 2
    // e no máximo 100 caracteres.
    @Size(min = 2, max = 100)

    // Armazena o nome de usuário informado no cadastro.
    private String username;


    // Valida que a senha não seja nula, vazia ou contenha apenas espaços.
    @NotBlank

    // Define que a senha deve possuir no mínimo 8
    // e no máximo 60 caracteres.
    @Size(min = 8, max = 60)

    // Armazena a senha informada durante o cadastro.
    private String password;
}
