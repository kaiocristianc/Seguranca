package entidades;

import utils.Constantes;

import java.io.File;
import java.util.Map;

public abstract class Conectavel extends Thread {

    private GerenciadorArquivos gerenciadorArquivos;

    public Conectavel() {
        this.gerenciadorArquivos = new GerenciadorArquivos();
    }

    public GerenciadorArquivos getGerenciadorArquivos() {
        return this.gerenciadorArquivos;
    }

    public abstract void iniciarServicos() throws Exception;

    public void executarRequisicaoServidor(Map<String, Object> requisicao) throws Exception {
        File arquivo = (File) requisicao.get("arquivo");
        String ordem = (String) requisicao.get("ordem");
        if (ordem.equals(Constantes.SALVAR))
            this.getGerenciadorArquivos().salvarArquivoLocalmente(arquivo);
        else if (ordem.equals(Constantes.ATUALIZAR))
            this.getGerenciadorArquivos().atualizarArquivoLocalmente(arquivo);
        else if (ordem.equals(Constantes.EXCLUIR))
            this.getGerenciadorArquivos().deletarArquivoLocalmente(arquivo);
    }

    public abstract void enviarRequisicao(Map<String, Object> requisicao) throws Exception;


}
