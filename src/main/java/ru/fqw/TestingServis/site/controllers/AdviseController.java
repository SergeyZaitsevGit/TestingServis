package ru.fqw.TestingServis.site.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.fqw.TestingServis.site.models.exception.ExceptionBody;
import ru.fqw.TestingServis.site.models.exception.ResourceNotFoundException;

@ControllerAdvice
public class AdviseController {

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleResourceNotFound(final ResourceNotFoundException e, Model model) {
    model.addAttribute("error", new ExceptionBody(e.getMessage()));
    return "error/errorNotFound";
  }

  @ExceptionHandler(IllegalStateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ExceptionBody handleIllegalState(final IllegalStateException e) {
    return new ExceptionBody(e.getMessage());
  }

  @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public String handleAccessDenied(Model model) {
    model.addAttribute("error", new ExceptionBody("Доступ запрещен"));
    return "error/errorAccessDenied";
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ExceptionBody handleException(final Exception e) {
    e.printStackTrace();
    return new ExceptionBody("Неизвестная ошибка");
  }

}
