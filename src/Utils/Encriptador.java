package Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.MimeUtility;

public class Encriptador{

    public static byte[] encode(byte[] b,byte[]chave) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream b64os = MimeUtility.encode(baos, "base64");
        b64os.write(b);
        b64os.close();
        return baos.toByteArray();

    }

    public static byte[] decode(byte[] b) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
        InputStream inputBase64 = MimeUtility.decode(inputStream, "base64");
        byte[] tmp = new byte[b.length];
        int n = inputBase64.read(tmp);
        byte[] res = new byte[n];
        System.arraycopy(tmp, 0, res, 0, n);
        return res;
    }

    private static SecretKeySpec getKey(byte[] chave) {
        SecretKeySpec key = new SecretKeySpec(chave, "DES");
        return key;
    }

    public static byte[] desencriptar(byte[] s,byte[] chave) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(2, getKey(chave));
        byte arrayDesencriptado[] = cipher.doFinal(decode(s));
        return arrayDesencriptado;
    }

    public static byte[] encriptar(byte[] s,byte[] chave) throws Exception {
        byte arrayEncriptado[];
        SecureRandom securerandom = new SecureRandom();
        securerandom.nextBytes(chave);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(1, getKey(chave));
        arrayEncriptado = encode(cipher.doFinal(s),chave); // antes
        return arrayEncriptado;
    }
}