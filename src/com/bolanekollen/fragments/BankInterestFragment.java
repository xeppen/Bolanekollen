package com.bolanekollen.fragments;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bolanekollen.R;
import com.bolanekollen.util.Bank;
import com.bolanekollen.util.BankInterestAdapter;

public class BankInterestFragment extends Fragment {

	TextView repayTextView;
	List<Bank> banks = new ArrayList<Bank>();
	BankInterestAdapter adapter;
	ListView bankList;
	TextView BankInterestInformationHeadline;
	static String PREFS_NAME = "Bolåneappen";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Retrieving the currently selected item number
		int position = getArguments().getInt("position");

		// List of rivers
		String[] menus = getResources().getStringArray(R.array.menus);

		// Set app name
		String app_name = getResources().getString(R.string.app_name);
		getActivity().getActionBar().setTitle(app_name);

		// Creating view corresponding to the fragment
		View v = inflater.inflate(R.layout.activity_bank_interest_layout,
				container, false);

		bankList = (ListView) v.findViewById(R.id.bankInterestList);
		BankInterestInformationHeadline = (TextView) v
				.findViewById(R.id.BankInterestInformationHeadline);
		BankInterestInformationHeadline.setText(menus[position]);
		FetchBankDataTask task = new FetchBankDataTask(getActivity());
		task.execute(banks);

		return v;
	}

	private class FetchBankDataTask extends
			AsyncTask<List<Bank>, Integer, NodeList> {
		private Context mContext;
		private List<Bank> bankList;

		public FetchBankDataTask(Context context) {
			mContext = context;
		}

		@Override
		protected NodeList doInBackground(List<Bank>... params) {
			bankList = params[0];
			String xml = null;
			String date = null;

			String url = "http://smartkalkyl.se/rateapp.aspx?user=sebastian&pass=mk9zfB7rS";

			// xml =
			// xml =
			// "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <RootNode> <Banks> <Bank> <BankId>17</BankId> <BankName>Bluestep</BankName> <BankUrl>http://www.bluestep.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/bluestep.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>6,23</RateInterest> <RateDescription /> <RateBefore>6,27</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-08-13</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>1</BankId> <BankName>Danske Bank</BankName> <BankUrl>http://www.danskebank.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/danskebank.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,89</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-14</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,99</RateInterest> <RateDescription /> <RateBefore>2,89</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-06-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,04</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,4</RateInterest> <RateDescription /> <RateBefore>3,19</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,9</RateInterest> <RateDescription /> <RateBefore>3,59</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,45</RateInterest> <RateDescription /> <RateBefore>4,25</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>16</BankId> <BankName>Ekobanken</BankName> <BankUrl>http://www.ekobanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/ekobanken.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,25</RateInterest> <RateDescription /> <RateBefore>3,5</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-01-01</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,75</RateInterest> <RateDescription /> <RateBefore>4,25</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2012-10-02</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>5</BankId> <BankName>Handelsbanken</BankName> <BankUrl>http://www.handelsbanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/handelsbanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,91</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,06</RateInterest> <RateDescription /> <RateBefore>3,08</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,27</RateInterest> <RateDescription /> <RateBefore>3,22</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-05</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,73</RateInterest> <RateDescription /> <RateBefore>3,75</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,36</RateInterest> <RateDescription /> <RateBefore>4,47</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>6</BankId> <BankName>ICA Banken</BankName> <BankUrl>http://www.icabanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/icabanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>7</BankId> <BankName>Ikanobanken</BankName> <BankUrl>http://www.ikanobanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/ikanobanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>8</BankId> <BankName>Länsförsäkringar</BankName> <BankUrl>http://www.lansforsakringar.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/lansforsakringar.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,94</RateInterest> <RateDescription /> <RateBefore>2,98</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,1</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,27</RateInterest> <RateDescription /> <RateBefore>3,21</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,73</RateInterest> <RateDescription /> <RateBefore>3,66</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,52</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>19</BankId> <BankName>Marginalen</BankName> <BankUrl>http://www.marginalen.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/marginalen.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>1</RateType> <RateInterest>6,25</RateInterest> <RateDescription /> <RateBefore>7,75</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2011-11-09</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>9</BankId> <BankName>Nordea</BankName> <BankUrl>http://www.nordea.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/nordea.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,78</RateInterest> <RateDescription /> <RateBefore>2,98</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-05-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,81</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>2,99</RateInterest> <RateDescription /> <RateBefore>2,92</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,05</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,65</RateInterest> <RateDescription /> <RateBefore>3,5</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>11</BankId> <BankName>SBAB</BankName> <BankUrl>http://www.sbab.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sbab.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>12</BankId> <BankName>SEB</BankName> <BankUrl>http://www.seb.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/seb.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-04</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,91</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,09</RateInterest> <RateDescription /> <RateBefore>3,12</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,29</RateInterest> <RateDescription /> <RateBefore>3,21</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-04</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,77</RateInterest> <RateDescription /> <RateBefore>3,74</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,48</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-26</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>13</BankId> <BankName>Skandiabanken</BankName> <BankUrl>http://www.skandiabanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/skandiabanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,97</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-05-21</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,03</RateInterest> <RateDescription /> <RateBefore>2,95</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,26</RateInterest> <RateDescription /> <RateBefore>3,18</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,71</RateInterest> <RateDescription /> <RateBefore>3,52</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-11</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>27</BankId> <BankName>Sparbanken Öresund</BankName> <BankUrl>http://www.sparbankenoresund.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sparbanken_oresund.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,94</RateInterest> <RateDescription /> <RateBefore>3,01</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>20</BankId> <BankName>Svea Ekonomi</BankName> <BankUrl>http://www.sveadirekt.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sveaekonomi.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>1</RateType> <RateInterest>7,9</RateInterest> <RateDescription /> <RateBefore>8,4</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2011-08-15</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>14</BankId> <BankName>Swedbank</BankName> <BankUrl>http://www.swedbank.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/swedbank.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,91</RateInterest> <RateDescription /> <RateBefore>2,94</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,89</RateInterest> <RateDescription /> <RateBefore>2,92</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,06</RateInterest> <RateDescription /> <RateBefore>3,11</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,26</RateInterest> <RateDescription /> <RateBefore>3,31</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,74</RateInterest> <RateDescription /> <RateBefore>3,79</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,33</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> </Rates> </Bank> </Banks> </RootNode>";

			boolean refreshXML = checkXMLFreshness();
			Log.d("Seb", "refreshXML: " + refreshXML);
			// refreshXML = true;
			boolean NetworkAvailable = isNetworkAvailable(mContext);
			// NetworkAvailable = true;
			if (refreshXML) {
				if (NetworkAvailable) {
					try {
						// defaultHttpClient
						DefaultHttpClient httpClient = new DefaultHttpClient();
						HttpProtocolParams.setUserAgent(httpClient.getParams(),
								System.getProperty("http.agent"));
						HttpPost httpPost = new HttpPost(url);

						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						HttpEntity httpEntity = httpResponse.getEntity();
						xml = EntityUtils.toString(httpEntity);

						// clearSavePreferences();
						savePreferences(PREFS_NAME + "_xml", xml);
						savePreferences(PREFS_NAME + "_time",
								String.valueOf(System.currentTimeMillis()));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						xml = loadSavedPreferences("Bolåneappen_xml");
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						xml = loadSavedPreferences("Bolåneappen_xml");
					} catch (IOException e) {
						e.printStackTrace();
						xml = loadSavedPreferences("Bolåneappen_xml");
					}

				} else {
					xml = loadSavedPreferences("Bolåneappen_xml");
				}

			} else {
				xml = loadSavedPreferences("Bolåneappen_xml");
			}
			Document dom = getDomElement(xml);

			if (dom != null) {
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName("Bank");

				return nl;
			} else {
				Log.e("Bolanekollen", "ERROR fetching DOM element (was null) despite no saved preferences and apparently working connection");
				xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <RootNode> <Banks> <Bank> <BankId>17</BankId> <BankName>Bluestep</BankName> <BankUrl>http://www.bluestep.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/bluestep.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>6,23</RateInterest> <RateDescription /> <RateBefore>6,27</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-08-13</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>1</BankId> <BankName>Danske Bank</BankName> <BankUrl>http://www.danskebank.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/danskebank.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,89</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-14</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,99</RateInterest> <RateDescription /> <RateBefore>2,89</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-06-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,04</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,4</RateInterest> <RateDescription /> <RateBefore>3,19</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,9</RateInterest> <RateDescription /> <RateBefore>3,59</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,45</RateInterest> <RateDescription /> <RateBefore>4,25</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>16</BankId> <BankName>Ekobanken</BankName> <BankUrl>http://www.ekobanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/ekobanken.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,25</RateInterest> <RateDescription /> <RateBefore>3,5</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-01-01</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,75</RateInterest> <RateDescription /> <RateBefore>4,25</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2012-10-02</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>5</BankId> <BankName>Handelsbanken</BankName> <BankUrl>http://www.handelsbanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/handelsbanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,91</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,06</RateInterest> <RateDescription /> <RateBefore>3,08</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,27</RateInterest> <RateDescription /> <RateBefore>3,22</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-05</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,73</RateInterest> <RateDescription /> <RateBefore>3,75</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,36</RateInterest> <RateDescription /> <RateBefore>4,47</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-30</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>6</BankId> <BankName>ICA Banken</BankName> <BankUrl>http://www.icabanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/icabanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>7</BankId> <BankName>Ikanobanken</BankName> <BankUrl>http://www.ikanobanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/ikanobanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>8</BankId> <BankName>Länsförsäkringar</BankName> <BankUrl>http://www.lansforsakringar.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/lansforsakringar.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,94</RateInterest> <RateDescription /> <RateBefore>2,98</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,1</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,27</RateInterest> <RateDescription /> <RateBefore>3,21</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,73</RateInterest> <RateDescription /> <RateBefore>3,66</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,52</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-10</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>19</BankId> <BankName>Marginalen</BankName> <BankUrl>http://www.marginalen.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/marginalen.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>1</RateType> <RateInterest>6,25</RateInterest> <RateDescription /> <RateBefore>7,75</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2011-11-09</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>9</BankId> <BankName>Nordea</BankName> <BankUrl>http://www.nordea.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/nordea.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,78</RateInterest> <RateDescription /> <RateBefore>2,98</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-05-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,81</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>2,99</RateInterest> <RateDescription /> <RateBefore>2,92</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,15</RateInterest> <RateDescription /> <RateBefore>3,05</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,65</RateInterest> <RateDescription /> <RateBefore>3,5</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-13</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>11</BankId> <BankName>SBAB</BankName> <BankUrl>http://www.sbab.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sbab.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,92</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>12</BankId> <BankName>SEB</BankName> <BankUrl>http://www.seb.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/seb.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-04</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,88</RateInterest> <RateDescription /> <RateBefore>2,91</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,09</RateInterest> <RateDescription /> <RateBefore>3,12</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,29</RateInterest> <RateDescription /> <RateBefore>3,21</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-04</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,77</RateInterest> <RateDescription /> <RateBefore>3,74</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,48</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-26</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>13</BankId> <BankName>Skandiabanken</BankName> <BankUrl>http://www.skandiabanken.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/skandiabanken.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,97</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-05-21</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,87</RateInterest> <RateDescription /> <RateBefore>2,9</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,03</RateInterest> <RateDescription /> <RateBefore>2,95</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-11</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,26</RateInterest> <RateDescription /> <RateBefore>3,18</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-10-16</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,71</RateInterest> <RateDescription /> <RateBefore>3,52</RateBefore> <RateChange>True</RateChange> <RateBeforeDate>2013-09-11</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>27</BankId> <BankName>Sparbanken Öresund</BankName> <BankUrl>http://www.sparbankenoresund.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sparbanken_oresund.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,94</RateInterest> <RateDescription /> <RateBefore>3,01</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,93</RateInterest> <RateDescription /> <RateBefore>2,99</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-07-12</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,07</RateInterest> <RateDescription /> <RateBefore>3,09</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,31</RateInterest> <RateDescription /> <RateBefore>3,37</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,76</RateInterest> <RateDescription /> <RateBefore>3,89</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,37</RateInterest> <RateDescription /> <RateBefore>4,49</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-29</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>20</BankId> <BankName>Svea Ekonomi</BankName> <BankUrl>http://www.sveadirekt.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/sveaekonomi.jpg</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>1</RateType> <RateInterest>7,9</RateInterest> <RateDescription /> <RateBefore>8,4</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2011-08-15</RateBeforeDate> </Rate> </Rates> </Bank> <Bank> <BankId>14</BankId> <BankName>Swedbank</BankName> <BankUrl>http://www.swedbank.se</BankUrl> <BankImage>http://smartkalkyl.se/smartfiles/layout/banklogos/swedbank.png</BankImage> <Rates> <Rate> <RateDate>2013-11-03</RateDate> <RateType>2</RateType> <RateInterest>2,91</RateInterest> <RateDescription /> <RateBefore>2,94</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>3</RateType> <RateInterest>2,89</RateInterest> <RateDescription /> <RateBefore>2,92</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-09-15</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>4</RateType> <RateInterest>3,06</RateInterest> <RateDescription /> <RateBefore>3,11</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>5</RateType> <RateInterest>3,26</RateInterest> <RateDescription /> <RateBefore>3,31</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>6</RateType> <RateInterest>3,74</RateInterest> <RateDescription /> <RateBefore>3,79</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> <Rate> <RateDate>2013-11-03</RateDate> <RateType>7</RateType> <RateInterest>4,33</RateInterest> <RateDescription /> <RateBefore>4,4</RateBefore> <RateChange>False</RateChange> <RateBeforeDate>2013-10-31</RateBeforeDate> </Rate> </Rates> </Bank> </Banks> </RootNode>";
				dom = getDomElement(xml);
				Element docEle = dom.getDocumentElement();
				NodeList nl = docEle.getElementsByTagName("Bank");

				return nl;
			}
		}

		@Override
		protected void onPostExecute(NodeList nl) {
			// Clear bank list so no old data is left
			banks.clear();

			if (nl != null && nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Bank aBank = getBankInformation((Element) nl.item(i));
					if (aBank != null)
						banks.add(aBank);
				}
			}
			setCustomAdapter();
		}
	}

	private boolean checkXMLFreshness() {
		long time = System.currentTimeMillis();
		long savedTime = Long.parseLong(loadSavedPreferences(PREFS_NAME
				+ "_time"));
		savedTime += 1000 * 60 * 60 * 24;
		if (savedTime < time) {
			return true;
		} else {
			return false;
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

	private void savePreferences(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private void clearSavePreferences() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		Editor editor = sharedPreferences.edit();
		editor.clear().commit();
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

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

	public void setCustomAdapter() {
		adapter = new BankInterestAdapter(getActivity(), R.id.bankInterestList,
				banks);
		bankList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		
		bankList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Bank b = banks.get(position);
				final String url = b.getBankUrl();
				Log.d("Bolanekollen", "url: " + url);
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());

				// set title
				alertDialogBuilder.setTitle("Öppna länk?");

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setMessage("Vill du gå till \n" + url + "?")
						.setPositiveButton("Öppna",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								startActivity(browserIntent);
							}
						})
						.setNegativeButton("Stäng",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				
				/*
				// show it
				LayoutInflater inflater = alertDialog.getLayoutInflater();
				View dialoglayout = inflater.inflate(
						R.layout.dialog_open_browser_question,
						(ViewGroup) alertDialog.getCurrentFocus());
				
				TextView urlTV = (TextView) dialoglayout.findViewById(R.id.openURLTextView);
				urlTV.setText(url);
				
				alertDialog.setView(dialoglayout);*/
				alertDialog.show();
				
			}
		});
	}

	public boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void alert(String s) {
		Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
	}
}