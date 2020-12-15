package com.foxdigitaltech.store.ui.about;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.about.view.AboutFragment;
import com.foxdigitaltech.store.ui.about.view.ContactFragment;
import com.google.android.material.tabs.TabLayout;

public class AboutActivity extends AppCompatActivity {
    TabLayout tabLayout;

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tabLayout = findViewById(R.id.tabs);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.contentMain,new AboutFragment()).commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() ==0){
                    fragmentManager.beginTransaction().replace(R.id.contentMain,new AboutFragment()).commit();
                }else{
                    fragmentManager.beginTransaction().replace(R.id.contentMain,new ContactFragment()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        
    }


}