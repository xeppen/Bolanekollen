package com.bolanekollen.util;

public class Result {
	String key = "";
	String result = "";
	boolean bold = false;
	
	public Result(String k, String r, boolean b){
		this.key = k;
		this.result = r;
		this.bold = b;
	}
	
	public String getKey() {
		return key;
	}
	public String getResult() {
		return result;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}
	
	
}
