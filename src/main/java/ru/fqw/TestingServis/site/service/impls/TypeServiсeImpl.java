package ru.fqw.TestingServis.site.service.impls;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.TypeRepo;
import java.util.List;
import ru.fqw.TestingServis.site.service.TypeServiсe;
import ru.fqw.TestingServis.site.service.UserServiсe;


@Service
@AllArgsConstructor
public class TypeServiсeImpl implements TypeServiсe {

  TypeRepo typeRepo;
  UserServiсe userServiсe;

  @Override
  public List<Type> getTypeByAuthenticationUser() {
    User user = userServiсe.getAuthenticationUser();
    return typeRepo.findByCreator(user);
  }

  @Override
  public Type saveType(Type type) {
    User user = userServiсe.getAuthenticationUser();
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
