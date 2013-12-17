package entidades;

import java.io.PrintStream;
import java.util.Map;
import utils.Utils;

public abstract class Conectavel extends Thread {

    protected GerenciadorArquivos gerenciadorArquivos = null;

    public abstract void iniciarServicos() throws Exception;

    public abstract void sinalizarAlteracaoLocal(Map<String, Object> requisicao) throws Exception;

    public void enviarRequisicao(Map<String, Object> requisicao,PrintStream out) throws Exception {
        byte[] array = Utils.getBytes(requisicao);
        out.write(array);
        out.flush();
    }
}
