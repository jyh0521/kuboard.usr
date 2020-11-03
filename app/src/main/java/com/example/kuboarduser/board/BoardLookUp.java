package com.example.kuboarduser.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuboarduser.R;
import com.example.kuboarduser.main.MainActivity;
import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usePHP.SetValuePHP;
import com.example.kuboarduser.usr.LoginPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoardLookUp extends AppCompatActivity {
    // 글 조회 페이지
    private TextView title_Tv;
    private TextView content_Tv;
    private Button back_Btn;
    private Button upt_Btn;
    private Button del_Btn;

    // 게시판 내용
    private String idx;
    private String usrId;
    private String writtenId;
    private String title;
    private String content;

    // 데이터 통신
    public String url;
    public String param;
    public String result;
    private SetValuePHP setValuePHP;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_board);

        title_Tv = (TextView)findViewById(R.id.title_Tv);
        content_Tv = (TextView)findViewById(R.id.content_Tv);
        back_Btn = (Button)findViewById(R.id.back_Btn);
        upt_Btn = (Button)findViewById(R.id.upt_Btn);
        del_Btn = (Button)findViewById(R.id.del_Btn);

        usrId = PreferenceManager.getString(LoginPage.getContext(), "loginId");
        Intent curIntent = getIntent();
        idx = curIntent.getStringExtra("idx");

        getBoard();

        upt_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인텐트 전달하면서 기존 엑티비티 제거
                Intent intent = new Intent(BoardLookUp.this, BoardWrite.class);
                intent.putExtra("flag", "update");
                intent.putExtra("title", title);
                intent.putExtra("content", content);
                intent.putExtra("idx", idx);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BoardLookUp.this.startActivity(intent);
            }
        });

        del_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBoard();

                // 인텐트 전달하면서 기존 엑티비티 제거
                Intent intent = new Intent(BoardLookUp.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BoardLookUp.this.startActivity(intent);
            }
        });

        //목록으로 버튼 클릭 시
        back_Btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getBoard() {
        param = "idx=" + idx; // 수정
        url = "http://218.152.181.81/kuboard/board/boardLookUp.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        // php에서 불러온 값을 JSON 형태로 받아와서 출력 : 배열 형태
        try {
            JSONObject root = new JSONObject(result);

            JSONArray ja = root.getJSONArray("result");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                writtenId = jo.getString("id");
                title = jo.getString("title");
                content = jo.getString("content");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        title_Tv.setText(title);
        content_Tv.setText(content);

        // 글 작성한 ID와 사용자 ID가 같으면 수정, 삭제 버튼을 보여준다.
        if(writtenId.equals(usrId)) {
            upt_Btn.setVisibility(View.VISIBLE);
            del_Btn.setVisibility(View.VISIBLE);
        }
    }

    public void deleteBoard() {
        param = "idx=" + idx; // 수정
        url = "http://218.152.181.81/kuboard/board/boardDelete.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        startToast("삭제 되었습니다.");
    }

    // Toast 메시지 출력
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
