package com.hrytsik.crmbot.entity.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private Long id;
    private String doctorFirstName;
    private String doctorLastName;
    private String speciality;
    private String about;
    private String linkPhoto;
    private Long userId;
    private String login;
    private String password;
    private String rePassword;
    private String email;
}
