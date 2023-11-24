package ru.fqw.TestingServis.site.service;

import java.util.List;
import ru.fqw.TestingServis.site.models.Type;

public interface TypeService {

  List<Type> getTypeByAuthenticationUser();

  Type saveType(Type type);

  Type getTypeById(long id);

}
