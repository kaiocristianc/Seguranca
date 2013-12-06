package entidades;

public class Servidor extends Conectavel{

    @Override
    public void iniciarServicos() throws Exception{
        GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
        gerenciadorArquivos.iniciarMonitoramento(this,"/home/zeroglosa/Downloads/pastaTeste");
    }

	@Override
	public boolean enviarArquivo(Object arquivo) {
		return false;
	}

	@Override
	public byte[] getChavePrivada() {
		return new byte[0];
	}
}
