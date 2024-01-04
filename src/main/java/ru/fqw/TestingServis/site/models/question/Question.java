package ru.fqw.TestingServis.site.models.question;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PreRemove;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseQuestion {

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "questionSet")
  @JsonIgnore
  @org.springframework.data.annotation.Transient
  private Set<Test> testSet = new LinkedHashSet<>();

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id", nullable = false)
  @JsonIgnore
  @org.springframework.data.annotation.Transient
  private User creator;


  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)

  @JoinColumn(name = "type_id", nullable = true)
  @org.springframework.data.annotation.Transient
  private Type type;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "question", cascade = {CascadeType.MERGE,
      CascadeType.PERSIST}, orphanRemoval = true)
  @org.springframework.data.annotation.Transient
  @BatchSize(size = 10)
  private List<Answer> answerList = new ArrayList<>();

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append(super.text).append("\n");

    switch (super.typeAnswerOptions) {
      case ONE_ANSWER -> result.append("Выберите один из перечисленных вариантов ответа:\n\n");
      case MANY_ANSWER ->
          result.append("Выберите несколько из перечисленных вариантов ответа:\n\n");
      case FREE_ANSWER -> result.append("Введите ответ на вопрос в свободной форме:\n\n");
    }

    for (int i = 0; i < answerList.size(); i++) {
      result.append((i + 1)).append(". ").append(answerList.get(i).getText()).append("\n");
    }
    return result.toString();
  }

  @PreRemove
  private void removeQuestionAssociations() {
    for (Test test : this.testSet) {
      test.getQuestionSet().remove(this);
    }
    this.creator.getQuestionList().remove(this);
    if (this.type != null) {
      this.type.getQuestionList().remove(this);
    }
  }

  @PostLoad
  private void init() {
    baseAnswerList.addAll(answerList);
  }

}
