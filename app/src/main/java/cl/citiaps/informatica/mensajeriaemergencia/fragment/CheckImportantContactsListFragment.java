package cl.citiaps.informatica.mensajeriaemergencia.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.activity.ContactAnswerDetailActivity;
import cl.citiaps.informatica.mensajeriaemergencia.constants.Constants;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactAnswerData;

/**
 * Created by kayjt on 19-12-2016.
 */


public class CheckImportantContactsListFragment extends ListFragment {

    private static final String ARG_CONTACT_LIST = "contact_list";

    public CheckImportantContactsListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CheckImportantContactsListFragment newInstance(
            ArrayList<ImportantContactAnswerData> contacts) {
        CheckImportantContactsListFragment fragment = new CheckImportantContactsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CONTACT_LIST, contacts);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.check_important_contacts_list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ArrayList<ImportantContactAnswerData> contacts = getArguments().getParcelableArrayList(ARG_CONTACT_LIST);
        CheckImportantContactsInEmergencyListAdapter adapter =
                new CheckImportantContactsInEmergencyListAdapter(contacts, getActivity());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImportantContactAnswerData importantContactAnswerData =  (ImportantContactAnswerData) getListView().getAdapter().getItem(position);

                if(importantContactAnswerData.isAnswer()){

                    Intent intentContactAnswerDetail = new Intent(getActivity(), ContactAnswerDetailActivity.class);

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
