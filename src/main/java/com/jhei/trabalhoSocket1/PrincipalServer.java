package com.jhei.trabalhoSocket1;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.jhei.trabalhos.conversaoJson.ControlerDeConversao;
import com.jhei.trabalhos.conversaoJson.ViewProcesso;

public class PrincipalServer {
	

	public static void main(String[] args) throws IOException {
		ServerSocket server;
		Socket client = null;
		try{
		    server = new ServerSocket( 12345 );
		    client = server.accept();
		    ObjectInputStream ois = null;
		    new Atende(client).start();
		    }catch( Exception e ){
		    	System.out.println(e);
		    }
	}
}

class Atende extends Thread {

	private Socket cliente;

	public Atende(Socket cliente) {
		this.cliente = cliente;
	}

	@SuppressWarnings("null")
	@Override
	public void run() {
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		ControlerDeConversao controlConversao = null;
		PathProcesso pathPrincipal;
		try {
			ois = new ObjectInputStream(cliente.getInputStream());
			oos = new ObjectOutputStream(cliente.getOutputStream());

			pathPrincipal = (PathProcesso) ois.readObject();
			System.out.println(pathPrincipal.getStatus());
			if (pathPrincipal.getStatus().equals("Iniciado")) {
				controlConversao = new ControlerDeConversao(new File(pathPrincipal.getPathCsv()),
						new File(pathPrincipal.getPathJson()));
				controlConversao.iniciarProcesso();
				oos.writeObject(controlConversao.getViewProcesso());
			}
			do {
				 if (controlConversao.getViewProcesso().isTerminatedEscrever()) {
					 oos.flush();
					 ViewProcesso atual =  controlConversao.getViewProcesso();
				    System.out.println(atual.getResgistrosLidos() + ": Lidos if:");
					oos.writeObject(atual);
					
				} else {
					oos.flush();
					ViewProcesso atual =  controlConversao.getViewProcesso();
					System.out.println(atual.getResgistrosLidos() + ": Lidos:");
					oos.writeObject(atual);
					cliente.close();
				}
			} while (cliente.isConnected());

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	
	
}
