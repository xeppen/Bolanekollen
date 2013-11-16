package com.bolanekollen.fragments;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.bolanekollen.util.GenericTextWatcher;
import com.javatechig.drawer.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BankLoanCostFragment extends Fragment {

	// Define all fields
	EditText bankLoanEditText;
	EditText bankLoanInterestEditText;
	SeekBar interestChangeSeekBar;
	TextView updatedInterestChangeTextView;
	TextView bankLoanInterestCostAmount;
	TextView bankLoanUpdatedInterestCostTextView;
	TextView bankLoanResultInterestDifferenceTextView;
	TextView BankLoanHeadline;

	double interest = 0.001;
	double bankLoanAmount = 0;
	double interestDiff = 0;
	double updateInterest = 0.001;
	double currentInterestCost = 0;
	double updatedInterestCost = 0;
	double diffInterestCost = 0;

	static double INTEREST_INTERVALL = 0.002;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_bankloan_layout, container,
				false);

		// Assign all elements
		bankLoanEditText = (EditText) v.findViewById(R.id.bankLoanEditText);
		bankLoanInterestEditText = (EditText) v
				.findViewById(R.id.bankLoanInterestEditText);
		interestChangeSeekBar = (SeekBar) v
				.findViewById(R.id.interestChangeSeekBar);
		updatedInterestChangeTextView = (TextView) v
				.findViewById(R.id.updatedInterestChangeTextView);
		bankLoanInterestCostAmount = (TextView) v
				.findViewById(R.id.bankLoanInterestCostAmount);
		bankLoanUpdatedInterestCostTextView = (TextView) v
				.findViewById(R.id.bankLoanUpdatedInterestCostTextView);
		bankLoanResultInterestDifferenceTextView = (TextView) v
				.findViewById(R.id.bankLoanResultInterestDifferenceTextView);
		BankLoanHeadline = (TextView) v.findViewById(R.id.BankLoanHeadlineTextView);
		BankLoanHeadline.setText("Bolånekostnad vid ränteskillnad");
		addListeners();

		bankLoanEditText.setText("0");
		bankLoanInterestEditText.setText("0");
		return v;
	}

	protected void addListeners() {
		interestChangeSeekBar
				.setOnSeekBarChangeListener(interestChangeSeekBarListener);
		addEditBoxChangeListener();

		bankLoanEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					calculate();
					return true;
				}
				return false;
			}
		});

		bankLoanInterestEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					calculate();
					return true;
				}
				return false;
			}
		});

		bankLoanEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					calculate();
				}
			}
		});
		
		bankLoanInterestEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					calculate();
				}
			}
		});
	}

	private OnSeekBarChangeListener interestChangeSeekBarListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			interestDiff = interestChangeSeekBar.getProgress()
					* INTEREST_INTERVALL;
			updateInterest = interest + interestDiff;
			updatedInterestChangeTextView.setText("+"
					+ truncate(interestDiff * 100) + " %");

			calculate();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}
	};

	protected void calculate() {

		// Fetch values
		String t = bankLoanInterestEditText.getText().toString();
		interest = Double.valueOf(bankLoanInterestEditText.getText().toString()
				.replaceAll("\\s+", "")) / 100;
		bankLoanAmount = Double.valueOf(bankLoanEditText.getText().toString()
				.replaceAll("\\s+", ""));
		interestDiff = interestChangeSeekBar.getProgress() * INTEREST_INTERVALL;
		updateInterest = interest + interestDiff;

		currentInterestCost = bankLoanAmount * interest / 12;
		updatedInterestCost = bankLoanAmount * updateInterest / 12;
		diffInterestCost = updatedInterestCost - currentInterestCost;

		updateResult();
	}

	protected void updateResult() {
		bankLoanInterestCostAmount.setText(truncate(currentInterestCost)
				+ " kr/mån");
		bankLoanUpdatedInterestCostTextView
				.setText(truncate(updatedInterestCost) + " kr/mån");
		bankLoanResultInterestDifferenceTextView
				.setText(truncate(diffInterestCost) + "kr");
	}

	private void addEditBoxChangeListener() {
		bankLoanEditText.addTextChangedListener(new GenericTextWatcher(
				bankLoanEditText));
	}

	protected boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			Toast.makeText(this.getActivity(), "Ogiltigt tecken",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public static double sum(double... values) {
		double result = 0;
		for (double value : values)
			result += value;
		return result;
	}

	protected double truncate(double d) {
		return Math.floor(d * 100) / 100;
	}

	private static String prettifyString(Integer i) {

		DecimalFormat format = new DecimalFormat();
		DecimalFormatSymbols customSymbols = new DecimalFormatSymbols();
		customSymbols.setGroupingSeparator(' ');
		format.setDecimalFormatSymbols(customSymbols);
		String s = format.format(i);
		return s;
	}

	private static String prettifyString2(String s) {
		return prettifyString(Integer.valueOf(s));
	}
}