package Conexao;

import entidades.Conectavel;
import entidades.GerenciadorArquivos;
import utils.Constantes;
import utils.Utils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ServerSocketFactory;

public class Servidor extends Conectavel {
    public static final int HTTPS_PORT = 8080;
    String keystore = "kservidor";
    char keystorepass[];
    char keypassword[];
    boolean autCliente;
    private String nome;
    private String pasta = "/home/kaio/Downloads/pastaTeste";
    private Map clientes;

    public Servidor(String nome, boolean autCliente, String password) {
        this.nome = nome;
        this.autCliente = autCliente;
        keystorepass = password.toCharArray();
        keypassword = password.toCharArray();
    }

    public static void main(String[] args) throws Exception {
        try {
            String password = "batuta";
            Servidor servidor = new Servidor("Servidor HTTPs", false, password);
            servidor.run();
        } catch (Exception e) {
            System.out.println("erro servidor:" + e.getMessage());
        }
    }

    private ServerSocket criaSSLServerSocket() throws Exception {

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
            iniciarMonitoramentoArquivos();
            System.out.println("Monitoramento de arquivos iniciado.Pasta monitorada:" + this.pasta);
            System.out.println("Aguardando conexões...");
            this.clientes = new HashMap();
            while (true) {
                Socket cliente = listen.accept();
                System.out.println("Aceitando conexão");
                ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
                PrintStream out = new PrintStream(cliente.getOutputStream());
                String nomeUsuario = "kaio";
                List lista = new ArrayList();
                lista.add(in);
                lista.add(out);
                this.clientes.put(nomeUsuario, lista);
                System.out.println("Conexão do usuário " + nomeUsuario + " aceita");
            }
        } catch (Exception e) {
            System.out.println("Exception no run do servidor.Erro:" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void iniciarMonitoramentoArquivos() {
        try {
            this.gerenciadorArquivos = new GerenciadorArquivos(this, pasta);
            Thread threadGerenciadorArquivos = this.gerenciadorArquivos;
            threadGerenciadorArquivos.start();
        } catch (Exception e) {
            System.out.println("Falha ao iniciar o monitoramento de arquivos no servidor.Erro:" + e.getMessage());
        }
    }

    public void sinalizarAlteracaoLocal(Map<String, Object> requisicao) throws Exception {
        try{
        File arquivo = (File) requisicao.get("endereco");
        PrintStream saidaCliente = getSaidaCliente(arquivo.getAbsolutePath());
        if(saidaCliente != null){
            saidaCliente.write(Utils.getBytes(requisicao));
            saidaCliente.flush();
        }
        }catch(Exception e){
            System.out.println("Falha ao sinalizar a alteração local.Falha:"+e.getMessage());
        }
    }

    private PrintStream getSaidaCliente(String arquivoModificado) {
        Pattern pattern = Pattern.compile("(?<=" + pasta + "/)[^/]+");
        Matcher matcher = pattern.matcher(arquivoModificado);
        matcher.find();
        String nomeUsuario = matcher.group();
        List propriedades = (ArrayList)this.clientes.get(nomeUsuario);
        PrintStream saida = null;
        if(propriedades != null)
            saida = (PrintStream)propriedades.get(1);
        return saida;
    }

    @Override
    public void iniciarServicos() throws Exception {
        String password = "batuta";
        Servidor servidor = new Servidor("Servidor HTTPs", false, password);
        servidor.run();
    }
}

