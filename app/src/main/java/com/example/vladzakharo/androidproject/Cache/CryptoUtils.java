package com.example.vladzakharo.androidproject.Cache;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Vlad Zakharo on 02.01.2017.
 */

public class CryptoUtils
{
    private static final String MD5_ALGO = "MD5";
    public static String encryptToMD5(String text)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance(MD5_ALGO);
            md.update(text.getBytes());
            byte[] bytes = md.digest();
            String hex = bytesToHexString(bytes);
            return hex;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return String.valueOf(text.hashCode());
        }
    }

    private static String bytesToHexString(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++)
        {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if(hex.length() == 1)
            {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static String encodeToBase64(String str) {
        String tmp = "";
        if(str.isEmpty()) {
            try {
                tmp = new String(Base64.encode(str.getBytes(), Base64.DEFAULT)).trim();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }
}
