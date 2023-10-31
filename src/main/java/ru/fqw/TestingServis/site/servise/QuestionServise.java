package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.test.Test;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.QuestionRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionServise {
    QuestionRepo questionRepo;
    UserServise userServise;

    public List<Question> getQuestionsByAuthenticationUser() {
        return questionRepo.findByCreator(userServise.getAuthenticationUser());
    }

    public Question saveQuestion(Question question) {
        User user =  userServise.getAuthenticationUser();
        question.setCreator(user);
        question.getBaseAnswerList().addAll(question.getAnswerList());
        return questionRepo.save(question);
    }

    public List<Question> getQuestionsByType(Type type) {
        return questionRepo.findByType(type);
    }

    public List<Question> getQuestionsByTest(Test test) {
        return questionRepo.getQuestionByTestSet(test);
    }

    public Question getQuestionById(long questionId) {
        return questionRepo.findById(questionId).orElseThrow(() -> new ResourceNotFoundException("Question not found"));
    }

    public void deliteQuestionById(long questionId){
         questionRepo.deleteById(questionId);
    }

}
