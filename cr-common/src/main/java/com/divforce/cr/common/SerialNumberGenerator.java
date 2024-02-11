package com.divforce.cr.common;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author Oscar Makala
 */
public class SerialNumberGenerator {
    public static String generate(String voucher) {
        return DigestUtils.md5DigestAsHex(voucher.getBytes(StandardCharsets.UTF_8));
    }
}
