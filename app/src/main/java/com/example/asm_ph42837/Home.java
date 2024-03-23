package com.example.asm_ph42837;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_ph42837.Adapter.SinhVienAdapter;
import com.example.asm_ph42837.Api.ApiService;
import com.example.asm_ph42837.Modal.SinhVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    TextView txt;
    List<SinhVien> list = new ArrayList<>();
    SinhVienAdapter adapter;
    RecyclerView rcvSV;
    FloatingActionButton fltadd;
    ImageView imagePiker;
    private  Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rcvSV = findViewById(R.id.rcvSV);
        fltadd = findViewById(R.id.fltadd);
        txt = findViewById(R.id.txt);

        loadData();

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Home.this,new SinhVien(),1);
            }
        });
    }

    public void showDialog (Context context, SinhVien sinhVien, Integer type){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_sinhvien,null);
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();

        EditText edtMaSV = view.findViewById(R.id.edtMaSV);
        EditText edtNameSV = view.findViewById(R.id.edtNameSV);
        EditText edtDiemTB = view.findViewById(R.id.edtDiemTB);
        EditText edtAvatar = view.findViewById(R.id.edtAvatar);
        Button btnSave =view.findViewById(R.id.btnSave);

        if (type == 0){
            edtMaSV.setText(sinhVien.getMasv());
            edtNameSV.setText(sinhVien.getName());
            edtDiemTB.setText(sinhVien.getPoint()+"");
            edtAvatar.setText(sinhVien.getAvatar());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masv = edtMaSV.getText().toString().trim();
                String name = edtNameSV.getText().toString().trim();
                String diemTB = edtDiemTB.getText().toString();
                String avatar = edtAvatar.getText().toString().trim();
                if (masv.isEmpty() || name.isEmpty()|| diemTB.isEmpty()){
                    Toast.makeText(context, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (!isDouble(diemTB)) {
                    Toast.makeText(context, "Điểm trung bình phải là số", Toast.LENGTH_SHORT).show();
                } else {
                    Double point = Double.parseDouble(diemTB);
                    if (point < 0 || point > 10){
                        Toast.makeText(context, "Điểm phải từ 0-10", Toast.LENGTH_SHORT).show();
                    }else {
                        SinhVien sv = new SinhVien(masv,name,point,avatar);

                        if (type == 1){
                            Call<SinhVien> call = ApiService.apiService.addStudent(sv);

                            if(type == 0){
                                call = ApiService.apiService.updateStudent(sinhVien.get_id(),sv);
                            }
                            call.enqueue(new Callback<SinhVien>() {
                                @Override
                                public void onResponse(Call<SinhVien> call, Response<SinhVien> response) {
                                    if (response.isSuccessful()){
                                        String msg = "Thêm thành công";
                                        if (type == 0){
                                            msg = "Update thành công";
                                        }
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                                        loadData();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SinhVien> call, Throwable t) {
//                                    Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            }
        });

    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadData (){
        Call<List<SinhVien>> call = ApiService.apiService.getData();

        call.enqueue(new Callback<List<SinhVien>>() {
            @Override
            public void onResponse(Call<List<SinhVien>> call, Response<List<SinhVien>> response) {
                if (response.isSuccessful()){
                    list = response.body();
                    adapter = new SinhVienAdapter(Home.this, list);
                    rcvSV.setLayoutManager(new LinearLayoutManager(Home.this));
                    rcvSV.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<SinhVien>> call, Throwable t) {

            }
        });
    }
}