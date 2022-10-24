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

import com.example.kmj_reco.DTO.ALERT;
import com.example.kmj_reco.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class NoticeAdapter extends ArrayAdapter<ALERT> {
    private Context context;
    private List noticelist;
    private ListView noticeListView;

    class UserViewHoldern {
        private TextView notice_id;
        private TextView notice_title;
        private TextView notice_text;
        private TextView notice_date;
    }

    public NoticeAdapter(Context context, int resource, List<ALERT> noticelist, ListView noticeListView) {
        super(context, resource, noticelist);
        this.context = context;
        this.noticelist = noticelist;
        this.noticeListView = noticeListView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserViewHoldern viewHolder;
        String Status;
        View mView = convertView;

        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_notice, parent, false);

            viewHolder = new UserViewHoldern();
            viewHolder.notice_title = (TextView) mView.findViewById(R.id.noticetitle);
            viewHolder.notice_text = (TextView) mView.findViewById(R.id.noticetext);
            viewHolder.notice_date = (TextView) mView.findViewById(R.id.noticedate);

            mView.setTag(viewHolder);

            Status = "created";
        } else {
            viewHolder = (UserViewHoldern) mView.getTag();

            Status = "reused";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        if (noticelist.size() > 0) {
            ALERT alert = (ALERT) noticelist.get(position);

            viewHolder.notice_title.setText(alert.getalert_Title());
            viewHolder.notice_text.setText(alert.getAlert_text());
//          추후 구현: 현재 날짜에서 하루 넘으면 n월 n일로
            viewHolder.notice_date.setText("\n" + sdf.format(alert.getalert_Date()));
        }
        return mView;
    }
}
