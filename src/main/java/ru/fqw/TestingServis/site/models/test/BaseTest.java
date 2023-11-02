package ru.fqw.TestingServis.site.models.test;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import ru.fqw.TestingServis.site.models.user.BaseUser;

import java.util.Date;

@Data
@MappedSuperclass
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


}
