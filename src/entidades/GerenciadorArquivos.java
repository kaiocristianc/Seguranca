package entidades;

import Monitoradores.MonitoradorLocal;
import utils.ArquivoUtils;
import utils.Constantes;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GerenciadorArquivos extends Thread implements Observer {

    private MonitoradorLocal monitor;
    private String enderecoPasta;
    private Conectavel conectavel;
    private String arquivoTravado = "";

    public void destravarArquivo() {
        this.arquivoTravado = "";
    }


    public GerenciadorArquivos(Conectavel conectavel, String enderecoPasta) throws Exception {
        this.enderecoPasta = enderecoPasta;
        this.conectavel = conectavel;
        tratarExistenciaCliente(enderecoPasta);
        Path dir = Paths.get(enderecoPasta);
        monitor = new MonitoradorLocal(dir, true);
        monitor.addObserver(this);
    }

    private void tratarExistenciaCliente(String pasta) {
        File file = new File(pasta);
        if (!file.exists())
            file.mkdirs();
    }

    public void run() {
        monitor.processEvents();
    }

    public void notificarAlteracaoAoResponsavel(Map mapa) throws Exception {
        String acao = (String) mapa.get("evento");
        if (!acao.equals(Constantes.EXCLUIR)) {
            File file = new File((String) mapa.get("endereco"));
            mapa.remove("endereco");
            mapa.put("arquivo", file);
        }
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
    public void update(Observable observable, Object mapaArquivo) {
        Map<String, String> mapa = (HashMap) mapaArquivo;
        try {
            String nomeArquivo = mapa.get("endereco");
            if (!arquivoTravado.equals(nomeArquivo)) {
                arquivoTravado = nomeArquivo;
                notificarAlteracaoAoResponsavel(mapa);
            }
        } catch (Exception e) {
            System.out.println("Erro Notificando o servidor de alteração.Erro:" + e.getMessage());
        }
    }
}
