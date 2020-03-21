package com.jhei.utils;

public class TelaCaminho {

	private String identificacao;
	private String FXML;

	public TelaCaminho(String identificacao, String fXML) {
		super();
		this.identificacao = identificacao;
		FXML = fXML;
	}

	@Override
	public String toString() {
		return identificacao;
	}

	public String getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(String identificacao) {
		this.identificacao = identificacao;
	}

	public String getFXML() {
		return FXML;
	}

	public void setFXML(String fXML) {
		FXML = fXML;
	}
	
	
	
	
}
