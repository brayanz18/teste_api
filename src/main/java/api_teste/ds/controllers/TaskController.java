
// Define o pacote onde está localizada esta classe.
package api_teste.ds.controllers;

// Importa URI para criar o endereço da tarefa cadastrada.
import java.net.URI;

// Importa List para trabalhar com listas de tarefas.
import java.util.List;

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

// Importa a ferramenta que permite construir o endereço de um recurso criado.
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// Importa a classe Task, que representa uma tarefa no sistema.
import api_teste.ds.models.Task;

// Importa TaskProjection, que permite retornar apenas alguns dados da tarefa.
import api_teste.ds.models.projections.TaskProjection;

// Importa TaskService, responsável pelas regras de negócio das tarefas.
import api_teste.ds.services.TaskService;

// Importa Valid para validar os dados recebidos nas requisições.
import jakarta.validation.Valid;


// Indica que esta classe é um controlador REST.
// Assim, ela recebe requisições HTTP e retorna respostas para o cliente.
@RestController

// Define que todas as rotas desta classe começam com "/task".
@RequestMapping("/task")

// Habilita validações nos parâmetros e objetos utilizados nesta classe.
@Validated
public class TaskController {

    // Injeta automaticamente o TaskService nesta classe.
    // O serviço é responsável por executar as operações com as tarefas.
    @Autowired
    private TaskService taskService;


    // Define uma rota GET para buscar uma tarefa pelo seu ID.
    // Exemplo: GET /task/1
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById(@PathVariable Long id) {

        // Chama o serviço para procurar a tarefa correspondente ao ID informado.
        Task obj = this.taskService.findById(id);

        // Retorna a tarefa encontrada com o código HTTP 200 (OK).
        return ResponseEntity.ok().body(obj);
    }


    // Define uma rota GET para listar as tarefas do usuário autenticado.
    // Exemplo: GET /task/user
    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findAllByLoggedUser() {

        // Chama o serviço para buscar as tarefas pertencentes ao usuário logado.
        // O retorno utiliza TaskProjection para selecionar apenas os campos necessários.
        List<TaskProjection> objs = this.taskService.findByLoggedUser();

        // Retorna a lista de tarefas com o código HTTP 200 (OK).
        return ResponseEntity.ok().body(objs);
    }


    // Define uma rota POST para cadastrar uma nova tarefa.
    // Exemplo: POST /task
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) {

        // Recebe os dados da requisição e solicita ao serviço que cadastre a tarefa.
        // O objeto retornado contém os dados da tarefa salva, incluindo seu ID.
        obj = this.taskService.create(obj);

        // Constrói o endereço da tarefa que acabou de ser cadastrada.
        URI url = ServletUriComponentsBuilder.fromCurrentRequest()

                // Adiciona o ID da tarefa ao final do endereço.
                .path("/{id}")

                // Substitui {id} pelo ID gerado para a tarefa.
                .buildAndExpand(obj.getId())

                // Converte o endereço construído para o tipo URI.
                .toUri();

        // Retorna HTTP 201 (Created), indicando que a tarefa foi cadastrada.
        // O endereço da tarefa criada é enviado no cabeçalho Location.
        return ResponseEntity.created(url).build();
    }


    // Define uma rota PUT para atualizar uma tarefa existente.
    // Exemplo: PUT /task/1
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(

            // Recebe os dados da tarefa enviados no corpo da requisição.
            // @Valid verifica se os dados atendem às validações definidas.
            @Valid @RequestBody Task obj,

            // Recebe o ID da tarefa informado no endereço da requisição.
            @PathVariable Long id) {

        // Define no objeto o ID recebido pela URL.
        // Assim, o serviço sabe qual tarefa deverá ser atualizada.
        obj.setId(id);

        // Chama o serviço para executar a atualização da tarefa.
        this.taskService.update(obj);

        // Retorna HTTP 204 (No Content), indicando que a atualização foi realizada
        // com sucesso e que não existe conteúdo para devolver na resposta.
        return ResponseEntity.noContent().build();
    }


    // Define uma rota DELETE para excluir uma tarefa pelo ID.
    // Exemplo: DELETE /task/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Chama o serviço para excluir a tarefa correspondente ao ID informado.
        this.taskService.delete(id);

        // Retorna HTTP 204 (No Content), indicando que a exclusão foi realizada.
        return ResponseEntity.noContent().build();
    }
}
