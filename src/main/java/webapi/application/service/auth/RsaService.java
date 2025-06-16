package webapi.application.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Log4j2
public class RsaService {
  public String rsaEncrypt(String information) {
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream("poolPublicKey.rsa");
      if (inputStream == null) {
        throw new FileNotFoundException("Public key file not found in classpath");
      }

      byte[] b = inputStream.readAllBytes();
      inputStream.close();

      X509EncodedKeySpec spec = new X509EncodedKeySpec(b);
      KeyFactory factory = KeyFactory.getInstance("RSA");
      PublicKey pubKey = factory.generatePublic(spec);

      Cipher c = Cipher.getInstance("RSA");
      c.init(Cipher.ENCRYPT_MODE, pubKey);
      byte[] encryptOut = c.doFinal(information.getBytes());
      return Base64.getEncoder().encodeToString(encryptOut);

    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  public String rsaDecrypt(String decryptString) {
    try {
      ClassLoader classLoader = getClass().getClassLoader();
      InputStream inputStream = classLoader.getResourceAsStream("poolPrivateKey.rsa");
      if (inputStream == null) {
        throw new FileNotFoundException("Private key file not found in classpath");
      }

      byte[] b = inputStream.readAllBytes();
      inputStream.close();

      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
      KeyFactory factory = KeyFactory.getInstance("RSA");
      PrivateKey priKey = factory.generatePrivate(spec);

      Cipher c = Cipher.getInstance("RSA");
      c.init(Cipher.DECRYPT_MODE, priKey);
      byte[] decryptOut = c.doFinal(Base64.getDecoder().decode(decryptString));
      return new String(decryptOut);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
