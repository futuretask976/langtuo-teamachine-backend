package com.gx.sp3.demo.web.security.encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5PasswordEncoder implements PasswordEncoder {
    private static final String salt = "gx-springboot2-demo";

    @Override
    public String encode(CharSequence rawPassword) {
        System.out.printf("!!! MD5PasswordEncoder#encode rawPassword=%s\n", rawPassword);
        try {
            // 使用JDK自带的MD5加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // CharSequence转换为String，才能获得字节数组
            byte[] bytes = md5.digest((rawPassword.toString() + salt).getBytes(StandardCharsets.UTF_8));

            return new String(bytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        System.out.printf("!!! MD5PasswordEncoder#matches rawPassword=%s, encodedPassword=%s\n", rawPassword, encodedPassword);
        // rawPassword是需要验证的原密码，encodedPassword是加密后数据库中保存的密码，
        // 这个方法不需要我们手动调用，而是由Spring Security调用，这里写好规则即可
        if (rawPassword != null && encodedPassword != null) {
            System.out.printf("!!! MD5PasswordEncoder#matches encode(rawPassword).equals(encodedPassword)=%s, rawPassword.equals(encodedPassword)=%s\n", encode(rawPassword).equals(encodedPassword), rawPassword.equals(encodedPassword));
            return encode(rawPassword).equals(encodedPassword)
                    || rawPassword.equals(encodedPassword);
        } else {
            return false;
        }
    }
}
