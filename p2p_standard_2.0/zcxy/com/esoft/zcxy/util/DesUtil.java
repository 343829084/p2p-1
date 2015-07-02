package com.esoft.zcxy.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
 
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
public class DesUtil {
 
	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
 
    public static void main(String[] args) throws Exception {
        String data = "S078JKKtSvAMWap/sIGpd4eiphXoB0MB8EfFuFogsz/R8o7hYkKApjAPka9hlVKYiMEDcsSmh6uKm8p9f1d32jhcgVaczB3YRdkBiFy9C5tV2+xLJsQvEcHRb8AD32c6JOqSyMb8ey0xOOMqHsU4usR0IZLltE/TSaFAfSrd2CfTqoHrKqpb9BVBuUfpwW8DLUon3XFsLNRxAt48gGq82NDERdFnWb70RULI/rbJihR846EgZVU7tg==";
        String key = "12345678";
        System.err.println(decrypt(data, key));
//        System.err.println(decrypt(encrypt(data, key), key));
 
    }
     
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes("GBK"), key.getBytes());
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt, "GBK");
    }
 
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
        IvParameterSpec iv = new IvParameterSpec(key);// 向量
        AlgorithmParameterSpec paramSpec = iv;
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, paramSpec);
 
        return cipher.doFinal(data);
    }
     
     
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
 
        IvParameterSpec iv = new IvParameterSpec(key);
        AlgorithmParameterSpec paramSpec = iv;

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, paramSpec);
 
        return cipher.doFinal(data);
    }
}
