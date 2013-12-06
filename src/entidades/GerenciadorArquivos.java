package entidades;

import utils.Constantes;
import utils.Monitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GerenciadorArquivos implements Observer {

	private Monitor monitor;
	private String enderecoPasta;
	private Conectavel conectavel;

	public void iniciarMonitoramento(Conectavel conectavel, String enderecoPasta) throws Exception {
		this.enderecoPasta = enderecoPasta;
//        this.conectavel = conectavel;
//        Path dir = Paths.get(enderecoPasta);
//        monitor = new Monitor(dir, true);
//        monitor.addObserver(this);
//        monitor.processEvents();
	}

	public void salvarArquivoRemotamente(Map<String, String> mapa) {
		System.out.println("Tentando salvar");
	}

	public void deletarArquivoRemotamente(Map<String, String> mapa) {
		System.out.println("Tentando deletar");
	}

	public void atualizarArquivoRemotamente(Map<String, String> mapa) {
		System.out.println("Tentando atualizar");
	}

	public void salvarArquivoLocalmente(Map mapa) throws Exception {
		File file = (File) mapa.get("arquivo");
		criarDiretorioLocalmente(file);


		System.out.println(file.isDirectory());
		System.out.println(file.isFile());

		System.out.println("Tentando salvar");
	}

	public void deletarArquivoLocalmente(Map<String, String> mapa) {
		System.out.println("Tentando deletar");
	}

	public void atualizarArquivoLocalmente(Map<String, String> mapa) {
		System.out.println("Tentando atualizar");
	}

	public void criarDiretorioLocalmente(File file) throws Exception{
		int finalEnderecoAtual = this.enderecoPasta.lastIndexOf('/');
		String nomePastaAtual = this.enderecoPasta.substring(finalEnderecoAtual, this.enderecoPasta.length());
		Pattern pattern = Pattern.compile("(?<=" + nomePastaAtual + "/)[a-zA-Z\\s/\\.]+");
		Matcher matcher = pattern.matcher(file.getAbsolutePath());
		matcher.find();
		String diretorioRemoto = matcher.group();
		String arquivoFinalLocal = this.enderecoPasta + "/" + diretorioRemoto;
		finalEnderecoAtual = arquivoFinalLocal.lastIndexOf("/");
		String pastaAtual = arquivoFinalLocal.substring(0, finalEnderecoAtual);
		File novaPasta = new File(pastaAtual);

		if (!novaPasta.exists())
			novaPasta.mkdirs();
		if (file.isFile()) {
			File origem = file;
			File destino = new File(arquivoFinalLocal);

// abrimos os streams para leitura/escrita
			FileInputStream fis = new FileInputStream(origem);
			FileOutputStream fos = new FileOutputStream(destino);

// Obtém os canais por onde lemos/escrevemos nos arquivos
			FileChannel inChannel = fis.getChannel();
			FileChannel outChannel = fos.getChannel();

// copia todos o conteúdo do canal de entrada para o canal de saída
			outChannel.transferFrom(inChannel, 0, inChannel.size());



		}
		if (file.isDirectory()) {
			new File(arquivoFinalLocal).mkdirs();
		}

	}

	@Override
	public void update(Observable observable, Object o) {
		Map<String, String> mapa = (HashMap) o;
		String evento = mapa.get("evento");
		if (evento.equals(Constantes.SALVAR))
			salvarArquivoRemotamente(mapa);
		else if (evento.equals(Constantes.ATUALIZAR))
			atualizarArquivoRemotamente(mapa);
		else if (evento.equals(Constantes.EXCLUIR))
			deletarArquivoRemotamente(mapa);
	}
}
