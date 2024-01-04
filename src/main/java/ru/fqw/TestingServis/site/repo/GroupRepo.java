package ru.fqw.TestingServis.site.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.user.User;

public interface GroupRepo extends CrudRepository<Group, Long> {

  List<Group> findGroupByCreator(User user);
}
