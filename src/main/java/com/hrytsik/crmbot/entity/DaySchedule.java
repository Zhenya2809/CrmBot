package com.hrytsik.crmbot.entity;

import lombok.Data;
import java.util.HashSet;

@Data
public class DaySchedule {
    String date;
    HashSet<String> available;

}
