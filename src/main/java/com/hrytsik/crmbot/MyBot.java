package com.hrytsik.crmbot;


import com.hrytsik.crmbot.commands.Command;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.inlinequerry.InlineTelegramBot;
import com.hrytsik.crmbot.service.impl.TelegramUserServiceImpl;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Slf4j
@Component
public class MyBot extends TelegramLongPollingBot {
    @Autowired
    private InlineTelegramBot inlineTelegramBot;
    @Autowired
    private TelegramUserService userService;
    @Autowired
    public List<Command> commands;
    @Autowired
    private TelegramUserServiceImpl telegramUserService;
    public HashMap<Long, TelegramUser> user = new HashMap<>();

    @Override
    public String getBotUsername() {
        return "CRM bot";
    }

    @Override
    public String getBotToken() {
        return "5220644891:AAEOsFotO-rlhBHyCBf7pZEmnseZP7x8S5U";
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasInlineQuery()) {
                inlineTelegramBot.handleIncomingInlineQuery(update.getInlineQuery(), this);
                log.info(" update query= " + update.getInlineQuery().getQuery());
            }
            if ((update.hasMessage()) && (update.getMessage().hasContact())) {
                String phoneNumber = update.getMessage().getContact().getPhoneNumber();
                registerContactNumber(update, phoneNumber);
            } else if (update.hasMessage()) {

                Long chatId = update.getMessage().getChatId();
                String firstName = update.getMessage().getChat().getFirstName();
                String lastName = update.getMessage().getChat().getLastName();
                String inputText = update.getMessage().getText();

                MDC.put("firstName", firstName);
                MDC.put("lastName", lastName);

                if (!CheckLoggin(chatId)) {
                    userService.createTelegramUser(chatId, firstName, lastName, "ROLE_USER");
                }

                user.computeIfAbsent(chatId, a -> new TelegramUser(chatId, firstName, lastName));
                Command command = null;
                for (Command choseCommand : commands) {
                    if (inputText != null) {
                        if (choseCommand.shouldRunOnText(inputText) || (choseCommand.getGlobalState() != null && user.get(chatId).globalState == choseCommand.getGlobalState())) {
                            command = choseCommand;
                            user.get(chatId).globalState = choseCommand.getGlobalState();
                        }
                    }
                }

                ExecutionContext context = new ExecutionContext();
                context.setFirstName(firstName);
                context.setLastName(lastName);
                context.setChatId(chatId);
                context.setInputText(inputText);
                context.setMyBot(this);
                context.setUpdate(update);
                context.setTelegramUserService(telegramUserService);


                if (command != null) {
                    log.info(context.printDateAndState() + " start command: " + command.getClass().getSimpleName());
                    command.doCommand(context);
                }
            }

        } catch (Exception e) {
            log.error("error", e);

            e.printStackTrace();

        } finally {
            MDC.clear();
        }
    }

    public void replyMessage(String sendTEXT, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(sendTEXT);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("error", e);
        }
    }

    public boolean CheckLoggin(Long chatId) {
        Optional<TelegramUser> dataUserByChatId = userService.findTelegramUserByChatId(chatId);
        return dataUserByChatId.isPresent();
    }

    public void registerContactNumber(Update update, String phoneNumber) {
        try {
            System.out.println(phoneNumber);

            TelegramUser user = userService.findTelegramUserByChatId(update.getMessage().getChatId()).get();
            user.setPhone(phoneNumber.replaceAll("\\+", ""));
            userService.save(user);

            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(update.getMessage().getChatId()));
            message.setText("Регистрация успешна! Спасибо!");
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboardRowList = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            KeyboardButton keyboardButton = new KeyboardButton();
            keyboardButton.setText("Главное меню");
            row.add(keyboardButton);
            keyboardRowList.add(row);
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            replyKeyboardMarkup.setKeyboard(keyboardRowList);
            message.setReplyMarkup(replyKeyboardMarkup);
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void register() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(this);
    }
}
