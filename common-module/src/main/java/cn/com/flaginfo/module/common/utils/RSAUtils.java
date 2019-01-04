package cn.com.flaginfo.module.common.utils;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author meng.liu
 */
public class RSAUtils {

    private static final String KEY_ALGORITHM = "RSA";
    private static final int KEY_SIZE = 1024;

    public static byte[] decryptBASE64(String key){
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 生成密钥对
     * @return
     * @throws Exception
     */
    public static synchronized RSAKeyPair generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        kpg.initialize(KEY_SIZE);
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
        RSAPrivateKey privateKey =  (RSAPrivateKey)keyPair.getPrivate();

        RSAKeyPair rsaKeyPair = new RSAKeyPair();
        rsaKeyPair.setPublicKey(encryptBASE64(publicKey.getEncoded()));
        rsaKeyPair.setPrivateKey(encryptBASE64(privateKey.getEncoded()));
        return rsaKeyPair;
    }

    /**
     * 公钥加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(String data, String key) throws Exception {
        return buildPublicCipher(decryptBASE64(key), Cipher.ENCRYPT_MODE).doFinal(data.getBytes());
    }

    /**
     * 私钥加密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(String data, String key) throws Exception {
        return buildPrivateCipher(decryptBASE64(key), Cipher.ENCRYPT_MODE).doFinal(data.getBytes());
    }

    /**
     * 公钥解密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
        return buildPublicCipher(decryptBASE64(key), Cipher.DECRYPT_MODE).doFinal(data);
    }

    /**
     * 公钥解密
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(String content, String key) throws Exception {
        return buildPublicCipher(decryptBASE64(key), Cipher.DECRYPT_MODE).doFinal(decryptBASE64(content));
    }

    /**
     * 私钥解密
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
        return buildPrivateCipher(decryptBASE64(key), Cipher.DECRYPT_MODE).doFinal(data);
    }

    /**
     * 私钥解密
     * @param content
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(String content, String key) throws Exception {
        return buildPrivateCipher(decryptBASE64(key), Cipher.DECRYPT_MODE).doFinal(decryptBASE64(content));
    }

    /**
     * 生成私钥密码器
     * @param keyBytes
     * @return
     * @throws Exception
     */
    private static Cipher buildPrivateCipher(byte[] keyBytes, int mode) throws Exception{
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, privateKey);
        return cipher;
    }

    /**
     * 生成公钥密码器
     * @param keyBytes
     * @return
     * @throws Exception
     */
    private static Cipher buildPublicCipher(byte[] keyBytes, int mode) throws Exception{
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(mode, publicKey);
        return cipher;
    }


    @Data
    public static class RSAKeyPair {
        private String publicKey;
        private String privateKey;
    }
}
