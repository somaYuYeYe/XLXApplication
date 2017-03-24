package wza.slx.com.xlxapplication.net;

import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

//import org.apache.http.NameValuePair;
//import org.apache.http.message.BasicNameValuePair;


/**
 * 3DES算法
 *
 */
@SuppressWarnings("restriction")  
public class TripleDES {
    public static void main(String[] args) throws Exception { 
    	
    	Map<String, String> paramMap  = new HashMap<String,String>();
		paramMap.put("mobile", "143321432");
		paramMap.put("content", "发生发的是");
		paramMap.put("ip", "134214321");
		paramMap.put("type", "1");
		paramMap.put("source", "43"); 
		StringBuilder a=new StringBuilder();
    	SignUtils.buildPayParams(a, paramMap, false);
    	System.out.println(a.toString());
    	
        byte[] key = "1DF6BF44DACD4248B0B5A863".getBytes();  
        byte[] iv = "1DF6BF44DACD4248B0B5A863".substring(0,8).getBytes();   
        byte[] data=a.toString().getBytes("utf-8");  
          
        System.out.println("3DES加密:");  
        String hexString = encryptToHex(data,key,iv);
        System.out.println(hexString);

        // 解密
        String hexString2 = new String(decrypt(hexStringToBytes(hexString), key, iv),"utf-8");
        System.out.println(hexString2);


    }
	
//    static {
//        Security.addProvider(new com.sun.crypto.provider.SunJCE());
//    }
  
    private static final String MCRYPT_TRIPLEDES = "DESede";  
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";  
  
    /**
     * 加密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
      public static byte[] encrypt(byte[] data, byte[] key, byte[] iv) throws Exception {  
          DESedeKeySpec spec = new DESedeKeySpec(key);  
          SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");  
          SecretKey sec = keyFactory.generateSecret(spec);  
          Cipher cipher = Cipher.getInstance(TRANSFORMATION);  
          IvParameterSpec IvParameters = new IvParameterSpec(iv);  
          cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);  
          return cipher.doFinal(data);  
      }  
    /**
     * 解密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, byte[] key, byte[] iv) throws Exception {  
        DESedeKeySpec spec = new DESedeKeySpec(key);  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);  
        SecretKey sec = keyFactory.generateSecret(spec);  
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);  
        IvParameterSpec IvParameters = new IvParameterSpec(iv);  
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);  
        return cipher.doFinal(data);  
    }  
  
  
    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {  
        KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);  
        return keygen.generateKey().getEncoded();  
    }  
  
    public static byte[] randomIVBytes() {  
        Random ran = new Random();  
        byte[] bytes = new byte[8];  
        for (int i = 0; i < bytes.length; ++i) {  
            bytes[i] = (byte) ran.nextInt(Byte.MAX_VALUE + 1);  
        }  
        return bytes;  
    }
    /**
     * Convert byte[] string to hex
     * @param arrB
     * @return
     */
    public static String byteArr2HexStr(byte[] arrB) {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		// 最大128位
		String result = sb.toString();
		return result;
	}
    /** 
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
        }  
        return d;  
    }
    /** 
     * Convert char to byte 
     * @param c char 
     * @return byte 
     */  
     private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    } 
    /**
     * 加密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String encryptToHex(byte[] data, byte[] key,byte[] iv) throws Exception {
    	return byteArr2HexStr(encrypt(data, key, iv));
    }
    /**
     * 解密
     * @param data
     * @param key
     * @param iv
     * @return
     * @throws Exception
     */
    public static String decryptToString(byte[] data, byte[] key,byte[] iv) throws Exception {
    	return new String(decrypt(data, key, iv));
    }
  
  
}  
