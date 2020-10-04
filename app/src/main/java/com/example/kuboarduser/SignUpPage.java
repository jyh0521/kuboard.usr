package com.example.kuboarduser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuboarduser.usePHP.SetValuePHP;

public class SignUpPage extends AppCompatActivity {
    private String url;
    private String param;
    private String result;
    private SetValuePHP setValuePHP;

    private Button btn_signUp;
    private Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

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

    private void signUp() {
        String id = ((EditText)findViewById(R.id.editText_id)).getText().toString();
        String pw = ((EditText)findViewById(R.id.editText_password)).getText().toString();
        String pwChk = ((EditText)findViewById(R.id.editText_passwordChk)).getText().toString();
        String name = ((EditText)findViewById(R.id.editText_name)).getText().toString();
        String major = ((EditText)findViewById(R.id.editText_major)).getText().toString();

        // 이메일, 비밀번호, 비밀번호 확인 칸이 비어있는지 확인
        if(id.length() > 0 && pw.length() > 0 && pwChk.length() > 0) {
            if(id.length() != 10) {
                startToast("학번은 10자리 입니다.");
            }
            else if (pw.equals(pwChk)) {
                url = "http://218.152.181.81/kuboard/signUp/signUp.php";
                param = "id=" + id + "&pw=" + pw + "&name=" + name + "&major=" + major;
                setValuePHP = new SetValuePHP(url, param);
                setValuePHP.start();

                try {
                    setValuePHP.join();
                    result = setValuePHP.returnResult();
                } catch (InterruptedException e) {
                }

                // PHP 창에서 ID와 PW가 일치하는지 확인
                if(result.equals("signUp")) {
                    Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                    startToast("회원가입 되었습니다.");
                    startActivity(intent);
                } else {
                    startToast("아이디가 중복됩니다.");
                }

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