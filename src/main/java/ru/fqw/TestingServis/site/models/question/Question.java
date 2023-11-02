package ru.fqw.TestingServis.site.models.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.models.answer.Answer;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseQuestion{

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "questionSet")
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private Set<Test> testSet = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @org.springframework.data.annotation.Transient
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "type_id", nullable = true)
    @org.springframework.data.annotation.Transient
    private Type type;

    @PreRemove
    private void removeQuestionAssociations() {
        for (Test test: this.testSet) {
            test.getQuestionSet().remove(this);
        }
       this.creator.getQuestionList().remove(this);
        if (this.type != null)this.type.getQuestionList().remove(this);
    }
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question",  cascade = {CascadeType.MERGE}, orphanRemoval=true)
    @org.springframework.data.annotation.Transient
    private List<Answer> answerList = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(super.text).append("\n");

        switch (super.typeAnswerOptions){
            case ONE_ANSWER -> result.append("Выберите один из перечисленных вариантов ответа:\n\n");
            case MANY_ANSWER -> result.append("Выберите несколько из перечисленных вариантов ответа:\n\n");
            case FREE_ANSWER -> result.append("Введите ответ на вопрос в свободной форме:\n\n");
        }

        for (int i = 0; i<answerList.size(); i++){
            result.append((i + 1)).append(". ").append(answerList.get(i).getText()).append("\n");
        }
        return result.toString();
    }

    @PostLoad
    private void init(){
        baseAnswerList.addAll(answerList);
    }

}