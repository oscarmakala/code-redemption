package com.divforce.cr.voucherservice;

import com.divforce.cr.encrypt.util.AttributeEncryptor;
import org.junit.jupiter.api.Test;

/**
 * @author Oscar Makala
 */
public class CryptoTest {


    @Test
    public void testDecryptionTest() {
        try {
            AttributeEncryptor attributeEncryptor = new AttributeEncryptor("amigo");
            System.out.println(attributeEncryptor.convertToEntityAttribute("rKr/zwkOlFDPwELHSAvqy2CSKB1IvY6YJ4GfhsogKck="));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDecrypt() {
        try {
//            AttributeEncryptor codeEncoder = createEncryptor(campaign.getPassPhrase());
//            String encryptedCode = codeEncoder.convertToDatabaseColumn(uv.getCode());
//
            AttributeEncryptor attributeEncryptor = new AttributeEncryptor();
            String passPhrase = attributeEncryptor.convertToEntityAttribute("733F5vxHkVdxZPeEJUjfzWCSKB1IvY6YJ4GfhsogKck=");

            AttributeEncryptor encrypt = new AttributeEncryptor(passPhrase);
            System.out.println(encrypt.convertToEntityAttribute("ZMy4sUc8/WciMz9ZsNJv4w=="));

        } catch (Exception e) {

        }
    }
}
