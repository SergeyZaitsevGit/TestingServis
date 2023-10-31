package ru.fqw.TestingServis.site.models.answer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;

@Data
@MappedSuperclass
public class BaseAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "Введите вариант ответа")
    protected String text;
    protected boolean corrected;
}
