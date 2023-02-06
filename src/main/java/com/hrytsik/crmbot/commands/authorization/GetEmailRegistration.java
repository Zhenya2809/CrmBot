package com.hrytsik.crmbot.commands.authorization;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.entity.TelegramUser;

public class GetEmailRegistration implements Registration {
    @Override
    public void execute(ExecutionContext executionContext) {
        String inputMessage = executionContext.getUpdate().getMessage().getText();

        TelegramUser user = executionContext.getAuthorizationUser();
        if (inputMessage.contains("@")) {
            user.setEmail(inputMessage);
            executionContext.getTelegramUserService().save(user);
            executionContext.getContactKeyboard();
            executionContext.setLocalState("authorized");
        } else {
            executionContext.replyMessage("ввід помилковий, спробуйте ще раз");
        }

    }
}
