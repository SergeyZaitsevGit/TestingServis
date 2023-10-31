package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.repo.AnswerRepo;

import java.util.List;

@Service
@AllArgsConstructor
public class AnswerServise {
    AnswerRepo answerRepo;
    public Answer saveAnswer(Answer answer, Question question){
        answer.setQuestion(question);
        return answerRepo.save(answer);
    }

    public List<Answer> getAnswersByQuestion(Question question){
        return answerRepo.findByQuestion(question);
    }
}
