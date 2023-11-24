package ru.fqw.TestingServis.site.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TypeRepo;
import ru.fqw.TestingServis.site.service.UserService;
import ru.fqw.TestingServis.site.service.impls.TypeServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class TypeServiceImplTest {

  @Mock
  private TypeRepo typeRepo;

  @Mock
  private UserService userService;

  @InjectMocks
  private TypeServiceImpl typeService;

  @Test
  public void getTypeByAuthenticationUser_ShouldReturnListOfTypes() {
    // Arrange
    User user = new User();
    user.setId(1L);
    Type type1 = new Type();
    type1.setId(1L);
    type1.setCreator(user);
    Type type2 = new Type();
    type2.setId(2L);
    type2.setCreator(user);
    List<Type> types = Arrays.asList(type1, type2);

    when(userService.getAuthenticationUser()).thenReturn(user);
    when(typeRepo.findByCreator(user)).thenReturn(types);

    List<Type> result = typeService.getTypeByAuthenticationUser();

    assertEquals(types, result);
  }

  @Test
  public void saveType_ShouldReturnSavedType() {
    User user = new User();
    user.setId(1L);
    Type type = new Type();
    type.setId(1L);
    type.setName("Test");
    type.setName("Test description");

    when(userService.getAuthenticationUser()).thenReturn(user);
    when(typeRepo.save(type)).thenReturn(type);

    Type result = typeService.saveType(type);

    assertEquals(type, result);
  }

  @Test
  public void getTypeById_ShouldReturnTypeById() {
    Type type = new Type();
    type.setId(1L);
    Optional<Type> optionalType = Optional.of(type);

    when(typeRepo.findById(1L)).thenReturn(optionalType);

    Type result = typeService.getTypeById(1L);

    assertEquals(type, result);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void getTypeById_ShouldThrowResourceNotFoundException() {
    Optional<Type> optionalType = Optional.empty();

    when(typeRepo.findById(1L)).thenReturn(optionalType);

    // Act
    typeService.getTypeById(1L);

    // Assert (expected exception)
  }

}
