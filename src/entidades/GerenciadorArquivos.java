package entidades;

import utils.ArquivoUtils;
import utils.MonitoradorLocal;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class GerenciadorArquivos implements Observer {

    private MonitoradorLocal monitor;
    private String enderecoPasta;
    private Conexao conexao;

    public void iniciarMonitoramento(Conexao conexao, String enderecoPasta) throws Exception {
        this.enderecoPasta = enderecoPasta;
        this.conexao = conexao;
        Path dir = Paths.get(enderecoPasta);
        monitor = new MonitoradorLocal(dir, true);
        monitor.addObserver(this);
        monitor.processEvents();
    }

    public void notificarAlteracaoParaOServidor(Map mapa) throws Exception {
        File file = new File((String)mapa.get("endereco"));
        mapa.put("endereco",file);
        conexao.executarRequisicao(mapa);
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
            notificarAlteracaoParaOServidor(mapa);
        } catch (Exception e) {
            System.out.println("Erro Notificando o servidor de alteração.Erro:"+e.getMessage());
        }
    }
}
