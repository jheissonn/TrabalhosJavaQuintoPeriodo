package com.jhei.trabalhoSocket1;

import java.io.Serializable;

public class SerializeiDocument implements Serializable{

private static final long serialVersionUID = 1L;
private String doc;

public String getDoc() {
    return doc;
}

public void setDoc( String doc ){
   this.doc = doc; 
}

}