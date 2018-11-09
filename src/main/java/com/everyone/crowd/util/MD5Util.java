package com.everyone.crowd.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class MD5Util {

    private static final int SALT_LENGTH = 16;

    /**
     * MD5 hash
     * @param str input string
     * @return MD5 of the input string
     */
    private static String encrypt(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * MD5 with salt
     * @param str input string
     * @return salted MD5 of input string
     */
    public static String saltEncrypt(String str) {
        String salt = getSalt();
        String saltedMD5 = encrypt(encrypt(str) + salt);
        return saltedMD5 + salt;
    }

    /**
     * Verify salted string with original string
     * @param salted salted string
     * @param orig original string
     * @return true if salted string is corresponding to original string
     */
    public static boolean verifySaltedString(String salted, String orig) {
        String salt = salted.substring(32);
        String md5 = encrypt(encrypt(orig) + salt);
        return md5.equals(salted.substring(0, 32));
    }

    /**
     * Get random salt
     * @return salt string
     */
    private static String getSalt() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < SALT_LENGTH; i++) {
            sb.append(Integer.toHexString(new Random().nextInt(16)));
        }
        return sb.toString();
    }
}
