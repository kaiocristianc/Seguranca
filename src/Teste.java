import entidades.Conectavel;
import entidades.Servidor;

public class Teste {
    public static void main(String args[]) throws Exception{
        Conectavel servidor = new Servidor();
        servidor.iniciarServicos();
    }
}
