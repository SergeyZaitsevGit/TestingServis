package ru.fqw.TestingServis.site.models.exception;

public class ObjectAlreadyExistsExeption extends RuntimeException {

  public ObjectAlreadyExistsExeption(final String message) {
    super(message);
  }
}
