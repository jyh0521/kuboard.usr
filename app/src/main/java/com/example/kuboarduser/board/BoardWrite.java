package com.example.kuboarduser.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuboarduser.main.MainActivity;
import com.example.kuboarduser.R;
import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usePHP.SetValuePHP;
import com.example.kuboarduser.usr.LoginPage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BoardWrite extends AppCompatActivity {
    // 글 작성 페이지
    private Button write_Btn;
    private Button back_Btn;
    private EditText title_Et;
    private EditText content_Et;

    // 게시판 내용
    private String usrId;
    private String title;
    private String content;
    private String date;

    // 데이터 통신
    public String url;
    public String param;
    public String result;
    private SetValuePHP setValuePHP;

    private String flag = "";
    private String idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_board);

        /////////////////// 초기화 /////////////////////
        title_Et = (EditText)findViewById(R.id.title_Et);
        content_Et = (EditText)findViewById(R.id.content_Et);
        write_Btn = (Button)findViewById(R.id.write_Btn);
        back_Btn = (Button)findViewById(R.id.back_Btn);
        ///////////////////////////////////////////////

        Intent curIntent = getIntent();
        flag = curIntent.getStringExtra("flag");

        if(flag.equals("update")) {
            title_Et.setText(curIntent.getStringExtra("title"));
            content_Et.setText(curIntent.getStringExtra("content"));
            idx = curIntent.getStringExtra("idx");
        }

        // 글 작성 버튼 클릭 시
        write_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 게시판에 넣을 값을 받아온다.
                usrId = PreferenceManager.getString(LoginPage.getContext(), "loginId");
                title = title_Et.getText().toString();
                content = content_Et.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);

                if(title.equals("")) {
                    startToast("제목을 입력해주세요.");
                }
                else if(content.equals("")) {
                    startToast("내용을 입력해주세요.");
                }
                else {
                    if(flag.equals("update")) {
                        updateBoard();
                    }
                    else {
                        writeBoard();
                    }

                    title_Et.setText("");
                    content_Et.setText("");

                    // 인텐트 전달하면서 기존 엑티비티 제거
                    Intent intent = new Intent(BoardWrite.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    BoardWrite.this.startActivity(intent);
                }
            }
        });

        //목록으로 버튼 클릭 시
        back_Btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 글 작성 함수
    public void writeBoard() {
        param = "id=" + usrId + "&date=" + date + "&title=" + title + "&content=" + content; // 수정
        url = "http://218.152.181.81/kuboard/board/boardWrite.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        startToast(result);
    }

    // 글 수정 함수
    public void updateBoard() {
        param = "idx=" + idx + "&date=" + date + "&title=" + title + "&content=" + content; // 수정
        url = "http://218.152.181.81/kuboard/board/boardUpdate.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        startToast(result);
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
