package com.bolanekollen.util;

import java.util.List;

import com.bolanekollen.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BankInterestAdapter extends ArrayAdapter<Bank> {
	private final Context context;
	private final List<Bank> objects;

	public BankInterestAdapter(Context context, int resource,
			List<Bank> bankList) {
		super(context, resource);
		this.context = context;
		this.objects = bankList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = new ViewHolder();

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.bank_interest_spinner_row, parent, false);
			holder.v1 = (TextView) row.findViewById(R.id.BankNameTextView);
			holder.v2 = (TextView) row.findViewById(R.id.BankInterest1TextView);
			holder.v3 = (TextView) row.findViewById(R.id.BankInterest2TextView);
			holder.v4 = (TextView) row.findViewById(R.id.BankInterest3TextView);
			holder.v5 = (TextView) row.findViewById(R.id.BankInterest4TextView);
			holder.v6 = (TextView) row.findViewById(R.id.BankInterest5TextView);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		final Bank bank = objects.get(position);
		if (bank != null) {
			holder.v1.setText(bank.getBankName());
			holder.v2.setText(bank.getThreeMonthInterest());
			holder.v3.setText(bank.getOneYearInterest());
			holder.v4.setText(bank.getTwoYearInterest());
			holder.v5.setText(bank.getThreeYearInterest());
			holder.v6.setText(bank.getFiveYearInterest());
		}
		return row;
	}

	public static class ViewHolder {
		TextView v1; // view1
		TextView v2; // view2
		TextView v3; // view3
		TextView v4; // view3
		TextView v5; // view3
		TextView v6; // view3
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Bank getItem(int position) {
		// TODO Auto-generated method stub
		return objects.get(position);
	}
}
