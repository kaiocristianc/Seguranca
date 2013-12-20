package Test;

import utils.ArquivoUtils;

import java.io.File;
public class ArquivoUtilsTeste {

    public static void main(String[] args) {
        File f2 = new File("/media/EntregaSeguranca/servidor/marcos");
        File f1 = new File("/media/EntregaSeguranca/cliente/marcos/Arquivo");

        try {
            ArquivoUtils.criarArquivo(f1, f2.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Falha:" + e.getMessage());
        }

    }

}
