package ru.fqw.TestingServis.site.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.fqw.TestingServis.site.models.Emuns.TypeAnswerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Вопрос не может быть пустым")
    private String text;
    @Min(value = 1, message = "Ошибка ввода балла")
    private int ball;
    private TypeAnswerOptions typeAnswerOptions = TypeAnswerOptions.ONE_ANSWER;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questionSet", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Test> testSet = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question",  cascade = {CascadeType.MERGE}, orphanRemoval=true)
    private List<Answer> answerList;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = true)
    private Type type;

    @PreRemove
    private void removeQuestionAssociations() {
        for (Test test: this.testSet) {
            test.getQuestionSet().remove(this);
        }
       this.creator.getQuestionList().remove(this);
        if (this.type != null)this.type.getQuestionList().remove(this);
    }


}
