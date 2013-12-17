package entidades;

import Monitoradores.MonitoradorLocal;
import utils.ArquivoUtils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorArquivos extends Thread implements Observer {

    private MonitoradorLocal monitor;
    private String enderecoPasta;
    private Conectavel conectavel;

    public GerenciadorArquivos(Conectavel conectavel, String enderecoPasta)throws Exception{
        this.enderecoPasta = enderecoPasta;
        this.conectavel = conectavel;
        Path dir = Paths.get(enderecoPasta);
        monitor = new MonitoradorLocal(dir, true);
        monitor.addObserver(this);
    }

    public void run(){
        monitor.processEvents();
    }

    public void notificarAlteracaoAoResponsavel(Map mapa) throws Exception {
        File file = new File((String)mapa.get("endereco"));
        mapa.put("endereco",file);
        conectavel.sinalizarAlteracaoLocal(mapa);
    }

    public void salvarArquivoLocalmente(File file) throws Exception {
        ArquivoUtils.criarArquivo(file, this.enderecoPasta);
    }

    public void deletarArquivoLocalmente(File file) {
        ArquivoUtils.deletarArquivo(file, this.enderecoPasta);
    }

    public void atualizarArquivoLocalmente(File file) throws Exception {
        ArquivoUtils.deletarArquivo(file, this.enderecoPasta);
        ArquivoUtils.criarArquivo(file, this.enderecoPasta);
    }

    @Override
    public void update(Observable observable, Object o) {
        Map<String, String> mapa = (HashMap) o;
        try {
            System.out.println("Notificando alteração");
            notificarAlteracaoAoResponsavel(mapa);
        } catch (Exception e) {
            System.out.println("Erro Notificando o servidor de alteração.Erro:"+e.getMessage());
        }
    }
}
