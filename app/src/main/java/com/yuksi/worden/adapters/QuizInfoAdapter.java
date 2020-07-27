package com.yuksi.worden.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yuksi.worden.dataclass.Quiz;
import com.yuksi.worden.R;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;

public class QuizInfoAdapter extends BaseAdapter {

    private final Context context;
    private ArrayList<Quiz> ArrayList;

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setData(ArrayList a1) {
        ArrayList = a1;
    }

    public QuizInfoAdapter(Context context, ArrayList<Quiz> QuizArray) {
        this.context = context;
        this.ArrayList = QuizArray;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return ArrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        QuizInfoAdapter.ViewHolder viewHolder = null;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (inflater != null) {
                convertView = inflater.inflate(R.layout.custom_quiz_info_list, parent, false);
                viewHolder = new QuizInfoAdapter.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (QuizInfoAdapter.ViewHolder) convertView.getTag();
        }

        if (viewHolder != null) {
            viewHolder.setTexts(ArrayList.get(position));
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return this.ArrayList.size();
    }

    class ViewHolder {

        private TextView t1_id_cql;
        private TextView t2_date_cql;
        private TextView t3_quizN_cql;
        private TextView t4_quizC_cql;
        private TextView t5_score_cql;


        ViewHolder(View view) {
            t1_id_cql = view.findViewById(R.id.id_txt_cql);
            t2_date_cql = view.findViewById(R.id.tarih_txt_cql);
            t3_quizN_cql = view.findViewById(R.id.quizNumber_txt_cql);
            t4_quizC_cql = view.findViewById(R.id.soruCount_txt_cql);
            t5_score_cql = view.findViewById(R.id.score_txt_cql);
        }

        void setTexts(Quiz q) {
            t1_id_cql.setText(String.valueOf(q.getQuizId()));
            t2_date_cql.setText(q.getQuizDate());
            t3_quizN_cql.setText(String.valueOf(q.getQuizId()));
            t4_quizC_cql.setText(String.valueOf(q.getQuestionCount()));
            t5_score_cql.setText(String.valueOf(q.getQuizScore()));
            t1_id_cql.setBackgroundColor(Color.WHITE);
            t2_date_cql.setBackgroundColor(Color.rgb(255, 165, 0));
            t3_quizN_cql.setBackgroundColor(Color.rgb(0, 191, 255));
            t4_quizC_cql.setBackgroundColor(Color.YELLOW);//sarı
            t5_score_cql.setBackgroundColor(Color.rgb(148, 0, 211));
            t1_id_cql.setVisibility(INVISIBLE);
        }

    }

}

