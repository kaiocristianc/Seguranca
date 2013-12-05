package entidades;

import utils.Encriptador;
import utils.Monitor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GerenciadorArquivos implements Observer{

    Servidor servidor;
    private Monitor monitor;
    private Encriptador encriptador;
    private String enderecoPasta = "/home/kaio/Downloads/pastaTeste";

    public void iniciarMonitoramento() throws Exception {
        Path dir = Paths.get(enderecoPasta);
        monitor = new Monitor(dir, true);
        servidor = new Servidor();
        monitor.addObserver(this);
        monitor.processEvents();
    }

    @Override
    public void update(Observable observable, Object o) {
        Map mapa = (HashMap)o;
        System.out.println(o);
    }
}
