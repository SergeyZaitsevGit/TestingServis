package ru.fqw.TestingServis.site.service.impls;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TypeRepo;
import ru.fqw.TestingServis.site.service.TypeService;
import ru.fqw.TestingServis.site.service.UserService;


@Service
@AllArgsConstructor
public class TypeServiceImpl implements TypeService {

  TypeRepo typeRepo;
  UserService userService;

  @Override
  public List<Type> getTypeByAuthenticationUser() {
    User user = userService.getAuthenticationUser();
    return typeRepo.findByCreator(user);
  }

  @Override
  public Type saveType(Type type) {
    User user = userService.getAuthenticationUser();
    type.setCreator(user);
    return typeRepo.save(type);
  }

  @Override
  public Type getTypeById(long id) {
    return typeRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Type not found")
    );
  }
}
