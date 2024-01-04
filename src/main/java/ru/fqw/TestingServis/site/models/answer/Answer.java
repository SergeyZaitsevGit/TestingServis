package ru.fqw.TestingServis.site.models.answer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.site.models.question.Question;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseAnswer {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "question_id")
  @JsonIgnore
  @org.springframework.data.annotation.Transient
  private Question question;

}
