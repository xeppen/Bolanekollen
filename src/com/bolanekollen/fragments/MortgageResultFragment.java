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

	ListView resultListView;

	List<Result> results = new ArrayList<Result>();
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
	ResultCalculationAdapter arrayAdapter;

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

		// Fetch results
		assignResultValues(bundle);

		// Assign elements
		resultListView = (ListView) v.findViewById(R.id.resultListView);
		TextView mortgageResult = (TextView) v
				.findViewById(R.id.mortgageResult);
		Button goBackButton = (Button) v.findViewById(R.id.goBackButton);
		TextView cashTextView = (TextView) v
				.findViewById(R.id.mortgageResultUnit);
		cashTextView.setText("varav kontantinsats: "
				+ prettifyString(cashPayment) + " kr (" + percentage + "%)");
		mortgageResult.setText(prettifyString2(String.valueOf(maxBuyPrice))
				+ " kr");
		
		arrayAdapter = new ResultCalculationAdapter(getActivity(), R.layout.mortgage_result_row, results);
		resultListView.setAdapter(arrayAdapter);
		
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
		interest = data.getDouble("interest", 0.07);
		totalBuyPrice = (double) data.getInt("totalBuyPrice", 0);
		cashPayment = (double) data.getInt("cashPayment", 0);
		leftOverCash = data.getInt("leftOverCash", 0);
		
		// Income
		
		if(income == 0){
			Log.d("Bolanekollen", "Something wrong, income can't be 0!");
		} else
			results.add(new Result("Inkomst", "+"+income + " kr"));
		
		if(otherIncome != 0)
			results.add(new Result("Annan inkomst ", "+"+otherIncome + " kr"));
			
		// Costs
		if(otherCosts != 0)
			results.add(new Result("Andra lånekostnader", "-"+otherCosts + " kr"));
		if(maintenenceCost != 0)
			results.add(new Result("Driftkostnader", "-"+maintenenceCost + " kr"));
		if(monthlyCost != 0)
			results.add(new Result("Månadskostnad", "-"+income + " kr"));
			
		// Adults
		if(adults == 1)
			results.add(new Result("1 vuxen", "-"+COST_ONE_ADULT + " kr"));
		else
			results.add(new Result("2 vuxna", "-"+COST_TWO_ADULTS + " kr"));
		// Children
		if(children != 0)
			results.add(new Result(children + " barn", "-"+children*COST_CHILDREN + " kr"));
		// Cars
		if(cars == 1)
			results.add(new Result(cars + " bil", "-"+cars*COST_CAR + " kr"));
		else if(cars > 1)
			results.add(new Result(cars + " bilar", "-"+cars*COST_CAR + " kr"));
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