package entidades;

import utils.Utils;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.Map;

public class Cliente extends Conectavel {

    private static final int HTTPS_PORT = 8080;
    private static SSLSocket socket;
    private String keystore;
    private char[] byteSenha;
    private String senha;
    private String nome;
    private String host;
    private String enderecoPasta;

    public Cliente(String keystore,String endereco,String nome, String password, String host) {
        this.nome = nome;
        this.senha = password;
        this.byteSenha = password.toCharArray();
        this.host = host;
        this.enderecoPasta = endereco;
        this.keystore = keystore;
    }

    public static void main(String argv[]) {
        try {
            String senha = argv[0];
            String enderecoKeyStore = argv[1];
            String pastaMonitorada = argv[2];
            String nome_senha = argv[3];
            String local = argv[4];
            new Cliente(enderecoKeyStore,pastaMonitorada,nome_senha,senha,local).iniciarServicos(enderecoKeyStore,pastaMonitorada,nome_senha);
        } catch (Exception e) {
            System.out.println("Erro no main do cliente");
        }
    }

    private SSLSocket criaSSLSocket(String host) throws Exception {

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), byteSenha);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, byteSenha);

        SSLContext sslcontext = Utils.criaSSLContext("SSLv3");
        sslcontext.init(kmf.getKeyManagers(), null, null);

        SSLSocketFactory ssf = sslcontext.getSocketFactory();
        SSLSocket socket = (SSLSocket) ssf.createSocket(host, HTTPS_PORT);

        return socket;
    }

    public void run() {
        try {
            iniciarMonitoramentoArquivos(enderecoPasta);
            System.out.println("Monitoramento sendo realizado na pasta:"+enderecoPasta);
            iniciarTratadorRedundancia();
            PrintStream out = new PrintStream(socket.getOutputStream());
           System.out.println("Enviando o nome:" + nome);
            out.write(Utils.getBytes(nome));
            out.flush();
            this.escutarRequisicoes(socket);
        } catch (Exception e) {
            System.out.println("Excessão durante o run do cliente.Erro: " + e.getMessage());
        }
    }

    @Override
    public void iniciarServicos(String keystore,String enderecoPasta,String nome) throws Exception {
        try {
            Cliente cliente = new Cliente(keystore,enderecoPasta,nome,senha, host);
            cliente.socket = cliente.criaSSLSocket(host);
            cliente.run();
        } catch (Exception e) {
            System.out.println("Falha ao iniciar os serviços do cliente.Erro:" + e.getMessage());
        }
    }

    @Override
    public void sinalizarAlteracaoLocal(Map<String, Object> requisicao) throws Exception {
        try {
            String evento = (String)requisicao.get("evento");
            System.out.println("Cliente sinalizando alteração local:"+evento);
            PrintStream saidaCliente = new PrintStream(socket.getOutputStream());
            saidaCliente.write(Utils.getBytes(requisicao));
            saidaCliente.flush();
        } catch (Exception e) {
            System.out.println("Falha ao sinalizar a alteração local, cliente.Falha:" + e.getMessage());
        }
    }
}