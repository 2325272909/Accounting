package com.example.accounting.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.accounting.R;
import com.example.accounting.entity.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Activity_modify_consume extends AppCompatActivity {

    private EditText spending_money,spending_stores,spending_times;
    private List<String> temp_spendingCredentials = new ArrayList<>();
    private List<String> temp_spendingtypes = new ArrayList<>();
    Calendar calendar= Calendar.getInstance(Locale.CHINA);
    private EditText spending_credential,spending_types;
    private Button btn_add,btn_return;
    AlertDialog.Builder builder_type=null;
    AlertDialog.Builder builder_credential=null;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_consume);
        this.user =(User) getIntent().getSerializableExtra("user");
        spending_money=findViewById(R.id.modify_spending_money);
        spending_stores=findViewById(R.id.modify_spending_stores);
        spending_times=findViewById(R.id.modify_spending_times);
        spending_credential=findViewById(R.id.modify_spending_credential);
        spending_types=findViewById(R.id.modify_spending_types);
        btn_add=findViewById(R.id.btn_modify_add);
        btn_return=findViewById(R.id.btn_modify_return);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



    }
}
