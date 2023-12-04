package ru.fqw.TestingServis.bot.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.data.domain.Page;
import ru.fqw.TestingServis.bot.models.AnalysisQuestion;
import ru.fqw.TestingServis.bot.models.ResultTest;

public interface ResultAnalysisServise {
    List<AnalysisQuestion> getQusetionsAnalysesByTitleTesting(String titleTesting);
    Map<String, List<AnalysisQuestion>> getAnalysisQuestionMapByPage(
        Page<Entry<String, List<ResultTest>>> resultsGroupedByTestName);

}
