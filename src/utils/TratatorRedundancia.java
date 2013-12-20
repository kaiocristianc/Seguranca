package utils;

import entidades.GerenciadorArquivos;

public class TratatorRedundancia extends Thread {

    private GerenciadorArquivos gerenciadorArquivos;

    public TratatorRedundancia(GerenciadorArquivos gerenciadorArquivos) {
        this.gerenciadorArquivos = gerenciadorArquivos;
    }

    public void run() {
        System.out.println("Iniciando gerenciador de redundancia");
        while (true) {
            try {
                Thread.sleep(3000);
                this.gerenciadorArquivos.destravarArquivo();
            } catch (Exception e) {
            }
        }

    }
}
