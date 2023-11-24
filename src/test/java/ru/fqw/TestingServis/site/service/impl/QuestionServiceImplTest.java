package ru.fqw.TestingServis.site.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.QuestionRepo;
import ru.fqw.TestingServis.site.service.UserService;
import ru.fqw.TestingServis.site.service.impls.QuestionServiceImpl;


@ExtendWith(MockitoExtension.class)
public class QuestionServiceImplTest {
  @Mock
  private QuestionRepo questionRepo;

  @Mock
  private UserService userService;

  @InjectMocks
  private QuestionServiceImpl questionServise;


  @Test
  public void tesrGetQuestionByAuthenticationUser_WhenUserAuthentication_ReturnQuestionList(){
    User user = new User();
    user.setUsername("testUser");

    Question question1 = new Question();
    question1.setCreator(user);
    Question question2 = new Question();
    question2.setCreator(user);

    List<Question> questions = Stream.of(question1,question2).toList();

    when(userService.getAuthenticationUser()).thenReturn(user);
    when(questionRepo.findByCreator(user)).thenReturn(questions);

   List<Question> result = questionServise.getQuestionsByAuthenticationUser();

    assertEquals(result.get(0), question1);
    assertEquals(result.get(1), question2);
    assertEquals(result.size(), questions.size());
    verify(questionRepo, times(1)).findByCreator(user);

  }

  @Test
  public void testSaveQuestion_WhenQuestionNotExists_CreateQuestion(){
    User user = new User();
    user.setUsername("testUser");
    Question question = new Question();
    question.setId(0L);

    when(questionRepo.existsById(0L)).thenReturn(false);
    when(questionRepo.save(question)).thenReturn(question);
    when(userService.getAuthenticationUser()).thenReturn(user);

    Question res = questionServise.saveQuestion(question);

    assertEquals(res, question);
    assertEquals(res.getCreator(), user);
    verify(questionRepo, times(1)).save(question);
    verify(userService, times(1)).getAuthenticationUser();
  }

  @Test
  public void testSaveQuestion_WhenQuestionExists_UpdateQuestion(){
    User user = new User();
    user.setUsername("testUser");
    Question question = new Question();
    question.setId(0L);

    when(questionRepo.existsById(0L)).thenReturn(true);
    when(questionRepo.save(question)).thenReturn(question);

    Question res = questionServise.saveQuestion(question);

    assertEquals(res, question);
    verify(questionRepo, times(1)).save(question);
    verify(userService, times(0)).getAuthenticationUser();
  }

  @Test
  public void  testGetQuestionsByTest_WhenQuestionByTypeExists_ReturnQuestionList() {
    Type type = new Type();
    Question question1 = new Question();
    Question question2 = new Question();
    List<Question> expectedQuestions = Stream.of(question1,question2).toList();

   when(questionRepo.findByType(type)).thenReturn(expectedQuestions);
   List<Question> res = questionServise.getQuestionsByType(type);

    assertEquals(expectedQuestions, res);
  }

  @Test
  public void testGetQuestionsByTest() {
    Question question1 = new Question();
    question1.setText("testq");
    Question question2 = new Question();
    question2.setText("testq2");
    Set<Question> questionSet = new LinkedHashSet<>();
    questionSet.add(question1);
    questionSet.add(question2);
    ru.fqw.TestingServis.site.models.test.Test test = new ru.fqw.TestingServis.site.models.test.Test();
    test.setQuestionSet(questionSet);

    List<Question> expectedQuestions = new ArrayList<>();
    expectedQuestions.add(question1);
    expectedQuestions.add(question2);

    List<Question> actualQuestions = questionServise.getQuestionsByTest(test);

    assertEquals(expectedQuestions, actualQuestions);
  }

  @Test
  public void testGetTestById_WhenTestExsist_ReturnTest() {
    Long id = 1L;
    Question question = new Question();
    when(questionRepo.findById(id)).thenReturn(Optional.of(question));

    Question result = questionServise.getQuestionById(id);

    assertEquals(question, result);
  }

  @Test
  public void testGetTestById_WhenTestNotExsist_ResourceNotFoundException() {
    Long id = 1L;
    Question question = new Question();
    when(questionRepo.findById(id)).thenReturn(Optional.empty());

    assertThrowsExactly(ResourceNotFoundException.class, () -> questionServise.getQuestionById(id));
  }

  @Test
  public void testDeliteQuestionById() {
    Question question = new Question();
    question.setId(1L);
    question.setText("Test question");
    questionRepo.save(question);

    questionServise.deliteQuestionById(1L);

    verify(questionRepo, times(1)).deleteById(1L);
  }
}

