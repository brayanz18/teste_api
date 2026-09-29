// Declara o pacote onde a classe está localizada
package api_teste.ds.services;

// Importa a classe Optional para trabalhar com valores que podem ou não existir
import java.util.Optional;

// Permite que o Spring faça a injeção automática de dependências
import org.springframework.beans.factory.annotation.Autowired;

// Permite controlar as transações realizadas no banco de dados
import org.springframework.transaction.annotation.Transactional;

// Importa a anotação que identifica a classe como um serviço do Spring
import org.springframework.stereotype.Service;

// Importa a classe User, que representa a entidade de usuários
import api_teste.ds.models.User;

// Importa o repositório responsável pelas operações da entidade User
import api_teste.ds.repositories.UserRepository;

// Indica ao Spring que esta classe contém regras de negócio e funciona como um serviço
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Busca um usuário pelo ID
    public User findById(Long id) {

        Optional<User> user = this.userRepository.findById(id);

        return user.orElseThrow(() -> new RuntimeException(
            "Usuário não encontrado! Id: " + id +
            ", Tipo: " + User.class.getName()
        ));
    }

    // Cria um novo usuário
    @Transactional
    public User create(User obj) {

        // Garante que o ID seja nulo para criar um novo registro
        obj.setId(null);

        // Salva o usuário no banco de dados
        obj = this.userRepository.save(obj);

        // Retorna o usuário criado
        return obj;
    }

    // Atualiza um usuário existente
    @Transactional
    public User update(User obj) {

        // Busca o usuário existente pelo ID
        User newObj = findById(obj.getId());

        // Atualiza a senha
        newObj.setPassword(obj.getPassword());

        // Salva as alterações no banco de dados
        return this.userRepository.save(newObj);
    }

    // Deleta um usuário pelo ID
    public void delete(Long id) {

        // Verifica se o usuário existe
        findById(id);

        try {

            // Deleta o usuário pelo ID
            this.userRepository.deleteById(id);

        } catch (Exception e) {

            // Captura erros relacionados a entidades
            throw new RuntimeException(
                "Não é possível excluir o usuário pois há entidades relacionadas."
            );
        }
    }
}