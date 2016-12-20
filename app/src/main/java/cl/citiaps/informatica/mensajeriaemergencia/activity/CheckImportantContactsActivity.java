package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.fragment.CheckImportantContactsListFragment;
import cl.citiaps.informatica.mensajeriaemergencia.fragment.CheckImportantContactsMapFragment;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ContactData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactAnswerData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactInEmergencyData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import retrofit2.Call;
import retrofit2.Response;

public class CheckImportantContactsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ArrayList<ImportantContactAnswerData> importantContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_important_contacts);

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(
                Constants.SHARED_PREFERENCES_USER_ID, 0);

        if (userId != 0) {
            new CheckImportantContactsInEmergencyTask().execute(userId);
        }
       /* mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager_check_important_contact);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_check_important_contact);
        tabLayout.setupWithViewPager(mViewPager);*/


    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_check_important_contacts, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return CheckImportantContactsListFragment.newInstance(importantContacts);
                    /*return PlaceholderFragment.newInstance(position + 1);*/

                case 1:

                    return CheckImportantContactsMapFragment.newInstance(importantContacts);
                    /*return PlaceholderFragment.newInstance(position + 1);*/

            }

            return null;

        }

        @Override
        public int getCount() {

            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Lista";
                case 1:
                    return "Mapa";
            }
            return null;
        }
    }



    private class CheckImportantContactsInEmergencyTask extends AsyncTask<Integer, Void, ArrayList<ImportantContactAnswerData>> {

        @Override
        protected ArrayList<ImportantContactAnswerData> doInBackground(Integer... userID) {

            RestService restService = RestService.retrofit.create(RestService.class);
            Call<ImportantContactInEmergencyData> call = restService.lastAnswersImportantContacts(userID[0]);

            try {

                Response<ImportantContactInEmergencyData> response = call.execute();

                if (response.code() == 500 || response.body().getError()) {

                    if (response.code() == 500) {
                        Log.d(Constants.LOG_CONTACTS, response.errorBody().toString());

                    } else {
                        Log.d(Constants.LOG_CONTACTS, response.body().getError_message());

                    }

                    return new ArrayList<ImportantContactAnswerData>();
                } else {
                    if (response.body().getContacts() == null){
                        return new ArrayList<ImportantContactAnswerData>();
                    }
                    return response.body().getContacts();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<ImportantContactAnswerData>();
            }

        }


        @Override
        protected void onPostExecute(ArrayList<ImportantContactAnswerData> contacts) {

            importantContacts = contacts;
             mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager_check_important_contact);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_check_important_contact);
            tabLayout.setupWithViewPager(mViewPager);
        }

    }
}
