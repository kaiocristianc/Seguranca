package Teste;

import javax.net.ssl.*;
import java.security.*;

public class Utils{

    protected static KeyStore getKeyStore(String tipo) throws KeyStoreException{

        //utiliza a implementação do keystore provido pela Sun
        return KeyStore.getInstance(tipo);

    }

    protected static KeyManagerFactory getKMFactory(String algoritmo) throws NoSuchAlgorithmException{

        //cria um caminho de certificação baseado em X509
        return KeyManagerFactory.getInstance(algoritmo);

    }

    protected static SSLContext criaSSLContext(String protocolo) throws NoSuchAlgorithmException{

        //cria um SSLContext segundo o protocolo informado
        return SSLContext.getInstance(protocolo);

    }

}