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
		gerenciadorArquivos.iniciarMonitoramento(servidor,"/home/zeroglosa/Downloads/pastaTeste");
		File file = new File("/home/zeroglosa/Documentos/pastaTeste/popo/pepe/pipi/Seguranca-master.zip");
		Map mapa = new HashMap();
		mapa.put("arquivo",file);
		gerenciadorArquivos.salvarArquivoLocalmente(mapa);
    }
}
