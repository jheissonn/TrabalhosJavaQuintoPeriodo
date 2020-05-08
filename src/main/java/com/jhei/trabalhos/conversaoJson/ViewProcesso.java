package com.jhei.trabalhos.conversaoJson;

import java.io.Serializable;

public class ViewProcesso implements Serializable {
	private static final long serialVersionUID = -4615197110660260261L;
	private int qtdeRegistros;
	private int resgistrosLidos = 0;
	private int qdeRegistrosConvertidos = 0;
	private int qdeRegistrosEscritos = 0;
	private boolean continuaLituraCsv = true;
	private int aConverter = 0;
	private int aEscrever = 0;

	public synchronized boolean isTerminatedConvert() {
		return qdeRegistrosConvertidos != qtdeRegistros;
	}
	
	public synchronized boolean isTerminatedEscrever() {		
		return qdeRegistrosEscritos != qtdeRegistros;
	}
	
	public synchronized boolean isContinuaLeituraCsv() {
		return continuaLituraCsv;
	}

	public int getQtdeRegistros() {
		return qtdeRegistros;
	}

	public void setQtdeRegistros(int qtdeRegistros) {
		this.qtdeRegistros = qtdeRegistros;
	}

	public int getResgistrosLidos() {
		return resgistrosLidos;
	}

	public void addResgistrosLidos() {
		addAConverter();
		this.resgistrosLidos++;
	}

	public int getQdeRegistrosConvertidos() {
		return qdeRegistrosConvertidos;
	}

	public void addQdeRegistrosConvertidos() {
		addAEscrever();
		this.qdeRegistrosConvertidos++;
	}

	public int getQdeRegistrosEscritos() {
		return qdeRegistrosEscritos;
	}

	public void addQdeRegistrosEscritos() {
		this.qdeRegistrosEscritos++;
	}

	public void setContinuaLituraCsv(boolean continuaLituraCsv) {
		this.continuaLituraCsv = continuaLituraCsv;
	}
	
	public int getAConverter() {
		return aConverter;
	}
	
	public void addAConverter() {
		aConverter++;
	}
	public void removeAConverter() {
		aConverter--;
	}
	
	public int getAEscrever() {
		return aEscrever;
	}
	
	public void addAEscrever() {
		aEscrever++;
	}
	public void removeAEscrever() {
		aEscrever--;
	}

	@Override
	public String toString() {
		return "ViewProcesso [qtdeRegistros=" + qtdeRegistros + ", resgistrosLidos=" + resgistrosLidos
				+ ", qdeRegistrosConvertidos=" + qdeRegistrosConvertidos + ", qdeRegistrosEscritos="
				+ qdeRegistrosEscritos + ", continuaLituraCsv=" + continuaLituraCsv + ", aConverter=" + aConverter
				+ ", aEscrever=" + aEscrever + "]";
	}
	
	
}
