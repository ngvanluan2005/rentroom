package com.luannv.rentroom.utils;

import org.springframework.stereotype.Component;

import java.util.Base64;

import static com.luannv.rentroom.constants.UrlConstants.DEFAULT_AVATAR;

@Component
public class StringHandleUtils {
    public String toCapitalizeString(String name) {
        if (name != null && !name.isEmpty()){
            String strippedName = name.trim();
            return strippedName.substring(0, 1).toUpperCase() + strippedName.substring(1, strippedName.length()).toLowerCase();
        }
        return null;
    }
}
