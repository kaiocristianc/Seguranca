package entidades;

import utils.Constantes;
import utils.Encriptador;

import java.io.File;
import java.util.Map;

public abstract class  Conectavel extends Thread{

	public Conectavel(){
		this.autenticador = new Autenticador();
		this.gerenciadorArquivos = new GerenciadorArquivos();
	}
	private GerenciadorArquivos gerenciadorArquivos;
	private Autenticador autenticador;

	public Autenticador getAutenticador(){
		return this.autenticador;
	}

	public GerenciadorArquivos getGerenciadorArquivos(){
		return this.gerenciadorArquivos;
	}

    public abstract void iniciarServicos() throws Exception;

    public abstract boolean enviarArquivo(Object arquivo);

    public abstract byte[] getChavePrivada() throws Exception;

	public abstract byte[] getChaveSimetricaParaCriptografia() throws Exception;

	public void executarRequisicao(Map<String,Object> requisicao)throws Exception{
		File arquivo = (File)requisicao.get("arquivo");
		String ordem = (String) requisicao.get("ordem");
		if(ordem.equals(Constantes.SALVAR))
			this.getGerenciadorArquivos().salvarArquivoLocalmente(arquivo);
		else if(ordem.equals(Constantes.ATUALIZAR))
			this.getGerenciadorArquivos().atualizarArquivoLocalmente(arquivo);
		else if(ordem.equals(Constantes.EXCLUIR))
			this.getGerenciadorArquivos().deletarArquivoLocalmente(arquivo);
	}
	//TODO modificar
    public byte[] getBytesParaEnvioSeguro(Object arquivo)throws Exception{
        return Encriptador.encriptar(arquivo,getChavePrivada());
    }
	//TODO modificar
	public byte[] tratarRequisicaoParaEnvio(File file,byte[] chaveParaEncriptar)throws Exception{
		byte[] arquivoEncriptado = Encriptador.encriptar(file,chaveParaEncriptar);
		return arquivoEncriptado;
	}
	//TODO modificar
	public byte[] tratarRequisicaoParaRecebimento(File file,byte[] chaveParaDesencriptar)throws Exception{
		//TODO modificar
//		byte[] arquivoEncriptado = Encriptador.desencriptar(file,chaveParaDesencriptar);
//		return arquivoEncriptado;
		return null;
	}
}
