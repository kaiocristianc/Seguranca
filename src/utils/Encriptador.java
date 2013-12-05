package utils;

import java.io.*;
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

    public static byte[] encriptar(Object arquivo,byte[] chave) throws Exception {
        byte[] arrayNaoEncriptado = prepararArquivoParaEnvio(arquivo);
        byte[] arrayEncriptado;
        SecureRandom securerandom = new SecureRandom();
        securerandom.nextBytes(chave);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(1, getKey(chave));
        arrayEncriptado = encode(cipher.doFinal(arrayNaoEncriptado),chave); // antes
        return arrayEncriptado;
    }

    private static byte[] prepararArquivoParaEnvio(Object arquivo){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(arquivo);
            return bos.toByteArray();
        }catch(Exception e){
            System.out.println("Erro ao bytear o arquivo.Erro:"+e.getMessage());
        }
        return null;
    }
}