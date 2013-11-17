package com.bolanekollen.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

	// Define usable variables
	int adults = 0;
	int children = 0;
	int cars = 0;

	int income = 0;
	int otherIncome = 0;
	int totalIncome = 0;

	int otherCosts = 0;
	int maintenenceCost = 0;
	int monthlyCost = 0;

	boolean useMaxCashPayment = false;
	int maxCashPayment = 0;
	

	// Define static values
	static final Integer COST_ONE_ADULT = 7200;
	static final Integer COST_TWO_ADULTS = 11000;
	static final Integer COST_CHILDREN = 2500;
	static final Integer COST_CAR = 3000;
	static final Integer CASH_OVER = 4000;

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

		resultButton = (Button) v.findViewById(R.id.resultButton);

		useMaxCashPaymentCheckBox = (CheckBox) v
				.findViewById(R.id.useMaxCashPaymentCheckBox);

		resultButton.setOnClickListener(resultButtonListener);

		return v;
	}

	OnClickListener resultButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			calulate();
		}
	};

	protected void calulate() {
		fetchValues();
		
		

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
		;

		useMaxCashPayment = useMaxCashPaymentCheckBox.isChecked();
		if (useMaxCashPayment) {
			maxCashPayment = Integer.valueOf(cashPaymentUnitEditText.getText()
					.toString());
		}
	}
}