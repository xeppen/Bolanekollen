package com.bolanekollen.fragments;

import java.io.IOException;
import java.io.StringReader;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bolanekollen.R;
import com.bolanekollen.util.Bank;
import com.bolanekollen.util.GenericTextWatcher;

public class MortgageFragment extends Fragment {

	// Define input fields
	Spinner householdAdultsSpinner;
	Spinner householdChildrenSpinner;
	Spinner householdCarSpinner;
	Spinner newHouseholdSpinner;

	EditText incomeSalaryEditText;
	EditText incomeOtherEditText;
	EditText costNewLivingOperatingCostEditText;
	EditText costNewLivingMonthlyFeeEditText;
	EditText otherCostsEditText;
	EditText cashPaymentUnitEditText;

	Button resultButton;
	CheckBox useMaxCashPaymentCheckBox;
	TextView cashPaymentUnit;
	ImageView mortgageInfoButton;
	
	LinearLayout cashPaymentUnitRow;
	
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
	int householdType = 0;

	boolean debug = false;

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

		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_mortgage_layout, container,
				false);
		
		setupUI(v);

		// Assign fields
		householdAdultsSpinner = (Spinner) v
				.findViewById(R.id.householdAdultsSpinner);
		householdChildrenSpinner = (Spinner) v
				.findViewById(R.id.householdChildrenSpinner);
		householdCarSpinner = (Spinner) v
				.findViewById(R.id.householdCarSpinner);
		newHouseholdSpinner = (Spinner) v
				.findViewById(R.id.newHouseholdSpinner);
		cashPaymentUnitRow = (LinearLayout) v.findViewById(R.id.cashPaymentUnitRow);
		
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
		cashPaymentUnit = (TextView) v.findViewById(R.id.cashPaymentUnit);

		resultButton = (Button) v.findViewById(R.id.resultButton);
		mortgageInfoButton = (ImageView) v
				.findViewById(R.id.mortgageInfoButton);

		useMaxCashPaymentCheckBox = (CheckBox) v
				.findViewById(R.id.useMaxCashPaymentCheckBox);
		useMaxCashPaymentCheckBox
				.setOnCheckedChangeListener(useMaxCashPaymentCheckBoxListener);
		resultButton.setOnClickListener(resultButtonListener);
		cashPaymentUnitRow.setVisibility(LinearLayout.GONE);
		interest = checkUpdatedInterest();

		setStartValues();

		if (getArguments().containsKey("adults"))
			setSavedValues(this.getArguments());

		addEditBoxChangeListener();
		return v;
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
			p += 100 * ADDED_PERCENTAGE;
			p = p / 100;
		}
		return p;
	}

	private void setStartValues() {
		if (debug) {
			incomeSalaryEditText.setText(prettifyString2("50000"));
			incomeOtherEditText.setText(prettifyString2("1000"));
			costNewLivingOperatingCostEditText.setText(prettifyString2("500"));
			costNewLivingMonthlyFeeEditText.setText(prettifyString2("3100"));
			otherCostsEditText.setText(prettifyString2("2400"));
			cashPaymentUnitEditText.setText(prettifyString2("150000"));
		} else {
			/*
			incomeSalaryEditText.setText(prettifyString2("0"));
			incomeOtherEditText.setText(prettifyString2("0"));
			costNewLivingOperatingCostEditText.setText(prettifyString2("0"));
			costNewLivingMonthlyFeeEditText.setText(prettifyString2("0"));
			otherCostsEditText.setText(prettifyString2("0"));
			cashPaymentUnitEditText.setText(prettifyString2("0"));
			*/
		}

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

	private void setSavedValues(Bundle data) {

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
		interest = data.getDouble("interest", interest);
		totalBuyPrice = (double) data.getInt("totalBuyPrice", 0);
		cashPayment = (double) data.getInt("cashPayment", 0);
		householdType = data.getInt("householdType", 0);

		setSavedValuesToFields();

	}

	private void setSavedValuesToFields() {
		householdAdultsSpinner.setSelection(adults - 1);
		householdChildrenSpinner.setSelection(children);
		householdCarSpinner.setSelection(cars);

		if(income != 0)
			incomeSalaryEditText.setText(prettifyString(income));
		if(otherIncome != 0)
			incomeOtherEditText.setText(prettifyString(otherIncome));
		if(maintenenceCost != 0)
		costNewLivingOperatingCostEditText
				.setText(prettifyString(maintenenceCost));
		if(monthlyCost != 0)
		costNewLivingMonthlyFeeEditText.setText(prettifyString(monthlyCost));
		if(otherCosts != 0)
			otherCostsEditText.setText(prettifyString(otherCosts));
		newHouseholdSpinner.setSelection(householdType);

		if (useMaxCashPayment) {
			useMaxCashPaymentCheckBox.setChecked(useMaxCashPayment);
			cashPaymentUnitEditText.setText(prettifyString3(cashPayment));
			cashPaymentUnitRow.setVisibility(LinearLayout.VISIBLE);
			//cashPaymentUnit.setEnabled(true);
			//cashPaymentUnitEditText.setEnabled(true);
		}
	}

	OnClickListener resultButtonListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			final InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			calulate();
		}
	};

	OnCheckedChangeListener useMaxCashPaymentCheckBoxListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (isChecked) {
				cashPaymentUnitRow.setVisibility(LinearLayout.VISIBLE);
				//cashPaymentUnit.setEnabled(true);
				//cashPaymentUnitEditText.setEnabled(true);
			} else {
				cashPaymentUnitRow.setVisibility(LinearLayout.GONE);
				//cashPaymentUnit.setEnabled(false);
				//cashPaymentUnitEditText.setEnabled(false);
			}

		}
	};

	private void addEditBoxChangeListener() {
		incomeSalaryEditText.addTextChangedListener(new GenericTextWatcher(
				incomeSalaryEditText));
		incomeOtherEditText.addTextChangedListener(new GenericTextWatcher(
				incomeOtherEditText));
		costNewLivingOperatingCostEditText
				.addTextChangedListener(new GenericTextWatcher(
						costNewLivingOperatingCostEditText));
		costNewLivingMonthlyFeeEditText
				.addTextChangedListener(new GenericTextWatcher(
						costNewLivingMonthlyFeeEditText));
		otherCostsEditText.addTextChangedListener(new GenericTextWatcher(
				otherCostsEditText));
		cashPaymentUnitEditText.addTextChangedListener(new GenericTextWatcher(
				cashPaymentUnitEditText));

		mortgageInfoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle("Bolånekalkyl information");

				// set dialog message
				alertDialogBuilder.setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				LayoutInflater inflater = alertDialog.getLayoutInflater();
				View dialoglayout = inflater.inflate(
						R.layout.dialog_bankloan_layout,
						(ViewGroup) alertDialog.getCurrentFocus());
				alertDialog.setView(dialoglayout);
				alertDialog.show();
			}
		});
	}

	protected void calulate() {
		fetchValues();
		double maxLoanAmount = 0;

		leftOverCash = totalIncome - totalCost;
		if (leftOverCash > 0) {
			maxLoanAmount = (leftOverCash * 12)
					/ (interest + PAYBACK_PERCENTAGE);

			totalBuyPrice = 0;
			totalBuyPrice = maxLoanAmount / 0.85;
			cashPayment = totalBuyPrice - maxLoanAmount;
			if (useMaxCashPayment) {
				if (maxCashPayment > cashPayment) {
					cashPayment = (double) maxCashPayment;
					totalBuyPrice = cashPayment + maxLoanAmount;
				} else {
					totalBuyPrice = maxCashPayment / 0.15;
					cashPayment = maxCashPayment;
					maxLoanAmount = totalBuyPrice - cashPayment;

				}
			}

			Log.d("Bolanekollen", "maxLoanAmount: " + maxLoanAmount);
			Log.d("Bolanekollen", "cashPayment: " + cashPayment);
			Log.d("Bolanekollen", "totalBuyPrice: " + totalBuyPrice);

			openResultFragment();
		} else {
			maxLoanAmount = 0;
			cashPayment = 0;
			totalBuyPrice = 0;
			// alert("Du har för liten inkomst. Total inkomst: " + totalIncome
			// +" kr och totala utgift: " + totalCost + " kr.");
			openResultFragment();
		}
	}

	private void alert(String s) {
		Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
	}

	private void openResultFragment() {

		// Create data bundle
		Bundle data = new Bundle();
		data.putInt("adults", adults);
		data.putInt("children", children);
		data.putInt("cars", cars);
		data.putInt("income", income);
		data.putInt("otherIncome", otherIncome);
		data.putInt("totalIncome", totalIncome);
		data.putInt("otherCosts", otherCosts);
		data.putInt("maintenenceCost", maintenenceCost);
		data.putInt("monthlyCost", monthlyCost);
		data.putInt("totalCost", totalCost);
		data.putInt("maxCashPayment", maxCashPayment);
		data.putBoolean("useMaxCashPayment", useMaxCashPayment);
		data.putDouble("interest", interest);
		data.putInt("totalBuyPrice", (int) truncate(truncate(totalBuyPrice)));
		data.putInt("cashPayment", (int) truncate(truncate(cashPayment)));
		data.putInt("leftOverCash", leftOverCash);
		data.putInt("householdType", householdType);

		MortgageResultFragment mrFragment = new MortgageResultFragment();

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// Adding a fragment to the fragment transaction
		mrFragment.setArguments(data);
		ft.replace(R.id.content_frame, mrFragment);

		ft.commit();
	}

	private void fetchValues() {
		adults = Integer.valueOf(householdAdultsSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		children = Integer.valueOf(householdChildrenSpinner.getSelectedItem()
				.toString().replaceAll("\\D+", ""));
		cars = Integer.valueOf(householdCarSpinner.getSelectedItem().toString()
				.replaceAll("\\D+", ""));
		
		String sIncome = incomeSalaryEditText.getText().toString().replaceAll("\\D+", "");
		if(!sIncome.equals(""))
			income = Integer.valueOf(sIncome);
		else
			income = 0;
		
		String sIncomeOtherEditText = incomeOtherEditText.getText().toString().replaceAll("\\D+", "");
		if(!sIncomeOtherEditText.equals(""))
			otherIncome = Integer.valueOf(sIncomeOtherEditText);
		else
			otherIncome = 0;
		totalIncome = income + otherIncome;

		householdType = newHouseholdSpinner.getSelectedItemPosition();
		
		String sMaintenenceCost = costNewLivingOperatingCostEditText.getText().toString().replaceAll("\\D+", "");
		if(!sMaintenenceCost.equals(""))
			maintenenceCost = Integer.valueOf(sMaintenenceCost);
		else
			maintenenceCost = 0;
		
		String sMonthlyCost = costNewLivingMonthlyFeeEditText.getText().toString().replaceAll("\\D+", "");
		if(!sMonthlyCost.equals(""))
			monthlyCost = Integer.valueOf(sMonthlyCost);
		else
			monthlyCost = 0;
		
		String sOtherCosts = otherCostsEditText.getText().toString().replaceAll("\\D+", "");
		if(!sOtherCosts.equals(""))
			otherCosts = Integer.valueOf(sOtherCosts);
		else
			otherCosts = 0;

		int adultCosts = 0;
		if (adults == 1)
			adultCosts = COST_ONE_ADULT;
		else
			adultCosts = COST_TWO_ADULTS;
		totalCost = otherCosts + maintenenceCost + monthlyCost + adultCosts
				+ children * COST_CHILDREN + cars * COST_CAR + CASH_OVER;

		useMaxCashPayment = useMaxCashPaymentCheckBox.isChecked();
		if (useMaxCashPayment) {
			String sMaxCashPayment = cashPaymentUnitEditText.getText().toString().replaceAll("\\D+", "");
			if(!sMaxCashPayment.equals(""))
				maxCashPayment = Integer.valueOf(sMaxCashPayment);
			else
				maxCashPayment = 0;
		}

		Log.d("Bolanekollen", "adults: " + adults);
		Log.d("Bolanekollen", "children: " + children);
		Log.d("Bolanekollen", "cars: " + cars);
		Log.d("Bolanekollen", "totalIncome: " + totalIncome);
		Log.d("Bolanekollen", "totalCost: " + totalCost);
		Log.d("Bolanekollen", "useMaxCashPayment: " + useMaxCashPayment);
		Log.d("Bolanekollen", "householdType: " + householdType);
	}

	private boolean isVariableAvaliable(Bundle b, String key) {
		int a = b.getInt(key, -1);
		if (a == -1)
			return false;
		else
			return true;
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

	private static String prettifyString3(Double d) {
		return prettifyString(d.intValue());
	}

	protected double truncate(double d) {
		return Math.floor(d / 1000) * 1000;
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
