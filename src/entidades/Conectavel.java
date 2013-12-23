package entidades;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

import utils.Constantes;
import utils.TratatorRedundancia;
import utils.Utils;

public abstract class Conectavel extends Thread {

    protected GerenciadorArquivos gerenciadorArquivos = null;

    public abstract void iniciarServicos(String keystore,String enderecoPasta,String nome) throws Exception;

    public abstract void sinalizarAlteracaoLocal(Map<String, Object> requisicao) throws Exception;

    protected void iniciarMonitoramentoArquivos(String pastaMonitorada) {
        try {
            this.gerenciadorArquivos = new GerenciadorArquivos(this, pastaMonitorada);
            Thread threadGerenciadorArquivos = this.gerenciadorArquivos;
            threadGerenciadorArquivos.start();
        } catch (Exception e) {
            System.out.println("Falha ao iniciar o monitoramento de arquivos no servidor.Erro:" + e.getMessage());
        }
    }

    protected void escutarRequisicoes(Socket soc) {
        try {
            while (true) {
                ObjectInputStream mensagem = new ObjectInputStream(soc.getInputStream());
                Map mapa = (Map) mensagem.readObject();
                receberArquivo(mapa);
            }
        } catch (Exception e) {
            System.out.println("falha ao escutar cliente.Erro:" + e.getMessage());
        }
    }

    private void receberArquivo(Map mapa) {
        try {
            String evento = (String) mapa.get("evento");
            String enderecoArquivoRemoto = (String) mapa.get("endereco");
            File arquivo = new File(enderecoArquivoRemoto);
            if (evento.equals(Constantes.EXCLUIR)) {
                gerenciadorArquivos.deletarArquivoLocalmente(arquivo);
            }else if(evento.equals(Constantes.SALVAR)){
                FileInputStream conteudo = (FileInputStream)mapa.get("arquivo");
                gerenciadorArquivos.salvarArquivoLocalmente(conteudo,arquivo);
            }else if(evento.equals(Constantes.ATUALIZAR))  {
                File arquivoSalvar = (File)mapa.get("arquivo");
                FileInputStream conteudo = (FileInputStream)mapa.get("arquivo");
                gerenciadorArquivos.atualizarArquivoLocalmente(conteudo,arquivoSalvar);
            }
        } catch (Exception e) {
            System.out.println("Falha ao receber arquivo.Erro:" + e.getMessage());
        }
    }

    protected void iniciarTratadorRedundancia(){
        Thread t = new TratatorRedundancia(gerenciadorArquivos);
        t.start();
    }

    public void enviarRequisicao(Map<String, Object> requisicao,PrintStream out) throws Exception {
        byte[] array = Utils.getBytes(requisicao);
        out.write(array);
        out.flush();
    }
}
