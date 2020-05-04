package com.jhei.trabalhoSocket1;

import java.io.Serializable;

public class PathProcesso implements Serializable {
	private static final long serialVersionUID = 4947022242080140283L;
	private String pathCsv;
	private String pathJson;
	private String status;
	
	public String getPathCsv() {
		return pathCsv;
	}
	public void setPathCsv(String pathCsv) {
		this.pathCsv = pathCsv;
	}
	public String getPathJson() {
		return pathJson;
	}
	public void setPathJson(String pathJson) {
		this.pathJson = pathJson;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String iniciado) {
		this.status = iniciado;
	}
		
}
