package ru.fqw.TestingServis.site.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.servise.UserServise;

@Controller
@AllArgsConstructor
public class RegistrationController {
    UserServise userServise;
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }
    @PostMapping ("/registration")
    public String registration(@ModelAttribute("user") User user, Model model){
        userServise.saveUser(user);
        return "redirect:/login";
    }
}
