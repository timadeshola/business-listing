package com.business.core.utils;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import static com.business.core.constants.AppConstant.Security.*;
import static com.business.core.constants.AppConstant.Utf8CharSet.UTF_8;

@Slf4j
public class AppUtils {

    public static <T> String toJSON(T t) {
        return new Gson().toJson(t);
    }

    public static <T> T fromJSON(String t, Class<T> clazz) {
        return new Gson().fromJson(t, clazz);
    }

    public static String encryptStringData(String data){
        return myEncryption(data, DEFUALT_KEY);
    }

    public static String decryptStringData(String data){
        return myDecryption(data, DEFUALT_KEY);
    }

    private static String myEncryption(String text, String secretKey){
        String myEncryptedData ="";
        try{

            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            byte[] enc = ecipher.doFinal(text.getBytes(UTF_8));

            myEncryptedData = new String(Base64.encodeBase64(enc), UTF_8);
            // escapes for url
            myEncryptedData =myEncryptedData.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");
        }catch (Exception e){
            log.error("Error occurred:: {}", e.getMessage());
        }
        return myEncryptedData;
    }

    private static String myDecryption(String textS, String secretKey){
        try {

            String input = textS.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

            byte[] dec = Base64.decodeBase64(input.getBytes(UTF_8));

            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            byte[] decoded = dcipher.doFinal(dec);

            textS = new String(decoded, UTF_8);

        }catch (Exception e){
            log.error("Error occurred:: {}", e.getMessage());
        }

        return textS;

    }

}
