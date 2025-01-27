package com.example.kuboarduser.usr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuboarduser.main.MainActivity;
import com.example.kuboarduser.R;
import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usePHP.SetValuePHP;

public class LoginPage extends AppCompatActivity {
    private String param = "";
    private String url;
    private String result;

    private EditText editTextId;
    private EditText editTextPw;

    private Button btn_login;
    private Button btn_signUp;

    private SetValuePHP setValuePHP;

    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        context = this;

        // 로그인 아이디가 데이터에 남아있는경우 지우기
        String chk = PreferenceManager.getString(LoginPage.getContext(), "loginId");
        if(!chk.equals("")) {
            PreferenceManager.removeKey(LoginPage.getContext(), "loginId");
        }

        // 로그인 버튼을 누른 경우, 인증 성공 시 메인으로 이동
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextId = (EditText)findViewById(R.id.editText_id);
                editTextPw = (EditText)findViewById(R.id.editText_password);
                login();
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

    private void login() {
        String id = editTextId.getText().toString();
        String pw = editTextPw.getText().toString();

        // 이메일, 비밀번호, 비밀번호 확인 칸이 비어있는지 확인
        if(id.length() > 0 && pw.length() > 0) {
            url = "http://218.152.181.81/kuboard/login/login.php";
            param = "id=" + id + "&pw=" + pw;
            setValuePHP = new SetValuePHP(url, param);
            setValuePHP.start();

            try {
                setValuePHP.join();
                result = setValuePHP.returnResult();
            } catch (InterruptedException e) {
            }

            // PHP 창에서 ID와 PW가 일치하는지 확인
            if(result.equals("login")) {
                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                startToast("로그인 되었습니다.");

                PreferenceManager.setString(context, "loginId", id);

                editTextId.setText("");
                editTextPw.setText("");

                startActivity(intent);
            } else {
                startToast("아이디 혹은 비밀번호를 다시 확인해주세요.");
            }

        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public static Context getContext() {
        return context;
    }
}