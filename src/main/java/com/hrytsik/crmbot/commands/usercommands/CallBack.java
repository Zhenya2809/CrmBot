package com.hrytsik.crmbot.commands.usercommands;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.repository.TelegramUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CallBack implements Command {
    private final TelegramUserRepository telegramUserRepository;

    public CallBack(TelegramUserRepository telegramUserRepository) {
        this.telegramUserRepository = telegramUserRepository;
    }

    @Override
    public void doCommand(ExecutionContext executionContext) {

        executionContext.replyMessage("В скором времени наш менеджер свяжеться с Вами");
        Optional<TelegramUser> dataUserByChatId = executionContext.getTelegramUserService().findTelegramUserByChatId(executionContext.getChatId());
        if (dataUserByChatId.isPresent()) {
            String phone = dataUserByChatId.get().getPhone();

            List<TelegramUser> adminList = telegramUserRepository.findAllByRole("ADMIN");

            for (TelegramUser telegramUser : adminList) {
                executionContext.sendMessageToUserWithId(
                        "Перезвоните мне: " +
                                phone, telegramUser.getChatId() +
                                executionContext.getUser().getFirstName() +
                                " " + executionContext.getUser().getLastName());
                log.info("send message to admin ----Phone: "+phone+" ----FirstName: "+executionContext.getUser().getFirstName()
                        + " ----LastName: "+executionContext.getUser().getLastName());
            }

        }
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);
    }

    @Override
    public boolean shouldRunOnText(String text) {
        return text.equals("Заказать обратный звонок");
    }

    @Override
    public TelegramUser.botstate getGlobalState() {
        return TelegramUser.botstate.CALL_BACK;
    }
}
