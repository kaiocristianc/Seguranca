package entidades;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Servidor extends Conectavel {

	private Socket conexao = null;

	public Servidor(Socket conexao) {
		this.conexao = conexao;
	}

	public static void main(String[] args) throws IOException {
		ServerSocket servidor = new ServerSocket(12345);
		System.out.println("Servidor iniciado!");
		while (true) {
			Socket cliente = servidor.accept();
			Thread thread = new Servidor(cliente);
			thread.start();
		}
	}

	public void run() {
		System.out.println("Nova conexão com o cliente " + conexao.getInetAddress().getHostAddress());
		try {
			ObjectInputStream entrada = new ObjectInputStream(this.conexao.getInputStream());
			PrintStream saida = new PrintStream(this.conexao.getOutputStream());
			Map requisicao = (HashMap) entrada.readObject();
			if (autenticarUsuario(requisicao)) {
				requisicao = tratarRequisicaoParaLeitura((byte[])requisicao.get("requisicao"));
				executarRequisicao(requisicao);
			}
		} catch (Exception e) {
			System.out.println("Erro na Thread." + e.getMessage());
		}
	}

	private boolean autenticarUsuario(Map requisicao) {
		return this.getAutenticador().autenticarUsuario((byte[]) requisicao.get("cabecalho"));
	}

	@Override
	public void iniciarServicos() throws Exception {
		this.getGerenciadorArquivos().iniciarMonitoramento(this, "/home/zeroglosa/Downloads/pastaTeste");
	}

	@Override
	public boolean enviarArquivo(Object arquivo) {
		//TODO modificar
		return false;
	}

	@Override
	public byte[] getChavePrivada() throws Exception {
		//TODO modificar
		KeyGenerator k = KeyGenerator.getInstance("DES");
		SecretKey s = k.generateKey();
		return s.getEncoded();
	}

	@Override
	public byte[] getChaveSimetricaParaCriptografia() throws Exception {
		//TODO modificar
		KeyGenerator k = KeyGenerator.getInstance("DES");
		SecretKey s = k.generateKey();
		return null;
	}
}
