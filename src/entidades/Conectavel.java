package entidades;

import utils.Encriptador;

public abstract class  Conectavel {

    public abstract void iniciarServicos() throws Exception;

    public abstract boolean enviarArquivo(Object arquivo);

    public abstract byte[] getChavePrivada();

    public byte[] getBytesParaEnvioSeguro(Object arquivo)throws Exception{
        return Encriptador.encriptar(arquivo,getChavePrivada());
    }
}
