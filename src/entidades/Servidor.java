package entidades;

public class Servidor extends Conectavel{

    @Override
    public void iniciarServicos() throws Exception{
        GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
        gerenciadorArquivos.iniciarMonitoramento(this,"/home/kaio/Downloads/pastaTeste");
    }
}
