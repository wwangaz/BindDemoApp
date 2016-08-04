package com.wangweimin.binddemoapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Created by wangweimin on 16/8/3.
 */

public class MD5VerifyUtil {

    private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private static String toHexString(byte[] b) {  //转化成16进制
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i]& 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }
    private static String md5sum(String filename) {
        InputStream fis;
        byte[]buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try{
            fis= new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while((numRead=fis.read(buffer)) > 0) {
                md5.update(buffer,0,numRead);
            }
            fis.close();
            return toHexString(md5.digest());
        }catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }

    public static boolean md5Verify(File file, String md5){
        String fileName = file.getAbsolutePath() + file.getName();
        return md5.equals(md5sum(fileName));
    }

}
