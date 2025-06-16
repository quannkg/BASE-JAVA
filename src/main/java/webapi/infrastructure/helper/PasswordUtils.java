package webapi.infrastructure.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class PasswordUtils {
  @Value("${encoder.password.salt}")
  private String salt;

  @Value("${encoder.password.iterations}")
  private String iterations;

  @Value("${encoder.password.length}")
  private String length;

  @Value("${encoder.password.algorithm}")
  private String algorithm;

  public String encode(String rawPassword)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    KeySpec spec =
        new PBEKeySpec(
            rawPassword.toCharArray(),
            salt.getBytes(),
            Integer.parseInt(iterations),
            Integer.parseInt(length));
    SecretKeyFactory factory = SecretKeyFactory.getInstance(algorithm);
    SecretKey secretKey = factory.generateSecret(spec);

    byte[] hash = secretKey.getEncoded();
    String hashPassword = Base64.getEncoder().encodeToString(hash);
    return String.format("%s$%s$%s$%s", "pbkdf2_sha256", iterations, salt, hashPassword);
  }
}
