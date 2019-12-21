package com.example.fellowtraveller;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProsforesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private OfferFragAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<TripB> Listoftrips ;
    private JsonApi jsonPlaceHolderApi;
    private Retrofit retrofit ;
    private TextView textError;
    private final String FILE_NAME = "fellow_login_state.txt";
    private View mMainView;
    private int id;

    public ProsforesFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_prosfores, container, false);

        retrofit = new Retrofit.Builder().baseUrl("http://snf-871339.vm.okeanos.grnet.gr:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolderApi = retrofit.create(JsonApi.class);

        Listoftrips = new ArrayList<>();

        getUserTrips();


        return mMainView;
    }

    @Override
    public void onStart(){
        Log.i("onStart","ProsforesFragment onStart");

        super.onStart();

    }


     public void buildRecyclerView(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerViewOffer);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new OfferFragAdapter(Listoftrips );
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OfferFragAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               Intent intent = new Intent(getActivity(), TripPageActivity.class);
                intent.putExtra("Trip", Listoftrips.get(position));
                startActivity(intent);

            }
        });


    }
    private void getUserTrips() {

        if(CheckInternetConnection()){
            Call<List<TripB>> call = jsonPlaceHolderApi.getTripsCreated(loadUserId());
            call.enqueue(new Callback<List<TripB>>() {
                @Override
                public void onResponse(Call<List<TripB>> mcall, Response<List<TripB>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(),"responsed "+response.message(),Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<TripB> trips = response.body();
                    for (int i=0; i<trips.size(); i++){
                        Listoftrips.add(trips.get(i));
                    }
                    buildRecyclerView(mMainView);

                }
                @Override
                public void onFailure(Call<List<TripB>> call, Throwable t) {
                    //Toast.makeText(getActivity(),"t: "+t.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_SHORT).show();
        }
    }
    public int loadUserId() {
        int id =0;
        FileInputStream fis = null;
        try {
            fis = getActivity().openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            int i = 0;
            while ((text = br.readLine()) != null) {
                if(i==1){
                    id =  Integer.parseInt(text);
                    break;
                }
                i++;
            }
            return id;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return id;
    }

    public boolean CheckInternetConnection(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}