package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.TypeRepo;
import ru.fqw.TestingServis.site.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TypeServise {
    TypeRepo typeRepo;
    UserRepository userRepository;

    public List<Type> getTypeByAuthenticationUser(){
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        return typeRepo.findByCreator(user);
    }

    public Optional<Type> getTypeByName(String name){
        return typeRepo.findByName(name);
    }

    public Type createType(Type type){
        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        type.setCreator(user);
        return typeRepo.save(type);
    }

    public Type getTypeById(long id){
        return typeRepo.findById(id).get();
    }
}
