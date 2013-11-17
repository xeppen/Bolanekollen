package com.bolanekollen.fragments;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.bolanekollen.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MortgageResultFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		final Bundle bundle = this.getArguments();
		int maxBuyPrice = bundle.getInt("totalBuyPrice", 0);
		int cashPayment = bundle.getInt("cashPayment", 0);
		float percentage = 0;
		if(maxBuyPrice != 0){
			float p = (float) cashPayment/maxBuyPrice * 100;
			percentage = Math.round(p);
		}
		Log.d("Bolanekollen", "maxBuyPrice: " + maxBuyPrice);

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.mortgage_result_layout, container,
				false);

		// Assign elements
		TextView mortgageResult = (TextView) v
				.findViewById(R.id.mortgageResult);
		Button goBackButton = (Button) v.findViewById(R.id.goBackButton);
		TextView cashTextView = (TextView) v.findViewById(R.id.mortgageResultUnit);
		cashTextView.setText("varav kontantinsats: " + prettifyString(cashPayment) + " kr (" + percentage + "%)");
		mortgageResult.setText(prettifyString2(String.valueOf(maxBuyPrice)) + " kr");

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