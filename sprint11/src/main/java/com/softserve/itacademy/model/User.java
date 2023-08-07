package com.softserve.itacademy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @GenericGenerator(name = "sequence-generator", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "user_sequence"),
            @Parameter(name = "initial_value", value = "10"),
            @Parameter(name = "increment_size", value = "1") })
    private long id;

    @NotBlank(message = "The first_name cannot be empty")
    @Column(name = "First_Name", nullable = false)
    @Pattern(regexp = "[A-Z]\\w*(-[A-Z]\\w*)?", message = "Please provide a valid name")
    @Size(min = 1, max = 255, message = "the first name range must be from 1 to 255")
    private String firstName;

    @NotBlank(message = "The last_name cannot be empty")
    @Column(name = "Last_Name", nullable = false)
    @Pattern(regexp = "[A-Z]\\w*(-[A-Z]\\w*)?", message = "Please provide a valid last name")
    @Size(min = 1, max = 255, message = "the last name range must be from 1 to 255")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @Email(regexp = ".+@.+\\..+", message = "Please provide a valid email address")
    @Size(max = 255, message = "the email range must be from 1 to 255")
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Column(name = "Password", nullable = false)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[_!@#$%\\^&\\*]).{6,}$", message = "Please provide a valid password: Quiew12_!")
    @Size(min = 1, max = 255, message = "the password range must be from 1 to 255")
    private String password;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "owner")
    private Set<ToDo> ownedToDos;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "collaborator_id"),
            inverseJoinColumns = @JoinColumn(name = "todo_id"))
    private Set<ToDo> collaboratedToDos;

}
