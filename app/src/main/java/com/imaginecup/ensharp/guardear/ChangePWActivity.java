package com.imaginecup.ensharp.guardear;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ChangePWActivity extends AppCompatActivity {

    EditText etPassword, etPassword2;
    Button btn_change;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pw);

        etPassword = (EditText)findViewById(R.id.etPassword);
        etPassword2 = (EditText)findViewById(R.id.etPassword2);
        btn_change = (Button)findViewById(R.id.btn_change);



        // 비밀번호 일치 검사
        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Log.d("태그", "비밀번호 일치 검사");

                String password = etPassword.getText().toString();
                String confirm = etPassword2.getText().toString();

                if (password.equals(confirm)) {
                    etPassword.setTextColor(Color.BLACK);
                    etPassword2.setTextColor(Color.BLACK);
                } else {
                    etPassword.setTextColor(Color.RED);
                    etPassword2.setTextColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });




        //비밀번호 변경 클릭 시
        btn_change.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //비밀번호 일치할때
                if(etPassword.getText().toString().equals(etPassword2.getText().toString())){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePWActivity.this);

                    builder.setMessage(getString(R.string.diolog_changePW));

                    //확인 버튼 클릭 시 설정
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        public  void onClick(DialogInterface dialog, int whichButton){
                            return;
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                    finish(); // ChangePW창 종료

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangePWActivity.this);

                    builder.setMessage(getString(R.string.diolog_wrongPW));
                    //확인 버튼 클릭 시 설정
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        public  void onClick(DialogInterface dialog, int whichButton){
                            return;
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return;
                }
            }
        });
        

    }
}
