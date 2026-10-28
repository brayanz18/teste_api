private UserService userService;

/* 

// Busca uma tarefa por ID validando se ela existe e se o usuário logado tem autorização para visualizá-la
@Transactional(readOnly = true)
public Task findById(Long id) {
@@ -118,4 +121,66 @@ public void delete(Long id) {
        throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas!");
    }
}
}
}
*/

public Task findById(Long id){
Optional<Task> task = this.taskRepository.findById(id);

Task t = task.orElseThrow(() ->new ObjectNotFoundException(
    "Tarefa não encontrada! Id:" + id + ", Tipo:" + Task.class.getName()));

    UserSpringSecurity userSpringSecurity = UserService.authenticated();

    if(Object.isNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnums.ADMIN) && !userHasTask(userSpringSecurity, t)){
        throw new AuthorizationException("Acesso Negado, Voc~e não possui permissão para acessar esta tarefa.");
    }
    return t;
}

public List<TaskProjection> findByLoggedUser(){
    UserSpringSecurity userSpringSecurity = UserService.authenticated();

    if(Objects.isNull(userSpringSecurity){
        throw new AuthorizationException("Acesso negado! Usuário não autenticado.");
    }

    return this.taskRepository.findByUser_Id(userSpringSecurity.getId());
}

@Transactional 
public Task create(Task obj){
    UserSpringSecurity userSpringSecurity = UserService.authenticated();

    if(Objects.isNull(userSpringSecurity)){
        throw new AuthorizationException("Acesso negado, você precisa estar autenticado.");
    }

    User user = this.userService.findById(userSpringSecurity.getId());

    obj.setId(null);
    obj.setUser(user);

    return this.taskRepository.save(obj);
}

@Transactional 
public Task update (Task obj){
    Task newObj = findById(obj.getId());
    newObj.setDescription(obj.getDescription());
    return this.taskRepository.save(newObj);
}

public void delete(Long id){
    findById(id);
    try{
        this.taskRepository.deleteById(id);
    } catch (Exception e){
        throw new AuthorizationException("Não foi possivel excluir a tarefa especificada");
    }
}

private boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
    return task.getUser().getId().equals(userSpringSecurity.getId());
}
}