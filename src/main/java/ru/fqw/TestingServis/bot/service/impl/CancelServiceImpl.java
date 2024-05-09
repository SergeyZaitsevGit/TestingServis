package ru.fqw.TestingServis.bot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fqw.TestingServis.bot.models.Cancellable;
import ru.fqw.TestingServis.bot.models.enums.Command;
import ru.fqw.TestingServis.bot.service.CancelService;

import java.util.Map;

@Service
public class CancelServiceImpl implements CancelService {
    @Autowired
    private ApplicationContext context;

    @Override
    public void cancel(Update update) {
        Message  message = update.getMessage();
        if (message.getText().equals(Command.CANCEL.getCommandText())){
            Map<String, Cancellable> beans = context.getBeansOfType(Cancellable.class);

            for (Cancellable cancellable : beans.values()) {
                cancellable.cancel(message.getChatId());
            }
        }
    }
}
