package com.hrytsik.crmbot.service;

import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.dto.TelegramUserDto;

import java.util.List;
import java.util.Optional;

public interface TelegramUserService {
    Optional<TelegramUser> findTelegramUserByChatId(Long chatId);

    void createTelegramUser(Long chatId, String firstName, String lastName, String role);

    void save(TelegramUser user);

    List<TelegramUser> getAllUsersForAdmin(String lastName);

    List<Long> findAll();
    List<TelegramUser> findAllByRole(String role);
}
