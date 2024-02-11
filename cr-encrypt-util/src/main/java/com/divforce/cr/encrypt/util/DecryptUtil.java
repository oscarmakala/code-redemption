package com.divforce.cr.encrypt.util;

/**
 * @author Oscar Makala
 */
public class DecryptUtil {
    public static String decryptCode(String serial, String code, String keyPhrase) {
        try {
            AttributeEncryptor attributeEncryptor = new AttributeEncryptor();
            String passPhrase = attributeEncryptor.convertToEntityAttribute(keyPhrase);

            AttributeEncryptor encrypt = new AttributeEncryptor(passPhrase);
            return encrypt.convertToEntityAttribute(code);
        } catch (Exception e) {
            return serial;
        }
    }
}
