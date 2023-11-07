package ru.fqw.TestingServis.bot.servise;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.repo.ResultsTestRepo;
import ru.fqw.TestingServis.site.models.exception.ObjectAlreadyExistsExeption;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.test.BaseTest;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.servise.UserServise;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
            () -> new ResourceNotFoundException("Результат тестирования не найден")
        );
    }

    public Page<ResultTest> getResultTestByAuthenticationUser(Pageable pageable){
        BaseUser user = userServise.getAuthenticationUser();
        return resultsTestRepo.findResultTestByTestBaseUser(pageable,user);
    }
    public Map<String, List<ResultTest>> getResultTestByAuthenticationUserSortetByTesting(Pageable pageable){
        Map<String, List<ResultTest>> res = getResultTestByAuthenticationUser(pageable).stream()
                .collect(Collectors.groupingBy(ResultTest::getTitle));
               return res;
    }

    public boolean existByTitle(String title){
        return resultsTestRepo.existsByTitle(title);
    }

}
