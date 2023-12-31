package ru.fqw.TestingServis.site.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.fqw.TestingServis.site.models.user.User;
import ru.fqw.TestingServis.site.service.UserService;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

  final UserService userService;

  @GetMapping("/registration")
  public String registration(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @PostMapping("/registration")
  public String registration(@ModelAttribute("user") User user, Model model) {
    userService.saveUser(user);
    return "redirect:/login";
  }
}
