package ru.fqw.TestingServis.site.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ru.fqw.TestingServis.site.models.answer.Answer;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.question.Question;
import ru.fqw.TestingServis.site.repo.AnswerRepo;
import ru.fqw.TestingServis.site.service.impls.AnswerServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnswerServiceImplTest {

  @Mock
  private AnswerRepo answerRepo;

  @InjectMocks
  private AnswerServiceImpl answerService;

  @Test
  public void saveAnswerTest() {
    Question question = new Question();
    Answer answer = new Answer();
    when(answerRepo.save(answer)).thenReturn(answer);
    assertEquals(answer, answerService.saveAnswer(answer, question));
    verify(answerRepo, times(1)).save(answer);
  }

  @Test
  public void delAnswerByIdTest() {
    long answerId = 1L;
    answerService.delAnswerById(answerId);
    verify(answerRepo, times(1)).deleteById(answerId);
  }

  @Test
  public void getAnswerByIdTest() {
    long answerId = 1L;
    Answer answer = new Answer();
    when(answerRepo.findById(answerId)).thenReturn(Optional.of(answer));
    assertEquals(answer, answerService.getAnswerById(answerId));
    verify(answerRepo, times(1)).findById(answerId);
  }

  @Test
  public void getAnswerByIdNotFoundTest() {
    long answerId = 1L;
    when(answerRepo.findById(answerId)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> answerService.getAnswerById(answerId));
    verify(answerRepo, times(1)).findById(answerId);
  }

  @Test
  public void getAnswersByQuestionTest() {
    Question question = new Question();
    List<Answer> answers = new ArrayList<>();
    answers.add(new Answer());
    answers.add(new Answer());
    when(answerRepo.findByQuestion(question)).thenReturn(answers);
    assertEquals(answers, answerService.getAnswersByQuestion(question));
    verify(answerRepo, times(1)).findByQuestion(question);
  }
}