package com.bolanekollen.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

import com.bolanekollen.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ResultCalculationAdapter extends ArrayAdapter<Result> {
	private final Context context;
	private final List<Result> objects;

	public ResultCalculationAdapter(Context context, int resource,
			List<Result> results) {
		super(context, resource);
		this.context = context;
		this.objects = results;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = new ViewHolder();

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.mortgage_result_row, parent, false);
			holder.v1 = (TextView) row.findViewById(R.id.mortgageResultKey);
			holder.v2 = (TextView) row.findViewById(R.id.mortgageResultValue);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		final Result r = objects.get(position);
		if (r != null) {
			holder.v1.setText(r.getKey());
			holder.v2.setText(r.getResult());
		}
		return row;
	}

	public static class ViewHolder {
		TextView v1; // view1
		TextView v2; // view2
	}
	
	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Result getItem(int position) {
		// TODO Auto-generated method stub
		return objects.get(position);
	}
}
