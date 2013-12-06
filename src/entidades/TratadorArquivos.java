package entidades;

import utils.Encriptador;

import java.io.File;

public class TratadorArquivos {

    public byte[] tratarArquivoParaEnvio(File file,byte[] chaveParaEncriptar)throws Exception{
        byte[] arquivoEncriptado = Encriptador.encriptar(file,chaveParaEncriptar);
        return arquivoEncriptado;
    }

    public byte[] tratarArquivoParaRecebimento(File file,byte[] chaveParaDesencriptar)throws Exception{
        byte[] arquivoEncriptado = Encriptador.desencriptar(file,chaveParaDesencriptar);
        return arquivoEncriptado;
    }
}
