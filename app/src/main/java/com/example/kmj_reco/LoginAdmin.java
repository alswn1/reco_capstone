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
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        loginAdminListView = (ListView) findViewById(R.id.loginAdminListView);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminHome.class);
                startActivity(intent);
            }
        });

        reference.child("USER").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAccountList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    USER UA = data.getValue(USER.class);
                    UserAccountList.add(UA);
                }

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

    private void setUpOnClickListener(){
        loginAdminListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                USER selectUserAccount = (USER) loginAdminListView.getItemAtPosition(position);
            }
        });
    }

    private void searchfun(List<USER> UserAccountList){
        SearchView searchView = (SearchView) findViewById(R.id.user_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                List<USER> filterUser = new ArrayList<>();
                for (USER data : UserAccountList){
                    if(data.getUser_name().toLowerCase().contains(s.toLowerCase())){
                        filterUser.add(data);
                    }
                }
                final LoginAdminAdapter loginAdminAdapter = new LoginAdminAdapter(getApplicationContext(),0, filterUser, loginAdminListView);

                loginAdminListView.setAdapter(loginAdminAdapter);
                return false;
            }
        });
    }
}