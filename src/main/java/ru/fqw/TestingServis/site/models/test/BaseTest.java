package ru.fqw.TestingServis.site.models.test;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import ru.fqw.TestingServis.site.models.user.BaseUser;

@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class BaseTest {

  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
  protected Long id;

  @NotBlank(message = "Имя теста не может быть пустым")
  protected String name;

  protected Date dateCreated;

  protected boolean activ = false;

  @Min(value = 3, message = "Время теста должно быть от 3 минут")
  protected int timeActiv;

  protected boolean mixQuestions = true;

  protected boolean mixAnswers = true;

  @Transient
  protected BaseUser baseUser;

  protected Integer countQuestion;

  protected Integer maxBall;

  public String getFormatedDataCreated() {
    SimpleDateFormat sdf =
        new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm");
    return sdf.format(dateCreated);
  }

  public BaseTest(BaseTest baseTest) {
    this.id = baseTest.getId();
    this.name = baseTest.getName();
    this.dateCreated = baseTest.dateCreated;
    this.activ = baseTest.isActiv();
    this.timeActiv = baseTest.timeActiv;
    this.mixQuestions = baseTest.mixQuestions;
    this.mixAnswers = baseTest.mixAnswers;
    this.baseUser = baseTest.baseUser;
  }
}
