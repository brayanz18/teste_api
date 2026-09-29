// Declaração do pacote onde esta classe controller está localizada
package api_teste.ds.controllers;

// Import de classe utilitária para manipular e representar URIs/URLs
import java.net.URI;

// Anotações e classes do Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

// Import para ativar a validação
import org.springframework.validation.annotation.Validated;

// Mapeamentos de requisições HTTP
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Utilitário para construir URIs dinamicamente
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// Import das classes do projeto
import api_teste.ds.models.User;
import api_teste.ds.models.User.CreateUser;
import api_teste.ds.models.User.UpdateUser;
import api_teste.ds.services.UserService;

// Classe responsável pelos endpoints REST relacionados aos usuários
@RestController

// Define que todas as rotas desta classe terão o prefixo "/user"
@RequestMapping("/user")

// Ativa a validação dos parâmetros recebidos
@Validated
public class UserController {

    // Injeta automaticamente o UserService nesta classe
    @Autowired
    private UserService userService;

    // Mapeia requisições HTTP GET na rota "/user/{id}"
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {

        // Busca o usuário através do ID recebido pela URL
        User obj = this.userService.findById(id);

        // Retorna HTTP 200 (OK) com o usuário no corpo da resposta
        return ResponseEntity.ok().body(obj);
    }

    // Mapeia requisições HTTP POST na rota "/user"
    // Responsável pela criação de um novo usuário
    @PostMapping
    public ResponseEntity<Void> create(
            @Validated(CreateUser.class) @RequestBody User obj) {

        // Valida as regras de CreateUser e desserializa o corpo JSON
        this.userService.create(obj);

        // Monta a URL do usuário que acabou de ser criado
        URI url = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(obj.getId())
                .toUri();

        // Retorna HTTP 201 (Created)
        // e a URL do novo usuário no cabeçalho Location
        return ResponseEntity.created(url).build();
    }

    // Mapeia requisições HTTP PUT na rota "/user/{id}"
    // Responsável pela atualização do usuário
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @Validated(User.UpdateUser.class) @RequestBody User obj,
            @PathVariable Long id) {

        // Garante que o ID do objeto seja o mesmo informado na URL
        obj.setId(id);

        // Executa a atualização do usuário no banco de dados
        this.userService.update(obj);

        // Retorna HTTP 204 (No Content), indicando sucesso sem corpo de resposta
        return ResponseEntity.noContent().build();
    }

    // Mapeia requisições HTTP DELETE na rota "/user/{id}"
    // Responsável pela exclusão do usuário
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Invoca o método de exclusão do serviço
        this.userService.delete(id);

        // Retorna HTTP 204 (No Content), confirmando a exclusão
        return ResponseEntity.noContent().build();
    }
}
