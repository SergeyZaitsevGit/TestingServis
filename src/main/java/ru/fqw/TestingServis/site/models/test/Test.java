package ru.fqw.TestingServis.site.models.test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.models.question.Question;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Test extends BaseTest{
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "TestLikeQuestion",
            joinColumns = @JoinColumn(name = "test_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    @org.springframework.data.annotation.Transient
    private Set<Question> questionSet = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @org.springframework.data.annotation.Transient
    private User creator;
    @Override
    public String toString() {
        String result = "Название теста: " + super.name
                + "\nВремя на тест: " + super.timeActiv + " минут"
                + "\n Создатель теста " + super.baseUser.getUsername();
        return result;
    }

    public Test(Test test) {
        super(test);
        this.questionSet = Set.copyOf(test.questionSet);
        this.creator = test.getCreator();
    }

    @PostLoad
    private void init(){
        baseUser = creator;
        countQuestion = questionSet.size();
        for (Question q:questionSet) {
            maxBall += q.getBall();
        }
    }
}
