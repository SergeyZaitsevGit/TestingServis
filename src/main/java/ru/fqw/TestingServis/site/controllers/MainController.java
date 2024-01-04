package ru.fqw.TestingServis.site.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.fqw.TestingServis.site.models.Group;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.GroupService;
import ru.fqw.TestingServis.site.service.TypeService;


@Controller
@RequiredArgsConstructor
public class MainController {

  final TypeService typeService;
  final GroupService groupService;

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("title", "Главная страница");
    return "home";
  }

  @GetMapping("/main")
  public String mainPage(Model model) {
    model.addAttribute("name", SecurityContextHolder.getContext().getAuthentication().getName());
    return "main";
  }

  @GetMapping("/lk")
  public String lk(Model model) {
    List<Type> types = typeService.getTypeByAuthenticationUser();
    List<Group> groups = groupService.getByAuthenticationUser();
    model.addAttribute("types", types);
    model.addAttribute("type", new Type());
    model.addAttribute("groups", groups);
    model.addAttribute("group", new Group());
    return "lk";
  }

  @PostMapping("/lk/type")
  public String lkSaveType(Model model, @ModelAttribute Type type) {
    if (type != null) {
      typeService.saveType(type);
    }
    return "redirect:/lk";
  }

  @PostMapping("/lk/group")
  public String lkSaveGroup(Model model, @ModelAttribute Group group) {
    if (group != null) {
      groupService.save(group);
    }
    return "redirect:/lk";
  }
}