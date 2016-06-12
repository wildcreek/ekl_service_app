package com.leyikao.onlinelearn.serviceapp.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class MD5Utils {

	/**
	 * md5加密的算法
	 */
	public static String encode(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] result = digest.digest(text.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int number = b & 0xff; // 加盐 +1 ;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			// can't reach
			return "";
		}
	}

	// MD5加密
	public static String getMD5Str(String pwd) {
		// 返回字符串
		String md5Str = "";
		try {
			// 操作字符串
			StringBuffer buf = new StringBuffer();
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
			md.update(pwd.getBytes());
			// 计算出摘要,完成哈希计算。
			byte b[] = md.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				// 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
				buf.append(Integer.toHexString(i));
			}

			// 32位的加密
			md5Str = buf.toString().toUpperCase();
			// 16位的加密
			// md5Str = buf.toString().md5Strstring(8,24);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}

	/**
	 * 获取到文件的MD5
	 */
	public static String getFileMd5(String sourceDir) {

		File file = new File(sourceDir);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);

			byte[] buffer = new byte[1024];

			int len = -1;
			// 获取到数字摘要
			MessageDigest messageDigest = MessageDigest.getInstance("md5");

			while ((len = fis.read(buffer)) != -1) {

				messageDigest.update(buffer, 0, len);

			}
			byte[] result = messageDigest.digest();

			StringBuffer sb = new StringBuffer();

			for (byte b : result) {
				int number = b & 0xff;
				String hex = Integer.toHexString(number);
				if (hex.length() == 1) {
					sb.append("0" + hex);
				} else {
					sb.append(hex);
				}
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null){ fis.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @Description:@DES算法，加密
	 */
	public static String decrypt(String message, String key) throws Exception {

		byte[] bytesrc = convertHexString(message);

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	// 将字符串转换成byte[]数组
	public static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}
		return digest;
	}

	/**
	 * @DES算法，加密
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws CryptException
	 *             异常
	 */
	public static byte[] encrypt(String message, String key) {
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
			return cipher.doFinal(message.getBytes("UTF-8"));
		} catch (Exception ex) {
			return null;
		}
	}
	
	public static void main(String[] args){
		System.out.println(encode("qweasd"));
	}

}
