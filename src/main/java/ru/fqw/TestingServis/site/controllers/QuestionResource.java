package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.Answer;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.servise.AnswerServise;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TypeServise;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionResource {
    QuestionServise questionServise;
    AnswerServise answerServise;
    TypeServise typeServise;
    @PostMapping
    public Question createQuestion(@RequestBody  Question question, @RequestParam("typeId") Long typeId){
        question.setType(typeServise.getTypeById(typeId));
        List<Answer>  answerList = new ArrayList<>(question.getAnswerList());
        question.getAnswerList().clear();
        questionServise.saveQuestion(question);
            for (Answer a : answerList){
                answerServise.saveAnswer(a,question);
            }
        return question;
    }
}
