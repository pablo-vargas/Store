package com.foxdigitaltech.store.ui.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.foxdigitaltech.store.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotifyActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView notFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        recyclerView = findViewById(R.id.recyclerViewNotify);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        notFound = findViewById(R.id.textNotify);
        getNot();

    }

    private void getNot(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NotificationOrder> notificationOrders = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    notificationOrders.add(new NotificationOrder(dataSnapshot));
                }

                recyclerView.setAdapter(new NotifyAdapter(notificationOrders));
                recyclerView.setVerticalScrollbarPosition(notificationOrders.size() ==0?0:notificationOrders.size()-1);
                if(notificationOrders.size() ==0 ){
                    notFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                notFound.setVisibility(View.VISIBLE);
            }
        };
        FirebaseDatabase.getInstance().getReference("user-notification").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).limitToLast(8).addListenerForSingleValueEvent(valueEventListener);
    }
}