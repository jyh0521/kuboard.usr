package com.example.kuboarduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    private Button btn_login;
    private Button btn_signUp;

    //private FirebaseDatabase database;
    //private DatabaseReference databaseReference;
    //private ArrayList<USR_INFO> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // 로그인 버튼을 누른 경우, 인증 성공 시 메인으로 이동
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                // 파이어베이스 디비 연동
                database = FirebaseDatabase.getInstance();
                databaseReference = database.getReference("USR_INFO");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 디비의 데이터를 받아옴
                        arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // 반복문으로 데이터 List를 추출해냄
                            USR_INFO usrInfo = snapshot.getValue(USR_INFO.class); // 만들어뒀던 USR_INFO 객체에 데이터를 담는다.
                            arrayList.add(usrInfo); // 담은 데이터들을 배열리스트에 넣는다.
                        }
                    }

                    //String toast = arrayList.getUSR_ID();

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // 디비를 가져오던 중 에러 발생 시
                        Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
                    }
                });*/

                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startActivity(intent); // 로그인 페이지에서 메인 화면으로 이동
            }
        });

        // 회원가입 버튼을 누른 경우, 회원가입 페이지로 이동
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, SignUpPage.class);
                startActivity(intent);
            }
        });
    }
}