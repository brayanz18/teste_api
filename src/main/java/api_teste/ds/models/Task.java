package api_teste.ds.models;

import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* 
@Entity
@Table(name = Task.TABLE_NAME)
public class Task {
    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "description", length = 255, nullable = false)
    @NotBlank
    @Size(min = 1, max = 255)
    private String description;

    public Task() {
    }

    public Task(Long id, User user, String description) {
        this.id = id;
        this.user = user;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) &&
               Objects.equals(user, task.user) &&
               Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, description);
    }
}
    */

@Entity

// Define que a entidade será armazenada na tabela chamada "task".
@Table(name = Task.TABLE_NAME)

// Gera automaticamente um construtor com todos os atributos.
@AllArgsConstructor

// Gera automaticamente um construtor sem parâmetros.
// O JPA precisa de um construtor vazio para trabalhar com a entidade.
@NoArgsConstructor

// Gera automaticamente Getters, Setters e outros métodos úteis.
@Data
public class Task {

    // Define uma constante com o nome da tabela utilizada pela entidade.
    public static final String TABLE_NAME = "task";


    // Identifica o atributo abaixo como chave primária da tabela.
    @Id

    // Define que o banco de dados será responsável por gerar o ID automaticamente.
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // Define a coluna "id" como única na tabela.
    @Column(name = "id", unique = true)

    // Armazena o identificador único da tarefa.
    private Long id;


    // Define um relacionamento muitos para um (ManyToOne).
    // Isso significa que um usuário pode possuir várias tarefas,
    // mas cada tarefa está associada a um usuário.
    @ManyToOne

    // Define a coluna "user_id" como chave estrangeira.
    // nullable = false: a tarefa obrigatoriamente precisa ter um usuário.
    // updatable = false: o usuário associado não poderá ser alterado
    // por uma atualização comum desta entidade.
    @JoinColumn(name = "user_id", nullable = false, updatable = false)

    // Armazena o usuário responsável pela tarefa.
    private User user;


    // Define as configurações da coluna "description" no banco.
    // length = 250: permite armazenar até 250 caracteres na coluna.
    // nullable = false: a descrição não pode ser nula.
    @Column(name = "description", length = 250, nullable = false)

    // Impede que a descrição seja nula, vazia ou contenha apenas espaços.
    @NotBlank

    // Define que a descrição deve possuir no mínimo 1
    // e no máximo 255 caracteres.
    @Size(min = 1, max = 255)

    // Armazena o texto que descreve a tarefa.
    private String description;

}