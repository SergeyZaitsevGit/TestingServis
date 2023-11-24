package ru.fqw.TestingServis.site.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.TypeService;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

  final TypeService typeService;

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
    model.addAttribute("types", types);
    model.addAttribute("type", new Type());
    return "lk";
  }

  @PostMapping("/lk")
  public String lk(Model model, @ModelAttribute Type type) {
    if (type != null) {
      typeService.saveType(type);
    }

    return "redirect:lk";
  }
}