package ru.fqw.TestingServis.site.service.impls;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.repo.GroupRepo;
import ru.fqw.TestingServis.site.service.GroupService;
import ru.fqw.TestingServis.site.service.UserService;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

  final GroupRepo groupRepo;
  final UserService userService;

  @Override
  @Transactional
  public Group getById(Long id) {
    return groupRepo.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("Группа не найдена.")
    );
  }

  @Override
  @Transactional
  public List<Group> getByAuthenticationUser() {
    return groupRepo.findGroupByCreator(userService.getAuthenticationUser());
  }

  @Override
  @Transactional
  public List<Group> getByUser(User user) {
    return groupRepo.findGroupByCreator(user);
  }

  @Override
  @Transactional
  public Group save(Group group) {
    if (group.getId() == null) {
      group.setCreator(userService.getAuthenticationUser());
    }
    return groupRepo.save(group);
  }
}
