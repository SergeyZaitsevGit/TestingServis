package ru.fqw.TestingServis.site.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fqw.TestingServis.bot.servise.TelegramBot;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.servise.TypeServise;

import java.util.List;
//import ru.fqw.TestingServis.site.repo.UserRepository;

@Controller
@AllArgsConstructor
public class MainController {
   TypeServise typeServise;

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

    @GetMapping("/lk")
    public String lk(Model model) {
        List<Type> types = typeServise.getTypeByAuthenticationUser();
        model.addAttribute("types", types);
        model.addAttribute("type", new Type());
        return "lk";
    }

    @PostMapping("/lk")
    public String lk(Model model, @ModelAttribute Type type){
        if (type != null)typeServise.createType(type);

        return "redirect:lk";
    }
}