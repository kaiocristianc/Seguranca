package utils;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import entidades.Conexao;

public class MonitoradorRemoto extends Thread{

    private ObjectInputStream in;
    private Socket socket;
    private Conexao conexao;

    public MonitoradorRemoto(ObjectInputStream in, Socket socket, Conexao conexao) {
        this.in = in;
        this.socket = socket;
        this.conexao = conexao;
    }

    public void run() {
        try {
            while (true) {
                Map mapa = (HashMap)in.readObject();
                conexao.executarRequisicao(mapa);
            }
        } catch (Exception e) {
            System.out.println("Erro na thread de leitura na classe Monitorador Remoto.Erro:"+e.getMessage());
        }
    }
}
