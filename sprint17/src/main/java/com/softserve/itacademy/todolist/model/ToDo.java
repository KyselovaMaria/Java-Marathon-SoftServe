package com.softserve.itacademy.todolist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity @Table(name = "todos")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "todo_seq")
    @SequenceGenerator(name = "todo_seq",
            sequenceName = "todo_sequence",
            initialValue = 10,
            allocationSize = 1)
    private Long id;

    @NotBlank(message = "The 'title' cannot be empty")
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
    private Set<User> collaborators;

    public ToDo(String title, LocalDateTime createdAt) {
        this.title = title;
        this.createdAt = createdAt;
    }

    public void addCollaborator(User collaborator) {
        collaborator.getMyTodos().add(this);
        collaborators.add(collaborator);
    }

    public void deleteCollaborator(User collaborator) {
        collaborator.getMyTodos().remove(this);
        collaborators.remove(collaborator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ToDo toDo = (ToDo) o;
        return getId() != null && getId().equals(toDo.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "ToDo { " +
               "id = " + id +
               ", title = '" + title + '\'' +
               ", createdAt = " + createdAt +
               " }";
    }
}
