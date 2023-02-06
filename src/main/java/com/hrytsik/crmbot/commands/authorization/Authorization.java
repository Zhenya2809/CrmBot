package com.hrytsik.crmbot.commands.authorization;



import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class Authorization implements Command {
    @Override
    public void doCommand(ExecutionContext executionContext) {

        try {
            String localState = executionContext.getLocalState();
            TelegramUser authorizationUser = executionContext.getAuthorizationUser();

            if ((authorizationUser.getEmail() != null) && (authorizationUser.getPhone() != null) && (localState == null)) {
                executionContext.setLocalState("authorized");
            }
            if ((localState == null) && (authorizationUser.getEmail() == null) && (authorizationUser.getPhone() == null)) {
                executionContext.setLocalState("start_registration");
            }
            String step = executionContext.getLocalState();
            Map<String, Registration> authorizationMap = new HashMap<>();
            authorizationMap.put("start_registration", new StartRegistration());
            authorizationMap.put("get_email_and_phone_registration", new GetEmailRegistration());
            authorizationMap.put("authorized", new Authorized());
            Registration registration = authorizationMap.get(step);

            if (registration == null) {
                executionContext.getContactKeyboard();

            } else {
                registration.execute(executionContext);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Начнем \uD83D\uDE09") || (text.equals("Главное меню"));
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.START_BOT_CHATTING;
    }
}