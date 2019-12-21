package com.example.fellowtraveller.BetaActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fellowtraveller.HomeBetaActivity;
import com.example.fellowtraveller.JsonApi;
import com.example.fellowtraveller.MainActivity;
import com.example.fellowtraveller.Notification;
import com.example.fellowtraveller.NotificationAdapter;
import com.example.fellowtraveller.Profile;
import com.example.fellowtraveller.R;
import com.example.fellowtraveller.Settings;
import com.example.fellowtraveller.Status_handling;
import com.example.fellowtraveller.Trip;
import com.example.fellowtraveller.TripPageCreatorActivity;
import com.example.fellowtraveller.User;
import com.example.fellowtraveller.Wallet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Notification> mExampleList;
    private JsonApi jsonPlaceHolderApi;
    private Retrofit retrofit ;
    private final String FILE_NAME = "fellow_login_state.txt";
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private  BottomNavigationView bottomNavigationView;
    private  CircleImageView circleImageView;
    private int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        Toolbar toolbar =  findViewById(R.id.home_appBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        navigationView.getMenu().getItem(0).setChecked(true);



        bottomNavigationView = findViewById(R.id.notificationActivity_Bottom_Nav);
        bottomNavigationView.setSelectedItemId(R.id.bottom_nav_notification);


        BottonNav();


        loadUserInfo();

        retrofit = new Retrofit.Builder().baseUrl("http://snf-871339.vm.okeanos.grnet.gr:5000/").addConverterFactory(GsonConverterFactory.create()).build();
        jsonPlaceHolderApi = retrofit.create(JsonApi.class);
        mExampleList = new ArrayList<>();
        getNotifications();

       // loadImageFromStorage();
    }


    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.NotificationActivity_RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(NotificationActivity.this);
        mAdapter = new NotificationAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(NotificationActivity.this, position+1+"",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NotificationActivity.this, TripPageCreatorActivity.class);
                intent.putExtra("Trip",mExampleList.get(position).getTrip());
                startActivity(intent);
                mExampleList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }



    public void BottonNav(){

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.bottom_nav_chat:
                                /*Intent a = new Intent(HomeBetaActivity.this, Profile.class);
                                startActivity(a);
                                finish();*/
                                break;
                            case R.id.bottom_nav_home:
                                Intent a = new Intent(NotificationActivity.this, HomeBetaActivity.class);
                                startActivity(a);
                                overridePendingTransition(0,0);
                                finish();
                                break;
                            case R.id.bottom_nav_notification:

                                break;
                        }
                        return true;
                    }
                });

    }

    public void getNotifications() {
        int id = loadUserId();
        Log.i("NotificationDev","id: "+id);
        Call<List<Notification>> call = jsonPlaceHolderApi.getNotificationOfUser(id);
        call.enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> mcall, Response<List<Notification>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NotificationActivity.this,"responseb "+response.message(),Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Notification> notifications = response.body();
                for (int i=0; i<notifications.size(); i++){
                    mExampleList.add(notifications.get(i));
                }
                buildRecyclerView();
            }
            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                Toast.makeText(NotificationActivity.this,"Δεν υπάρχουν ειδοποιήσεις",Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void ReadNot(int id){
        final int  not_id = id;
        Call<Status_handling> call = jsonPlaceHolderApi.NotificationRead(not_id);
        call.enqueue(new Callback<Status_handling>() {
            @Override
            public void onResponse(Call<Status_handling> mcall, Response<Status_handling> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NotificationActivity.this,"responseb "+response.message(),Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.i("NotificationDev","not_id: "+not_id);
            }
            @Override
            public void onFailure(Call<Status_handling> call, Throwable t) {
                Toast.makeText(NotificationActivity.this,"Δεν υπάρχουν ειδοποιήσεις",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int loadUserId() {
        int id =0;
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
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

    public void save() {
        String text = "false";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        closeDrawer();
        switch (item.getItemId()){
            case R.id.home:
                Intent a = new Intent(NotificationActivity.this, HomeBetaActivity.class);
                startActivity(a);
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.profile:
                Intent b = new Intent(NotificationActivity.this, Profile.class);
                startActivity(b);
                finish();
                break;
            case R.id.wallet:
                Intent s = new Intent(NotificationActivity.this, Wallet.class);
                startActivity(s);
                finish();
                break;
            case R.id.settings:
                Intent i = new Intent(NotificationActivity.this, Settings.class);
                startActivity(i);
                finish();
                break;
            case R.id.logout:
                save();
                Intent j = new Intent(NotificationActivity.this, MainActivity.class);
                startActivity(j);
                finish();
                break;
        }
        return true;
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }


    private void openDrawer(){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            super.onBackPressed();
    }


    public void loadUserInfo() {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            View header = navigationView.getHeaderView(0);
            TextView name = header.findViewById(R.id.user_name_drawer);
            TextView email = header.findViewById(R.id.user_email_drawer);
            int i = 0;
            while ((text = br.readLine()) != null) {
                if (i==2){
                    name.setText(text);
                }else if(i==3){
                    email.setText(text);
                }else if(i==1){
                    id = Integer.parseInt(text);
                }
                i++;
            }
            //String t = "name : "+name.getText()+"\n"+"email: "+email.getText()+"\n"+"id : "+id;
            //Toast.makeText(MainHomeActivity.this,t,Toast.LENGTH_SHORT).show();
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
    }

    public void save(String status,String id,String name,String email) {
        String text = status+"\n"+id+"\n"+name+"\n"+email;
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadImageFromStorage()
    {
        circleImageView = navigationView.getHeaderView(0).findViewById(R.id.nav_user_pic);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        try {
            File f = new File(directory, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            circleImageView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
}
