package com.bolanekollen.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Spinner;

import com.bolanekollen.MainActivity;
import com.bolanekollen.R;

public class HomeFragment extends Fragment {
	
	// Define elements
	Spinner homeFormAdultSpinner;
	Spinner homeFormChildrenSpinner;
	Spinner homeFormHouseTypeSpinner;
	Button goToHouseLoanFormButton;
	MortgageFragment mFragment = new MortgageFragment();
	
	int adults = 1;
	int children = 0;
	int householdType = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_home_layout, container,
				false);
		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);
		
		homeFormAdultSpinner = (Spinner) v.findViewById(R.id.homeFormAdultSpinner);
		homeFormChildrenSpinner = (Spinner) v.findViewById(R.id.homeFormChildrenSpinner);
		homeFormHouseTypeSpinner = (Spinner) v.findViewById(R.id.homeFormHouseTypeSpinner);
		goToHouseLoanFormButton = (Button) v.findViewById(R.id.goToHouseLoanFormButton);
		
		goToHouseLoanFormButton.setOnClickListener(homeFormButtonListener);
		
		
		return v;
	}
	
	private void fetchValuesFromForm(){
		adults = Integer.valueOf(homeFormAdultSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		children = Integer.valueOf(homeFormChildrenSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		householdType = homeFormHouseTypeSpinner.getSelectedItemPosition();
	}
	
	OnClickListener homeFormButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			fetchValuesFromForm();
			
			Bundle data = new Bundle();
			data.putInt("adults", adults);
			data.putInt("children", children);
			data.putInt("householdType", householdType);
			
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction ft = fragmentManager.beginTransaction();
			mFragment.setArguments(data);
			ft.replace(R.id.content_frame, mFragment);
			Activity activity = getActivity();
			
			if(activity instanceof MainActivity){
				MainActivity activityTwo = (MainActivity) activity;
				activityTwo.setCurrentPosition(1);
			}
			
			ft.commit();
		}
	};
}