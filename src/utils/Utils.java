package utils;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;

public class Utils {

    public static KeyStore getKeyStore(String tipo) throws KeyStoreException {
        //utiliza a implementação do keystore provido pela Sun
        return KeyStore.getInstance(tipo);
    }

    public static KeyManagerFactory getKMFactory(String algoritmo) throws NoSuchAlgorithmException {
        //cria um caminho de certificação baseado em X509
        return KeyManagerFactory.getInstance(algoritmo);
    }

    public static SSLContext criaSSLContext(String protocolo) throws NoSuchAlgorithmException {
        //cria um SSLContext segundo o protocolo informado
        return SSLContext.getInstance(protocolo);

    }

    public static byte[] getBytes(Object obj) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(obj);
            return out.toByteArray();
        } catch (Exception e) {
            System.out.println("Erro ao getBytes Utils");
        }
        return null;
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

}