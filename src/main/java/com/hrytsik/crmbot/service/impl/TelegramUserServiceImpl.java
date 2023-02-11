package com.hrytsik.crmbot.service.impl;

import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.dto.TelegramUserDto;
import com.hrytsik.crmbot.repository.TelegramUserRepository;
import com.hrytsik.crmbot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TelegramUserServiceImpl implements TelegramUserService {

    private final TelegramUserRepository telegramUsersRepository;


    public TelegramUserServiceImpl(TelegramUserRepository telegramUsersRepository) {
        this.telegramUsersRepository = telegramUsersRepository;
    }

    @Override
    public Optional<TelegramUser> findTelegramUserByChatId(Long chatId) {
        return Optional.ofNullable(telegramUsersRepository.findDataUserByChatId(chatId));
    }

    @Override
    public void createTelegramUser(Long chatId, String firstName, String lastName, String role) {
        TelegramUser user = new TelegramUser();
        user.setChatId(chatId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);
        user.setGlobalState(TelegramUser.botstate.START);
        user.setLocaleState("Main_menu");
        telegramUsersRepository.save(user);
        log.info("user created : chatId=" + chatId + " firstName=" + firstName + " lastName=" + lastName);
    }


    @Override
    public void save(TelegramUser user) {
        telegramUsersRepository.save(user);
    }

    @Override
    public List<TelegramUser> getAllUsersForAdmin(String lastName) {
        List<TelegramUser> chatIdList = telegramUsersRepository.searchUser(lastName).stream().toList();

        for (TelegramUser telegramUser : chatIdList) {
            log.info(telegramUser.getPhone());
        }

        return chatIdList;
    }

    @Override
    public List<Long> findAll() {
        List<Long> chatIdList = new ArrayList<>();
        telegramUsersRepository.findAll().forEach(e -> {
            Long chatId = e.getChatId();
            chatIdList.add(chatId);
        });
        return chatIdList;
    }

    @Override
    public List<TelegramUser> findAllByRole(String role) {
        return telegramUsersRepository.findAllByRole(role);
    }
}
