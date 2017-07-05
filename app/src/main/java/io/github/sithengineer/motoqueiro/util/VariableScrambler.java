package io.github.sithengineer.motoqueiro.util;

import android.support.v4.util.Pair;
import android.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class VariableScrambler {

  private static final int AES_BLOCKSIZE = 128;
  private static final String SALT = "MotoQ!!";
  private static final Charset CHARSET = Charset.forName("UTF-8");
  private final String mySecretPass;

  public VariableScrambler(String mySecretPass) {
    this.mySecretPass = mySecretPass;
  }

  /**
   * @return simple Hash for the user id
   */
  public Pair<String, String> scrambleUserId(String userId) {
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-1");
      byte[] result = sha.digest(userId.getBytes(CHARSET));
      return new Pair<>(new String(Base64.encode(result, Base64.NO_WRAP), CHARSET),
          "basic");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return new Pair<>(userId, "none");
  }

  // Code based on https://www.owasp.org/index.php/Using_the_Java_Cryptographic_Extensions
  /*
  public Pair<String, String> scrambleUserId(String userId) {
    try {

      byte[] key = (SALT + mySecretPass).getBytes(CHARSET);
      MessageDigest sha = MessageDigest.getInstance("SHA-1");
      key = sha.digest(key);
      key = Arrays.copyOf(key, 16); // use only first 128 bit
      SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

      byte[] iv = new byte[AES_BLOCKSIZE / 8];
      new SecureRandom().nextBytes(iv);

      Cipher aesCipherForEncryption = Cipher.getInstance("AES/CBC/PKCS7PADDING");
      aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKey,
          new IvParameterSpec(iv));

      byte[] byteDataToEncrypt = userId.getBytes(CHARSET);
      byte[] byteCipherText = aesCipherForEncryption.doFinal(byteDataToEncrypt);

      return new Pair<>(new String(Base64.encode(byteCipherText, Base64.NO_WRAP), CHARSET),
          new String(Base64.encode(iv, Base64.NO_WRAP), CHARSET));
    } catch (NoSuchAlgorithmException noSuchAlgo) {
      Timber.e(" No Such Algorithm exists " + noSuchAlgo);
    } catch (NoSuchPaddingException noSuchPad) {
      Timber.e(" No Such Padding exists " + noSuchPad);
    } catch (InvalidKeyException invalidKey) {
      Timber.e(" Invalid Key " + invalidKey);
    } catch (BadPaddingException badPadding) {
      Timber.e(" Bad Padding " + badPadding);
    } catch (IllegalBlockSizeException illegalBlockSize) {
      Timber.e(" Illegal Block Size " + illegalBlockSize);
    } catch (InvalidAlgorithmParameterException invalidParam) {
      Timber.e(" Invalid Parameter " + invalidParam);
    }

    Timber.e("Unable to cipher data.");
    return new Pair<>(userId, "none");
  }
  */
}
