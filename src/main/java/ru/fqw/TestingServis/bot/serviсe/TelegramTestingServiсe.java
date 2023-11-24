package ru.fqw.TestingServis.bot.serviсe;

import java.util.List;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.site.models.test.Test;

public interface TelegramTestingServiсe {
  void testing(Update update);
  void startTest(List<Long> tgUsersChatIds, Test test, String title);
}
