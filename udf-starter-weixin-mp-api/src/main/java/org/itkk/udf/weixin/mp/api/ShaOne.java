/**
 * ShaOne.java
 * Created at 2016-04-23
 * Created by Administrator
 * Copyright (C) 2016 LLSFW, All rights reserved.
 */
package org.itkk.udf.weixin.mp.api;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Title: SHA1算法
 * </p>
 */
public final class ShaOne {

    /**
     * ShaOne
     */
    private ShaOne() {

    }

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        final int num2 = 2;
        final int num4 = 4;
        final int num0x0f = 0x0f;
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * num2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(hexDigits[(aByte >> num4) & num0x0f]);
            buf.append(hexDigits[aByte & num0x0f]);
        }
        return buf.toString();
    }

    /**
     * <p>
     * Description: 编码
     * </p>
     *
     * @param str 字符串
     * @return 编码后的字符串
     */
    public static String encode(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes("UTF-8"));
            return getFormattedText(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e); //NOSONAR
        }
    }
}
