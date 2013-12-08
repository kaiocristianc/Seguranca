package entidades;

import utils.Constantes;

import java.io.File;
import java.io.PrintStream;
import java.util.Map;
import utils.Utils;

public abstract class Conectavel extends Thread {

    private GerenciadorArquivos gerenciadorArquivos;

    public Conectavel() {
        this.gerenciadorArquivos = new GerenciadorArquivos();
    }

    public GerenciadorArquivos getGerenciadorArquivos() {
        return this.gerenciadorArquivos;
    }

    public abstract void iniciarServicos() throws Exception;

    public void executarRequisicao(Map<String, Object> requisicao) throws Exception {
        File arquivo = (File) requisicao.get("arquivo");
        String ordem = (String) requisicao.get("evento");
        if (ordem.equals(Constantes.SALVAR))
            this.getGerenciadorArquivos().salvarArquivoLocalmente(arquivo);
        else if (ordem.equals(Constantes.ATUALIZAR))
            this.getGerenciadorArquivos().atualizarArquivoLocalmente(arquivo);
        else if (ordem.equals(Constantes.EXCLUIR))
            this.getGerenciadorArquivos().deletarArquivoLocalmente(arquivo);
    }

    public void enviarRequisicao(Map<String, Object> requisicao,PrintStream out) throws Exception {
        byte[] array = Utils.getBytes(requisicao);
        out.write(array);
        out.flush();
    }
}
