package com.example.kuboarduser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuboarduser.usePHP.SetValuePHP;

public class CalendarFragment extends Fragment {
    public CalendarView calendarView;
    public Button cha_Btn, del_Btn, save_Btn;
    public TextView diaryTextView,textView2,textView3;
    public EditText contextEditText;
    private View view;
    private String date;
    private String usrId = "1";
    public String str=null;

    // 값을 보내고 불러올 때 기본적으로 필요한 변수//////////////////
    public String url;
    public String param;
    public String result;
    private SetValuePHP setValuePHP;
    /////////////////////////////////////////////////////////////


  /*  @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.calendar_fragment);
        calendarView=view.findViewById(R.id.calendarView);
        diaryTextView=view.findViewById(R.id.diaryTextView);//날짜 출력 text
        save_Btn=view.findViewById(R.id.save_Btn);//저장 버튼
        del_Btn=view.findViewById(R.id.del_Btn);//삭제 버튼
        cha_Btn=view.findViewById(R.id.cha_Btn);//수정 버튼
        textView2=view.findViewById(R.id.textView2);//해당 날짜에 저장된 스케줄 출력
        textView3=view.findViewById(R.id.textView3);//CALENDAR 제목(건드릴 필요x)
        contextEditText=view.findViewById(R.id.contextEditText);//저장이 안되있다면 내용입력할 editText
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener (){//날짜가 변경될 때 이벤드를 받기 위한 리스너
            public void onSelectedDayChange(CalendarView view,int year, int month, int dayOfMonth){//선택된 날짜를 알려주는 메서드
                String strYear=Integer.toString(year);
                String strMonth=Integer.toString(month);
                String strDayOfMonth=Integer.toString(dayOfMonth);
                date=strYear+"-"+(strMonth+1)+"-"+strDayOfMonth;
            }
        });
    }*/


    // 캘린더 화면 뿌려주기

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_fragment, container, false);
        diaryTextView = view.findViewById(R.id.diaryTextView);//날짜 출력 text
        save_Btn=view.findViewById(R.id.save_Btn);//저장 버튼
        del_Btn=view.findViewById(R.id.del_Btn);//삭제 버튼
        cha_Btn=view.findViewById(R.id.cha_Btn);//수정 버튼
        textView3=view.findViewById(R.id.textView3);//CALENDAR 제목(건드릴 필요x)
        contextEditText=view.findViewById(R.id.contextEditText);//저장이 안되있다면 내용입력할 editText
        textView2 = view.findViewById(R.id.textView2);//해당 날짜에 저장된 스케줄 출력
        // 캘린더에 선택된 날짜 받아오기
        calendarView=view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener (){//날짜가 변경될 때 이벤드를 받기 위한 리스너
            public void onSelectedDayChange(CalendarView view,int year, int month, int dayOfMonth){//선택된 날짜를 알려주는 메서드
                String strYear=Integer.toString(year);
                String strMonth=Integer.toString(month + 1);
                String strDayOfMonth = Integer.toString(dayOfMonth);
                if(dayOfMonth < 10) {
                    strDayOfMonth = "0" + strDayOfMonth;
                }
                date=strYear+"-"+(strMonth)+"-"+strDayOfMonth;
                diaryTextView.setText(date);
                getSchedule();
            }
        });
        save_Btn.setOnClickListener(new View.OnClickListener() {//저장 버튼이 눌렸을 때
            public void onClick(View view) {
                str = contextEditText.getText().toString();//입력한 내용
                textView2.setText(str);
                save_Btn.setVisibility(View.INVISIBLE);
                cha_Btn.setVisibility(View.VISIBLE);//수정 버튼
                del_Btn.setVisibility(View.VISIBLE);//삭제 버튼
                contextEditText.setVisibility(View.INVISIBLE);
                textView2.setVisibility(View.VISIBLE);//저장된 일정

                System.out.println(str);
            }
        });

        return view;
    }

    public void getSchedule() {

        // 디비에서 date에 해당하는 일정들을 불러오기
        ////////////////////고정///////////////////////////////
        param = "id=" + usrId + "&date=" + date; // 수정
        url = "http://192.168.10.100/calendar.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }
        ////////////////////////고정////////////////////////////

        System.out.println(result);

        if(result.equals("no_sch")){//일정X
            diaryTextView.setVisibility(View.VISIBLE);//날짜
            contextEditText.setVisibility(View.VISIBLE);//내용입력 칸
            save_Btn.setVisibility(View.VISIBLE);//저장 버튼
            del_Btn.setVisibility(View.INVISIBLE);
            cha_Btn.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
        }
        else {
            diaryTextView.setVisibility(View.VISIBLE);//날짜
            contextEditText.setVisibility(View.INVISIBLE);
            save_Btn.setVisibility(View.INVISIBLE);//저장 버튼
            del_Btn.setVisibility(View.VISIBLE);//삭제 버튼
            cha_Btn.setVisibility(View.VISIBLE);//수정 버튼
            textView2.setVisibility(View.VISIBLE);// 일정 내용
            textView2.setText(result);
        }
    }

    public void saveSchedule() {

    }
}
/*
    TODO
    해당 날짜에 일정이 없을 때, EDITTEXT로 보여주기

    saveSchedule 함수
    1. 저장 버튼 클릭 시 contextEditText에 입력한 내용 DB에 저장
    (1) str에서 받아온 값과 날짜와 userId를 php에 넘겨준다.
    (1.5) echo로 php창에서 값을 잘 넘겨받는지 확인
    (2) php 창에서 insert into문 작성
    (3) db와 연동해서 usr_cal 테이블에 데이터 삽입
    (4) php창에서 반환값으로 결과값을 받아서 toast 메시지 출력

    deleteSchedule 함수
    2. 삭제 버튼 기능

    updateSchedule
    3. 수정 버튼 기능
 */
