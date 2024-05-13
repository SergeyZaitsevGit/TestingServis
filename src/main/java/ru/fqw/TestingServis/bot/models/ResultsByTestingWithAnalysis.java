package ru.fqw.TestingServis.bot.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResultsByTestingWithAnalysis {
    private List<ResultTest> resultTests;
    private List<AnalysisQuestion> analysisQuestionList;
}
