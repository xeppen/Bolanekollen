package com.bolanekollen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bolanekollen.fragments.BankInterestFragment;
import com.bolanekollen.fragments.BankLoanCostFragment;
import com.bolanekollen.fragments.HomeFragment;
import com.bolanekollen.fragments.MortgageFragment;
import com.bolanekollen.fragments.PrefsFragment;
import com.bolanekollen.fragments.PrivateLoanFragment;

public class MainActivity extends Activity {

	// Define Fragments
	HomeFragment hFragment = new HomeFragment();
	MortgageFragment mFragment = new MortgageFragment();
	BankLoanCostFragment blFragment = new BankLoanCostFragment();
	BankInterestFragment biFragment = new BankInterestFragment();
	PrivateLoanFragment plFragment = new PrivateLoanFragment();
	PrefsFragment pFragment = new PrefsFragment();

	// Within which the entire activity is enclosed
	private DrawerLayout mDrawerLayout;

	// ListView represents Navigation Drawer
	private ListView mDrawerList;

	// ActionBarDrawerToggle indicates the presence of Navigation Drawer in the
	// action bar
	private ActionBarDrawerToggle mDrawerToggle;

	// Title of the action bar
	private String mTitle = "";

	private Integer currentPosition = 0;
	private Integer previousPosition = 0;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// mTitle = "BolŒnekollen";
		// getActionBar().setTitle(mTitle);

		// Getting reference to the DrawerLayout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerList = (ListView) findViewById(R.id.drawer_list);

		// Getting reference to the ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			/** Called when drawer is closed */
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();

			}

			/** Called when a drawer is opened */
			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

		};

		// Setting DrawerToggle on DrawerLayout
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Creating an ArrayAdapter to add items to the listview mDrawerList
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(), R.layout.drawer_list_item, getResources()
						.getStringArray(R.array.menus));

		// Setting the adapter on mDrawerList
		mDrawerList.setAdapter(adapter);

		// Enabling Home button
		getActionBar().setHomeButtonEnabled(true);

		// Enabling Up navigation
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Setting item click listener for the listview mDrawerList
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Getting an array of rivers
				String[] menuItems = getResources().getStringArray(
						R.array.menus);

				// Getting reference to the FragmentManager
				FragmentManager fragmentManager = getFragmentManager();

				// Creating a fragment transaction
				FragmentTransaction ft = fragmentManager.beginTransaction();

				// Passing selected item information to fragment
				Bundle data = new Bundle();
				data.putInt("position", position);
				if (currentPosition != position) {
					currentPosition = position;
					if (position == 0) {
						hFragment.setArguments(data);

						// Adding a fragment to the fragment transaction
						ft.replace(R.id.content_frame, hFragment);
					} else if (position == 1) {
						mFragment.setArguments(data);

						// Adding a fragment to the fragment transaction
						ft.replace(R.id.content_frame, mFragment);
					} else if (position == 2) {
						blFragment.setArguments(data);

						// Adding a fragment to the fragment transaction
						ft.replace(R.id.content_frame, blFragment);
					} else if (position == 3) {
						biFragment.setArguments(data);

						// Adding a fragment to the fragment transaction
						ft.replace(R.id.content_frame, biFragment);
					} else if (position == 4) {
						plFragment.setArguments(data);

						// Adding a fragment to the fragment transaction
						ft.replace(R.id.content_frame, plFragment);
					}
					// Committing the transaction
					ft.commit();
				}

				// Closing the drawer
				mDrawerLayout.closeDrawer(mDrawerList);

			}
		});
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();

		Log.d("Bolånekollen", "Orientation change!");

		// Getting reference to the FragmentManager
		FragmentManager fragmentManager = getFragmentManager();

		// Creating a fragment transaction
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// Adding a fragment to the fragment transaction
		// ft.replace(R.id.content_frame, hFragment);

		// ft.commit();

		Bundle data = savedInstanceState;

		int position = currentPosition;
		if (position == 0) {
			hFragment.setArguments(data);

			// Adding a fragment to the fragment transaction
			ft.replace(R.id.content_frame, hFragment);
		} else if (position == 1) {
			mFragment.setArguments(data);

			// Adding a fragment to the fragment transaction
			ft.replace(R.id.content_frame, mFragment);
		} else if (position == 2) {
			blFragment.setArguments(data);

			// Adding a fragment to the fragment transaction
			ft.replace(R.id.content_frame, blFragment);
		} else if (position == 3) {
			biFragment.setArguments(data);

			// Adding a fragment to the fragment transaction
			ft.replace(R.id.content_frame, biFragment);
		} else if (position == 4) {
			plFragment.setArguments(data);

			// Adding a fragment to the fragment transaction
			ft.replace(R.id.content_frame, plFragment);
		}
		// Committing the transaction
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		/*
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		
		if(currentPosition == -1){
			
			int position = previousPosition;
			currentPosition = position;
			if (position == 0) {
				ft.replace(R.id.content_frame, hFragment);
			} else if (position == 1) {
				ft.replace(R.id.content_frame, mFragment);
			} else if (position == 2) {
				ft.replace(R.id.content_frame, blFragment);
			} else if (position == 3) {
				ft.replace(R.id.content_frame, biFragment);
			} else if (position == 4) {
				ft.replace(R.id.content_frame, plFragment);
			}
			// Committing the transaction
			ft.commit();
		} else{
			previousPosition = currentPosition;
			currentPosition = -1;
			
			ft.replace(R.id.content_frame, pFragment);
			ft.commit();
		}
		*/
		return super.onOptionsItemSelected(item);
	}

	/** Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the drawer is open, hide action items related to the content view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);

		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
