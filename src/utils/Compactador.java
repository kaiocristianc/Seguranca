package utils;

import java.io.*;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Compactador {

    public static boolean comprimir(String endEntrada, String endSaida) {
        String dirInterno = "";
        boolean retorno = true;
        try {
            File file = new File(endEntrada);
            ZipOutputStream zipDestino = new ZipOutputStream(new FileOutputStream(endSaida));
            if (file.isFile()) {
                ziparFile(file, dirInterno, zipDestino);
            } else {
                dirInterno = file.getName();
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {

                    ziparFile(files[i], dirInterno, zipDestino);

                }
            }
            zipDestino.close();

        } catch (IOException ex) {
            System.out.println("falhou3");
        }

        return retorno;
    }

    public static void descomprimir(File zipFile, File dir) throws IOException {
        ZipFile zip = null;
        File arquivo = null;
        InputStream is = null;
        OutputStream os = null;
        byte[] buffer = new byte[1024];

        try {
            // cria diretório informado, caso não exista
            if (!dir.exists()) {
                dir.mkdirs();
            }
            if (!dir.exists() || !dir.isDirectory()) {
                throw new IOException("O diretório " + dir.getName() +
                        " não é um diretório válido");
            }

            zip = new ZipFile(zipFile);
            Enumeration e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entrada = (ZipEntry) e.nextElement();
                arquivo = new File(dir, entrada.getName());
                if (entrada.isDirectory() && !arquivo.exists()) {
                    arquivo.mkdirs();
                    continue;
                }
                if (!arquivo.getParentFile().exists()) {
                    arquivo.getParentFile().mkdirs();
                }
                try {
                    is = zip.getInputStream(entrada);
                    os = new FileOutputStream(arquivo);
                    int bytesLidos = 0;
                    if (is == null) {
                        throw new ZipException("Erro ao ler a entrada do zip: " +
                                entrada.getName());
                    }
                    while ((bytesLidos = is.read(buffer)) > 0) {
                        os.write(buffer, 0, bytesLidos);
                    }
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (Exception ex) {
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static void ziparFile(File file, String dirInterno, ZipOutputStream zipDestino) throws IOException {

        byte data[] = new byte[1024];

        //Verifica se a file é um diretório, então faz a recursão
        if (file.isDirectory()) {


            File[] files = file.listFiles();


            for (int i = 0; i < files.length; i++) {

                ziparFile(files[i], dirInterno + File.separator + file.getName(), zipDestino);

            }


            return;

        }


        FileInputStream fi = new FileInputStream(file.getAbsolutePath());
        ZipEntry entry = new ZipEntry(dirInterno + File.separator + file.getName());
        zipDestino.putNextEntry(entry);
        int count;
        while ((count = fi.read(data)) > 0) {
            zipDestino.write(data, 0, count);
        }
        zipDestino.closeEntry();
        fi.close();

    }
}
