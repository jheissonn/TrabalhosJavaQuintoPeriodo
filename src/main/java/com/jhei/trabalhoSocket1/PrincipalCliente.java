package com.jhei.trabalhoSocket1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class PrincipalCliente {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = null;

		ObjectOutputStream oos = null;
		ObjectInputStream input = null;
		SerializeiDocument serialDoc = new SerializeiDocument();
		serialDoc.setDoc("Teste");
		try{
		    client = new Socket("127.0.0.1", 12345);
		    oos = new ObjectOutputStream( client.getOutputStream() );
		    input = new ObjectInputStream (client.getInputStream());
		    //Supondo que serialDoc j· esteja setado...
		    oos.writeObject( serialDoc );
		    oos.flush();
		    serialDoc = (SerializeiDocument) input.readObject();
		    System.out.println(serialDoc.getDoc());
		    
		    }catch( Exception e ){
		    	System.out.println(e);
		    }
		    finally{ 
		        oos.close(); 
		        client.close();
		    }

	}

	
	public void testeAnterior() throws IOException {
	Scanner teclado = new Scanner(System.in);
		
		SerializeiDocument serialDoc = new SerializeiDocument();
		serialDoc.setDoc("Primeiro Teste");
//		Solicita uma conex√£o
		Socket cliente = new Socket("127.0.0.1", 12345);
		ObjectOutputStream oos = new ObjectOutputStream( cliente.getOutputStream() );
//		Cria um canal de envio
		//PrintStream saida = new PrintStream(cliente.getOutputStream());
        //String enviar = null;
        
        oos.writeObject( serialDoc );
        
        
		System.out.println("Conex„o encerrada");
	}
}
