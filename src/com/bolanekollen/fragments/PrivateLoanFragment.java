package com.javatechig.drawer;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InterestFragment extends Fragment {

	// Define all fields
	EditText loanEditText;
	EditText interestEditText;
	SeekBar repayTimeSeekBar;
	TextView repayTextView;
	Spinner amortSpinner;

	static double 		interest 			= 0.001;
	static double 		loanAmount 			= 0;
	static Integer 		paybackTime 		= 1;
	static String 		interestType 		= "Rak amortering";
	static double 		totalInterestCost 	= 0;
	static double		monthlyPayback 		= 0;
	static double 		effectiveInterest	= 0;
	static double[]		totalMonthlyCost	= new double[]{0,0,0,0,0,0,0,0,0,0,0,0};
	static double[]		monthlyInterest 	= new double[]{0,0,0,0,0,0,0,0,0,0,0,0};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_interest_layout, container,
				false);

		// Updating the action bar title
		getActivity().getActionBar().setTitle(menus[position]);

		// Assign all elements
		loanEditText = (EditText) v.findViewById(R.id.loanEditText);
		interestEditText = (EditText) v.findViewById(R.id.interestEditText);
		repayTimeSeekBar = (SeekBar) v.findViewById(R.id.repayTimeSeekBar);
		repayTextView = (TextView) v.findViewById(R.id.repayTextView);
		amortSpinner = (Spinner) v.findViewById(R.id.amortSpinner);
		
		addKeyListeners();
		repayTimeSeekBar.setOnSeekBarChangeListener(repayTimeSeekBarListener);
		return v;
	}

	protected void addKeyListeners(){
		
		// Add key listener to keep track of user input
		loanEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 
				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
					&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					
					if(isNumeric(loanEditText.getText().toString())){
						loanAmount = Integer.parseInt(loanEditText.getText().toString());
						Log.d("Seb", "interestEditText.getText().toString(): " + interestEditText.getText().toString());
						if(interestEditText.getText().toString() == "")
							interestEditText.setText("0.1"); 
						calculate();
					}
					return true;
				}		 
				return false;
			}
		 });
		
		interestEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				 
				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
					&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
		 
					if(isNumeric(interestEditText.getText().toString())){
						String st = interestEditText.getText().toString();
						interest = Double.parseDouble(st) / 100;
						
						
						
						Log.d("Seb", "st: " + Double.parseDouble(st));
						Log.d("Seb", "interest: " + interest);
						calculate();
					}
					return true;
				}		 
				return false;
			}
		 });
	}
	
	private OnSeekBarChangeListener repayTimeSeekBarListener = new OnSeekBarChangeListener(){
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
			if(repayTimeSeekBar.getProgress() == 0){
				repayTimeSeekBar.setProgress(1);
			}
			paybackTime = (repayTimeSeekBar.getProgress());
			TextView repayTextView = (TextView) getActivity().findViewById(R.id.repayTextView);
			repayTextView.setText(paybackTime + " Œr");
			//Log.d("Seb", "paybackTime: " + paybackTime);
			calculate();
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			}
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {}
		};

	protected void calculate() {
		// Calculate monthly payback
		if(paybackTime != 0)
			monthlyPayback = loanAmount/(paybackTime*12);
		
		// Calculate total interest cost
		for(int i = 0; i < 12; i++){
			monthlyInterest[i] = (loanAmount - monthlyPayback*i) * interest/12;
		}
		totalInterestCost = sum(monthlyInterest);
		
		// Calculate effectiveInterest
		if(loanAmount != 0)
			effectiveInterest = totalInterestCost/loanAmount;
		
		// Calculate total monthly cost
		for(int i = 0; i < 12; i++)
			totalMonthlyCost[i] = monthlyPayback + monthlyInterest[i];
		
		updateResult();
	}
	
	protected void updateResult(){
		
		TextView totalInterestCostTextView = (TextView) this.getActivity().findViewById(R.id.privateLoanTotalInterestCostEditTextView);
		TextView effectiveInterestTextView = (TextView) this.getActivity().findViewById(R.id.privateLoanEffectiveInterestEditTextView);
		TextView privateLoanPaybackTextView = (TextView) this.getActivity().findViewById(R.id.privateLoanPaybackTextView);
		
		totalInterestCostTextView.setText(truncate(totalInterestCost) + " kr");
		effectiveInterestTextView.setText(truncate(effectiveInterest) + " %");
		privateLoanPaybackTextView.setText(truncate(monthlyPayback) + " kr");
	}

	protected boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			Toast.makeText(this.getActivity(),
					"Ogiltigt tecken", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	public static double sum(double...values) {
		   double result = 0;
		   for (double value:values)
		     result += value;
		   return result;
		 }
	protected double truncate(double d){
		return Math.floor(d*100)/100;
	}
}