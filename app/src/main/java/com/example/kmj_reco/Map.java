package com.example.kmj_reco;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class Map extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    Button btn_search;
    EditText editText;

    // 데이터베이스 선언
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        checkDangerousPermissions();

        editText = findViewById(R.id.editText);
        btn_search = findViewById(R.id.btn_search);

        ImageView btn_home = (ImageView) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        ImageView btn_settings = (ImageView) findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        ImageView btn_alert = (ImageView) findViewById(R.id.btn_alert);
        btn_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alert.class);
                startActivity(intent);
            }
        });

        try {
            // Loading map
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    // 맵이 사용 가능하면 호출되는 콜백 메소드
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // 시작 위치 지정 --> 63 빌딩
        LatLng startLocation = new LatLng(37.51981489443144, 126.94026724041389);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));

        // 줌 애니메이션
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);

        // 마커 표시 --> TRASHCAN / RECOBIN
        database = FirebaseDatabase.getInstance(); // db

        // TRASHCAN 데이터 불러오기
        database.getReference("TRASHCAN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i < snapshot.getChildrenCount(); i++) {
                    // 위도 받아와 latitude에 저장
                    Double latitude = snapshot.child(String.valueOf(i)).child("Latitude").getValue(Double.class);
                    //Log.v("test", "latitude" + latitude);

                    // 경도 받아와 longitude에 저장
                    Double longitude = snapshot.child(String.valueOf(i)).child("Longitude").getValue(Double.class);
                    //Log.v("test", "longitude" + longitude);

                    // 상세주소 받아와 fulladdress에 저장
                    String trashcan_fulladdress = snapshot.child(String.valueOf(i)).child("trashcan_fulladdress").getValue(String.class);

                    // LatLng 타입 place 변수에 위도 경도 저장
                    LatLng trashcan_place = new LatLng(latitude, longitude);

                    // 지도에 place마다 마커를 표시함
                    googleMap.addMarker(new MarkerOptions().position(trashcan_place).title("trashcan").snippet(trashcan_fulladdress)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.trashcan_marker));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러문 출력
                Log.e("Trashcan Marker", String.valueOf(error.toException()));
            }
        });

        // RECOBIN 데이터 불러오기
        database.getReference("RECOBIN").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int j=0; j < snapshot.getChildrenCount(); j++) {
                    // 위도 받아와 latitude에 저장
                    Double recobin_latitude = snapshot.child(String.valueOf(j)).child("recobin_latitude").getValue(Double.class);
                    //Log.v("test", "recobin_latitude" + recobin_latitude);

                    // 경도 받아와 longitude에 저장
                    Double recobin_longitude = snapshot.child(String.valueOf(j)).child("recobin_longitude").getValue(Double.class);
                    //Log.v("test", "recobin_longitude" + recobin_longitude);

                    // 상세주소 받아와 recobin_fulladdress에 저장
                    String recobin_fulladdress = snapshot.child(String.valueOf(j)).child("recobin_fulladdress").getValue(String.class);

                    // LatLng 타입 place 변수에 위도 경도 저장
                    LatLng recobin_place = new LatLng(recobin_latitude, recobin_longitude);

                    // 지도에 recobin_place마다 마커를 표시함
                    googleMap.addMarker(new MarkerOptions().position(recobin_place).title("recobin").snippet(recobin_fulladdress)).setIcon(BitmapDescriptorFactory.fromResource(R.drawable.recobin_marker));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 에러문 출력
                Log.e("Recobin Marker", String.valueOf(error.toException()));
            }
        });

        // 마커 클릭 시 상세주소 화면으로 이동
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                String title = marker.getTitle(); // title 불러오기
                String snippet = marker.getSnippet(); // snippet 불러오기

                // title 로 구분
                if (title.equals("trashcan")) {
                    Intent i = new Intent(getBaseContext(), AddressDetail.class);
                    i.putExtra("trashcan_fulladdress", snippet); // 상세주소 데이터 다음 액티비티에 전송
                    startActivity(i);
                } else if (title.equals("recobin")) {
                    Intent i = new Intent(getBaseContext(), RecobinDetail.class);
                    i.putExtra("recobin_fulladdress", snippet); // 상세주소 데이터 다음 액티비티에 전송
                    startActivity(i);
                }
                return true;
            }
        });

        // 자신의 위치
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // 검색 버튼
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() > 0) {
                    Location location = getLocationFromAddress(getApplicationContext(), editText.getText().toString());
                    showCurrentLocation(location);
                }
            }
        });
    }

    // 위치 받아오기
    private Location getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Location resLocation = new Location("");

        try {
            addresses = geocoder.getFromLocationName(address, 5);
            if((addresses == null) || (addresses.size() == 0)) {
                return null;
            }
            Address addressLoc = addresses.get(0);

            resLocation.setLatitude(addressLoc.getLatitude());
            resLocation.setLongitude(addressLoc.getLongitude());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resLocation;
    }

    // 현재 위치 설정
    private void showCurrentLocation(Location location) {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        String msg = "Latitutde : " + curPoint.latitude + "\nLongitude : " + curPoint.longitude;

        // 화면 확대, 숫자가 클수록 확대
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    // -----------------권한 설정
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    // 권한 설정 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한 승인", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한 미승인", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    // -----------------권한 설정 끝
}
