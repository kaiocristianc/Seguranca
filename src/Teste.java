import entidades.Conectavel;
import entidades.GerenciadorArquivos;
import entidades.Servidor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Teste {
    public static void main(String args[]) throws Exception{
        Conectavel servidor = new Servidor();
        servidor.iniciarServicos();
		GerenciadorArquivos gerenciadorArquivos = new GerenciadorArquivos();
		gerenciadorArquivos.iniciarMonitoramento(servidor,"/home/kaio/Downloads/pastaTeste");
		File file = new File("/home/kaio/Documents/pastaTeste/papa/CampoMinado.tar.gz");
		Map mapa = new HashMap();
		mapa.put("arquivo",file);
		gerenciadorArquivos.atualizarArquivoLocalmente(mapa);
    }
}
