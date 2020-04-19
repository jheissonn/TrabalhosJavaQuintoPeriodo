package com.jhei.trabalhos.conversaoJson;

import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;
import com.jhei.utils.JsonBrazil;

public class ConverterJson implements Runnable{

	private InterfaceConvertJson interfaceControler;
	
	public ConverterJson(InterfaceConvertJson inJson) {
		this.interfaceControler = inJson;
	}	
	 
	@Override
	public void run() {
		do {
			CSVRecord csvRecord = interfaceControler.getCsv();
			JsonBrazil newObj = new JsonBrazil();
			if (csvRecord != null) {
				setCamposJson(csvRecord, newObj);
				interfaceControler.addJson(new Gson().toJson(newObj));
			}
		}while(interfaceControler.isTerminatedConvert());
	}
	
	private void setCamposJson(CSVRecord campos, JsonBrazil obj) {
		obj.setNumber(Long.parseLong(campos.get(0))); 
		obj.setGender(campos.get(1));
		obj.setNameSet(campos.get(2));
		obj.setTitle(campos.get(3));
		obj.setGivenName(campos.get(4));
		obj.setSurname(campos.get(5));
		obj.setStreetAddress(campos.get(6));
		obj.setCity(campos.get(7));
		obj.setState(campos.get(8));
		obj.setZipCode(campos.get(9));
		obj.setCountryFull(campos.get(10));
		obj.setEmailAddress(campos.get(11));
		obj.setUsername(campos.get(12));
		obj.setPassword(campos.get(13));
		obj.setTelephoneNumber(campos.get(14));
		obj.setBirthday(campos.get(15));
		obj.setCCType(campos.get(16));
		obj.setCCNumber(Long.parseLong(campos.get(17)));
		obj.setCVV2(Integer.parseInt(campos.get(18)));
		obj.setCCExpires(campos.get(19));
		obj.setNationalID(campos.get(20));
		obj.setColor(campos.get(21));
		obj.setKilograms(Double.parseDouble(campos.get(22)));
		obj.setCentimeters(Integer.parseInt(campos.get(23)));
		obj.setGUID(campos.get(24));
	}
}
