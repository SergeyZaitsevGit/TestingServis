package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.Question;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.TestRepo;
import ru.fqw.TestingServis.site.repo.UserRepository;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class TestServise {
    TestRepo testRepo;
    UserRepository userRepository;

    public Test saveTest(Test test) {
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        test.setCreator(user);;
        test.setDateCreated(new Timestamp(System.currentTimeMillis()));
        return testRepo.save(test);
    }

    public Iterable<Test>getTestsByAuthenticationUser(){
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        return testRepo.findByCreator(user);
    }

    public Test getTestById(Long testId){return testRepo.findById(testId).get();}

}
