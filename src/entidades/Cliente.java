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
    private String keystore = "kcliente";
    private char[] password;
    private String nome;
    private String host;
    private String enderecoPasta;

    public Cliente() {
    }

    public Cliente(String endereco,String nome, String password, String host) {
        this.nome = nome;
        this.password = password.toCharArray();
        this.host = host;
        this.enderecoPasta = endereco;
    }

    public static void main(String argv[]) {
        try {
            new Cliente().iniciarServicos(argv[0],argv[1]);
        } catch (Exception e) {
            System.out.println("Erro no main do cliente");
        }
    }

    private SSLSocket criaSSLSocket(String host) throws Exception {

        KeyStore ks = Utils.getKeyStore("JKS");
        ks.load(new FileInputStream(keystore), password);

        KeyManagerFactory kmf = Utils.getKMFactory("SunX509");
        kmf.init(ks, password);

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
    public void iniciarServicos(String enderecoPasta,String nome) throws Exception {
        String host = "localhost";
        try {
            String password = "batuta";
            Cliente cliente = new Cliente(enderecoPasta,nome, password, host);
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