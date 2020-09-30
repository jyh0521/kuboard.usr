package com.example.kuboarduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button btn_login;
    private Button btn_signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 로그인 버튼을 누른 경우, 인증 성공 시 메인으로 이동
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signIn() {
        String email = ((EditText)findViewById(R.id.editText_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();

        // 이메일, 비밀번호, 비밀번호 확인 칸이 비어있는지 확인
        if(email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // 로그인 성공 시
                        FirebaseUser user = mAuth.getCurrentUser();
                        startToast("로그인에 성공하였습니다.");

                        // 로그인 페이지에서 메인 화면으로 이동
                        Intent intent = new Intent(LoginPage.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        // 로그인 실패 시
                        if (task.getException() != null) {
                            startToast(task.getException().toString());
                        }
                    }
                }
            });
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}