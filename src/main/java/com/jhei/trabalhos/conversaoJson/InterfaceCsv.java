package com.jhei.trabalhos.conversaoJson;

import org.apache.commons.csv.CSVRecord;

public interface InterfaceCsv {

	void addRegistro(CSVRecord gravarCsv);
	void setContinuaLeituraCsv(boolean terminou);	
}
