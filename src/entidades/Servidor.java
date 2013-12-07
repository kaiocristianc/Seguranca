package entidades;

import Teste.Utils;
import utils.MonitoradorRemoto;

import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Servidor extends Conectavel {

    public static final int HTTPS_PORT = 8080;
    String keystore = "kservidor";
    char keystorepass[];
    char keypassword[];
    boolean autCliente;
    String nome;

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
                new Conexao(cliente, this);
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void iniciarServicos() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void enviarRequisicao(Map<String, Object> requisicao) throws Exception {



    }


}

