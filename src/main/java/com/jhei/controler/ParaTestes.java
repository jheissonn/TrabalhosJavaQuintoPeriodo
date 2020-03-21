package com.jhei.controler;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.application.Application;

public class ParaTestes {
	 public static void main(String[] args) {
		 
		 
		 
		 System.out.println(new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z").format(new Date(System.currentTimeMillis())));
	    }
}
