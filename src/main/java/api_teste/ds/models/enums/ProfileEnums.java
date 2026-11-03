
package api_teste.ds.models.enums;

// Importa Objects para verificar se um valor é nulo.
import java.util.Objects;

// Importa AllArgsConstructor para gerar um construtor com todos os atributos.
import lombok.AllArgsConstructor;

// Importa Getter para gerar automaticamente os métodos de leitura.
import lombok.Getter;


// Gera automaticamente um construtor que recebe todos os atributos.
@AllArgsConstructor

// Gera automaticamente os métodos Getters para acessar os atributos.
@Getter
public enum ProfileEnums {

    // Define o perfil de administrador.
    // Possui código 1 e descrição ROLE_ADMIN.
    ADMIN(1, "ROLE_ADMIN"),

    // Define o perfil de usuário comum.
    // Possui código 2 e descrição ROLE_USER.
    USER(2, "ROLE_USER");


    // Armazena o código numérico de cada perfil.
    private Integer code;

    // Armazena a descrição do perfil utilizada pelo Spring Security.
    private String description;


    // Método estático que recebe um código numérico
    // e retorna o perfil correspondente.
    public static ProfileEnums toEnum(Integer code) {

        // Verifica se o código recebido é nulo.
        if (Objects.isNull(code)) {

            // Retorna null quando não existe código informado.
            return null;
        }

        // Percorre todos os perfis definidos neste Enum.
        for (ProfileEnums x : ProfileEnums.values()) {

            // Compara o código recebido com o código de cada perfil.
            if (code.equals(x.getCode())) {

                // Retorna o perfil correspondente ao código encontrado.
                return x;
            }
        }

        // Caso nenhum perfil corresponda ao código informado,
        // lança uma exceção indicando que o ID é inválido.
        throw new IllegalArgumentException("Id inválido: " + code);
    }
}
