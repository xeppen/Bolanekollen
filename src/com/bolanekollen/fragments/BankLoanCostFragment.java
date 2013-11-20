package com.bolanekollen.fragments;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.bolanekollen.R;
import com.bolanekollen.util.Bank;
import com.bolanekollen.util.GenericTextWatcher;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BankLoanCostFragment extends Fragment {

	// Define all fields
	EditText bankLoanEditText;
	EditText bankLoanInterestEditText;
	SeekBar interestChangeSeekBar;
	TextView updatedInterestChangeTextView;
	TextView bankLoanInterestCostAmount;
	TextView bankLoanUpdatedInterestCostTextView;
	TextView bankLoanResultInterestDifferenceTextView;
	TextView BankLoanHeadline;

	double interest = 0.04;
	double bankLoanAmount = 0;
	double interestDiff = 0;
	double updateInterest = 0.001;
	double currentInterestCost = 0;
	double updatedInterestCost = 0;
	double diffInterestCost = 0;

	static double INTEREST_INTERVALL = 0.002;
	static final Double ADDED_PERCENTAGE = 0.02;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);
		
		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_bankloan_cost_layout, container,
				false);
		
		setupUI(v);

		// Assign all elements
		bankLoanEditText = (EditText) v.findViewById(R.id.bankLoanEditText);
		bankLoanInterestEditText = (EditText) v
				.findViewById(R.id.bankLoanInterestEditText);
		interestChangeSeekBar = (SeekBar) v
				.findViewById(R.id.interestChangeSeekBar);
		updatedInterestChangeTextView = (TextView) v
				.findViewById(R.id.updatedInterestChangeTextView);
		bankLoanInterestCostAmount = (TextView) v
				.findViewById(R.id.bankLoanInterestCostAmount);
		bankLoanUpdatedInterestCostTextView = (TextView) v
				.findViewById(R.id.bankLoanUpdatedInterestCostTextView);
		bankLoanResultInterestDifferenceTextView = (TextView) v
				.findViewById(R.id.bankLoanResultInterestDifferenceTextView);
		BankLoanHeadline = (TextView) v.findViewById(R.id.BankLoanHeadlineTextView);
		BankLoanHeadline.setText("Bolånekostnad vid ränteskillnad");
		addListeners();
		
		double p = checkUpdatedInterest();
		
		//bankLoanEditText.setText("0");
		bankLoanInterestEditText.setText(String.valueOf(p));
		return v;
	}

	protected void addListeners() {
		interestChangeSeekBar
				.setOnSeekBarChangeListener(interestChangeSeekBarListener);
		addEditBoxChangeListener();

		bankLoanEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					calculate();
					return true;
				}
				return false;
			}
		});

		bankLoanInterestEditText.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				// if keydown and "enter" is pressed
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					calculate();
					return true;
				}
				return false;
			}
		});

		bankLoanEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					calculate();
				}
			}
		});
		
		bankLoanInterestEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					calculate();
				}
			}
		});
	}

	private OnSeekBarChangeListener interestChangeSeekBarListener = new OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			interestDiff = interestChangeSeekBar.getProgress()
					* INTEREST_INTERVALL;
			updateInterest = interest + interestDiff;
			updatedInterestChangeTextView.setText("+"
					+ truncate(interestDiff * 100) + " %");

			calculate();
		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}
	};

	protected void calculate() {

		// Fetch values
		String t = bankLoanInterestEditText.getText().toString();
		interest = Double.valueOf(bankLoanInterestEditText.getText().toString()
				.replaceAll("\\s+", "")) / 100;
		String s = bankLoanEditText.getText().toString()
				.replaceAll("\\s+", "");
		if(s.equals(""))
			bankLoanAmount = 0;
		else
			bankLoanAmount = Double.valueOf(s);
		interestDiff = interestChangeSeekBar.getProgress() * INTEREST_INTERVALL;
		updateInterest = interest + interestDiff;

		currentInterestCost = bankLoanAmount * interest / 12;
		updatedInterestCost = bankLoanAmount * updateInterest / 12;
		diffInterestCost = updatedInterestCost - currentInterestCost;

		updateResult();
	}

	protected void updateResult() {
		bankLoanInterestCostAmount.setText(prettifyString((int)currentInterestCost)
				+ " kr/mån");
		bankLoanUpdatedInterestCostTextView
				.setText(prettifyString((int)updatedInterestCost) + " kr/mån");
		bankLoanResultInterestDifferenceTextView
				.setText("+" + prettifyString((int)diffInterestCost) + " kr");
	}

	private void addEditBoxChangeListener() {
		bankLoanEditText.addTextChangedListener(new GenericTextWatcher(
				bankLoanEditText));
	}

	protected boolean isNumeric(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			Toast.makeText(this.getActivity(), "Ogiltigt tecken",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	public static double sum(double... values) {
		double result = 0;
		for (double value : values)
			result += value;
		return result;
	}

	protected double truncate(double d) {
		return Math.floor(d * 100) / 100;
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
	
	private double checkUpdatedInterest() {
		String xml = loadSavedPreferences("Bolåneappen_xml");
		Document dom = getDomElement(xml);
		NodeList nl = null;
		double p = interest;
		List<Bank> banks = new ArrayList<Bank>();

		if (dom != null) {
			Element docEle = dom.getDocumentElement();
			nl = docEle.getElementsByTagName("Bank");
		}

		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Bank aBank = getBankInformation((Element) nl.item(i));
				if (aBank != null)
					banks.add(aBank);
			}
			for (int j = 0; j < banks.size(); j++) {
				p += Double.valueOf(banks.get(j).getFiveYearInterest()
						.replace(",", "."));
			}
			p = p / banks.size();
			p = Math.floor(p * 100) / 100;
		}
		return p;
	}
	
	private String loadSavedPreferences(String key) {
		String value;
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		if (key.toLowerCase().contains("_time"))
			value = sharedPreferences.getString(key, "0");
		else
			value = sharedPreferences.getString(key, "");
		return value;
	}
	private Bank getBankInformation(Element entry) {

		NodeList rates = entry.getElementsByTagName("Rate");

		Integer bankId = Integer.valueOf(getTextValue(entry, "BankId"));
		if (bankId != 15 && bankId != 16 && bankId != 17
				&& rates.getLength() >= 4) {
			String bankName = getTextValue(entry, "BankName");
			String imgUrl = getTextValue(entry, "BankImage");

			String bankUrl = getTextValue(entry, "BankUrl");
			String threeMonthInterest = getTextValue((Element) rates.item(0),
					"RateInterest");
			String oneYearInterest = getTextValue((Element) rates.item(1),
					"RateInterest");
			String twoYearInterest = getTextValue((Element) rates.item(2),
					"RateInterest");
			String threeYearInterest = getTextValue((Element) rates.item(3),
					"RateInterest");
			String fiveYearInterest = getTextValue((Element) rates.item(4),
					"RateInterest");

			Bank theBank = new Bank();
			theBank.setBankId(bankId);
			theBank.setBankName(bankName);
			theBank.setImgUrl(imgUrl);
			theBank.setBankUrl(bankUrl);
			theBank.setThreeMonthInterest(threeMonthInterest);
			theBank.setOneYearInterest(oneYearInterest);
			theBank.setTwoYearInterest(twoYearInterest);
			theBank.setThreeYearInterest(threeYearInterest);
			theBank.setFiveYearInterest(fiveYearInterest);

			return theBank;
		} else {
			return null;
		}
	}
	private String getTextValue(Element entry, String tagName) {
		String tagValueToReturn = null;
		NodeList nl = entry.getElementsByTagName(tagName);

		if (nl != null && nl.getLength() > 0) {
			Element element = (Element) nl.item(0);
			tagValueToReturn = element.getFirstChild().getNodeValue();
		}
		return tagValueToReturn;
	}
	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}
	
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public void setupUI(View view) {

		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					hideSoftKeyboard(getActivity());
					return false;
				}

			});
		}
	}
}