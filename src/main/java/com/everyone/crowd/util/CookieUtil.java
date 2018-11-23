package com.everyone.crowd.util;

import javax.servlet.http.Cookie;

public class CookieUtil {

    /**
     * Get cookie value
     * @param key Cookie name
     * @param cookies Cookies array
     * @return Cookie value or empty string if not found
     */
    public static String getCookieValue(String key, Cookie[] cookies) {
        String value = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    value = cookie.getValue();
                }
            }
        }
        return value;
    }
}
