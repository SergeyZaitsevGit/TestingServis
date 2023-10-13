package ru.fqw.TestingServis.site.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Type { //Предметная область вопроса
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   // @Column(unique = true)
    private String name;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User creator;

}
