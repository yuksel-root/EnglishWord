package com.yuksi.worden.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yuksi.worden.R;
import com.yuksi.worden.ui.ShowWordInfos;
import com.yuksi.worden.dataclass.TableContracts;
import com.yuksi.worden.database.WordsDbHelper;

import java.util.ArrayList;

public class KelimelerFragment extends Fragment {
    ListView listView;
    ArrayList<String> wordNames;
    ArrayAdapter arrayAdapter;
    ArrayList<Integer> idArray;
    WordsDbHelper wordsDb;
    public static int wordCount;

    public KelimelerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_kelimeler, container, false);
        wordsDb = new WordsDbHelper(rootView.getContext());
        listView = rootView.findViewById(R.id.wordListView);
        wordNames = new ArrayList<>();
        idArray = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, wordNames);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowWordInfos.class);
                intent.putExtra("id", idArray.get(position));
                startActivity(intent);
            }
        });
        viewData();
        return rootView;
    }

    private void viewData() {
        try {
            Cursor cursor = wordsDb.showData();
            wordCount = cursor.getCount();
            if (cursor.getCount() == 0) {
                Toast.makeText(getContext(), "KF Gösterilecek veri yok", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    wordNames.add(cursor.getString(cursor.getColumnIndex(TableContracts.WordsTable.COLUMN_WORD_EN)));
                    idArray.add(Integer.valueOf(cursor.getString(cursor.getColumnIndex(TableContracts.WordsTable.COLUMN_ID))));
                }
                arrayAdapter.notifyDataSetChanged();
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("KF ListViewdeki itemlerin gösterimi hata");
        }
    }
}
