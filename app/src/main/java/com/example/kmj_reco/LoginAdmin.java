package com.example.kmj_reco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kmj_reco.DTO.RECOBIN;
import com.example.kmj_reco.DTO.USER;
import com.example.kmj_reco.utils.LoginAdminAdapter;
import com.example.kmj_reco.utils.RecobinAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginAdmin extends AppCompatActivity {
    ListView loginAdminListView;
    private List<USER> UserAccountList = new ArrayList<USER>();

    // 데이터베이스 선언
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        // xml 요소
        loginAdminListView = (ListView) findViewById(R.id.loginAdminListView);

        // DB 설정
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // 뒤로가기 버튼 터치 시 어드민 홈 화면으로 이동
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
            }
        });

        // DB 내의 USER 테이블 데이터 받아오기
        reference.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccountList.clear(); // 리스트 초기화

                for (DataSnapshot data : snapshot.getChildren()) {
                    USER UA = data.getValue(USER.class);
                    UserAccountList.add(UA);
                }

                // 검색 기능 : 유저 이름으로 검색
                searchfun(UserAccountList);
                final LoginAdminAdapter LoginAdminAdapter = new LoginAdminAdapter(getApplicationContext(), 0, UserAccountList, loginAdminListView);
                loginAdminListView.setAdapter(LoginAdminAdapter);

                setUpOnClickListener();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("OncalledError", "error");
            }
        });
    }

    // 유저 리스트 item 클릭 이벤트
    private void setUpOnClickListener(){
        loginAdminListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 해당 item 데이터
                USER selectUserAccount = (USER) loginAdminListView.getItemAtPosition(position);
            }
        });
    }

    // 검색 기능
    private void searchfun(List<USER> UserAccountList){
        SearchView searchView = (SearchView) findViewById(R.id.user_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                List<USER> filterUser = new ArrayList<>();

                for (USER data : UserAccountList){
                    // 적은 글자가 유저 이름에 포함된 경우 유저 정보를 가져옴
                    if(data.getUser_name().toLowerCase().contains(s.toLowerCase())){
                        filterUser.add(data);
                    }
                }
                // 리스트에 표시
                final LoginAdminAdapter loginAdminAdapter = new LoginAdminAdapter(getApplicationContext(),0, filterUser, loginAdminListView);
                loginAdminListView.setAdapter(loginAdminAdapter);

                return false;
            }
        });
    }
}