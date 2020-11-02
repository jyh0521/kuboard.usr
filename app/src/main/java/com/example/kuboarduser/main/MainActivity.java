package com.example.kuboarduser.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.kuboarduser.R;
import com.example.kuboarduser.board.BoardFragment;
import com.example.kuboarduser.bustimetable.BusFragment;
import com.example.kuboarduser.calendar.CalendarFragment;
import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usr.LoginPage;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private BusFragment busFragment;
    private BoardFragment boardFragment;
    private CalendarFragment calendarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 하단바에서 선택된것에 따라 setFragment함수를 불러온다.
        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_board:
                        setFragment(0);
                        break;
                    case R.id.action_bus:
                        setFragment(1);
                        break;
                    case R.id.action_calendar:
                        setFragment(2);
                        break;
                }
                return true;
            }
        });

        busFragment = new BusFragment();
        boardFragment = new BoardFragment();
        calendarFragment = new CalendarFragment();

        // 첫 화면(게시판) 출력
        bottomNavigationView.setSelectedItemId(R.id.action_board);
        setFragment(0);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        startToast("로그아웃 되었습니다.");
        PreferenceManager.removeKey(LoginPage.getContext(), "loginId");
        moveTaskToBack(true);
        finishAndRemoveTask();
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
                ft.replace(R.id.main_frame, busFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, calendarFragment);
                ft.commit();
                break;
        }
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}