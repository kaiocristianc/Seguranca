package Teste;

import java.io.*;
import java.net.*;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.*;

public class ClienteSimplesSSL{

    private static final int HTTPS_PORT = 8080;
    private static SSLSocket socket;
    private String keystore = "kcliente";
    private char[]password;
    private String nome;
    private String host;

    public ClienteSimplesSSL(String nome, String password, String host){
        this.nome = nome;
        this.password = password.toCharArray();
        this.host = host;
    }

    private SSLSocket criaSSLSocket(String host) throws Exception{

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), password);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, password);

        SSLContext sslcontext = Utils.criaSSLContext("SSLv3");
        sslcontext.init(kmf.getKeyManagers(), null, null);

        SSLSocketFactory ssf = sslcontext.getSocketFactory();
        SSLSocket socket = (SSLSocket) ssf.createSocket(host,HTTPS_PORT);

        return socket;
    }

    public void run(){

        try{
            socket = criaSSLSocket(host);
        }catch(Exception e){
            System.out.println("Excecao ao criar o SSLSocket erro: "+e.getMessage());
        }

        try{
            Map mapa = new HashMap();
            mapa.put("entrada",new ArrayList());
            byte[] meumapa = getBytesParaEncriptacao(mapa);

            PrintStream saida = new PrintStream(socket.getOutputStream());
            saida.write(meumapa);
            saida.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            System.out.println("");

        }catch(Exception e){
            System.out.println("Excessão durante a transação.Erro: "+e.getMessage());
        }

    }

    public static void main(String argv[]) {
                                           try{
        if (argv.length != 1) {
            System.out.println("Deve ser informado o host em que o cliente deve se conectar.");
            System.exit(0);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = "batuta";
        ClienteSimplesSSL cliente = new ClienteSimplesSSL("Cliente 1", password, argv[0]);
        cliente.run();
                                           }catch(Exception e){
                                               System.out.println("alalelele");
                                           }
    }


    private static byte[] getBytesParaEncriptacao(Object arquivo){
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