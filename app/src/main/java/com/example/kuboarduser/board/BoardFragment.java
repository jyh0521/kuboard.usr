package com.example.kuboarduser.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.kuboarduser.R;
import com.example.kuboarduser.board.BoardWrite;
import com.example.kuboarduser.preferenceManager.PreferenceManager;
import com.example.kuboarduser.usePHP.GetValuePHP;
import com.example.kuboarduser.usr.LoginPage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoardFragment extends Fragment {
    // 게시판 화면 뿌려주기
    private View view;
    private ListView boardlist;
    private List<String> data = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // fragment 교체
    private FragmentManager fm;
    private FragmentTransaction ft;

    // 게시판 불러오기
    private GetValuePHP getValuePHP;
    private String url;

    // 버튼
    private Button write_btn;

    // 선택된 게시판 id
    private String selectedId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.board_fragment, container, false);

        boardlist = (ListView) view.findViewById(R.id.boardlist);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        boardlist.setAdapter(adapter);

        getBoardList();

        write_btn = view.findViewById(R.id.write_btn);

        write_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BoardWrite.class);
                intent.putExtra("flag", "write");

                startActivity(intent);
            }
        });

        // 게시판 글 중 하나 선택 시
        boardlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedId = (String)parent.getAdapter().getItem(position).toString().substring(0, 3).trim();

                Intent intent = new Intent(getActivity(), BoardLookUp.class);
                intent.putExtra("idx", selectedId);

                startActivity(intent);
            }
        });

        return view;
    }

    public void getBoardList() {
        data.clear();
        url = "http://218.152.181.81/kuboard/board/boardList.php";

        getValuePHP = new GetValuePHP(url);
        getValuePHP.start();

        try {
            getValuePHP.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {}

        String result = getValuePHP.getResult();

        System.out.println("result: " + result);

        // php에서 불러온 값을 JSON 형태로 받아와서 출력 : 배열 형태
        try {
            JSONObject root = new JSONObject(result);

            JSONArray ja = root.getJSONArray("result");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String idx = jo.getString("idx");
                String title = jo.getString("title");
                String date = jo.getString("date").substring(0, 10);
                String usrId = jo.getString("usrId");
                String inputList = "";

                inputList = idx + "       " + title + " " + date + " " + usrId;

                data.add(inputList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }
}
