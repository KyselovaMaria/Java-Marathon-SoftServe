package com.softserve.itacademy.todolist.repository;

import com.softserve.itacademy.todolist.dto.todo.ToDoDto;
import com.softserve.itacademy.todolist.dto.todo.ToDoResponse;
import com.softserve.itacademy.todolist.model.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    @Query(value = "select * " +
                   "from todos\n" +
                   "    where owner_id = :userId\n" +
                   "union\n" +
                   "select * " +
                   "from todos t inner join todo_collaborator tc\n" +
                   "    on t.id = tc.todo_id and tc." +
                   "collaborator_id = :userId;", nativeQuery = true)
    List<ToDo> getByUserId(long userId);

    @Query("SELECT new com.softserve.itacademy.todolist.dto.todo.ToDoResponse(" +
            "t.id, t.title, t.createdAt, t.owner.email) " +
            "FROM ToDo t")
    List<ToDoResponse> findAllToDoDtos();

    @Query("SELECT new com.softserve.itacademy.todolist.dto.todo.ToDoResponse(" +
            "t.id, t.title, t.createdAt, t.owner.email) " +
            "FROM ToDo t WHERE t.owner.id = :userId")
    List<ToDoResponse> getAllToDoDtosByUserId(@Param("userId") long userId);

    @Query("SELECT new com.softserve.itacademy.todolist.dto.todo.ToDoResponse(" +
            "t.id, t.title, t.createdAt, t.owner.email) " +
            "FROM ToDo t WHERE t.id = :id")
    Optional<ToDoResponse> findToDoDtoById(@Param("id") long id);

    @Query("SELECT DISTINCT t FROM ToDo t " +
            "LEFT JOIN FETCH t.collaborators " +
            "LEFT JOIN FETCH t.owner owner " +
            "LEFT JOIN FETCH owner.role " +
            "WHERE t.id = :id")
    ToDo findByIdFetchCollaborators(@Param("id") Long id);
}
