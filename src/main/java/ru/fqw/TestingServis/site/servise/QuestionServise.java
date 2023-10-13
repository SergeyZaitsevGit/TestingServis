package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.QuestionRepo;
import ru.fqw.TestingServis.site.repo.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class QuestionServise {
    QuestionRepo questionRepo;
    UserRepository userRepository;

    public List<Question> getQuestionsByAuthenticationUser(){
        return questionRepo.findByCreator(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get());
    }

    public Question saveQuestion(Question question){
            User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
            question.setCreator(user);
            return questionRepo.save(question);
    }

//    public List<Question> getQuestionsByType(Type type){
//        return questionRepo.findByType(type);
//    }
}
