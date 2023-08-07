package com.softserve.itacademy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "states")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class State {

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "state_sequence"),
                    @Parameter(name = "initial_value", value = "10"),
                    @Parameter(name = "increment_size", value = "1")
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @NotNull(message = "State name is mandatory")
    @Size(min = 1, max = 20, message = "State name length should be between 1 and 20 characters")
    @Pattern(regexp = "[-a-zA-Z0-9_ ]+", message = "State name can only contain letters, numbers, dash, space, and underscore")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

}