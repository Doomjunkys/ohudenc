package org.itkk.udf.starter.core.util;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES对称加密
 */
public class Aes {
    /**
     * AES_NAME
     */
    private static final String AES_NAME = "AES";

    /**
     * INSTANCE_NAME
     */
    private static final String INSTANCE_NAME = "AES/ECB/PKCS5Padding";

    /**
     * 私有化构造函数
     */
    private Aes() {

    }

    /**
     * 加密
     *
     * @param pswd    pswd
     * @param content content
     * @return String
     */
    public static String encrypt(String pswd, String content) {
        try {
            Cipher cipher = Cipher.getInstance(INSTANCE_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, getKey(pswd));
            byte[] result = cipher.doFinal(content.getBytes(CoreConstant.CHARACTER_SET));
            return Hex.encodeHexString(result);
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 解密
     *
     * @param pswd    pswd
     * @param content content
     * @return String
     */
    public static String decrypt(String pswd, String content) {
        try {
            Cipher cipher = Cipher.getInstance(INSTANCE_NAME);
            cipher.init(Cipher.DECRYPT_MODE, getKey(pswd));
            return new String(cipher.doFinal(Hex.decodeHex(content)), CoreConstant.CHARACTER_SET);
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 生成key
     *
     * @param pswd pswd
     * @return Key
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    private static Key getKey(String pswd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(pswd.getBytes(CoreConstant.CHARACTER_SET));
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_NAME);
        keyGenerator.init(random);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] byteKey = secretKey.getEncoded();
        return new SecretKeySpec(byteKey, AES_NAME);
    }
}
