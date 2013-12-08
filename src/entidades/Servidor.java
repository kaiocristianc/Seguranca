package entidades;

import utils.Utils;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.*;
import java.security.KeyStore;
import javax.net.ServerSocketFactory;

public class Servidor extends Conectavel {
    public static final int HTTPS_PORT = 8080;
    String keystore = "kservidor";
    char keystorepass[];
    char keypassword[];
    boolean autCliente;
    String nome;

    public Servidor(){}
    
    public Servidor(String nome, boolean autCliente, String password) {
        this.nome = nome;
        this.autCliente = autCliente;
        keystorepass = password.toCharArray();
        keypassword = password.toCharArray();
    }

    public static void main(String[] args) throws Exception {
        String password = "batuta";
        Servidor servidor = new Servidor("Servidor HTTPs", false, password);
        servidor.run();
    }

    public ServerSocket criaSSLServerSocket() throws Exception {

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), keystorepass);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, keypassword);

        SSLContext contextoSSL = Utils.criaSSLContext("SSLv3");
        contextoSSL.init(kmf.getKeyManagers(), null, null);

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
                Conexao x = new Conexao(cliente, this);
                this.getGerenciadorArquivos().iniciarMonitoramento(x,"/home/kaio/Downloads/pastaTeste");
            }
        } catch (Exception e) {
            System.out.println("Exception no run do servidor.Erro:" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void iniciarServicos() throws Exception {
        String password = "batuta";
        Servidor servidor = new Servidor("Servidor HTTPs", false, password);
        servidor.run();
    }
}

