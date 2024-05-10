package ru.fqw.TestingServis.site.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TestRepo;
import ru.fqw.TestingServis.site.service.UserService;
import ru.fqw.TestingServis.site.service.impls.TestServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TestServiseImplTest {

  @Mock
  private TestRepo testRepo;

  @Mock
  private UserService userService;

  @InjectMocks
  private TestServiceImpl testServise;

//  @Test
//  public void saveTest_WhenTestNotExists_SaveTest() {
//    User user = new User();
//    ru.fqw.TestingServis.site.models.test.Test test = new ru.fqw.TestingServis.site.models.test.Test();
//    test.setId(0L);
//
//    when(testRepo.existsById(0L)).thenReturn(false);
//    when(testRepo.save(test)).thenReturn(test);
//    when(userService.getAuthenticationUser()).thenReturn(user);
//
//    ru.fqw.TestingServis.site.models.test.Test res = testServise.saveTest(test);
//
//    assertEquals(res, test);
//    assertEquals(res.getCreator(), user);
//    verify(testRepo, times(1)).save(test);
//    verify(userService, times(1)).getAuthenticationUser();
//  }

  @Test
  public void testSaveTest_WhenTestExists_UpdateTest() {
    User testOwnerUser = new User();
    testOwnerUser.setUsername("testuUer");
    ru.fqw.TestingServis.site.models.test.Test test = new ru.fqw.TestingServis.site.models.test.Test();
    test.setId(0L);

    when(testRepo.existsById(0L)).thenReturn(true);
    when(testRepo.save(test)).thenReturn(test);

    ru.fqw.TestingServis.site.models.test.Test res = testServise.saveTest(test);

    assertEquals(res, test);
    verify(testRepo, times(1)).save(test);
    verify(userService, times(0)).getAuthenticationUser();
  }

  @Test
  public void testGetTestsByAuthenticationUser_WhenUserAuthentication_ReturnTestPage() {
    User testOwnerUser = new User();
    testOwnerUser.setUsername("testUser");

    ru.fqw.TestingServis.site.models.test.Test test1 = new ru.fqw.TestingServis.site.models.test.Test();
    test1.setCreator(testOwnerUser);
    ru.fqw.TestingServis.site.models.test.Test test2 = new ru.fqw.TestingServis.site.models.test.Test();
    test2.setCreator(testOwnerUser);

    List<ru.fqw.TestingServis.site.models.test.Test> tests = Stream.of(test1, test2).toList();
    Pageable pageable = PageRequest.of(0, 10);

    when(userService.getAuthenticationUser()).thenReturn(testOwnerUser);
    when(testRepo.findByCreator(pageable, testOwnerUser)).thenReturn(
        new PageImpl<>(tests, pageable, tests.size()));

    Page<ru.fqw.TestingServis.site.models.test.Test> result = testServise.getTestsByAuthenticationUser(
        pageable);

    assertEquals(result.getContent().get(0), test1);
    assertEquals(result.getContent().get(0), test2);
    assertEquals(result.getContent().size(), tests.size());
    verify(testRepo, times(1)).findByCreator(pageable, testOwnerUser);

  }

  @Test
  public void testGetTestById_WhenTestExsist_ReturnTest() {
    Long testId = 1L;
    ru.fqw.TestingServis.site.models.test.Test expectedTest = new ru.fqw.TestingServis.site.models.test.Test();
    when(testRepo.findById(testId)).thenReturn(Optional.of(expectedTest));

    ru.fqw.TestingServis.site.models.test.Test result = testServise.getTestById(testId);

    assertEquals(expectedTest, result);
  }

  @Test
  public void testGetTestById_WhenTestNotExsist_ResourceNotFoundException() {
    Long testId = 1L;
    ru.fqw.TestingServis.site.models.test.Test expectedTest = new ru.fqw.TestingServis.site.models.test.Test();
    when(testRepo.findById(testId)).thenReturn(Optional.empty());

    assertThrowsExactly(ResourceNotFoundException.class, () -> testServise.getTestById(testId));
  }

  @Test
  public void testGetTestsByAuthenticationUserAndNameContaining() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testUser");

    ru.fqw.TestingServis.site.models.test.Test test1 = new ru.fqw.TestingServis.site.models.test.Test();
    test1.setCreator(user);
    test1.setName("Test1");
    ru.fqw.TestingServis.site.models.test.Test test2 = new ru.fqw.TestingServis.site.models.test.Test();
    test2.setCreator(user);
    test2.setName("Test2");

    Pageable pageable = PageRequest.of(0, 10);

    List<ru.fqw.TestingServis.site.models.test.Test> testList = Stream.of(test1, test2).toList();
    Page<ru.fqw.TestingServis.site.models.test.Test> testPage = new PageImpl<>(testList);

    when(userService.getAuthenticationUser()).thenReturn(user);
    when(testRepo.findByCreatorAndNameContaining(pageable, user, "Test")).thenReturn(testPage);

    Page<ru.fqw.TestingServis.site.models.test.Test> result = testServise.getTestsByAuthenticationUserAndNameContaining(
        pageable, "Test");
    assertEquals(2, result.getTotalElements());
  }

}
