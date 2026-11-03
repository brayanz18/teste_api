
package api_teste.ds.models.dtos;

// Importa NotBlank para impedir que a senha seja nula, vazia
// ou contenha somente espaços.
import jakarta.validation.constraints.NotBlank;

// Importa Size para definir o tamanho mínimo e máximo da senha.
import jakarta.validation.constraints.Size;

// Importa AllArgsConstructor para gerar um construtor com todos os atributos.
import lombok.AllArgsConstructor;

// Importa NoArgsConstructor para gerar um construtor sem parâmetros.
import lombok.NoArgsConstructor;

// Importa Data para gerar automaticamente Getters, Setters,
// toString, equals e hashCode.
import lombok.Data;


// Gera automaticamente um construtor sem parâmetros.
@NoArgsConstructor

// Gera automaticamente um construtor com todos os atributos.
@AllArgsConstructor

// Gera Getters, Setters e outros métodos úteis do Java.
@Data
public class UserUpdateDTO {

    // Armazena o ID do usuário que será atualizado.
    private Long id;


    // Impede que a senha seja nula, vazia ou contenha apenas espaços.
    @NotBlank

    // Define que a nova senha deve possuir no mínimo 8
    // e no máximo 60 caracteres.
    @Size(min = 8, max = 60)

    // Armazena a nova senha informada pelo usuário.
    private String password;
}
