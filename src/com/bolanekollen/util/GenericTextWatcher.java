package com.bolanekollen.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher{

	private EditText mEditText;
    public GenericTextWatcher(EditText e) {
        this.mEditText = e;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
	public void onTextChanged(CharSequence s, int start, int before,
			int count) {

		mEditText.removeTextChangedListener(this);
		Integer d = 0;
		try {
			String p = s.toString().replaceAll("\\s+","");
			d = Integer.parseInt(p);
		} catch (NumberFormatException e) {
			d = 0;
		}
		mEditText.setText(prettifyString(d));
		Log.i("REACHES ON", "YES");
		mEditText.addTextChangedListener(this);
		mEditText.setSelection(mEditText.getText().length());
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}
	
	private static String prettifyString(Integer i) {

		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
		customSymbols.setGroupingSeparator(' ');
		format.setDecimalFormatSymbols(customSymbols);
		String s = format.format(i);
		return s;
	}
	
	private static String prettifyString2(String s){
		return prettifyString(Integer.valueOf(s));
	}
}
