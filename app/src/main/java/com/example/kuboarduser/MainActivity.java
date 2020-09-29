package com.example.kuboarduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private ChatFragment chatFragment;
    private BoardFragment boardFragment;
    private CalendarFragment calendarFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_board:
                        setFragment(0);
                        break;
                    case R.id.action_chat:
                        setFragment(1);
                        break;
                    case R.id.action_calendar:
                        setFragment(2);
                        break;
                }
                return true;
            }
        });

        chatFragment = new ChatFragment();
        boardFragment = new BoardFragment();
        calendarFragment = new CalendarFragment();

        // 첫 화면(게시판) 출력
        setFragment(0);
    }

    // Fragment 교체
    private void setFragment(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, boardFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, chatFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, calendarFragment);
                ft.commit();
                break;
        }

    }
}