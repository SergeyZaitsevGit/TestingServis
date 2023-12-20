package ru.fqw.TestingServis.site.service;

import java.util.List;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.user.BaseUser;
import ru.fqw.TestingServis.site.models.user.User;

public interface GroupService {
  Group getById(Long id);
  List<Group> getByAuthenticationUser();
  List<Group> getByUser(User User);
  Group save(Group group);


}
