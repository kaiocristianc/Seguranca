package Teste;

import java.io.*;
import java.net.*;
import java.security.*;
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
            socket = criaSSLSocket(host); //cria o socket SSL que o cliente utilizará
        }catch(Exception e){
            System.out.println("Excecao 1 lancada : "+e.getMessage());
        }

        try{
            BufferedWriter out = new BufferedWriter(new
                    OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.write("GET / HTTP/1.0\n\n");
            out.flush();
            //o trecho a seguir pega o arquivo do servidor, lê esse arquivo e imprime na tela
            String linha;
            StringBuffer sb = new StringBuffer();
            while((linha = in.readLine()) != null) {
                sb.append(linha);
                sb.append("\n");
            }
            out.close();
            in.close();
            System.out.println(sb.toString());
        }catch(Exception e){
            System.out.println("Excecao 2 lancada: "+e.getMessage());
        }

    }

    public static void main(String argv[]) throws IOException{

        if (argv.length != 1) {
            System.out.println("Deve ser informado o host em que o cliente deve se conectar.");
            System.exit(0);
        }

        System.out.print("Informe o password para o keystore do cliente:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = in.readLine();
        ClienteSimplesSSL cliente = new ClienteSimplesSSL("Cliente 1", password, argv[0]);
        cliente.run();
    }

}