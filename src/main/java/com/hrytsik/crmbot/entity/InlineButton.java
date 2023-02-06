package com.hrytsik.crmbot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InlineButton {
    private String text;
    private String url;
}
