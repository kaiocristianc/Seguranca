package Teste;

import java.io.*;
import java.net.*;
import javax.net.*;
import javax.net.ssl.*;
import java.security.*;
import java.util.*;

public class ServidorSimplesSSL{

    String keystore = "kservidor";
    char keystorepass[];
    char keypassword[] ;
    public static final int HTTPS_PORT = 8080;
    boolean autCliente;
    String nome;
    ObjectOutputStream out;
    ObjectInputStream in;

    //construtor
    public ServidorSimplesSSL(String nome, boolean autCliente, String password){
        this.nome = nome;
        this.autCliente = autCliente;
        keystorepass = password.toCharArray();
        keypassword = password.toCharArray();
    }

    public ServerSocket criaSSLServerSocket() throws Exception{

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), keystorepass);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, keypassword);

        SSLContext contextoSSL = Utils.criaSSLContext("SSLv3");
        contextoSSL.init(kmf.getKeyManagers(), null, null);

        showPropSSLContext(contextoSSL);

        ServerSocketFactory ssf = contextoSSL.getServerSocketFactory();
        SSLServerSocket servidorSSL = (SSLServerSocket) ssf.createServerSocket(HTTPS_PORT);
        //Se necessário, autentica o cliente
        if (autCliente){
            servidorSSL.setNeedClientAuth(autCliente);
        }
        return servidorSSL;
    }


    public void run(){

        ServerSocket listen;

        try{
            //vai criar um SSLServerSocket
            listen = criaSSLServerSocket();
            System.out.println(this.nome+" executando na porta "+HTTPS_PORT);
            System.out.println("Aguardando conexao...");
            //espera por uma conexão do cliente
            Socket cliente = listen.accept();
            Conexao con = new Conexao(cliente);
        }catch(Exception e){
            System.out.println("Exception "+e.getMessage());
            e.printStackTrace();
        }

    }


    private void showPropSSLContext(SSLContext contextoSSL){

        System.out.println("-------Informaçoes de contexto SSL-------");

        String protocol = contextoSSL.getProtocol();
        System.out.println("Protocolo : "+protocol);

        Provider provider = contextoSSL.getProvider();
        System.out.println("Nome do provedor : "+provider.getName());
        System.out.println("Versao do provedor : "+provider.getVersion());
        SSLSessionContext sslsessioncontext = contextoSSL.getServerSessionContext();

    }

    //main
    public static void main(String[] args) throws Exception{

        System.out.print("Informe o password para o keystore do servidor:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String password = in.readLine();
        ServidorSimplesSSL servidor = new ServidorSimplesSSL("Servidor HTTPs", false, password);
        servidor.run();

    }

    //classe interna
    class Conexao extends Thread {

        Socket cliente;
        BufferedReader in;
        DataOutputStream out;

        public Conexao(Socket s) {
            cliente = s;
            try {
                in = new BufferedReader(new InputStreamReader (cliente.getInputStream()));
                out = new DataOutputStream(cliente.getOutputStream());
            }catch (IOException e) {
                System.out.println("Excecao lancada: "+e.getMessage());
            }
            this.start(); // chama o método run
        }


        public void run(){
            try {
                String request = in.readLine();
                System.out.println( "Request: "+request );
                StringTokenizer st = new StringTokenizer(request);
                if ((st.countTokens() >= 2) && st.nextToken().equals("GET")) {
                    if ((request = st.nextToken()).startsWith("/"))
                        request = request.substring( 1 );
                    if (request.equals(""))
                        request = request + "index.html";
                    File arq = new File(request);
                    leDocumento(out, arq);
                }
                else{
                    out.writeBytes( "Erro 400: arquivo nao encontrado.");
                }
                cliente.close();
            }catch (Exception e) {
                System.out.println("Excecao lancada: " + e.getMessage());
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
                out.writeBytes("Tamanho do conteúdo: " + tam +"\r\n");
                out.writeBytes("Tipo do conteúdo: text/html\r\n\r\n");
                out.write(buffer);
                out.flush();
            }catch (Exception e) {
                out.writeBytes("<html><head><title>Erro</title></head><body>\r\n\r\n");
                out.writeBytes("HTTP/1.0 400 " + e.getMessage() + "\r\n");
                out.writeBytes("Tipo do conteúdo: text/html\r\n\r\n");
                out.writeBytes("</body></html>");
                out.flush();
            }finally {
                out.close();
            }
        }

    }

}

