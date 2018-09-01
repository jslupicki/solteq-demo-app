package com.slupicki.solteq.demoapp.common;

import java.time.format.DateTimeFormatter;

public interface Constants {

    int SEARCH_STRING_LENGTH = 1000;
    String DATE_PATTERN = "yyyy-MM-dd";
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
}
