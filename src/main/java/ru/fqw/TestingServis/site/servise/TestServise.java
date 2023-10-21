package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.Test;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.TestRepo;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class TestServise {
    TestRepo testRepo;
    UserServise userServise;

    public Test saveTest(Test test) {
        User user = userServise.getAuthenticationUser();
        test.setCreator(user);;
        test.setDateCreated(new Timestamp(System.currentTimeMillis()));
        return testRepo.save(test);
    }

    public Iterable<Test>getTestsByAuthenticationUser(){
        User user = userServise.getAuthenticationUser();
        return testRepo.findByCreator(user);
    }

    public Test getTestById(Long testId){return testRepo.findById(testId).orElseThrow(
            () -> new ResourceNotFoundException("Test not found")
    );}

}
