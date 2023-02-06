package com.hrytsik.crmbot.entity.dto;

import com.hrytsik.crmbot.entity.TelegramUser;
import lombok.Data;

@Data
public class TelegramUserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String role;

    public static TelegramUserDto getTelegramUserDtoWithTelegramUser(TelegramUser telegramUser) {
        TelegramUserDto telegramUserDto = new TelegramUserDto();
        telegramUserDto.setRole(telegramUser.getRole());
        telegramUserDto.setPhone(telegramUser.getPhone());
        telegramUserDto.setEmail(telegramUser.getEmail());
        telegramUserDto.setFirstName(telegramUser.getFirstName());
        telegramUserDto.setLastName(telegramUser.getLastName());
        return telegramUserDto;
    }
}
