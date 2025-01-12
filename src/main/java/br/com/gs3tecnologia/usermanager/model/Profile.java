package br.com.gs3tecnologia.usermanager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "TB_PROFILE")
public class Profile implements Serializable {

	@Serial
    private static final long serialVersionUID = 5680636709908286540L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    @Column(unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "profile")
    private List<User> users;

}

