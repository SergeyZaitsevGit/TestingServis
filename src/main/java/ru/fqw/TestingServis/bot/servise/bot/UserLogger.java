package ru.fqw.TestingServis.bot.servise.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLogger {

  private Logger logger;

  public UserLogger(String username, String userid) {
    logger = LoggerFactory.getLogger("ru.fqw.TestingServis." + username + "_" + userid);
  }

  public void logMessage(String message) {
    logger.info(message);
  }
}