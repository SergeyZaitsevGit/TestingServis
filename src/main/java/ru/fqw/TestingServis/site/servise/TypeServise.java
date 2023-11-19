package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TypeRepo;
import java.util.List;

@Service
@AllArgsConstructor
public class TypeServise {

  TypeRepo typeRepo;
  UserServise userServise;

  public List<Type> getTypeByAuthenticationUser() {
    User user = userServise.getAuthenticationUser();
    return typeRepo.findByCreator(user);
  }

  public Type createType(Type type) {
    User user = userServise.getAuthenticationUser();
    type.setCreator(user);
    return typeRepo.save(type);
  }

  public Type getTypeById(long id) {
    return typeRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Type not found")
    );
  }
}
