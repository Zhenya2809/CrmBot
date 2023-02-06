package com.hrytsik.crmbot.commands;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.entity.TelegramUser;


public interface Command {

     void doCommand(ExecutionContext context) throws Exception;
     boolean shouldRunOnText(String text);
     TelegramUser.botstate getGlobalState();
}
