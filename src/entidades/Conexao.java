package entidades;

import utils.MonitoradorRemoto;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class Conexao extends Thread{
    private Socket cliente;
    private ObjectInputStream in;
    private PrintStream out;
    private Conectavel conectavel;

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
            Thread r = new MonitoradorRemoto(in, cliente, this);
            r.run();
        } catch (Exception e) {
            System.out.println("Excessão na tread do run da conexao."+e.getMessage());
        }
    }

    public void executarRequisicao(Map mapa) throws Exception{
        conectavel.executarRequisicao(mapa);
    }



}