package entidades;

import utils.MonitoradorRemoto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

class Conexao extends Thread implements Observer {
    Socket cliente;
    ObjectInputStream in;
    PrintStream out;
    Conectavel conectavel;

    public Conexao(Socket socket, Conectavel conectavel) throws Exception {
        this.conectavel = conectavel;
        cliente = socket;
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
            Runnable r = new MonitoradorRemoto(in, cliente);
            Thread thr = new Thread(r);
            thr.start();
        } catch (Exception e) {
            System.out.println("Erro na Thread de conexão do servidor.");
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        Map mapa = (HashMap) o;
        try {
            conectavel.executarRequisicao(mapa);
        } catch (Exception e) {
            System.out.println("Falha ao executar a requisição");
        }
    }
    public void enviarRequisicao(Map<String, Object> requisicao) throws Exception{



    }
}