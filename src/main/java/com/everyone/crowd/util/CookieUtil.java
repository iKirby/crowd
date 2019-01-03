package com.everyone.crowd.util;

import com.everyone.crowd.entity.Message;
import com.everyone.crowd.entity.exception.InternalErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    public static void addMessage(HttpServletResponse response, String prefix, Message message, String path) {
        try {
            Cookie messageContent = new Cookie(prefix + "_messageContent",
                    URLEncoder.encode(message.getContent(), StandardCharsets.UTF_8.name()));
            messageContent.setHttpOnly(false);
            messageContent.setPath(path);
            Cookie messageType = new Cookie(prefix + "_messageType", message.getType());
            messageType.setHttpOnly(false);
            messageType.setPath(path);
            response.addCookie(messageContent);
            response.addCookie(messageType);
        } catch (UnsupportedEncodingException e) {
            throw new InternalErrorException("字符串编码错误");
        }
    }
}
