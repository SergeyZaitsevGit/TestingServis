package ru.fqw.TestingServis.site.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.fqw.TestingServis.site.models.User;
//import ru.fqw.TestingServis.site.repo.UserRepository;

@Controller
public class MainController {
    //@Autowired
    //private UserRepository userRepository;

    @GetMapping("/")
    public String greeting(Model model) {
        model.addAttribute("title", "Главная страница");
            return "home";
    }
    @GetMapping("/main")
    public String greeting3(Model model) {
        model.addAttribute("name", SecurityContextHolder.getContext().getAuthentication().getName());
        return "main";
    }


}