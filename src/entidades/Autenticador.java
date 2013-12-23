package entidades;

import utils.ArquivoUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Autenticador {

    private static String endereco;

    public static void definirArquivoSenhas(String arquivo) {
        endereco = arquivo;
    }

    public static String autenticar(String loginESenha) {
        Map registrosExistentes = buscarRegistros(endereco);
        String hash = gerarMD5(loginESenha);
        Set<String> conjuntoChaves = registrosExistentes.keySet();
        for (String chave : conjuntoChaves) {
            if (chave.equals(hash)) {
                return (String) registrosExistentes.get(chave);
            }
        }
        return null;
    }

    public static Map buscarRegistros(String endereco) {
        List<String> stringsValores = ArquivoUtils.getLinhasArquivo(endereco);
        Map mapa = new HashMap();
        for (String s : stringsValores) {
            String[] valor = s.split("\\s");
            mapa.put(valor[1], valor[0]);
        }
        return mapa;
    }

    private static String gerarMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            BigInteger hash = new BigInteger(1, md.digest(senha.getBytes()));
            String crypto = hash.toString(16);
            if (crypto.length() % 2 != 0)
                crypto = "0" + crypto;
            return crypto;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("O algoritimo não existe." + e.getMessage());
        }
        return "";
    }

}
