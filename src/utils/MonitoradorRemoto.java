package utils;

import entidades.Conectavel;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

public class MonitoradorRemoto extends Observable implements Runnable{

    private ObjectInputStream in;
    private Socket socket;

    public MonitoradorRemoto( ObjectInputStream in, Socket socket) {
        this.in = in;
        this.socket = socket;
    }

    public void run() {
        try {
            while (true) {
                in = new ObjectInputStream(socket.getInputStream());
                Map mapa = (HashMap)in.readObject();
                this.setChanged();
                this.notifyObservers(mapa);
            }
        } catch (Exception e) {
            System.out.println("Erro na thread de leitura");
        }
    }
}
