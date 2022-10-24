package com.example.kmj_reco.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kmj_reco.DTO.QUIZ;
import com.example.kmj_reco.R;

import java.util.List;

public class QuizAdapter extends ArrayAdapter<QUIZ> {
    private Context context;
    private List quizList;
    private ListView quizListView;

    class UserViewHolder {
        private TextView quiz_question;
        private TextView quiz_answer;
        private TextView quiz_answer_content;
        private TextView quiz_num;
    }

    public QuizAdapter(Context context, int resource, List<QUIZ> quizList, ListView quizListView) {
        super(context, resource, quizList);
        this.context = context;
        this.quizList = quizList;
        this.quizListView = quizListView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        QuizAdapter.UserViewHolder viewHolder;
        String Status;
        View mView = convertView;

        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.quiz_item, parent, false);

            viewHolder = new QuizAdapter.UserViewHolder();
            viewHolder.quiz_question = (TextView) mView.findViewById(R.id.noticetext);

            mView.setTag(viewHolder);

            Status = "created";
        } else {
            viewHolder = (QuizAdapter.UserViewHolder) mView.getTag();

            Status = "reused";
        }
        return mView;
    }
}
