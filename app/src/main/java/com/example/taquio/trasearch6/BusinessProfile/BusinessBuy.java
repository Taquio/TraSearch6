package com.example.taquio.trasearch6.BusinessProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.example.taquio.trasearch6.CustomAdapter;
import com.example.taquio.trasearch6.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Del Mar on 2/24/2018.
 */

public class BusinessBuy extends AppCompatActivity {


    DatabaseReference databaseReference;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_buy_activity);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Materials").child("Types");
        myListView = findViewById(R.id.listView);
        final ArrayList<String> arrayList = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.
                                                getChildren()) {
                    arrayList.add(String.valueOf(postSnapshot.getValue()));
                }
                ArrayAdapter adapter = new ArrayAdapter(BusinessBuy.this,android.R.layout.simple_list_item_1,arrayList);
                myListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BusinessBuy.this, "There was an error retrieving the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
