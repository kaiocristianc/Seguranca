package entidades;

import utils.Utils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Cliente extends Conectavel{

    private static final int HTTPS_PORT = 8080;
    private static SSLSocket socket;
    private String keystore = "kcliente";
    private char[] password;
    private String nome;
    private String host;

    public Cliente(){}
    
    public Cliente(String nome, String password, String host) {
        this.nome = nome;
        this.password = password.toCharArray();
        this.host = host;
    }

    public static void main(String argv[]) {
        try {
            if (argv.length != 1) {
                System.out.println("Deve ser informado o host em que o cliente deve se conectar.");
                System.exit(0);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String password = "batuta";
            Cliente cliente = new Cliente("Cliente 1", password, argv[0]);
            cliente.run();
        } catch (Exception e) {
            System.out.println("alalelele");
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
            socket = criaSSLSocket(host);
        } catch (Exception e) {
            System.out.println("Excecao ao criar o SSLSocket erro: " + e.getMessage());
        }

        try {
            Map mapa = new HashMap();
            File file = new File("/home/kaio/Documents/pastaTeste/bananaaaaaa");
            mapa.put("arquivo", file);
            mapa.put("evento", "ENTRY_CREATE");
            byte[] meumapa = Utils.getBytes(mapa);
            PrintStream saida = new PrintStream(socket.getOutputStream());
            saida.write(meumapa);
            saida.flush();
        } catch (Exception e) {
            System.out.println("Excessão durante o run do cliente.Erro: " + e.getMessage());
        }

    }

    @Override
    public void iniciarServicos() throws Exception {
        String host = "localhost";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String password = "batuta";
            Cliente cliente = new Cliente("Cliente 1", password, host);
            cliente.run();
        } catch (Exception e) {
            System.out.println("Falha ao iniciar os serviços do cliente.Erro:"+e.getMessage());
        }
    }
}