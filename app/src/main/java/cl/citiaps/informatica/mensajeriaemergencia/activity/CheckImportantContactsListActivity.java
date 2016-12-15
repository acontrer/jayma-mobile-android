package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.CheckImportantContactData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.CheckUserInEmergencyData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ContactData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactAnswerData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactInEmergencyData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import cl.citiaps.informatica.mensajeriaemergencia.task.CheckImportantContactsTaskParams;
import retrofit2.Call;
import retrofit2.Response;

public class CheckImportantContactsListActivity extends AppCompatActivity {

    private ListView importantContactsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_important_contacts_list);

        importantContactsListView = (ListView) findViewById(R.id.activity_check_important_contacts_list);

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        int userId = sharedPref.getInt(
                Constants.SHARED_PREFERENCES_USER_ID, 0);

        if (userId != 0) {
            new CheckImportantContactsInEmergencyTask().execute(userId);
        }

        // ListView Item Click Listener
        importantContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImportantContactAnswerData importantContactAnswerData =  (ImportantContactAnswerData) importantContactsListView.getAdapter().getItem(position);

                if(importantContactAnswerData.isAnswer()){

                    Intent intentContactAnswerDetail = new Intent(getBaseContext(), ContactAnswerDetailActivity.class);

                    intentContactAnswerDetail.putExtra(Constants.EXTRA_FULL_NAME,
                            importantContactAnswerData.getFullName());
                    intentContactAnswerDetail.putExtra(Constants.EXTRA_CONTACT_IS_OK,
                            importantContactAnswerData.isOk());
                    intentContactAnswerDetail.putExtra(Constants.EXTRA_DATE,
                            importantContactAnswerData.getCreationDateTime().getTime());
                    intentContactAnswerDetail.putExtra(Constants.EXTRA_FULL_ADDRESS,
                            importantContactAnswerData.getFullAddress());
                    intentContactAnswerDetail.putExtra(Constants.EXTRA_LATITUDE,
                            importantContactAnswerData.getLatitude());
                    intentContactAnswerDetail.putExtra(Constants.EXTRA_LONGITUDE,
                            importantContactAnswerData.getLongitude());

                    startActivity(intentContactAnswerDetail);
                }


            }
        });
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

            CheckImportantContactsInEmergencyListAdapter checkImportantContactsInEmergencyListAdapter =
                    new CheckImportantContactsInEmergencyListAdapter(contacts, getBaseContext());
            importantContactsListView.setAdapter(checkImportantContactsInEmergencyListAdapter);
        }

    }

    public class CheckImportantContactsInEmergencyListAdapter extends BaseAdapter {

        private ArrayList<ImportantContactAnswerData> contacts;
        private Context context;
        private LayoutInflater layoutInflater;

        public CheckImportantContactsInEmergencyListAdapter(ArrayList<ImportantContactAnswerData> contacts, Context context) {
            super();
            this.contacts = contacts;
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return this.contacts.size();
        }

        @Override
        public ImportantContactAnswerData getItem(int position) {

            return this.contacts.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = layoutInflater.inflate(R.layout.item_check_important_contact, null);

            TextView userNameText = (TextView) convertView.findViewById(R.id.user_name_check_important_contact_text);
            userNameText.setText(this.contacts.get(position).getFullName());

            TextView emergencyText = (TextView) convertView.findViewById(R.id.emergency_name_check_important_contact_text);
            emergencyText.setText(this.contacts.get(position).getEmergencyName());

            ImageView isImportantButton = (ImageView) convertView.findViewById(R.id.answer_check_important_contact_image);
            if (this.contacts.get(position).isAnswer()) {

                if (this.contacts.get(position).isOk()) {
                    isImportantButton.setImageResource(R.drawable.contact_answer_ok);
                }
                else{
                    isImportantButton.setImageResource(R.drawable.contact_answer_problem);
                }

            } else {
                isImportantButton.setImageResource(R.drawable.contact_no_answer);
            }

            // TextView distanceText =(TextView)convertView.findViewById(R.id.emergencyDistanceText);
            // distanceText.setText(this.contacts.get(position));
            return convertView;
        }
    }
}
