package com.example.kuboarduser;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usePHP.SetValuePHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    public CalendarView calendarView;
    public Button upt_Btn, del_Btn, save_Btn, add_Btn;
    public TextView diaryTextView;
    public EditText contextEditText;

    private View view;

    private String date;
    private String usrId;
    private String str = "";

    // Schedule Add 변수

    // Schedule Update 변수
    private String updateContent;

    // 일정 관리 변수
    private String mode;

    // 스케쥴 리스트 뷰
    private ListView listSchedule;
    private List<String> data = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // 값을 보내고 불러올 때 기본적으로 필요한 변수//////////////////
    public String url;
    public String param;
    public String result;
    private SetValuePHP setValuePHP;
    /////////////////////////////////////////////////////////////

    // 캘린더 화면 뿌려주기
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calendar_fragment, container, false);

        // 현재 사용자 ID를 불러온다.
        usrId =  PreferenceManager.getString(LoginPage.getContext(), "loginId");

        // Schedule 리스트 뷰
        listSchedule = (ListView) view.findViewById(R.id.listSchedule);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        listSchedule.setAdapter(adapter);

        // Fragment의 ID를 불러온다.
        diaryTextView = view.findViewById(R.id.diaryTextView);//날짜 출력 text
        save_Btn = view.findViewById(R.id.save_Btn);//저장 버튼
        add_Btn = view.findViewById(R.id.add_Btn); // 저장2 버튼
        del_Btn = view.findViewById(R.id.del_Btn);//삭제 버튼
        upt_Btn = view.findViewById(R.id.upt_Btn);//수정 버튼
        contextEditText = view.findViewById(R.id.contextEditText);//저장이 안되있다면 내용입력할 editText
        listSchedule = view.findViewById(R.id.listSchedule);//해당 날짜에 저장된 스케줄 출력

        // 맨 처음 선택된 날짜의 일정 받아오기
        Date currentTime = Calendar.getInstance().getTime();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentTime);
        diaryTextView.setText(date);
        getSchedule();

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

                listSchedule.setVisibility(View.INVISIBLE);
                getSchedule();
            }
        });

        // 저장 버튼 클릭 시
        save_Btn.setOnClickListener(new View.OnClickListener() {//저장 버튼이 눌렸을 때
            public void onClick(View view) {

                // mode가 add인 경우
                if(mode.equals("add")) {
                    str = contextEditText.getText().toString();//입력한 내용

                    if(str.equals("")) {
                        startToast("일정을 입력해주세요.");
                    }
                    else {
                        saveSchedule(str);
                    }
                }

                // mode가 update인 경우
                if(mode.equals("update")) {
                    str = contextEditText.getText().toString();
                    updateSchedule(str);
                }

                contextEditText.setText("");
                getSchedule();
            }
        });

        // 추가 버튼 클릭 시
        add_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                contextEditText.setVisibility(View.VISIBLE);//내용입력 칸
                save_Btn.setVisibility(View.VISIBLE);//저장 버튼
                del_Btn.setVisibility(View.INVISIBLE);
                upt_Btn.setVisibility(View.INVISIBLE);
                add_Btn.setVisibility(View.INVISIBLE);
                listSchedule.setVisibility(View.INVISIBLE);

                mode = "add";
            }
        });

        // 수정 버튼 클릭 시
        upt_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                contextEditText.setVisibility(View.VISIBLE);//내용입력 칸
                save_Btn.setVisibility(View.VISIBLE);//저장 버튼
                del_Btn.setVisibility(View.INVISIBLE);
                upt_Btn.setVisibility(View.INVISIBLE);
                add_Btn.setVisibility(View.INVISIBLE);
                listSchedule.setVisibility(View.INVISIBLE);

                contextEditText.setText(updateContent);
                mode = "update";
            }
        });

        // 삭제 버튼 클릭 시
        del_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deleteSchedule();
                updateContent = "";
                getSchedule();
            }
        });

        // 일정 중 하나가 선택된 경우
        listSchedule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 일정 선택 시 해당 일정을 받아온다.
                updateContent = (String)parent.getAdapter().getItem(position);
            }
        });

        return view;
    }

    // 일정 받아오는 함수
    public void getSchedule() {
        // ListView 데이터 초기화
        data.clear();
        adapter.clear();

        // 디비에서 date에 해당하는 일정들을 불러오기
        param = "id=" + usrId + "&date=" + date; // 수정
        url = "http://218.152.181.81/kuboard/calendar/calendarSearch.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        if(result.equals("no_sch")){//일정X
            listSchedule.setVisibility(View.INVISIBLE);
            contextEditText.setVisibility(View.VISIBLE);//내용입력 칸
            save_Btn.setVisibility(View.VISIBLE);//저장 버튼
            del_Btn.setVisibility(View.INVISIBLE);
            upt_Btn.setVisibility(View.INVISIBLE);
            add_Btn.setVisibility(View.INVISIBLE);
            mode = "add";
        }
        else {
            listSchedule.setVisibility(View.VISIBLE);// 일정 내용
            contextEditText.setVisibility(View.INVISIBLE);
            save_Btn.setVisibility(View.INVISIBLE);//저장 버튼
            del_Btn.setVisibility(View.VISIBLE);//삭제 버튼
            upt_Btn.setVisibility(View.VISIBLE);//수정 버튼
            add_Btn.setVisibility(View.VISIBLE);

            // Schedule을 불러와 ListView에 Item 더하기
            try {
                JSONObject root = new JSONObject(result);

                JSONArray ja = root.getJSONArray("result");

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String cal_sch = jo.getString("cal_sch");
                    data.add(cal_sch);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 새로운 날짜를 불러올 경우 맨 처음 Schedule을 저장
            updateContent = data.get(0);

            // ListView 그리기
            adapter.notifyDataSetChanged();

            // ListView에서 항상 맨 위에 선택
            listSchedule.requestFocusFromTouch();
            listSchedule.setSelection(0);
        }
    }

    // 저장 버튼 이벤트
    public void saveSchedule(String content) {
        // 디비에 입력한 일정 저장하기
        param = "id=" + usrId + "&date=" + date + "&content=" + content; // 수정
        url = "http://218.152.181.81/kuboard/calendar/calendarSave.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        System.out.println("result: " + result);

        startToast(result);

    }

    // 수정 버튼 이벤트
    public void updateSchedule(String content) {
        param = "id=" + usrId + "&date=" + date + "&content=" + content + "&updateContent=" + updateContent; // 수정
        url = "http://218.152.181.81/kuboard/calendar/calendarUpdate.php"; // 수정, 자신의 주소 입력

        setValuePHP = new SetValuePHP(url, param);
        setValuePHP.start();

        try {
            setValuePHP.join();
            result = setValuePHP.returnResult();
        } catch (InterruptedException e) {
        }

        startToast(result);
    }

    // 삭제 버튼 이벤트
    public void deleteSchedule() {
        param = "id=" + usrId + "&date=" + date + "&updateContent=" + updateContent; // 수정
        url = "http://218.152.181.81/kuboard/calendar/calendarDelete.php"; // 수정, 자신의 주소 입력

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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
