package com.javatechig.drawer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WebViewFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");
		
		String url = getArguments().getString("url");

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_test_fragment, container, false);

		// Updating the action bar title
		getActivity().getActionBar().setTitle(menus[position]);
		
		TextView tv = (TextView) v.findViewById(R.id.fragmentTextView);
		tv.setText(url);

		return v;
	}
}