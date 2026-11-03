
package api_teste.ds.repositories;

// Importa List para trabalhar com listas de tarefas retornadas do banco.
import java.util.List;

// Importa JpaRepository, que fornece métodos prontos para operações no banco.
import org.springframework.data.jpa.repository.JpaRepository;

// Importa Query para criar consultas personalizadas utilizando JPQL ou SQL.
import org.springframework.data.jpa.repository.Query;

// Importa Param para identificar parâmetros utilizados nas consultas.
import org.springframework.data.repository.query.Param;

// Importa Repository para identificar esta interface como um componente de repositório.
import org.springframework.stereotype.Repository;

// Importa Task, que representa a entidade de tarefas no banco de dados.
import api_teste.ds.models.Task;

// Importa TaskProjection, utilizada para retornar apenas alguns campos da tarefa.
import api_teste.ds.models.projections.TaskProjection;


// Indica que esta interface é responsável pelo acesso aos dados.
@Repository

// Herda JpaRepository, que fornece operações prontas para a entidade Task.
// Task representa a entidade que será manipulada.
// Long representa o tipo de dado utilizado como chave primária (ID).
public interface TaskRepository extends JpaRepository<Task, Long> {


    // =========================================================================
    // OPÇÃO 1: DERIVED QUERY METHOD
    // Método de consulta criado automaticamente pelo Spring Data JPA.
    // O Spring interpreta o nome do método e monta a consulta necessária.
    // =========================================================================

    // Busca as tarefas que pertencem ao usuário cujo ID foi informado.
    // findBy indica que será realizada uma consulta.
    // User_Id indica que a busca será feita pelo ID do usuário relacionado à tarefa.
    // Retorna uma lista de TaskProjection, trazendo apenas os campos definidos
    // na projeção, em vez de retornar necessariamente a entidade completa.
    List<TaskProjection> findByUser_Id(Long id);


    /*
    // =========================================================================
    // OPÇÃO 2: CONSULTA JPQL
    // Utiliza a linguagem de consulta do JPA.
    // Trabalha com entidades e atributos das classes Java,
    // em vez de utilizar diretamente os nomes das tabelas e colunas do banco.
    // =========================================================================

    // @Query permite escrever uma consulta personalizada.
    // SELECT t seleciona os registros representados pelo objeto t.
    // FROM Task t indica que a consulta será realizada na entidade Task.
    // WHERE t.user.id = :id filtra as tarefas pelo ID do usuário.
    // @Param("id") associa o parâmetro Java ao parâmetro :id da consulta.

    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
    List<Task> findByUser_IdJPQL(@Param("id") Long id);
    */


    /*
    // =========================================================================
    // OPÇÃO 3: CONSULTA SQL NATIVA
    // Utiliza SQL diretamente, seguindo a estrutura das tabelas do banco.
    // =========================================================================

    // @Query permite escrever uma consulta SQL personalizada.
    // SELECT * seleciona todas as colunas da tabela.
    // FROM task t indica que a consulta será feita na tabela task.
    // WHERE t.user_id = :id filtra as tarefas pelo ID do usuário.
    // @Param("id") associa o parâmetro Java ao parâmetro :id.
    // nativeQuery = true informa que a consulta utiliza SQL nativo,
    // e não JPQL.

    @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
    List<Task> findByUser_IdSQL(@Param("id") Long id);
    */

}
