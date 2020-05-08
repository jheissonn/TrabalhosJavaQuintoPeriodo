package com.jhei.trabalhos.conversaoJson;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.csv.CSVRecord;

public class ControlerDeConversao implements InterfaceCsv, InterfaceConvertJson,InterfaceWriterFile, Serializable {
	private static final long serialVersionUID = 1L;
	
	private ViewProcesso viewProcesso;
	private List<CSVRecord> filaCsv;
	private List<String> filaJson;
	private File arquivoCsv;
	private File arquivoJson;
	private ExecutorService pool;
	private Thread thLeitura;
	private Thread thConversao;
	private Thread thEscrita;
	private boolean jaAdiconadoConvert = false;
	private boolean jaAdiconadoEscrever = false;
	

	public ControlerDeConversao(File csv, File json) {
		pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		viewProcesso = new ViewProcesso();
		viewProcesso.setQtdeRegistros(iniciarQdteTotal(csv));
		filaCsv = new Vector<>();
		filaJson = new Vector<>();
		this.arquivoCsv = csv;
		this.arquivoJson = json;
		thLeitura = new Thread(new TrataCsv(this, arquivoCsv));
		thConversao = new Thread(new ConverterJson(this));
		thEscrita = new Thread(new EscreverJson(arquivoJson, this));
	}
	
	public void iniciarProcesso() {
		pool.execute(thLeitura);
		pool.execute(thConversao);
		pool.execute(thEscrita);
	}
	
	private void addNewThreadConvert() {
		if(!jaAdiconadoConvert) {
			new Thread(new ConverterJson(this)).start();
			jaAdiconadoConvert = true;
		}
	}
	
	private void addNewThreadEscrever() {
		if(!jaAdiconadoEscrever) {
			new Thread(new EscreverJson(arquivoJson, this)).start();
			jaAdiconadoEscrever = true;
		}
	}
	
	
	/* implementações InterfaceCsv */
	@Override
	public synchronized void addRegistro(CSVRecord gravarCsv) {
		filaCsv.add(gravarCsv);	
		viewProcesso.addResgistrosLidos();
		if (viewProcesso.getAConverter() > 99) {
			addNewThreadConvert();
		}
	}
	
	@Override
	public synchronized void setContinuaLeituraCsv(boolean terminou) {
		viewProcesso.setContinuaLituraCsv(terminou);
	}

	
	/* implementações InterfaceWriterFile */
	
	@Override
	public synchronized String getObjToConvert() {
		if(!filaJson.isEmpty()) {
			viewProcesso.removeAEscrever();
			return filaJson.remove(0);
		}
		return null;
	}

	@Override
	public synchronized boolean isTerminatedEscrever() {		
		return viewProcesso.isTerminatedEscrever();
	}

	@Override
	public synchronized void addEscrito() {
		viewProcesso.addQdeRegistrosEscritos();
	}	
	
	/* implementações InterfaceConvertJson */
	
	@Override
	public synchronized void addJson(String json) {
		filaJson.add(json);
		viewProcesso.addQdeRegistrosConvertidos();
		if(viewProcesso.getAEscrever() > 99) {
			addNewThreadEscrever();
		}
	}
	
	@Override
	public synchronized boolean emOperacao() {
		return !filaCsv.isEmpty() || viewProcesso.isContinuaLeituraCsv();
	}
	
	@Override
	public synchronized CSVRecord getCsv() {
		if(!filaCsv.isEmpty()) {
			viewProcesso.removeAConverter();
			return filaCsv.remove(0);
		}
		return null;
	}
	
	@Override
	public synchronized boolean isTerminatedConvert() {
		return viewProcesso.isTerminatedConvert();
	}
	
	
	/* métodos para controle fora da classe */
	
	public synchronized int getQtdeRegistros() {
		return viewProcesso.getQtdeRegistros();
	}
	
	public synchronized int getQdreRegistrosLidos() {
		return viewProcesso.getResgistrosLidos();
	}
	
	public synchronized int getQdreRegistrosConvertidos() {
		return viewProcesso.getQdeRegistrosConvertidos();
	}
	
	public synchronized boolean isContinuaLeituraCsv() {
		return viewProcesso.isContinuaLeituraCsv();
	}

	public synchronized int getQtdeEscritos() {
		return viewProcesso.getQdeRegistrosEscritos();
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
	
	public ViewProcesso getViewProcesso() {
		return this.viewProcesso;
	}
}
