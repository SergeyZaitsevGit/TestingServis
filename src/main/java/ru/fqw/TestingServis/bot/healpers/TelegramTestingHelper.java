package ru.fqw.TestingServis.bot.healpers;

import org.springframework.stereotype.Component;
import ru.fqw.TestingServis.site.models.answer.BaseAnswer;
import ru.fqw.TestingServis.site.models.question.BaseQuestion;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TelegramTestingHelper {
    public int getBallByAnswers(BaseQuestion question, List<BaseAnswer> selectedAnswers){
       switch (question.getTypeAnswerOptions()){
           case ONE_ANSWER -> {
               if (selectedAnswers.get(0).isCorrected()) return  question.getBall();
           }
           case MANY_ANSWER -> {
               final int ballBehindAnswer = question.getBall()/question.getBaseAnswerList().size();
               int resultBall = 0;
               for (BaseAnswer answer:selectedAnswers) {
                   if (answer.isCorrected()) resultBall += ballBehindAnswer;
                   else resultBall -= ballBehindAnswer;
               }
               if (resultBall<0) resultBall = 0;
               return resultBall;
           }
       }
        return 0;
    }

    public List<Integer> getIntListByAnswer(String textAnswer) {
        List<Integer> convertedtextAnswerList = Stream.of(textAnswer.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        return convertedtextAnswerList;
    }

}