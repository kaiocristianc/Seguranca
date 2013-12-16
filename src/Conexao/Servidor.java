package Conexao;

import entidades.Conectavel;
import entidades.Conexao;
import entidades.GerenciadorArquivos;
import utils.Utils;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.*;
import java.io.FileInputStream;
import java.net.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import javax.net.ServerSocketFactory;

public class Servidor extends Conectavel {
    public static final int HTTPS_PORT = 8080;
    private List clientes;
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

        try{String password = "batuta";
        Servidor servidor = new Servidor("Servidor HTTPs", false, password);
        servidor.run();
        }catch(Exception e){
            System.out.println("erro servidor:"+e.getMessage());
        }
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

    }

    @Override
    public void iniciarServicos() throws Exception {
        String password = "batuta";
        this.nome = "Servidor HTTPs";
        this.autCliente = false;
        this.keystorepass = password.toCharArray();
        this.keypassword = password.toCharArray();
        ServerSocket listen;
        this.clientes = new ArrayList();
        try {
            listen = criaSSLServerSocket();
            System.out.println(this.nome + " executando na porta " + HTTPS_PORT);
            System.out.println("Aguardando conexao...");
            while (true) {
                Socket cliente = listen.accept();
                System.out.println("Aceitando conexão!");
                Conexao x = new Conexao(cliente, this);
                this.clientes.add(x);
                this.gerenciadorArquivos = new GerenciadorArquivos(x,"/home/kaio/Downloads/pastaTeste");
                Thread threadGerenciadorArquivos = this.gerenciadorArquivos;
                threadGerenciadorArquivos.start();
            }
        } catch (Exception e) {
            System.out.println("Exception no run do servidor.Erro:" + e.getMessage());
            e.printStackTrace();
        }
    }
}

