package com.bolanekollen.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bolanekollen.R;
import com.bolanekollen.util.GenericTextWatcher;

public class MortgageFragment extends Fragment {

	// Define input fields
	Spinner householdAdultsSpinner;
	Spinner householdChildrenSpinner;
	Spinner householdCarSpinner;

	EditText incomeSalaryEditText;
	EditText incomeOtherEditText;
	EditText costNewLivingOperatingCostEditText;
	EditText costNewLivingMonthlyFeeEditText;
	EditText otherCostsEditText;
	EditText cashPaymentUnitEditText;

	Button resultButton;
	CheckBox useMaxCashPaymentCheckBox;
	TextView cashPaymentUnit;
	ImageView mortgageInfoButton;

	// Define usable variables
	int adults = 1;
	int children = 0;
	int cars = 0;

	int income = 0;
	int otherIncome = 0;
	int totalIncome = 0;

	int otherCosts = 0;
	int maintenenceCost = 0;
	int monthlyCost = 0;
	int totalCost = 0;

	boolean useMaxCashPayment = false;
	int maxCashPayment = 0;
	double interest = 0.07;
	double totalBuyPrice = 0;
	double cashPayment = 0;

	// Define static values
	static final Integer COST_ONE_ADULT = 7200;
	static final Integer COST_TWO_ADULTS = 11000;
	static final Integer COST_CHILDREN = 2500;
	static final Integer COST_CAR = 3000;
	static final Integer CASH_OVER = 4000;
	static final Double PAYBACK_PERCENTAGE = 0.01;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_mortgage_layout, container,
				false);

		// Assign fields
		householdAdultsSpinner = (Spinner) v
				.findViewById(R.id.householdAdultsSpinner);
		householdChildrenSpinner = (Spinner) v
				.findViewById(R.id.householdChildrenSpinner);
		householdCarSpinner = (Spinner) v
				.findViewById(R.id.householdCarSpinner);

		incomeSalaryEditText = (EditText) v
				.findViewById(R.id.incomeSalaryEditText);
		incomeOtherEditText = (EditText) v
				.findViewById(R.id.incomeOtherEditText);
		costNewLivingOperatingCostEditText = (EditText) v
				.findViewById(R.id.costNewLivingOperatingCostEditText);
		costNewLivingMonthlyFeeEditText = (EditText) v
				.findViewById(R.id.costNewLivingMonthlyFeeEditText);
		otherCostsEditText = (EditText) v.findViewById(R.id.otherCostsEditText);
		cashPaymentUnitEditText = (EditText) v
				.findViewById(R.id.cashPaymentUnitEditText);
		cashPaymentUnit = (TextView) v.findViewById(R.id.cashPaymentUnit);

		resultButton = (Button) v.findViewById(R.id.resultButton);
		mortgageInfoButton = (ImageView) v.findViewById(R.id.mortgageInfoButton);

		useMaxCashPaymentCheckBox = (CheckBox) v
				.findViewById(R.id.useMaxCashPaymentCheckBox);
		useMaxCashPaymentCheckBox
				.setOnCheckedChangeListener(useMaxCashPaymentCheckBoxListener);
		resultButton.setOnClickListener(resultButtonListener);
		
		setStartValues();

		// if (isVariableAvaliable(savedInstanceState, "totalBuyPrice"))
		if (getArguments().containsKey("totalBuyPrice"))
			setSavedValues(savedInstanceState);

		addEditBoxChangeListener();
		return v;
	}

	private void setStartValues() {
		boolean debug = true;
		if(debug){
		incomeSalaryEditText.setText(prettifyString2("50000"));
		incomeOtherEditText.setText(prettifyString2("1000"));
		costNewLivingOperatingCostEditText.setText(prettifyString2("500"));
		costNewLivingMonthlyFeeEditText.setText(prettifyString2("3100"));
		otherCostsEditText.setText(prettifyString2("2400"));
		cashPaymentUnitEditText.setText(prettifyString2("150000"));
		} else{
			incomeSalaryEditText.setText(prettifyString2("0"));
			incomeOtherEditText.setText(prettifyString2("0"));
			costNewLivingOperatingCostEditText.setText(prettifyString2("0"));
			costNewLivingMonthlyFeeEditText.setText(prettifyString2("0"));
			otherCostsEditText.setText(prettifyString2("0"));
			cashPaymentUnitEditText.setText(prettifyString2("0"));
		}
		
	}

	private void setSavedValues(Bundle data) {
		/*
		adults = data.getInt("adults", 0);
		children = data.getInt("children", 0);
		cars = data.getInt("cars", 0);

		income = data.getInt("income", 0);
		otherIncome = data.getInt("otherIncome", 0);
		totalIncome = data.getInt("totalIncome", 0);

		otherCosts = data.getInt("otherCosts", 0);
		maintenenceCost = data.getInt("maintenenceCost", 0);
		monthlyCost = data.getInt("monthlyCost", 0);
		totalCost = data.getInt("totalCost", 0);

		useMaxCashPayment = data.getBoolean("useMaxCashPayment", false);
		maxCashPayment = data.getInt("maxCashPayment", 0);
		interest = data.getDouble("interest", 0.07);
		totalBuyPrice = data.getDouble("totalBuyPrice", 0);
		cashPayment = data.getDouble("cashPayment", 0);
		*/
		setSavedValuesToFields();

	}

	private void setSavedValuesToFields() {
		householdAdultsSpinner.setSelection(adults - 1);
		householdChildrenSpinner.setSelection(children);
		householdCarSpinner.setSelection(cars);

		incomeSalaryEditText.setText(prettifyString(income));
		incomeOtherEditText.setText(prettifyString(otherIncome));
		costNewLivingOperatingCostEditText.setText(prettifyString(maintenenceCost));
		costNewLivingMonthlyFeeEditText.setText(prettifyString(monthlyCost));
		otherCostsEditText.setText(prettifyString(otherCosts));
		cashPaymentUnitEditText.setText(prettifyString(totalCost));
		
		if (useMaxCashPayment) {
			useMaxCashPaymentCheckBox.setChecked(useMaxCashPayment);
			cashPaymentUnit.setEnabled(true);
			cashPaymentUnitEditText.setEnabled(true);
		}
	}

	OnClickListener resultButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			calulate();
		}
	};

	OnCheckedChangeListener useMaxCashPaymentCheckBoxListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				cashPaymentUnit.setEnabled(true);
				cashPaymentUnitEditText.setEnabled(true);
			} else {
				cashPaymentUnit.setEnabled(false);
				cashPaymentUnitEditText.setEnabled(false);
			}

		}
	};

	private void addEditBoxChangeListener() {
		incomeSalaryEditText.addTextChangedListener(new GenericTextWatcher(
				incomeSalaryEditText));
		incomeOtherEditText.addTextChangedListener(new GenericTextWatcher(
				incomeOtherEditText));
		costNewLivingOperatingCostEditText
				.addTextChangedListener(new GenericTextWatcher(
						costNewLivingOperatingCostEditText));
		costNewLivingMonthlyFeeEditText
				.addTextChangedListener(new GenericTextWatcher(
						costNewLivingMonthlyFeeEditText));
		otherCostsEditText.addTextChangedListener(new GenericTextWatcher(
				otherCostsEditText));
		cashPaymentUnitEditText.addTextChangedListener(new GenericTextWatcher(
				cashPaymentUnitEditText));
		
		mortgageInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle("Bol�nekalkyl information");

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
						R.layout.dialog_bankloan_layout,
						(ViewGroup) alertDialog.getCurrentFocus());
				alertDialog.setView(dialoglayout);
				alertDialog.show();
			}
		});
	}

	protected void calulate() {
		fetchValues();

		int leftOverCash = totalIncome - totalCost - CASH_OVER;
		double maxLoanAmount = (leftOverCash * 12)
				/ (interest + PAYBACK_PERCENTAGE);

		totalBuyPrice = 0;
		totalBuyPrice = maxLoanAmount / 0.85;
		cashPayment = totalBuyPrice - maxLoanAmount;
		if (useMaxCashPayment) {
			if (maxCashPayment > cashPayment) {
				cashPayment = (double) maxCashPayment;
				totalBuyPrice = cashPayment + maxLoanAmount;
			} else {
				totalBuyPrice = maxCashPayment / 0.15;
				cashPayment = maxCashPayment;
				maxLoanAmount = totalBuyPrice - cashPayment;
				
			}
		}

		Log.d("Bolanekollen", "maxLoanAmount: " + maxLoanAmount);
		Log.d("Bolanekollen", "cashPayment: " + cashPayment);
		Log.d("Bolanekollen", "totalBuyPrice: " + totalBuyPrice);

		openResultFragment();
	}

	private void openResultFragment() {

		// Create data bundle
		Bundle data = new Bundle();
		data.putInt("adults", adults);
		data.putInt("children", children);
		data.putInt("cars", cars);
		data.putInt("income", income);
		data.putInt("otherIncome", otherIncome);
		data.putInt("totalIncome", totalIncome);
		data.putInt("otherCosts", otherCosts);
		data.putInt("maintenenceCost", maintenenceCost);
		data.putInt("monthlyCost", monthlyCost);
		data.putInt("totalCost", totalCost);
		data.putInt("maxCashPayment", maxCashPayment);
		data.putBoolean("useMaxCashPayment", useMaxCashPayment);
		data.putDouble("interest", interest);
		data.putInt("totalBuyPrice", (int)truncate(truncate(totalBuyPrice)));
		data.putInt("cashPayment", (int)truncate(truncate(cashPayment)));
		
		MortgageResultFragment mrFragment = new MortgageResultFragment();

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// Adding a fragment to the fragment transaction
		mrFragment.setArguments(data);
		ft.replace(R.id.content_frame, mrFragment);

		ft.commit();
	}

	private void fetchValues() {
		adults = Integer.valueOf(householdAdultsSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		children = Integer.valueOf(householdChildrenSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		cars = Integer.valueOf(householdCarSpinner.getSelectedItem().toString()
				.replaceAll("\\D+", ""));

		income = Integer.valueOf(incomeSalaryEditText.getText().toString().replaceAll("\\D+", ""));
		otherIncome = Integer.valueOf(incomeOtherEditText.getText().toString().replaceAll("\\D+", ""));
		totalIncome = income + otherIncome;

		otherCosts = Integer.valueOf(costNewLivingOperatingCostEditText
				.getText().toString().replaceAll("\\D+", ""));
		maintenenceCost = Integer.valueOf(costNewLivingMonthlyFeeEditText
				.getText().toString().replaceAll("\\D+", ""));
		monthlyCost = Integer.valueOf(otherCostsEditText.getText().toString().replaceAll("\\D+", ""));
		totalCost = otherCosts + maintenenceCost + monthlyCost;

		useMaxCashPayment = useMaxCashPaymentCheckBox.isChecked();
		if (useMaxCashPayment) {
			maxCashPayment = Integer.valueOf(cashPaymentUnitEditText.getText()
					.toString().replaceAll("\\D+", ""));
		}

		Log.d("Bolanekollen", "adults: " + adults);
		Log.d("Bolanekollen", "children: " + children);
		Log.d("Bolanekollen", "cars: " + cars);
		Log.d("Bolanekollen", "totalIncome: " + totalIncome);
		Log.d("Bolanekollen", "totalCost: " + totalCost);
		Log.d("Bolanekollen", "useMaxCashPayment: " + useMaxCashPayment);
	}

	private boolean isVariableAvaliable(Bundle b, String key) {
		int a = b.getInt(key, -1);
		if (a == -1)
			return false;
		else
			return true;
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
	protected double truncate(double d) {
		return Math.floor(d / 1000) * 1000;
	}
	
}