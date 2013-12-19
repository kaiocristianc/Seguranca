package entidades;

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
    //Configuração
    public static final int HTTPS_PORT = 8080;
    String keystore = "kservidor";
    static String password = "batuta";
    static String nomeServidor;
    //-------------
    char keystorepass[];
    char keypassword[];
    boolean autCliente;
    private String nome;
    private String pasta;
    private Map mapaCliente;
    private static Map clientes;
    private boolean transacaoAndamento = false;

    public Servidor(String enderecoPasta,String nome, boolean autCliente, String password, Map cliente) {
        this.nome = nome;
        this.pasta = enderecoPasta;
        this.autCliente = autCliente;
        keystorepass = password.toCharArray();
        keypassword = password.toCharArray();
        this.mapaCliente = cliente;
    }

    public static void main(String[] args) throws Exception {
        try {
            Servidor servidor = new Servidor(args[0],args[1], false, password, null);
            servidor.iniciarServicos(args[0],args[1]);
        } catch (Exception e) {
            System.out.println("Erro no main do servidor:" + e.getMessage());
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
        try {
            Socket cliente = (Socket) mapaCliente.get("cliente");
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            PrintStream out = new PrintStream(cliente.getOutputStream());
            String nomeUsuario = (String) in.readObject();
            String pastaCliente = pasta + "/" + nomeUsuario;
            iniciarMonitoramentoArquivos(pastaCliente);
            System.out.println("Monitoramento sendo realizado na pasta:"+pastaCliente);
            iniciarTratadorRedundancia();
            List lista = new ArrayList();
            lista.add(in);
            lista.add(out);
            clientes.put(nomeUsuario, lista);
            escutarRequisicoes(cliente);
        } catch (Exception e) {
            System.out.println("Falha no run do servidor.");
        }
    }

    public void sinalizarAlteracaoLocal(Map<String, Object> requisicao) throws Exception {
        try {
            String evento = (String)requisicao.get("evento");
            System.out.println("Servidor sinalizando alteração local:"+evento);
            File arquivo;
            if (evento.equals(Constantes.EXCLUIR))
                arquivo = new File((String) requisicao.get("endereco"));
            else
                arquivo = (File) requisicao.get("arquivo");
            PrintStream saidaCliente = getSaidaCliente(arquivo.getAbsolutePath());
            if (saidaCliente != null) {
                saidaCliente.write(Utils.getBytes(requisicao));
                saidaCliente.flush();
                gerenciadorArquivos.destravarArquivo();
            }
        } catch (Exception e) {
            System.out.println("Falha ao sinalizar a alteração local.Falha:" + e.getMessage());
        }
    }

    private PrintStream getSaidaCliente(String arquivoModificado) {
        Pattern pattern = Pattern.compile("(?<=" + pasta + "/)[^/]+");
        Matcher matcher = pattern.matcher(arquivoModificado);
        matcher.find();
        String nomeUsuario = matcher.group();
        List propriedades = (ArrayList) this.clientes.get(nomeUsuario);
        PrintStream saida = null;
        if (propriedades != null) {
            saida = (PrintStream) propriedades.get(1);
        }
        return saida;
    }

    @Override
    public void iniciarServicos(String enderecoPasta,String nome) throws Exception {
        ServerSocket listen;
        try {
            listen = criaSSLServerSocket();
            System.out.println(this.nome + " executando na porta " + HTTPS_PORT);
            System.out.println("Aguardando conexões...");
            this.clientes = new HashMap();
            while (true) {
                Socket cliente = listen.accept();
                System.out.println("Cliente aceito");
                Map mapa = new HashMap();
                mapa.put("cliente", cliente);
                Thread s = new Servidor(enderecoPasta,nome, false, password, mapa);
                s.start();
            }
        } catch (Exception e) {
            System.out.println("Exception no run do servidor.Erro:" + e.getMessage());
            e.printStackTrace();
        }
    }
}

