package utils;

import br.com.KaioLib.Util.Arquivos.EscritorUtil;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArquivoUtils {
    public static void criarArquivo(FileInputStream conteudo,File file, String pastaDiretorio) throws Exception {
        String pastaAtual = getLocalizacaoAtualArquivo(pastaDiretorio, file);
        String novoEnderecoArquivo = getNovoEnderecoArquivo(pastaAtual, file);
        File novaPasta = new File(pastaAtual);

        if (!novaPasta.exists())
            novaPasta.mkdirs();
        if (file.isFile()) {
            File destino = new File(novoEnderecoArquivo);
            copiarArquivo(conteudo, destino);
        }
        if (file.isDirectory()) {
            new File(novoEnderecoArquivo).mkdirs();
        }
    }

    public static void copiarArquivo(FileInputStream fis, File destino) throws Exception {
        FileOutputStream fos = new FileOutputStream(destino);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        outChannel.transferFrom(inChannel, 0, inChannel.size());
    }

    public static void deletarArquivo(File arquivo, String pasta) {
        String pastaAtual = getLocalizacaoAtualArquivo(pasta, arquivo);
        String enderecoAtualArquivo = getNovoEnderecoArquivo(pastaAtual, arquivo);
        new File(enderecoAtualArquivo).delete();
    }

    private static String getLocalizacaoAtualArquivo(String pastaDiretorio, File arquivo) {
        String tipoDiretorioLocal = pastaDiretorio.contains("servidor") ? "servidor" : "cliente";
        String tipoDiretorioRemoto = pastaDiretorio.contains("servidor") ? "cliente" : "servidor";
        try {
            Pattern padraoPastaLocal = Pattern.compile("[a-zA-Z\\s/\\.\\d]+(?=" + tipoDiretorioLocal + "/)");
            Pattern padraoPastaRemota = Pattern.compile("(?<=" + tipoDiretorioRemoto + "/)[a-zA-Z\\s/\\.\\d]+");

            System.out.println("Diretorio local:"+pastaDiretorio);
            System.out.println("Diretorio remoto:"+arquivo.getAbsolutePath());


            Matcher matcherPastaRemota = padraoPastaRemota.matcher(arquivo.getAbsolutePath());
            Matcher matcherPastaLocal = padraoPastaLocal.matcher(pastaDiretorio);
            matcherPastaRemota.find();
            matcherPastaLocal.find();
            String diretorioRemoto = matcherPastaRemota.group();
            diretorioRemoto = diretorioRemoto.substring(0,diretorioRemoto.lastIndexOf("/"));
            String diretorioLocal = matcherPastaLocal.group();
            diretorioLocal = diretorioLocal+tipoDiretorioLocal+"/"+diretorioRemoto+"/";
            return diretorioLocal;
        } catch (Exception e) {
            System.out.println("falha ao obter a localização atual do arquivo.Erro:" + e.getMessage());
        }
        return null;
    }

    private static String getUltimaPastaEmComum(String s1, String s2) {
        String[] v1 = s1.split("/");
        String[] v2 = s2.split("/");
        String baseEquivalente = null;
        int tamanhoString2 = v2.length - 2;
        for (int i = v1.length - 2; i >= 0; i--) {
            if (v1[i].equals(v2[tamanhoString2]) && !(v1[i - 1].equals(v2[i - 1]))) {
                baseEquivalente = v1[i];
                break;
            }
            tamanhoString2 = tamanhoString2 - 1;
        }
        return baseEquivalente;
    }

    private static String getNovoEnderecoArquivo(String novoDiretorio, File file) {
        return novoDiretorio + file.getName();
    }

    public static List getLinhasArquivo(String arquivo) {
        EscritorUtil escritor = new EscritorUtil();
        String texto = escritor.ler(arquivo);
        List lista = new ArrayList();
        Collections.addAll(lista, texto.split("\n"));
        return lista;
    }
}
