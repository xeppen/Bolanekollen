package com.bolanekollen.fragments;

import com.bolanekollen.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_home_layout, container,
				false);
		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);
		return v;
	}
}