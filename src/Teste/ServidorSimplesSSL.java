package Teste;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSessionContext;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Provider;
import java.util.HashMap;
import java.util.Map;

public class ServidorSimplesSSL {

    public static final int HTTPS_PORT = 8080;
    String keystore = "kservidor";
    char keystorepass[];
    char keypassword[];
    boolean autCliente;
    String nome;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ServidorSimplesSSL(String nome, boolean autCliente, String password) {
        this.nome = nome;
        this.autCliente = autCliente;
        keystorepass = password.toCharArray();
        keypassword = password.toCharArray();
    }

    public static void main(String[] args) throws Exception {
        String password = "batuta";
        ServidorSimplesSSL servidor = new ServidorSimplesSSL("Servidor HTTPs", false, password);
        servidor.run();
    }

    public ServerSocket criaSSLServerSocket() throws Exception {

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), keystorepass);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, keypassword);

        SSLContext contextoSSL = Utils.criaSSLContext("SSLv3");
        contextoSSL.init(kmf.getKeyManagers(), null, null);

        showPropSSLContext(contextoSSL);

        ServerSocketFactory ssf = contextoSSL.getServerSocketFactory();
        SSLServerSocket servidorSSL = (SSLServerSocket) ssf.createServerSocket(HTTPS_PORT);
        if (autCliente) {
            servidorSSL.setNeedClientAuth(autCliente);
        }
        return servidorSSL;
    }

    public void run() {
        ServerSocket listen;
        try {

            listen = criaSSLServerSocket();
            System.out.println(this.nome + " executando na porta " + HTTPS_PORT);
            System.out.println("Aguardando conexao...");
            while (true) {
                Socket cliente = listen.accept();
                System.out.println("Aceitando conexão!");
                new Conexao(cliente);
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }

    }

    private void showPropSSLContext(SSLContext contextoSSL) {

        System.out.println("-------Informaçoes de contexto SSL-------");

        String protocol = contextoSSL.getProtocol();
        System.out.println("Protocolo : " + protocol);

        Provider provider = contextoSSL.getProvider();
        System.out.println("Nome do provedor : " + provider.getName());
        System.out.println("Versao do provedor : " + provider.getVersion());
        SSLSessionContext sslsessioncontext = contextoSSL.getServerSessionContext();
    }

    class Conexao extends Thread {

        Socket cliente;
        ObjectInputStream in;
        PrintStream out;

        public Conexao(Socket s) throws Exception {
            cliente = s;
            try {
                out = new PrintStream(cliente.getOutputStream());
                in = new ObjectInputStream(cliente.getInputStream());
            } catch (IOException e) {
                System.out.println("Excessão ao criar o in e o out do client." + e.getMessage());
            }
            this.start();
        }

        public void run() {
            try {
                Map request = (HashMap) in.readObject();
                System.out.println("Request: " + request);
                String texto = "Eita nóis";
                out.write(getBytesParaEncriptacao(texto));

            } catch (Exception e) {
                System.out.println("EROOOOOO");
            }
        }


        // Lê o arquivo e o envia para o cliente

        public void leDocumento(DataOutputStream out, File arq) throws Exception {
            try {
                DataInputStream in = new DataInputStream(new FileInputStream(arq));
                int tam = (int) arq.length();
                byte[] buffer = new byte[tam];
                in.readFully(buffer);
                in.close();
                out.writeBytes("HTTP/1.0 200 OK\r\n");
                out.writeBytes("Tamanho do conteúdo: " + tam + "\r\n");
                out.writeBytes("Tipo do conteúdo: text/html\r\n\r\n");
                out.write(buffer);
                out.flush();
            } catch (Exception e) {
                out.writeBytes("<html><head><title>Erro</title></head><body>\r\n\r\n");
                out.writeBytes("HTTP/1.0 400 " + e.getMessage() + "\r\n");
                out.writeBytes("Tipo do conteúdo: text/html\r\n\r\n");
                out.writeBytes("</body></html>");
                out.flush();
            } finally {
                out.close();
            }
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

