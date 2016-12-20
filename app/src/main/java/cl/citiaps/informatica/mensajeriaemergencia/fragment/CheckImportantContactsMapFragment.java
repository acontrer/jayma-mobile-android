package cl.citiaps.informatica.mensajeriaemergencia.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import cl.citiaps.informatica.mensajeriaemergencia.R;
import cl.citiaps.informatica.mensajeriaemergencia.rest.ImportantContactAnswerData;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CheckImportantContactsMapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CheckImportantContactsMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckImportantContactsMapFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    MapView mapView;
    private GoogleMap googleMap;

    ArrayList<ImportantContactAnswerData> importantContactsAnswers;

    private static final String ARG_IMPORTANT_CONTACTS_ANSWERS = "param_important_contacts_answer";

    private OnFragmentInteractionListener mListener;

    public CheckImportantContactsMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CheckImportantContactsMapFragment newInstance(ArrayList<ImportantContactAnswerData> importantAnswers) {
        CheckImportantContactsMapFragment fragment = new CheckImportantContactsMapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_IMPORTANT_CONTACTS_ANSWERS, importantAnswers);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            importantContactsAnswers = getArguments().getParcelableArrayList(ARG_IMPORTANT_CONTACTS_ANSWERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_check_important_contacts_map, container, false);

        mapView = (MapView) rootView.findViewById(R.id.map_view_check_important_contacts_map);
        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (ImportantContactAnswerData answer :importantContactsAnswers) {

                    LatLng markerPos = new LatLng(answer.getLatitude(), answer.getLongitude());
                    builder.include(markerPos);
                    if (answer.isOk()){
                        googleMap.addMarker(new MarkerOptions()
                                .position(markerPos)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    else{
                        googleMap.addMarker(new MarkerOptions()
                                .position(markerPos)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }

                }
                if (importantContactsAnswers.size() > 0){
                    LatLngBounds bounds = builder.build();
                    int padding = 200; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    googleMap.moveCamera(cu);
                }

            }
        });

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
