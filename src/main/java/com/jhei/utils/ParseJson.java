package com.jhei.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class ParseJson implements Runnable {

	@Override
	public void run() {
		System.out.println("Iniciado conversão" + getData());
		do {

			String[] task = ContolQueue.getTask();

			if (task != null) {
				String[] campos = task;
				JsonBrazil newObj = new JsonBrazil();
				setCamposJson(campos, newObj);
				ContolQueue.addTaskEscrever(new Gson().toJson(newObj));
			}

		} while (ContolQueue.isTerminated());
		ContolQueue.setTerminouConversao(false);
		System.out.println("Finalizado conversão." + getData());
	}
	
	private String getData() {
		return new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss.SSS z").format(new Date(System.currentTimeMillis()));
	}
	
	private void setCamposJson(String[] campos, JsonBrazil obj) {
		obj.setNumber(Long.parseLong(campos[0])); 
		obj.setGender(campos[1]);
		obj.setNameSet(campos[2]);
		obj.setTitle(campos[3]);
		obj.setGivenName(campos[4]);
		obj.setSurname(campos[5]);
		obj.setStreetAddress(campos[6]);
		obj.setCity(campos[7]);
		obj.setState(campos[8]);
		obj.setZipCode(campos[9]);
		obj.setCountryFull(campos[10]);
		obj.setEmailAddress(campos[11]);
		obj.setUsername(campos[12]);
		obj.setPassword(campos[13]);
		obj.setTelephoneNumber(campos[14]);
		obj.setBirthday(campos[15]);
		obj.setCCType(campos[16]);
		obj.setCCNumber(Long.parseLong(campos[17]));
		obj.setCVV2(Integer.parseInt(campos[18]));
		obj.setCCExpires(campos[19]);
		obj.setNationalID(campos[20]);
		obj.setColor(campos[21]);
		obj.setKilograms(Double.parseDouble(campos[22]));
		obj.setCentimeters(Integer.parseInt(campos[23]));
		obj.setGUID(campos[24]);
	}
}