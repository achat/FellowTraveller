package com.example.fellowtraveller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private static final String FILE_NAME = "fellow_login_state.txt";
    private RecyclerView recyclerView;
    private ChatConversationAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String id, yourId;
    private ArrayList<ChatConversationItem> conversations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


//        conversations.add(new ChatConversationItem("default","Γιώργος Ανδρεου", true, true, 542346363, "131", "132"));
//        conversations.add(new ChatConversationItem("default","Σεμπάστιαν Ανδρεου", false, true, 545345363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Τόλης Μόλης", true, false, 542345563,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Γιώργος Γεωργίου", false, false, 542545363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Μάκης Ανδρεου", true, true, 542345333,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Γιώργος Ανδρεου", true, true, 542346363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Σεμπάστιαν Ανδρεου", false, true, 545345363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Τόλης Μόλης", true, false, 542345563,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Γιώργος Γεωργίου", false, false, 542545363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Μάκης Ανδρεου", true, true, 542345333,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Γιώργος Ανδρεου", true, true, 542346363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Σεμπάστιαν Ανδρεου", false, true, 545345363,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Τόλης Μόλης", true, false, 542345563,"131", "132"));
//        conversations.add(new ChatConversationItem("default","Γιώργος Γεωργίου", false, false, 542545363,"131", "132"));

        loadConversations();

        yourId = getId();
        recyclerView = findViewById(R.id.chat_convs_recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatConversationAdapter(conversations);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ChatConversationAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String senderId = conversations.get(position).getSenderId();

                Intent intent1 = new Intent(Chat.this,ChatConversation.class);
                intent1.putExtra("Creator_id",senderId);
                startActivity(intent1);

                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void loadConversations(){
        DatabaseReference convs = FirebaseDatabase.getInstance().getReference();
        convs.child("Chat").child("109").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    String chatterId = childDataSnapshot.child("chatterId").getValue().toString();
                    long timestamp = childDataSnapshot.child("lastMessage").getValue(Long.class);
                    String image = childDataSnapshot.child("image").getValue(String.class);
                    String name = childDataSnapshot.child("name").getValue(String.class);

                    conversations.add(new ChatConversationItem(image, name, true, true, timestamp, yourId, chatterId));
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public String getId () {
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            String id = "-1";

            int i = 0;
            while ((text = br.readLine()) != null) {
                if (i == 1) {
                    id = text;
                    return id;

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
        return id;
    }

}
