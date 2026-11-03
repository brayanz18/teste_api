
package api_teste.ds.controllers;

// Importa URI para construir o endereço do usuário cadastrado.
import java.net.URI;

// Importa Autowired para injetar automaticamente o serviço.
import org.springframework.beans.factory.annotation.Autowired;

// Importa ResponseEntity para controlar as respostas HTTP da API.
import org.springframework.http.ResponseEntity;

// Importa Validated para habilitar validações na classe.
import org.springframework.validation.annotation.Validated;

// Importa as anotações utilizadas para criar as rotas da API.
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Importa a ferramenta utilizada para construir o endereço do usuário criado.
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// Importa a classe User, que representa o usuário no sistema.
import api_teste.ds.models.User;

// Importa UserCreateDTO, utilizado para receber os dados de criação do usuário.
import api_teste.ds.models.dtos.UserCreateDTO;

// Importa UserUpdateDTO, utilizado para receber os dados de atualização do usuário.
import api_teste.ds.models.dtos.UserUpdateDTO;

// Importa UserService, responsável pelas regras de negócio dos usuários.
import api_teste.ds.services.UserService;

// Importa Valid para validar os dados recebidos nas requisições.
import jakarta.validation.Valid;


// Indica que esta classe é um controlador REST.
// Responsável por receber as requisições HTTP e retornar respostas para o cliente.
@RestController

// Define que todas as rotas desta classe começam com "/user".
@RequestMapping("/user")

// Habilita validações nos parâmetros e objetos utilizados nesta classe.
@Validated
public class UserController {

    // Injeta automaticamente o UserService nesta classe.
    // O serviço executa as operações relacionadas aos usuários.
    @Autowired
    private UserService userService;


    // Define uma rota GET para buscar um usuário pelo ID.
    // Exemplo: GET /user/1
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        // Chama o serviço para procurar o usuário pelo ID informado na URL.
        User obj = this.userService.findById(id);

        // Retorna o usuário encontrado com o código HTTP 200 (OK).
        return ResponseEntity.ok().body(obj);
    }


    // Define uma rota POST para cadastrar um novo usuário.
    // Exemplo: POST /user
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO objDto) {

        // Recebe os dados do DTO e converte para um objeto da classe User.
        // O DTO contém apenas os campos permitidos para a criação do usuário.
        User obj = this.userService.fromDTO(objDto);

        // Chama o serviço para cadastrar o usuário no banco de dados.
        // O objeto retornado contém os dados salvos, incluindo o ID gerado.
        obj = this.userService.create(obj);

        // Constrói o endereço do usuário que acabou de ser cadastrado.
        URI url = ServletUriComponentsBuilder

                // Obtém o endereço da requisição atual.
                .fromCurrentRequest()

                // Adiciona /{id} ao final do endereço.
                .path("/{id}")

                // Substitui {id} pelo ID do usuário cadastrado.
                .buildAndExpand(obj.getId())

                // Converte o endereço construído para o tipo URI.
                .toUri();

        // Retorna HTTP 201 (Created), indicando que o usuário foi criado.
        // O endereço do usuário criado é enviado no cabeçalho Location.
        return ResponseEntity.created(url).build();
    }


    // Define uma rota PUT para atualizar os dados de um usuário.
    // Exemplo: PUT /user/1
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(

            // Recebe os dados de atualização enviados no corpo da requisição.
            // @Valid verifica se os dados atendem às validações do DTO.
            @Valid @RequestBody UserUpdateDTO objDto,

            // Recebe o ID do usuário informado na URL.
            @PathVariable Long id) {

        // Define no DTO o ID recebido pela URL.
        // Assim, a atualização será direcionada ao usuário correto.
        objDto.setId(id);

        // Converte o DTO de atualização para um objeto da classe User.
        User obj = this.userService.fromDTO(objDto);

        // Chama o serviço para executar a atualização do usuário.
        this.userService.update(obj);

        // Retorna HTTP 204 (No Content), indicando que a atualização
        // foi realizada com sucesso, sem conteúdo na resposta.
        return ResponseEntity.noContent().build();
    }


    // Define uma rota DELETE para excluir um usuário pelo ID.
    // Exemplo: DELETE /user/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Chama o serviço para excluir o usuário correspondente ao ID informado.
        this.userService.delete(id);

        // Retorna HTTP 204 (No Content), indicando que a exclusão foi realizada.
        return ResponseEntity.noContent().build();
    }
}
