package com.hrytsik.crmbot.commands.user;

import lombok.Data;

@Data
public class Patient {
    private String id;
    private String fio;

    private String sex;

    private String birthday;

    private String placeOfResidence;

    private String insurancePolicy;

    private String email;

    private String phoneNumber;
}
