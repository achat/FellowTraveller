package com.example.fellowtraveller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NewOfferActivity extends AppCompatActivity {
    private Button btn_next_stage,btn_back;
    private Fragment fra;
    private TextView tvStage1,tvStage2,tvStage3;
    private NewOfferStage1Fragment stage1 = new NewOfferStage1Fragment() ;
    private NewOfferStage2Fragment stage2 = new NewOfferStage2Fragment();
    private NewOfferStage3Fragment stage3 = new NewOfferStage3Fragment() ;
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_offer);
        fragmentManager = getSupportFragmentManager();
        btn_next_stage = findViewById(R.id.new_offer_button_next_fragment);
        btn_back = findViewById(R.id.new_offer_button_back);
        tvStage1 = findViewById(R.id.new_offer_textView_stage1);
        tvStage2 = findViewById(R.id.new_offer_textView_stage2);
        tvStage3 = findViewById(R.id.new_offer_textView_stage3);



        fra = stage1;
        fragmentManager.beginTransaction().replace(R.id.new_offer_container,fra).commit();
        btn_next_stage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(fra.toString().equals("stage1")){
                    fra = stage2;
                    tvStage1.setBackgroundResource(R.drawable.new_offer_btn_bottom_default);
                    tvStage2.setBackgroundResource(R.drawable.new_offer_btn_bottom_click);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.new_offer_container,fra).commit();
               }
                else if(fra.toString().equals("stage2")){
                    fra = stage3;
                    tvStage2.setBackgroundResource(R.drawable.new_offer_btn_bottom_default);
                    tvStage3.setBackgroundResource(R.drawable.new_offer_btn_bottom_click);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left).replace(R.id.new_offer_container,fra).commit();
                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(fra.toString().equals("stage1")){
                    NewOfferActivity.this.onBackPressed();
                }
                else if(fra.toString().equals("stage2")){
                    fra = stage1;
                    tvStage2.setBackgroundResource(R.drawable.new_offer_btn_bottom_default);
                    tvStage1.setBackgroundResource(R.drawable.new_offer_btn_bottom_click);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.new_offer_container,fra).commit();
                }
                else if(fra.toString().equals("stage3")){
                    fra = stage2;
                    tvStage3.setBackgroundResource(R.drawable.new_offer_btn_bottom_default);
                    tvStage2.setBackgroundResource(R.drawable.new_offer_btn_bottom_click);
                    fragmentManager.beginTransaction().setCustomAnimations(R.anim.enter_from_left,R.anim.exit_to_right).replace(R.id.new_offer_container,fra).commit();
                }
            }
        });

    }
}
