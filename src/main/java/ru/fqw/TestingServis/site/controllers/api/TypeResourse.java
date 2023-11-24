package ru.fqw.TestingServis.site.controllers.api;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.service.TypeServiсe;

@RestController
@RequestMapping("/api/v1/type")
@AllArgsConstructor
public class TypeResourse {
    TypeServiсe typeServiсe;
    @GetMapping("/{typeId}")
    public Type findTypeById(@PathVariable long typeId) {
        return typeServiсe.getTypeById(typeId);
    }

}
