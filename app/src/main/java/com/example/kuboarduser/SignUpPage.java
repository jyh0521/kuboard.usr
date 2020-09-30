package com.example.kuboarduser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "SignUpActivity";

    private Button btn_signUp;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 회원가입 버튼을 누른 경우
        btn_signUp = findViewById(R.id.btn_signUp);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 회원가입 실행
                signUp();
            }
        });

        // 취소 버튼을 누른 경우, 로그인 페이지로 이동
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
       // updateUI(currentUser);
    }

    private void signUp() {
        String email = ((EditText)findViewById(R.id.editText_id)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();
        String passwordChk = ((EditText)findViewById(R.id.editText_passwordChk)).getText().toString();

        // 이메일, 비밀번호, 비밀번호 확인 칸이 비어있는지 확인
        if(email.length() > 0 && password.length() > 0 && passwordChk.length() > 0) {
            if (password.equals(passwordChk)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원가입 성공 시
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("회원가입에 성공하였습니다.");
                                } else {
                                    // 회원가입 실패 시
                                    if (task.getException() != null) {
                                        // id가 이메일 형식이 아닐 시, 비밀번호가 6자가 아닐 시
                                        startToast(task.getException().toString());
                                    }
                                }
                            }
                        });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}