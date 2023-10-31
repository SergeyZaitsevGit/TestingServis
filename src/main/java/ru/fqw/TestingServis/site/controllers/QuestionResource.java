package ru.fqw.TestingServis.site.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;
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
    @PreAuthorize("@customSecurityExpression.canAccessType(#typeId)")
    public Question createQuestion(@RequestBody Question question, @RequestParam("typeId") Long typeId) { //Создание нового вопроса
        if (typeId != -1) question.setType(typeServise.getTypeById(typeId));
        List<Answer> answerList = new ArrayList<>(question.getAnswerList());
        question.getAnswerList().clear();
        questionServise.saveQuestion(question);
        for (Answer a : answerList) {
            answerServise.saveAnswer(a, question);
        }
        return question;
    }

    @GetMapping("type/{typeId}")
    public List<Long> findQuestionByTypeId(@PathVariable long typeId) { //Получение списка id вопросов, соответствующих выбранному типу
        List<Question> questions = questionServise.getQuestionsByType(typeServise.getTypeById(typeId));
        List<Long> questionIds = new ArrayList<>();
        for (Question question : questions) {
            questionIds.add(question.getId());
        }

        return questionIds;
    }

    @GetMapping("/{questionId}")
    @PreAuthorize("@customSecurityExpression.canAccessQuestion(#questionId)")
    public Question findTypeById(@PathVariable long questionId) {
        return questionServise.getQuestionById(questionId);
    }

    @PreAuthorize("@customSecurityExpression.canAccessQuestion(#id)")
    @DeleteMapping("/delete/{id}")
    public void deliteQuestion(@PathVariable long id) {
        questionServise.deliteQuestionById(id);
    }
}
