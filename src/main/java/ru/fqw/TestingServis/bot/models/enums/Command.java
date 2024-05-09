package ru.fqw.TestingServis.bot.models.enums;

import lombok.Getter;

public enum Command {
    REGISTRATION("/reg"),
    ADD_INVITED("/addInvited"),
    REMOVE_INVITED("/removeInvited"),
    CANCEL("/cancel"),
    GO("/go");

    @Getter
    private String commandText;

    Command(String commandText) {
        this.commandText = commandText;
    }
}
