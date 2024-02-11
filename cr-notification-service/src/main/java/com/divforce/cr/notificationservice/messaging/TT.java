package com.divforce.cr.notificationservice.messaging;

import java.text.MessageFormat;
import java.util.Locale;

public class TT {
    public static void main(String[] args) {
        String pattern = "Congratulations! You have been refunded Tsh {0,number} to your Azampesa account. Dial *150*08# to use Azampesa and enjoy our services";
        MessageFormat formatter = new MessageFormat(pattern, Locale.US);
        String text = formatter.format(new Object[]{2000});
        System.out.println(text);
    }
}
