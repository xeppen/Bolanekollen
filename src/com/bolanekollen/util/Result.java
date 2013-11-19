package com.bolanekollen.util;

public class Result {
	String key = "";
	String result = "";
	
	public Result(String k, String r){
		this.key = k;
		this.result = r;
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
	
	
}
