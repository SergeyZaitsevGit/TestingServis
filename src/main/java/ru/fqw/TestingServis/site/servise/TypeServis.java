//package ru.fqw.TestingServis.site.servise;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.fqw.TestingServis.site.models.Type;
import ru.fqw.TestingServis.site.models.User;
import ru.fqw.TestingServis.site.repo.UserRepository;

import java.util.List;

//@Service
//@AllArgsConstructor
//public class TypeServis {
//    TypeRepo typeRepo;
//    UserRepository userRepository;
//
//    public Type createType(Type type){return typeRepo.save(type);}
////    public List<Type> getTypeByAuthenticationUser(){
////        User user = userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).get();
////        return typeRepo.findByUser(user);
////    }
//}
