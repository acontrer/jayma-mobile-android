package cl.citiaps.informatica.mensajeriaemergencia.activity;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.CheckImportantContactData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ContactData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ResponseData;
import cl.citiaps.informatica.mensajeriaemergencia.rest.RestService;
import cl.citiaps.informatica.mensajeriaemergencia.rest.SetImportantContactData;
import cl.citiaps.informatica.mensajeriaemergencia.task.CheckImportantContactsTaskParams;
import retrofit2.Call;
import retrofit2.Response;

public class SetImportantContactsActivity extends AppCompatActivity {

    private ArrayList<String> phoneNumbers;
    private ListView importantContactslistView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_important_contacts);

        phoneNumbers = new ArrayList<>();
        importantContactslistView = (ListView) findViewById(R.id.activity_set_important_contacts);


        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumbers.add(phoneNumber);
            Log.d(Constants.LOG_CONTACTS, "Teléfono: " + phoneNumber);
        }

        Context appContext = getApplicationContext();
        SharedPreferences sharedPref = appContext.getSharedPreferences(
                Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        int userId = sharedPref.getInt(
                Constants.SHARED_PREFERENCES_USER_ID, 0);

        if (userId != 0){
            CheckImportantContactsTaskParams params = new CheckImportantContactsTaskParams(phoneNumbers, userId);
            new CheckImportantContactsTask().execute(params);
        }




    }


    private class CheckImportantContactsTask extends AsyncTask<CheckImportantContactsTaskParams, Void, ArrayList<ContactData>> {

        @Override
        protected ArrayList<ContactData> doInBackground(CheckImportantContactsTaskParams... parameters) {

            RestService restService = RestService.retrofit.create(RestService.class);
            Call<CheckImportantContactData> call = restService.checkImportantContacts(
                    parameters[0].getPhoneNumbers(), parameters[0].getUserId());

            try {

                Response<CheckImportantContactData> response = call.execute();

                if (response.code() == 500 || response.body().getError()){

                    if (response.code() == 500){
                        Log.d(Constants.LOG_CONTACTS, response.errorBody().toString());

                    }
                    else {
                        Log.d(Constants.LOG_CONTACTS, response.body().getError_message());

                    }

                    return new ArrayList<ContactData>();
                }

                else{
                    return response.body().getContacts();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<ContactData>();
            }

        }


        @Override
        protected void onPostExecute(ArrayList<ContactData> contacts){

            SetImportantContactListAdapter setImportantContactListAdapter =
                    new SetImportantContactListAdapter(contacts, getBaseContext());
            importantContactslistView.setAdapter(setImportantContactListAdapter);
        }
    }



    public class SetImportantContactListAdapter extends BaseAdapter {

        private ArrayList<ContactData> contacts;
        private Context context;
        private LayoutInflater layoutInflater;

        public SetImportantContactListAdapter(ArrayList<ContactData> contacts, Context context) {
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
        public ContactData getItem(int position) {

            return this.contacts.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = layoutInflater.inflate(R.layout.item_set_important_contact_list, null);

            TextView userNameText =(TextView)convertView.findViewById(R.id.user_name_set_important_contact_text);
            userNameText.setText(this.contacts.get(position).getFullName());

            ImageButton isImportantButton =(ImageButton) convertView.findViewById(R.id.is_important_set_important_contact_button);
            if(this.contacts.get(position).getIs_important()){

                isImportantButton.setImageResource(R.drawable.star_black);
                isImportantButton.setTag(R.string.is_important_button_tag, true);
            }
            else{
                isImportantButton.setImageResource(R.drawable.star_white);
                isImportantButton.setTag(R.string.is_important_button_tag, false);
            }
            isImportantButton.setOnClickListener(setImportantContact);
            // TextView distanceText =(TextView)convertView.findViewById(R.id.emergencyDistanceText);
            // distanceText.setText(this.contacts.get(position));
            return convertView;
        }


        private View.OnClickListener setImportantContact = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView listView = (ListView) view.getParent().getParent();
                int position = listView.getPositionForView((LinearLayout)view.getParent());
                int isImportant = getItem(position).getUser_id();

                Context appContext = context.getApplicationContext();
                SharedPreferences sharedPref = appContext.getSharedPreferences(
                        Constants.SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

                int importantTo = sharedPref.getInt(
                        Constants.SHARED_PREFERENCES_USER_ID, 0);

                SetImportantContactTaskParams params = new SetImportantContactTaskParams(
                        position, isImportant, importantTo);
                toggleSetImportantButton(position);

                new SetImportantContactTask().execute(params);



            }

        };

        private class SetImportantContactTask extends AsyncTask<SetImportantContactTaskParams, Void, Boolean> {

            private int position;

            @Override
            protected Boolean doInBackground(SetImportantContactTaskParams... parameters) {

                position = parameters[0].getPosition();
                SetImportantContactData setImportantContactData = new SetImportantContactData(
                        parameters[0].getIsImportant(), parameters[0].getImportantTo());
                RestService restService = RestService.retrofit.create(RestService.class);
                Call<ResponseData> call = restService.setImportantContact(setImportantContactData);

                try {

                    Response<ResponseData> response = call.execute();

                    if (response.code() == 500 || response.body().isError()){

                        if (response.code() == 500){
                            Log.d(Constants.LOG_CONTACTS, response.errorBody().toString());

                        }
                        else {
                            Log.d(Constants.LOG_CONTACTS, response.body().getError_message());

                        }

                        return false;
                    }

                    else{
                        return true;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            }


            @Override
            protected void onPostExecute(Boolean okResponse){

                if(! okResponse){

                    toggleSetImportantButton(position);
                }

            }
        }

        private void toggleSetImportantButton(int position){

            LinearLayout linearLayout = (LinearLayout) getViewByPosition(position);
            ImageButton imageButton = (ImageButton) linearLayout.findViewById(
                    R.id.is_important_set_important_contact_button);

            Boolean isImportant = (Boolean) imageButton.getTag(R.string.is_important_button_tag);

            if(!isImportant){

                imageButton.setImageResource(R.drawable.star_black);
                imageButton.setTag(R.string.is_important_button_tag, true);
            }
            else{
                imageButton.setImageResource(R.drawable.star_white);
                imageButton.setTag(R.string.is_important_button_tag, false);
            }
        }

        public View getViewByPosition(int pos) {
            final int firstListItemPosition = importantContactslistView.getFirstVisiblePosition();
            final int lastListItemPosition = firstListItemPosition + importantContactslistView.getChildCount() - 1;

            if (pos < firstListItemPosition || pos > lastListItemPosition ) {
                return importantContactslistView.getAdapter().getView(pos, null, importantContactslistView);
            } else {
                final int childIndex = pos - firstListItemPosition;
                return importantContactslistView.getChildAt(childIndex);
            }
        }


        private class SetImportantContactTaskParams{

            private int position;
            private int isImportant;
            private int importantTo;

            public SetImportantContactTaskParams(int position, int isImportant, int importantTo) {
                this.position = position;
                this.isImportant = isImportant;
                this.importantTo = importantTo;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public int getIsImportant() {
                return isImportant;
            }

            public void setIsImportant(int isImportant) {
                this.isImportant = isImportant;
            }

            public int getImportantTo() {
                return importantTo;
            }

            public void setImportantTo(int importantTo) {
                this.importantTo = importantTo;
            }
        }
    }

}
