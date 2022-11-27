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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoticeAdapter extends ArrayAdapter<ALERT> {
    private Context context;
    private List noticelist;
    private ListView noticeListView;


    // xml에서 받아올 변수 담은 클래스 정의
    class UserViewHoldern {
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
        View mView = convertView;

        // 뷰를 받아오지 않았을 경우 받아온다.
        // xml에서 요소를 받아온다.
        if (mView == null) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.item_notice, parent, false);

            viewHolder = new UserViewHoldern();
            viewHolder.notice_title = (TextView) mView.findViewById(R.id.noticetitle);
            viewHolder.notice_text = (TextView) mView.findViewById(R.id.noticetext);
            viewHolder.notice_date = (TextView) mView.findViewById(R.id.noticedate);

            mView.setTag(viewHolder);

        } else {
            viewHolder = (UserViewHoldern) mView.getTag();

        }

        // 날짜와 시간 형식
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat datesdf = new SimpleDateFormat("MM월 dd일 HH:mm");
        // 어제 날짜 정의
        Date ysday = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(ysday);
        c.add(Calendar.DATE, -1);
        ysday = c.getTime();

        if (noticelist.size() > 0) {
            // 해당 알림의 내용 가져옴
            ALERT alert = (ALERT) noticelist.get(position);
            // 해당 알림의 내용을 목록에 표시
            viewHolder.notice_title.setText(alert.getalert_Title());
            viewHolder.notice_text.setText(alert.getAlert_text());
            // 해당 알림의 시간 표시
            // 현재부터 하루 이내: 시간 표시

            if(alert.getalert_Date().before(ysday)){
                viewHolder.notice_date.setText("\n" + datesdf.format(alert.getalert_Date()));
            }else{
                // 하루 이전: 날짜와 시간 표시
                viewHolder.notice_date.setText("\n" + sdf.format(alert.getalert_Date()));
            }
        }
        return mView;
    }
}
