package ru.fqw.TestingServis.site.models;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.servise.AnswerServise;
import ru.fqw.TestingServis.site.servise.QuestionServise;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionResource {
    QuestionServise questionServise;
    AnswerServise answerServise;
    @PostMapping
    public Question createQuestion(@RequestBody @Valid Question question, Errors errors){
        if (errors.hasErrors()){
            return null;
        }
        List<Answer>  answerList = new ArrayList<>(question.getAnswerList());
        question.getAnswerList().clear();
        questionServise.saveQuestion(question);
        for (Answer a : answerList){
            answerServise.saveAnswer(a,question);
        }
        return question;
    }
}
