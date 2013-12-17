package utils;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.*;

public class Utils{

    public static KeyStore getKeyStore(String tipo) throws KeyStoreException{
        //utiliza a implementação do keystore provido pela Sun
        return KeyStore.getInstance(tipo);
    }

    public static KeyManagerFactory getKMFactory(String algoritmo) throws NoSuchAlgorithmException{
        //cria um caminho de certificação baseado em X509
        return KeyManagerFactory.getInstance(algoritmo);
    }

    public static SSLContext criaSSLContext(String protocolo) throws NoSuchAlgorithmException{
        //cria um SSLContext segundo o protocolo informado
        return SSLContext.getInstance(protocolo);

    }

    public static byte[] getBytes(Object arquivo){
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