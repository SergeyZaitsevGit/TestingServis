package ru.fqw.TestingServis.site.models.test;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PreRemove;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.user.User;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "test")

public class Test extends BaseTest {

  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "TestLikeQuestion",
      joinColumns = @JoinColumn(name = "test_id"),
      inverseJoinColumns = @JoinColumn(name = "question_id"))
  @org.springframework.data.annotation.Transient
  private Set<Question> questionSet = new LinkedHashSet<>();

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn(name = "user_id", nullable = false)
  @org.springframework.data.annotation.Transient
  private User creator;

  @Override
  public String toString() {
    String result = "Название теста: " + super.name
        + "\nВремя на тест: " + super.timeActiv + " минут"
        + "\n Создатель теста " + super.baseUser.getUsername();
    return result;
  }

  public Test(Test test) {
    super(test);
    this.questionSet = new LinkedHashSet<>(test.questionSet);
    this.creator = test.getCreator();
  }

  @PreRemove
  private void removeTestAssociations() {
    questionSet.clear();
  }

  @PostLoad
  private void init() {
    baseUser = creator;
//    for (Question q : questionSet) {
//      maxBall += q.getBall();
//    }
  }
}
