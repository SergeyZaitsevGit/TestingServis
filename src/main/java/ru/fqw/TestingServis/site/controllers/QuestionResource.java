package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.Answer;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.servise.AnswerServise;
import ru.fqw.TestingServis.site.servise.QuestionServise;
import ru.fqw.TestingServis.site.servise.TypeServise;
import ru.fqw.TestingServis.site.servise.UserServise;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/question")
@AllArgsConstructor
public class QuestionResource {
    QuestionServise questionServise;
    AnswerServise answerServise;
    TypeServise typeServise;
    UserServise userServise;
    @PostMapping
    public Question createQuestion(@RequestBody  Question question, @RequestParam("typeId") Long typeId){
        if (typeId != -1)question.setType(typeServise.getTypeById(typeId));
        List<Answer>  answerList = new ArrayList<>(question.getAnswerList());
        question.getAnswerList().clear();
        questionServise.saveQuestion(question);
            for (Answer a : answerList){
                answerServise.saveAnswer(a,question);
            }
        return question;
    }

    @GetMapping("type/{typeId}")
    public List<Long> findQuestionByTypeId(@PathVariable long typeId) {
        List<Question> questions = questionServise.getQuestionsByType(typeServise.getTypeById(typeId));
        List<Long> questionIds = new ArrayList<>();
        for (Question question : questions){
            questionIds.add(question.getId());
        }

        return questionIds;
    }

    @GetMapping("/{questionId}")
    public Question findTypeById(@PathVariable long questionId) {
        Question result = questionServise.getQuestionById(questionId);
        if (result.getCreator().getUsername().equals(userServise.getAuthenticationUser().getUsername()))return result;
        return null;

    }

    @DeleteMapping("/delete/{id}")
    public void deliteQuestion(@PathVariable long id){
        questionServise.deliteQuestionById(id);
    }
}
