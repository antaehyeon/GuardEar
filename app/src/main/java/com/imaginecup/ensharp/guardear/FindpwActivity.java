package com.imaginecup.ensharp.guardear;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FindpwActivity extends AppCompatActivity {

    Button btn_send;
    EditText etNumber, etEmail;
    TextView text_alert1,text_alert2, findEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        etNumber = (EditText)findViewById(R.id.etNumber);
        text_alert1 = (TextView)findViewById(R.id.text_alert1);
        text_alert2 = (TextView)findViewById(R.id.text_alert2);
        etEmail = (EditText)findViewById(R.id.etEmail);
        findEmail = (TextView)findViewById(R.id.findEmail);

        btn_send = (Button)findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){

                equalsMail();
            }
        });
    }

    private  void equalsMail(){
        //이메일이 맞다면
        if(etEmail.getText().toString().equals("test")){

            findEmail.setTextColor(Color.parseColor("#2f4959"));
            etEmail.setBackgroundColor(Color.parseColor("#2f4959"));
            etEmail.setFocusable(false);
            etEmail.setClickable(false);

            //count ++;
            //숨겨뒀던 레이아웃 보이게
            btn_send.setText(getString(R.string.btn_change));
            etNumber.setVisibility(View.VISIBLE);
            text_alert1.setVisibility(View.VISIBLE);
            text_alert2.setVisibility(View.VISIBLE);

            changePW();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(FindpwActivity.this);

            //builder.setTitle("제목 설정");
            builder.setMessage(getString(R.string.diolog_mail));
            //확인 버튼 클릭 시 설정
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                public  void onClick(DialogInterface dialog, int whichButton){
                    return;
                }
            });
            //알림창 객체 설정
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
    }

    //비밀번호 변경 Activity로
    private void changePW(){

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //인증번호가 일치할 경우
                if(etNumber.getText().toString().equals("1")){

                    Intent intent = new Intent(getApplicationContext(), ChangePWActivity.class);

                    startActivity(intent);

                    finish(); // findPW창 종료
                }
                else{
                    //새로운 인증번호 재 전송
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindpwActivity.this);
                    builder.setTitle(getString(R.string.diolog_once1));
                    builder.setMessage(getString(R.string.diolog_once2));
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        public  void onClick(DialogInterface dialog, int whichButton){

                            return;
                        }
                    });
                    //알림창 객체 설정
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


    }
}
