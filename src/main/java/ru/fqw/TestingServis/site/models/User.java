package ru.fqw.TestingServis.site.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.site.models.Emuns.Role;

import java.util.List;


@Entity
@Table(name = "usr")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    private Long id;
    private String username;

    private String email;
    private String password;
    private boolean activite;
    private Role role = Role.USER;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private List<Test> testList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private List<Question> questionList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private List<Type> typeList;


}