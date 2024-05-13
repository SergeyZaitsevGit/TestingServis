package ru.fqw.TestingServis.bot.models;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.fqw.TestingServis.site.models.user.BaseUser;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AnalysisByTesting {
    @Id
    private ObjectId id;
    private String testing;
    private BaseUser baseUser;
    private List<AnalysisQuestion> analysisQuestionList;
}
