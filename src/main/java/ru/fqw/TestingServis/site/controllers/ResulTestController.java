package ru.fqw.TestingServis.site.controllers;

import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.fqw.TestingServis.bot.models.ResultTest;
import ru.fqw.TestingServis.bot.servise.ResultTestServise;
import ru.fqw.TestingServis.site.servise.UserServise;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ResulTestController {
    final UserServise userServise;
    final ResultTestServise resultTestServise;

    @GetMapping("/result")
    public String resultTest(Model model,@PageableDefault(size = 20) Pageable pageable, @RequestParam("keyword") Optional<String> keyword ){
        if (keyword.isPresent()){
        Page<Map.Entry<String, List<ResultTest>>> resultsGroupedByTestName = resultTestServise.getTestingResultsGroupedByTestName(pageable,  keyword.get());
        model.addAttribute("resultsGroupedByTestName",resultsGroupedByTestName);
            model.addAttribute("keyword",keyword.get());
        }
        else {
            Page<Map.Entry<String, List<ResultTest>>> resultsGroupedByTestName = resultTestServise.getTestingResultsGroupedByTestName(pageable,  null);
            model.addAttribute("resultsGroupedByTestName",resultsGroupedByTestName);
        }
        return "result";
    }

    @GetMapping("/result/{resultId}")
    public String resultTestById(Model model, @PathVariable String resultId){
        ResultTest resultTest = resultTestServise.getResultTestById(resultId);
        model.addAttribute("resultTest",resultTest);
        return "resultCurrent";
    }
    @PostMapping("/result/{resultId}")
    public String resultTestById(Model model, @PathVariable String resultId,
                                 @RequestParam("ballFree") int ballFree,
                                 @RequestParam("index") int index
    ){
        ResultTest resultTest = resultTestServise.getResultTestById(resultId);
        int oldBall = resultTest.getAnswerFromTelegramUserList().get(index).getBallBehindQuestion();
        resultTest.getAnswerFromTelegramUserList().get(index).setBallBehindQuestion(ballFree);
        resultTest.setBall(resultTest.getBall() + (ballFree - oldBall));
        resultTestServise.save(resultTest);
        return "redirect:/result/{resultId}";
    }

}
