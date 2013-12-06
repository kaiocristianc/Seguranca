package entidades;

import utils.ArquivoUtils;
import utils.Constantes;
import utils.Monitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GerenciadorArquivos implements Observer {

    private Monitor monitor;
    private String enderecoPasta;
    private Conectavel conectavel;

    public void iniciarMonitoramento(Conectavel conectavel, String enderecoPasta) throws Exception {
        this.enderecoPasta = enderecoPasta;
//        this.conectavel = conectavel;
//        Path dir = Paths.get(enderecoPasta);
//        monitor = new Monitor(dir, true);
//        monitor.addObserver(this);
//        monitor.processEvents();
    }

    public void salvarArquivoRemotamente(Map<String, String> mapa) {
        System.out.println("Tentando salvar");
    }

    public void deletarArquivoRemotamente(Map<String, String> mapa) {
        System.out.println("Tentando deletar");
    }

    public void atualizarArquivoRemotamente(Map<String, String> mapa) {
        System.out.println("Tentando atualizar");
    }

    public void salvarArquivoLocalmente(Map mapa) throws Exception {
        File file = (File) mapa.get("arquivo");
        ArquivoUtils.criarArquivo(file, this.enderecoPasta);
    }

    public void deletarArquivoLocalmente(Map mapa) {
        File file = (File) mapa.get("arquivo");
        ArquivoUtils.deletarArquivo(file, this.enderecoPasta);
    }

    public void atualizarArquivoLocalmente(Map mapa) throws Exception {
        File file = (File) mapa.get("arquivo");
        ArquivoUtils.deletarArquivo(file, this.enderecoPasta);
        ArquivoUtils.criarArquivo(file, this.enderecoPasta);
    }

    @Override
    public void update(Observable observable, Object o) {
        Map<String, String> mapa = (HashMap) o;
        String evento = mapa.get("evento");
        if (evento.equals(Constantes.SALVAR))
            salvarArquivoRemotamente(mapa);
        else if (evento.equals(Constantes.ATUALIZAR))
            atualizarArquivoRemotamente(mapa);
        else if (evento.equals(Constantes.EXCLUIR))
            deletarArquivoRemotamente(mapa);
    }
}
