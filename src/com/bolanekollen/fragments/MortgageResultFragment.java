package com.bolanekollen.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import com.bolanekollen.R;
import com.bolanekollen.util.Result;
import com.bolanekollen.util.ResultCalculationAdapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MortgageResultFragment extends Fragment {

	ListView resultIncomeListView;
	ListView resultCostListView;
	TextView resultTotalTextView;
	TextView resultOther;

	List<Result> resultIncome = new ArrayList<Result>();
	List<Result> resultCosts = new ArrayList<Result>();

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
	double interest = 0.06;
	double totalBuyPrice = 0;
	double cashPayment = 0;
	int leftOverCash = 0;
	ResultCalculationAdapter arrayAdapterIncome;
	ResultCalculationAdapter arrayAdapterCost;

	// Define static values
	static final Integer COST_ONE_ADULT = 7200;
	static final Integer COST_TWO_ADULTS = 11000;
	static final Integer COST_CHILDREN = 2500;
	static final Integer COST_CAR = 3000;
	static final Integer CASH_OVER = 4000;
	static final Double PAYBACK_PERCENTAGE = 0.01;
	static final Double ADDED_PERCENTAGE = 0.02;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		final Bundle bundle = this.getArguments();
		int maxBuyPrice = bundle.getInt("totalBuyPrice");
		int cashPayment = bundle.getInt("cashPayment");
		float percentage = 0;
		if (maxBuyPrice != 0) {
			float p = (float) cashPayment / maxBuyPrice * 100;
			percentage = Math.round(p);
		}
		Log.d("Bolanekollen", "maxBuyPrice: " + maxBuyPrice);

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_mortgage_result_layout,
				container, false);

		

		// Assign elements
		resultIncomeListView = (ListView) v
				.findViewById(R.id.resultIncomeListView);
		resultCostListView = (ListView) v
				.findViewById(R.id.resultCostListView);
		resultTotalTextView = (TextView) v.findViewById(R.id.resultTotalTextView);
		resultOther = (TextView) v.findViewById(R.id.resultOther);
		
		// Fetch results
		assignResultValues(bundle);
		
		TextView mortgageResult = (TextView) v
				.findViewById(R.id.mortgageResult);
		Button goBackButton = (Button) v.findViewById(R.id.goBackButton);
		TextView cashTextView = (TextView) v
				.findViewById(R.id.mortgageResultUnit);
		cashTextView.setText("varav kontantinsats: "
				+ prettifyString(cashPayment) + " kr (" + percentage + "%)");
		mortgageResult.setText(prettifyString2(String.valueOf(maxBuyPrice))
				+ " kr");

		arrayAdapterIncome = new ResultCalculationAdapter(getActivity(),
				R.layout.mortgage_result_row, resultIncome);
		resultIncomeListView.setAdapter(arrayAdapterIncome);
		
		arrayAdapterCost = new ResultCalculationAdapter(getActivity(),
				R.layout.mortgage_result_row, resultCosts);
		resultCostListView.setAdapter(arrayAdapterCost);

		goBackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Getting reference to the FragmentManager
				FragmentManager fragmentManager = getFragmentManager();

				// Creating a fragment transaction
				FragmentTransaction ft = fragmentManager.beginTransaction();
				MortgageFragment mFragment = new MortgageFragment();
				mFragment.setArguments(bundle);

				// Adding a fragment to the fragment transaction
				ft.replace(R.id.content_frame, mFragment);

				// Committing the transaction
				ft.commit();
			}
		});

		return v;
	}

	private void assignResultValues(Bundle data) {
		
		// Clear from old data
		resultIncome.clear();
		resultCosts.clear();
		
		adults = data.getInt("adults", 1);
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
		interest = data.getDouble("interest", 0.06);
		totalBuyPrice = (double) data.getInt("totalBuyPrice", 0);
		cashPayment = (double) data.getInt("cashPayment", 0);
		leftOverCash = data.getInt("leftOverCash", 0);
		int maxLoan = (int) (totalBuyPrice - cashPayment);

		// Income
		if (income == 0) {
			Log.d("Bolanekollen", "Something wrong, income can't be 0!");
		} else
			resultIncome.add(new Result("Inkomst", "+ "
					+ prettifyString(income) + " kr", false));

		if (otherIncome != 0)
			resultIncome.add(new Result("Annan inkomst ", "+ "
					+ prettifyString(otherIncome) + " kr", false));

		// Costs
		if (otherCosts != 0)
			resultCosts.add(new Result("Andra lånekostnader", "- "
					+ prettifyString(otherCosts) + " kr", false));
		if (maintenenceCost != 0)
			resultCosts.add(new Result("Driftkostnader", "- "
					+ prettifyString(maintenenceCost) + " kr", false));
		if (monthlyCost != 0)
			resultCosts.add(new Result("Månadskostnad", "- "
					+ prettifyString(monthlyCost) + " kr", false));

		// Adults
		if (adults == 1)
			resultCosts.add(new Result("Levnadsomkostnad - en vuxen", "- "
					+ prettifyString(COST_ONE_ADULT) + " kr", false));
		else
			resultCosts.add(new Result("Levnadsomkostnad - två vuxna", "- "
					+ prettifyString(COST_TWO_ADULTS) + " kr", false));
		// Children
		if (children != 0)
			resultCosts.add(new Result("Kostnad för " + children + " barn", "- "
					+ prettifyString(children * COST_CHILDREN) + " kr", false));
		// Cars
		if (cars == 1)
			resultCosts.add(new Result("Kostnad för " + cars + " bil", "- "
					+ prettifyString(cars * COST_CAR) + " kr", false));
		else if (cars > 1)
			resultCosts.add(new Result("Kostnad för " + cars + " bilar", "- "
					+ prettifyString(cars * COST_CAR) + " kr", false));
		
		resultTotalTextView.setText("+ " + prettifyString(leftOverCash) + " kr");
		resultOther.setText("Med " + PAYBACK_PERCENTAGE*100 + " % i årlig amortering och med en årlig ränta på " + interest*100 + " % så bör du/ni kunna låna ca\n "+ prettifyString(maxLoan) + " kr.");
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