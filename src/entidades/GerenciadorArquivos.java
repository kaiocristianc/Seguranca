package entidades;

import utils.Constantes;
import utils.Monitor;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GerenciadorArquivos implements Observer{

    private Monitor monitor;
    private String enderecoPasta;
    private Conectavel conectavel;

    public void iniciarMonitoramento(Conectavel conectavel,String enderecoPasta) throws Exception {
        this.enderecoPasta = enderecoPasta;
        this.conectavel = conectavel;
        Path dir = Paths.get(enderecoPasta);
        monitor = new Monitor(dir, true);
        monitor.addObserver(this);
        monitor.processEvents();
    }

    public void salvarArquivoNoSistema(Map<String, String> mapa){
        System.out.println("Tentando salvar");
    }

    public void deletarArquivoNoSistema(Map<String, String> mapa){
        System.out.println("Tentando deletar");
    }

    public void atualizarArquivoNoSistema(Map<String, String> mapa){
        System.out.println("Tentando atualizar");
    }

    @Override
    public void update(Observable observable, Object o) {
        Map<String,String> mapa = (HashMap) o;
        String evento = mapa.get("evento");
        if(evento.equals(Constantes.SALVAR))
            salvarArquivoNoSistema(mapa);
        else if(evento.equals(Constantes.ATUALIZAR))
            atualizarArquivoNoSistema(mapa);
        else if(evento.equals(Constantes.EXCLUIR))
            deletarArquivoNoSistema(mapa);
}
}
