package utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArquivoUtils {
    public static void criarArquivo(File file, String pastaDiretorio) throws Exception {
        String pastaAtual = getLocalizacaoAtualArquivo(pastaDiretorio, file);
        String novoEnderecoArquivo = getNovoEnderecoArquivo(pastaAtual, file);
        File novaPasta = new File(pastaAtual);

        if (!novaPasta.exists())
            novaPasta.mkdirs();
        if (file.isFile()) {
            File destino = new File(novoEnderecoArquivo);
            copiarArquivo(file, destino);
        }
        if (file.isDirectory()) {
            new File(novoEnderecoArquivo).mkdirs();
        }
    }

    public static void copiarArquivo(File origem, File destino) throws Exception {
        FileInputStream fis = new FileInputStream(origem);
        FileOutputStream fos = new FileOutputStream(destino);
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        outChannel.transferFrom(inChannel, 0, inChannel.size());
    }

    public static void deletarArquivo(File arquivo, String pasta) {
        String pastaAtual = getLocalizacaoAtualArquivo(pasta, arquivo);
        new File(getNovoEnderecoArquivo(pastaAtual, arquivo)).delete();
    }

    private static String getLocalizacaoAtualArquivo(String pastaDiretorio, File arquivo) {
        int indexFimEndereco = pastaDiretorio.lastIndexOf('/');
        String nomePastaAtual = pastaDiretorio.substring(indexFimEndereco, pastaDiretorio.length());
        Pattern pattern = Pattern.compile("(?<=" + nomePastaAtual + "/)[a-zA-Z\\s/\\.]+");
        Matcher matcher = pattern.matcher(arquivo.getAbsolutePath());
        matcher.find();
        String diretorioRemoto = matcher.group();
        String arquivoFinalLocal = pastaDiretorio + "/" + diretorioRemoto;
        indexFimEndereco = arquivoFinalLocal.lastIndexOf("/");
        return arquivoFinalLocal.substring(0, indexFimEndereco);
    }

    private static String getNovoEnderecoArquivo(String novoDiretorio, File file) {
        return novoDiretorio + "/" + file.getName();
    }
}
