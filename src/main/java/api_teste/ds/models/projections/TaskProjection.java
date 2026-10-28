package api_teste.ds.models.projections;

public interface TaskProjection {
    //metodo de getter para mapear e extrair apenas o ID da tarefa
    Long getId();

    //Metodo getter para mapear e extrair a descrição da tarefa.
    String getDescription();
    
} 
