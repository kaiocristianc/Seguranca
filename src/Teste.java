import java.io.*;

public class Teste {

	public static void main(String[] args) throws Exception {
//		KeyGenerator k = KeyGenerator.getInstance("DES");
//		SecretKey s = k.generateKey();
//		byte[] chaveSimetrica = s.getEncoded();
//
//		File file = new File("/home/zeroglosa/Documentos/pastaTeste/manoveio/grails-in-action.pdf");
//		String ordem = Constantes.SALVAR;
//		Map requisicao = new HashMap();
//		requisicao.put("arquivo",file);
//		requisicao.put("ordem",ordem);
//		byte[] requisicaoCriptografada = Encriptador.encriptar(requisicao,chaveSimetrica);
//
//
//		Servidor servidor = new Servidor(null);
//		servidor.chaveSimetrica = chaveSimetrica;
//		servidor.iniciarServicos();
//		Map requisicaoTratada = servidor.tratarRequisicaoParaLeitura(requisicaoCriptografada);
//		servidor.executarRequisicaoServidor(requisicaoTratada);
//
////		byte[] c = s.getEncoded();
//		File f = new File("/home/kaio/git1.8.1.2.tar.gz");
//		byte[] retornoEncriptado = Encriptador.encriptar(f, c);
//		byte[] retornoDesencriptado = Encriptador.desencriptar(retornoEncriptado, c);
	}

	public static byte[] getBytes(File file) {
		int len = (int) file.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
			inFile.read(sendBuf, 0, len);

		} catch (FileNotFoundException fnfex) {
		} catch (IOException ioex) {
		}
		return sendBuf;
	}

	public static void criaFileNovamente(byte[] arquivoDesencriptado) throws Exception {
		File sourceFile = new File("/home/kaio/Desktop/teste.tar.gz");
		FileOutputStream file = new FileOutputStream(sourceFile);
		BufferedOutputStream output = new BufferedOutputStream(file);
		output.flush();
		output.write(arquivoDesencriptado, 0, arquivoDesencriptado.length);
		output.flush();
		output.close();
	}

//    public static void main(String args[]) throws Exception{
//        Conectavel servidor = new Servidor();
//        servidor.iniciarServicos();
//		GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
//		gerenciadorArquivos.iniciarMonitoramento(servidor,"/home/kaio/Downloads/pastaTeste");
//		File file = new File("/home/kaio/Documents/pastaTeste/teste/nP_657PREVIA");
//        File[] files = new File[1];
//        files[0] = file;
//        File saida = new File("/home/kaio/Downloads/pastaTeste/teste/nP_657PREVIA.zip");
//        Compactador.comprimir(file.getAbsolutePath(), saida.getAbsolutePath());
//		Map mapa = new HashMap();
//		mapa.put("arquivo", file);
//        gerenciadorArquivos.atualizarArquivoLocalmente(mapa);
//        Compactador.descomprimir(saida,new File("/home/kaio/Downloads/pastaTeste/teste"));
//    }
//    public static void main(String args[]) throws Exception{
//        Conectavel servidor = new Servidor();
//        servidor.iniciarServicos();
//		GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
//		gerenciadorArquivos.iniciarMonitoramento(servidor,"/home/kaio/Downloads/pastaTeste");
//		File file = new File("/home/kaio/Documents/pastaTeste/teste/nP_657PREVIA");
//        File[] files = new File[1];
//        files[0] = file;
//        File saida = new File("/home/kaio/Downloads/pastaTeste/teste/nP_657PREVIA.zip");
//        Compactador.comprimir(file.getAbsolutePath(), saida.getAbsolutePath());
//		Map mapa = new HashMap();
//		mapa.put("arquivo", file);
//        gerenciadorArquivos.atualizarArquivoLocalmente(mapa);
//        Compactador.descomprimir(saida,new File("/home/kaio/Downloads/pastaTeste/teste"));
//    }
	//public static void main(String args[]) throws Exception{
//        Conectavel servidor = new Servidor();
//        servidor.iniciarServicos();
//		GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
//		gerenciadorArquivos.iniciarMonitoramento(servidor,"/home/kaio/Downloads/pastaTeste");
//		File file = new File("/home/kaio/Documents/pastaTeste/papa/CampoMinado.tar.gz");
//		Map mapa = new HashMap();
//		mapa.put("arquivo",file);
//		gerenciadorArquivos.atualizarArquivoLocalmente(mapa);
//    }
}
