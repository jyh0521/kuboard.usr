package com.example.kuboarduser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.kuboarduser.usePHP.GetValuePHP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusFragment extends Fragment {
    // 버스 시간표 화면 뿌려주기

    private View view;
    private ListView listBusTable;
    private List<String> data = new ArrayList<>();

    private GetValuePHP getValuePHP;
    private String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bus_fragment, container, false);

        listBusTable = (ListView) view.findViewById(R.id.listBusTable);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, data);
        listBusTable.setAdapter(adapter);

        getBusTimeTable();

        adapter.notifyDataSetChanged();

        return view;
    }

    public void getBusTimeTable() {
        data.clear();
        url = "http://218.152.181.81/kuboard/busTimeTable.php";

        getValuePHP = new GetValuePHP(url);
        getValuePHP.start();

        try {
            getValuePHP.join();
            System.out.println("waiting... for result");
        } catch (InterruptedException e) {}

        String result = getValuePHP.getResult();

        // php에서 불러온 값을 JSON 형태로 받아와서 출력 : 배열 형태
        try {
            JSONObject root = new JSONObject(result);

            JSONArray ja = root.getJSONArray("result");

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String num = jo.getString("num");
                String school = jo.getString("school");
                String station = jo.getString("station").substring(0, 5);
                String inputList = "";

                if(school.equals("")) {
                    school = "          ";
                }
                else {
                    school = school.substring(0, 5);
                }

                if(num.length() == 2) {
                    inputList = num + "               " + school + "                                " + station;
                }
                else {
                    inputList = num + "                 " + school + "                                " + station;
                }

                data.add(inputList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
