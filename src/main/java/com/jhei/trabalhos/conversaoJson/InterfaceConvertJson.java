package com.jhei.trabalhos.conversaoJson;

import org.apache.commons.csv.CSVRecord;

public interface InterfaceConvertJson {

	
	void addJson(String json);
    boolean emOperacao();
    CSVRecord getCsv();
	boolean isTerminatedConvert();
    
}
