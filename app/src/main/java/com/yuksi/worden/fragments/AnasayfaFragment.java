package com.yuksi.worden.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yuksi.worden.R;
import com.yuksi.worden.ui.AddWord;
import com.yuksi.worden.ui.QuizInfoList;


public class AnasayfaFragment extends Fragment {
    Button addWordBtn;
    Activity context;
    Button QuizInfoBtn;

    public AnasayfaFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_anasayfa, container, false);
        addWordBtn = rootView.findViewById(R.id.addWordBtn);
        QuizInfoBtn = rootView.findViewById(R.id.quizInfoBtn);
        context = getActivity();
        addWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddWord.class);
                intent.putExtra("info", "new");
                startActivity(intent);
            }
        });
        QuizInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), QuizInfoList.class);
                intent.putExtra("info", "new");
                startActivity(intent);
            }
        });
        return rootView;
    }


}
