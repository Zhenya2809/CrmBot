package com.hrytsik.crmbot.commands.authorization;


import com.hrytsik.crmbot.ExecutionContext;
import com.hrytsik.crmbot.commands.authorization.rolemenu.Admin;
import com.hrytsik.crmbot.commands.authorization.rolemenu.ChoseRole;
import com.hrytsik.crmbot.commands.authorization.rolemenu.Doctor;
import com.hrytsik.crmbot.commands.authorization.rolemenu.User;
import com.hrytsik.crmbot.entity.TelegramUser;
import com.hrytsik.crmbot.entity.dto.TelegramUserDto;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Authorized implements Registration {
    @Override
    public void execute(ExecutionContext executionContext) throws IOException {

        String role = executionContext.getUser().getRole();
        TelegramUserDto telegramUserDto = TelegramUserDto.getTelegramUserDtoWithTelegramUser(executionContext.getUser());
        executionContext.sendRequestWithJsonWithoutHeader(TelegramUserDto.class, telegramUserDto, HttpMethod.POST, "http://localhost:8085/api/v1/auth/createacc");
        Map<String, ChoseRole> map = new HashMap<>();
        map.put("ROLE_USER", new User());
        map.put("ROLE_ADMIN", new Admin());
        map.put("ROLE_DOCTOR", new Doctor());
        ChoseRole choseRole = map.get(role);
        if (choseRole == null) {
            throw new RuntimeException("fail to find by role");
        }
        choseRole.execute(executionContext);
        executionContext.setLocalState(null);
        executionContext.setGlobalState(null);
    }
}
