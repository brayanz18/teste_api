package api_teste.ds.controllers; // Pacote onde o controller está localizado

// Importações necessárias para manipular URIs e coleções
import java.net.URI;
import java.util.List;

// Importações de anotações e classes do Spring Framework
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// Importações das validações e das classes do próprio projeto (Model e Service)
import jakarta.validation.Valid;
import api_teste.ds.models.Task;
import api_teste.ds.services.TaskService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // Define esta classe como um Controller REST
@RequestMapping("/task") // Define a rota base "/task" para as requisições
@Validated // Ativa a validação de dados para os métodos do Controller

public class TaskController {

    @Autowired // Realiza a injeção automática de dependência do TaskService
    private TaskService taskService;

    @GetMapping("path") // Mapeia requisições GET no caminho especificado
    public ResponseEntity<Task> findById(@PathVariable Long id) { // Extrai o parâmetro "id" da URL
        Task obj = this.taskService.findById(id); // Chama a camada de serviço para buscar a tarefa
        return ResponseEntity.ok().body(obj); // Retorna a tarefa encontrada no corpo da resposta com HTTP 200 (OK)
    }

    @GetMapping("/user/{userid}") // Mapeia requisições GET para a rota de tarefas por usuário
    public ResponseEntity<List<Task>> findByUserId(@PathVariable Long userId) { // Extrai o parâmetro "userId" da URL
        List<Task> objs = this.taskService.findAllbyUserId(userId); // Busca todas as tarefas do usuário no serviço
        return ResponseEntity.ok().body(objs); // Retorna a lista de tarefas no corpo da resposta com HTTP 200 (OK)
    }

    @PostMapping // Mapeia requisições POST para criação
    public ResponseEntity<Void> create(@Valid @RequestBody Task obj) { // Mapeia o JSON do corpo da requisição para o
                                                                       // objeto Task e o valida
        this.taskService.create(obj); // Chama a camada de serviço para salvar a nova tarefa
        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(url).build(); // Retorna o status HTTP 201 (Created) contendo a URI no cabeçalho
    }

    @PostMapping("/{id}") // Mapeia requisições POST para a rota com ID do recurso a ser atualizado
    public ResponseEntity<Void> update(@Valid @RequestBody Task obj, @PathVariable Long id) { // Recebe o corpo validado
                                                                                              // e o ID da URL
        obj.setId(id); // Garante que o objeto receba o ID informado na URL
        this.taskService.update(obj); // Chama a camada de serviço para atualizar a tarefa
        return ResponseEntity.noContent().build(); // Retorna status HTTP 204 (No Content) indicando sucesso sem corpo
    }

    @DeleteMapping("/{id}") // Mapeia requisições DELETE para remover pelo ID
    public ResponseEntity<Void> delete(@PathVariable Long id) { // Extrai o ID da URL para remoção
        this.taskService.delete(id); // Chama a camada de serviço para deletar a tarefa
        return ResponseEntity.noContent().build(); // Retorna status HTTP 204 (No Content) indicando remoção realizada
    }

}