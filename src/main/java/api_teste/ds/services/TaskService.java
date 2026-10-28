
package api_teste.ds.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api_teste.ds.models.Task;
import api_teste.ds.models.User;
import api_teste.ds.models.enums.ProfileEnums;
import api_teste.ds.models.projections.TaskProjection;
import api_teste.ds.repositories.TaskRepository;
import api_teste.ds.security.UserSpringSecurity;
import api_teste.ds.exceptions.AuthorizationException;
import api_teste.ds.services.exceptions.ObjectNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    // Busca uma tarefa por ID, validando sua existência e autorização.
    @Transactional(readOnly = true)
    public Task findById(Long id) {

        Optional<Task> task = this.taskRepository.findById(id);

        Task t = task.orElseThrow(() ->
                new ObjectNotFoundException(
                        "Tarefa não encontrada! Id: " + id
                        + ", Tipo: " + Task.class.getName()));

        UserSpringSecurity userSpringSecurity =
                UserService.authenticated();

        if (Objects.isNull(userSpringSecurity)
                || (!userSpringSecurity.hasRole(ProfileEnums.ADMIN)
                && !userHasTask(userSpringSecurity, t))) {

            throw new AuthorizationException(
                    "Acesso negado! Você não possui permissão "
                    + "para acessar esta tarefa.");
        }

        return t;
    }

    // Lista as tarefas do usuário autenticado.
    @Transactional(readOnly = true)
    public List<TaskProjection> findByLoggedUser() {

        UserSpringSecurity userSpringSecurity =
                UserService.authenticated();

        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException(
                    "Acesso negado! Usuário não autenticado.");
        }

        return this.taskRepository.findByUser_Id(
                userSpringSecurity.getId());
    }

    // Cria uma tarefa vinculada ao usuário autenticado.
    @Transactional
    public Task create(Task obj) {

        UserSpringSecurity userSpringSecurity =
                UserService.authenticated();

        if (Objects.isNull(userSpringSecurity)) {
            throw new AuthorizationException(
                    "Acesso negado, você precisa estar autenticado.");
        }

        User user = this.userService.findById(
                userSpringSecurity.getId());

        obj.setId(null);
        obj.setUser(user);

        return this.taskRepository.save(obj);
    }

    // Atualiza uma tarefa após verificar a autorização.
    @Transactional
    public Task update(Task obj) {

        Task newObj = findById(obj.getId());

        newObj.setDescription(obj.getDescription());

        return this.taskRepository.save(newObj);
    }

    // Exclui uma tarefa após verificar a autorização.
    @Transactional
    public void delete(Long id) {

        findById(id);

        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new AuthorizationException(
                    "Não foi possível excluir a tarefa especificada.");
        }
    }

    // Verifica se a tarefa pertence ao usuário autenticado.
    private boolean userHasTask(
            UserSpringSecurity userSpringSecurity,
            Task task) {

        return task.getUser().getId()
                .equals(userSpringSecurity.getId());
    }
}
