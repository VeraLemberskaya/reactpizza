package com.lemberskay.reactpizza.util;

import org.apache.tomcat.util.codec.binary.Base64;

public class UserEncoder {
    private UserEncoder(){}

    public static String getUserName(String authorization){
        String usernameAndPassword = new String(Base64.decodeBase64(authorization.substring(6)));
        return usernameAndPassword.split(":")[0];
    }
}
