package com.bolanekollen.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bolanekollen.R;
import com.bolanekollen.util.GenericTextWatcher;

public class PrivateLoanFragment extends Fragment {

	// Define all fields
	EditText loanEditText;
	EditText interestEditText;
	SeekBar repayTimeSeekBar;
	TextView repayTextView;
	Spinner amortSpinner;
	ImageView privateLoanInfoButton;
	TextView privateLoanPayback;

	static double interest = 0.001;
	static double loanAmount = 0;
	static Integer paybackTime = 1;
	static String interestType = "Rak amortering";
	static double totalInterestCost = 0;
	static double monthlyPayback = 0;
	static double effectiveInterest = 0;
	// static double[] totalMonthlyCost = new double[] {};
	// static double[] monthlyInterest = new double[] {};
	// static double[] monthlyPayback2 = new double[] {};
	List<Double> totalMonthlyCost = new ArrayList<Double>();
	List<Double> monthlyInterest = new ArrayList<Double>();
	List<Double> monthlyPayback2 = new ArrayList<Double>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_private_loan_layout,
				container, false);

		// Updating the action bar title
		// getActivity().getActionBar().setTitle(menus[position]);

		// Assign all elements
		loanEditText = (EditText) v.findViewById(R.id.loanEditText);
		interestEditText = (EditText) v.findViewById(R.id.interestEditText);
		repayTimeSeekBar = (SeekBar) v.findViewById(R.id.repayTimeSeekBar);
		repayTextView = (TextView) v.findViewById(R.id.repayTextView);
		amortSpinner = (Spinner) v.findViewById(R.id.amortSpinner);
		privateLoanInfoButton = (ImageView) v
				.findViewById(R.id.privateLoanInfoButton);
		privateLoanPayback = (TextView) v.findViewById(R.id.privateLoanPayback);

		loanEditText.setText("0");
		interestEditText.setText("0.1");

		addKeyListeners();
		repayTimeSeekBar.setOnSeekBarChangeListener(repayTimeSeekBarListener);
		return v;
	}

	protected void addKeyListeners() {

		// Add key listener to keep track of user input
		loanEditText.setOnKeyListener(new OnKeyListener() {
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
		addEditBoxChangeListener();

		interestEditText.setOnKeyListener(new OnKeyListener() {
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

		loanEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				calculate();

			}
		});

		interestEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus)
					calculate();
			}
		});

		amortSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (amortSpinner.getSelectedItem().toString()
						.equals("Annuitet"))
					privateLoanPayback.setText("Annuitet:");
				else
					privateLoanPayback.setText("Amortering:");
				calculate();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		privateLoanInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle("RŠnteinformation");

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				LayoutInflater inflater = alertDialog.getLayoutInflater();
				View dialoglayout = inflater.inflate(
						R.layout.dialog_interest_info,
						(ViewGroup) alertDialog.getCurrentFocus());
				alertDialog.setView(dialoglayout);
				alertDialog.show();
			}
		});

	}

	private void addEditBoxChangeListener() {
		loanEditText
				.addTextChangedListener(new GenericTextWatcher(loanEditText));
	}

	private OnSeekBarChangeListener repayTimeSeekBarListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (repayTimeSeekBar.getProgress() == 0) {
				repayTimeSeekBar.setProgress(1);
			}
			paybackTime = (repayTimeSeekBar.getProgress());
			TextView repayTextView = (TextView) getActivity().findViewById(
					R.id.repayTextView);
			repayTextView.setText(paybackTime + " Œr");
			// Log.d("Seb", "paybackTime: " + paybackTime);
			calculate();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

	protected void calculate() {

		// Fetch values from all input fields
		fetchValues();

		// Clear old values
		totalMonthlyCost.clear();
		monthlyInterest.clear();
		monthlyPayback2.clear();

		// Payback time in months
		int paybackTimeMonths = paybackTime * 12;

		if (interestType.equals("Rak amortering")) {

			// Calculate monthly payback
			if (paybackTime != 0)
				monthlyPayback = loanAmount / (paybackTime * 12);

			// Calculate total interest cost
			for (int i = 0; i < paybackTimeMonths; i++) {
				double p = (loanAmount - monthlyPayback * i) * interest / 12;
				monthlyInterest.add(p);
			}
			totalInterestCost = sum(monthlyInterest);

			// Calculate effectiveInterest
			if (loanAmount != 0)
				effectiveInterest = totalInterestCost / loanAmount;

			// Calculate total monthly cost
			for (int i = 0; i < paybackTimeMonths; i++)
				totalMonthlyCost.add(monthlyPayback + monthlyInterest.get(i));

		} else if (interestType.equals("Annuitet")) {
			// Calculate annuity
			double annuity = caluclateAnnuity();
			double R = interest / 12;
			double loanStillLeft = loanAmount;

			for (int i = 0; i < paybackTimeMonths; i++) {
				monthlyInterest.add(loanStillLeft * R);
				monthlyPayback2.add(annuity - monthlyInterest.get(i));
				loanStillLeft -= monthlyPayback2.get(i);
			}

			// Calulate total interest cost
			totalInterestCost = sum(monthlyInterest);

			// Caculate effective interest
			if (loanAmount != 0)
				effectiveInterest = totalInterestCost / loanAmount;
			else
				effectiveInterest = 0;

			// Monthly payback
			monthlyPayback = annuity;
		}
		updateResult();
	}

	private void fetchValues() {
		// Interest
		if (isNumeric(interestEditText.getText().toString())) {
			String st = interestEditText.getText().toString();
			interest = Double.parseDouble(st) / 100;
			if (!(interest > 0))
				interest = 0.001;
		} else {
			interestEditText.setText("0.1");
			interest = 0.001;
		}

		// Loan Amount
		if (isNumeric(loanEditText.getText().toString().replaceAll("\\s+", ""))) {
			loanAmount = Integer.parseInt(loanEditText.getText().toString()
					.replaceAll("\\s+", ""));
		} else {
			loanEditText.setText("0");
			loanAmount = 0;
		}

		// Payback time [years]
		paybackTime = repayTimeSeekBar.getProgress();

		// Payback type
		interestType = amortSpinner.getSelectedItem().toString();

	}

	protected void updateResult() {

		TextView totalInterestCostTextView = (TextView) this.getActivity()
				.findViewById(R.id.privateLoanTotalInterestCostEditTextView);
		TextView effectiveInterestTextView = (TextView) this.getActivity()
				.findViewById(R.id.privateLoanEffectiveInterestEditTextView);
		TextView privateLoanPaybackTextView = (TextView) this.getActivity()
				.findViewById(R.id.privateLoanPaybackTextView);

		totalInterestCostTextView.setText(truncate(totalInterestCost) + " kr");
		effectiveInterestTextView.setText(truncate(effectiveInterest * 100)
				+ " %");
		privateLoanPaybackTextView.setText(truncate(monthlyPayback) + " kr");
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

	public static double sum(List<Double> dList) {
		double result = 0;
		for (int i = 0; i < dList.size(); i++)
			result += dList.get(i);
		return result;
	}

	protected double truncate(double d) {
		return Math.floor(d * 100) / 100;
	}

	private double caluclateAnnuity() {
		double a = 0;
		double p = interest / 12;
		double n = paybackTime * 12;
		double S = loanAmount;
		double k = Math.pow(1 + p, -n);
		a = (S * p) / (1 - k);

		return a;
	}

}