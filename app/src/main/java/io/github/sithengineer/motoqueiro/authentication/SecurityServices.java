package io.github.sithengineer.motoqueiro.authentication;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

// todo finish this and use for data encrypt / decrypt

// based on:
// https://nelenkov.blogspot.pt/2012/04/using-password-based-encryption-on.html
// https://android-developers.googleblog.com/2013/02/using-cryptography-to-store-credentials.html
// https://android-developers.googleblog.com/2016/06/security-crypto-provider-deprecated-in.html
public class SecurityServices {
  private static final String password = "password";
  private static final int iterationCount = 1000;
  private static final int keyLength = 256;
  private static final int saltLength = keyLength / 8; // same size as key output

  public String encrypt(String data) {
    try {
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[saltLength];
      random.nextBytes(salt);
      KeySpec keySpec =
          new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
      SecretKey key = new SecretKeySpec(keyBytes, "AES");

      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      byte[] iv = new byte[cipher.getBlockSize()];
      random.nextBytes(iv);
      IvParameterSpec ivParams = new IvParameterSpec(iv);
      cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
      byte[] ciphertext = cipher.doFinal(data.getBytes("UTF-8"));
      return new String(ciphertext);
    } catch (UnsupportedEncodingException | NoSuchPaddingException |
        InvalidKeyException | InvalidKeySpecException | InvalidAlgorithmParameterException
        | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException e) {
      e.printStackTrace();
    }

    return null;
  }

  public String decrypt(String data) {
    return null;
  }
}
