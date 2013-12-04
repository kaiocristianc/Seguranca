
import Utils.Encriptador;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author kaio
 */
public class Teste {
    public static void main(String[] args) throws Exception {
        KeyGenerator k = KeyGenerator.getInstance("DES");
        SecretKey s =    k.generateKey();
        byte[] c = s.getEncoded();
        File f = new File("/home/kaio/git-1.8.1.2.tar.gz");
        byte[] bits = getBytes(f);
        System.out.println("Arquivo normal:"+bits);
        byte[] retornoEncriptado = Encriptador.encriptar(bits,c);
        System.out.println("Arquivo encriptado:"+retornoEncriptado);
        byte[] retornoDesencriptado = Encriptador.desencriptar(retornoEncriptado,c);
        System.out.println("Arquivo Desencriptado:"+retornoDesencriptado);
        System.out.println("Encriptado e Desencriptado iguais?"+(bits==retornoDesencriptado));
        criaFileNovamente(retornoDesencriptado);
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
}
