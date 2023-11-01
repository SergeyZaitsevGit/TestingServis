package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.repo.ResultsTestRepo;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.servise.UserServise;
import java.util.List;

@Service
@AllArgsConstructor
public class ResultTestServise {
    ResultsTestRepo resultsTestRepo;
    UserServise userServise;
    public void save(ResultTest resultTest){
        resultsTestRepo.save(resultTest);
    }
    public ResultTest getResultTestById(String id){
        return resultsTestRepo.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("ResultTest not found")
        );
    }

    public List<ResultTest> getResultTestByAuthenticationUser(){
        BaseUser user = userServise.getAuthenticationUser();
        return resultsTestRepo.findResultTestByTestBaseUser(user);
    }
}
