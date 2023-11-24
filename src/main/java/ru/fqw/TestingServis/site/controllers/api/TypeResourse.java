package ru.fqw.TestingServis.site.controllers.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.TypeService;

@RestController
@RequestMapping("/api/v1/type")
@AllArgsConstructor
public class TypeResourse {
    TypeService typeService;
    @GetMapping("/{typeId}")
    public Type findTypeById(@PathVariable long typeId) {
        return typeService.getTypeById(typeId);
    }

}
