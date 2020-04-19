package com.jhei.trabalhos.conversaoJson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Vector;

import org.apache.commons.csv.CSVRecord;

public class ControlerDeConversao implements InterfaceCsv, InterfaceConvertJson,InterfaceWriterFile {
	
	private List<CSVRecord> filaCsv;
	private List<String> filaJson;
	private boolean continuaLituraCsv = true;
	private int qtdeRegistros;
	private int resgistrosLidos = 0;
	private int qdeRegistrosConvertidos = 0;
	private int qdeRegistrosEscritos = 0;
	private File arquivoCsv;
	private File arquivoJson;


	public ControlerDeConversao(File csv, File json) {
		qtdeRegistros = iniciarQdteTotal(csv);
		filaCsv = new Vector<>();
		filaJson = new Vector<>();
		this.arquivoCsv = csv;
		this.arquivoJson = json;
	}
	
	public void iniciarProcesso() {
		new Thread(new TrataCsv(this, arquivoCsv)).start();
		new Thread(new ConverterJson(this)).start();
		new Thread(new EscreverJson(arquivoJson, this)).start();
	}
	
	
	/* implementações InterfaceCsv */
	@Override
	public synchronized void addRegistro(CSVRecord gravarCsv) {
		filaCsv.add(gravarCsv);	
		resgistrosLidos++;
	}
	
	@Override
	public synchronized void setContinuaLeituraCsv(boolean terminou) {
		continuaLituraCsv = terminou;
	}

	
	/* implementações InterfaceWriterFile */
	
	@Override
	public synchronized String getObjToConvert() {
		if(!filaJson.isEmpty())
			return filaJson.remove(0);
		return null;
	}

	@Override
	public synchronized boolean isTerminatedEscrever() {		
		return qdeRegistrosEscritos != qtdeRegistros;
	}

	@Override
	public synchronized void addEscrito() {
		qdeRegistrosEscritos++;		
	}	
	
	/* implementações InterfaceConvertJson */
	
	@Override
	public synchronized void addJson(String json) {
		filaJson.add(json);
		qdeRegistrosConvertidos++;
	}
	
	@Override
	public synchronized boolean emOperacao() {
		return !filaCsv.isEmpty() || continuaLituraCsv;
	}
	
	@Override
	public synchronized CSVRecord getCsv() {
		if(!filaCsv.isEmpty())
			return filaCsv.remove(0);
		return null;
	}
	
	@Override
	public synchronized boolean isTerminatedConvert() {
		return qdeRegistrosConvertidos != qtdeRegistros;
	}
	
	
	/* métodos para controle fora da classe */
	
	public synchronized int getQtdeRegistros() {
		return qtdeRegistros;
	}
	
	public synchronized int getQdreRegistrosLidos() {
		return resgistrosLidos;
	}
	
	public synchronized int getQdreRegistrosConvertidos() {
		return qdeRegistrosConvertidos;
	}
	
	public synchronized boolean isContinuaLeituraCsv() {
		return continuaLituraCsv;
	}

	public synchronized int getQtdeEscritos() {
		return qdeRegistrosEscritos;
	}
	
	public synchronized int iniciarQdteTotal(File file) {
		try(LineNumberReader count = new LineNumberReader(new FileReader(file))){
			count.skip(Long.MAX_VALUE);
			return count.getLineNumber() - 1;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
