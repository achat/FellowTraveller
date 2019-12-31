package com.example.fellowtraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private static final String FILE_NAME = "fellow_login_state.txt";
    private Button btn_next_stage,button_back;
    private JsonApi jsonPlaceHolderApi;
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://snf-871339.vm.okeanos.grnet.gr:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;
    private Fragment fra;
    private RegisterStage1Fragment stage1 = new RegisterStage1Fragment() ;
    private RegisterStage2Fragment stage2 = new RegisterStage2Fragment();
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        jsonPlaceHolderApi = retrofit.create(JsonApi.class);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        fragmentManager = getSupportFragmentManager();
        btn_next_stage = findViewById(R.id.RegisterActivity_button_next);
        button_back = findViewById(R.id.RegisterActivity_button_back);


        fra = stage1;
        fragmentManager.beginTransaction().replace(R.id.register_stages_container,fra).commit();
        btn_next_stage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(fra.toString().equals("stage1") && stage1.Check()){
                    fra = stage2;
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.register_stages_container,fra).commit();
                }
                else if(fra.toString().equals("stage2") && stage2.Check()){
                    createUser();
                }
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                onBackPressed();

            }
        });
    }
    public void onBackPressed(){
        if(fra.toString().equals("stage1")){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
        else if(fra.toString().equals("stage2") ){
            fra = stage1;
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.register_stages_container,fra).commit();
        }
    }


    private void createUser(){
        final String name = stage1.getName();
        String birth = stage2.getDate();
        final String email =  stage1.getEmail();
        String password = stage1.getPassword();
        String phone = stage2.getNumber();


        Call<Status_handling> call = jsonPlaceHolderApi.createUser(name,birth,email,password,phone);
        FireBaseRegister(name, email, password, birth, phone);

        call.enqueue(new Callback<Status_handling> () {
            @Override
            public void onResponse(Call<Status_handling> mcall, Response<Status_handling> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,"response "+response.errorBody()+"\n"+"responseb "+response.message(),Toast.LENGTH_SHORT).show();
                    return;
                }
                Status_handling status = response.body();
                if(status.getStatus().equals("success")){
                    Toast.makeText(RegisterActivity.this,"Επιτυχής καταχώρηση",Toast.LENGTH_SHORT).show();
                    save("true",Integer.parseInt(status.getMsg())+"",name,email);
                    //we have to get the id of user and this id save it to the database
                    //Or we have to send the email of user as unique object

                    Intent intent = new Intent(RegisterActivity.this, HomeBetaActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                Toast.makeText(RegisterActivity.this,"Ανεπιτυχής καταχώρηση\n"+status.getMsg(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Status_handling> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"t: "+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void save(String status,String id,String name,String email) {
        //FireBase Database for storing the id of users
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
        HashMap<String, String> userMap = new HashMap<>();
        userMap.put("name",name);
        userMap.put("email", email);
        userMap.put("status", status);
        userMap.put("image","default");

        userDatabase.setValue(userMap);


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

    public void FireBaseRegister (String name, String email, String password, String birth, String phone){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //send to main main home activiy
                    //we have to get the id of user and this id save it to the database
                }else{
                    //error
                }
            }
        });

    }

}
