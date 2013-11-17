package com.bolanekollen.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.bolanekollen.R;

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
		cashPaymentUnit = (TextView) v
				.findViewById(R.id.cashPaymentUnit);

		resultButton = (Button) v.findViewById(R.id.resultButton);

		useMaxCashPaymentCheckBox = (CheckBox) v
				.findViewById(R.id.useMaxCashPaymentCheckBox);
		useMaxCashPaymentCheckBox.setOnCheckedChangeListener(useMaxCashPaymentCheckBoxListener);
		resultButton.setOnClickListener(resultButtonListener);
		

		//if (isVariableAvaliable(savedInstanceState, "totalBuyPrice"))
		if (getArguments().containsKey("totalBuyPrice"))
			setSavedValues(savedInstanceState);
		
		if(useMaxCashPayment){
			useMaxCashPaymentCheckBox.setChecked(useMaxCashPayment);
			cashPaymentUnit.setEnabled(true);
			cashPaymentUnitEditText.setEnabled(true);
		}
			

		return v;
	}

	private void setSavedValues(Bundle data) {

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

	}

	OnClickListener resultButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			calulate();
		}
	};
	
	OnCheckedChangeListener useMaxCashPaymentCheckBoxListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				cashPaymentUnit.setEnabled(true);
				cashPaymentUnitEditText.setEnabled(true);
			} else{
				cashPaymentUnit.setEnabled(false);
				cashPaymentUnitEditText.setEnabled(false);
			}
			
		}
	};
	protected void calulate() {
		fetchValues();

		int leftOverCash = totalIncome - totalCost - CASH_OVER;
		double maxLoanAmount = (leftOverCash * 12)
				/ (interest + PAYBACK_PERCENTAGE);

		totalBuyPrice = 0;
		cashPayment = maxLoanAmount / 0.85;
		if (useMaxCashPayment) {
			if (maxCashPayment > cashPayment) {
				cashPayment = (double) maxCashPayment;
				totalBuyPrice = cashPayment + maxLoanAmount;
			} else {
				maxLoanAmount = maxCashPayment / 0.15;
				cashPayment = maxCashPayment;
				totalBuyPrice = maxLoanAmount + cashPayment;
			}
		} else {
			cashPayment = maxLoanAmount / 0.85;
			totalBuyPrice = cashPayment + maxLoanAmount;
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
		data.putDouble("totalBuyPrice", totalBuyPrice);
		data.putDouble("cashPayment", cashPayment);

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

		income = Integer.valueOf(incomeSalaryEditText.getText().toString());
		otherIncome = Integer.valueOf(incomeOtherEditText.getText().toString());
		totalIncome = income + otherIncome;

		otherCosts = Integer.valueOf(costNewLivingOperatingCostEditText
				.getText().toString());
		maintenenceCost = Integer.valueOf(costNewLivingMonthlyFeeEditText
				.getText().toString());
		monthlyCost = Integer.valueOf(otherCostsEditText.getText().toString());
		totalCost = otherCosts + maintenenceCost + monthlyCost;

		useMaxCashPayment = useMaxCashPaymentCheckBox.isChecked();
		if (useMaxCashPayment) {
			maxCashPayment = Integer.valueOf(cashPaymentUnitEditText.getText()
					.toString());
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
}