package com.imaginecup.ensharp.guardear;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;




public class FindpwActivity extends AppCompatActivity {

    Button btn_send;
    EditText etNumber, etEmail;
    TextView text_alert1, text_alert2, findEmail;
    GmailSender sender;
    ProgressDialog dialog;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpw);
        etNumber = (EditText) findViewById(R.id.etNumber);
        text_alert1 = (TextView) findViewById(R.id.text_alert1);
        text_alert2 = (TextView) findViewById(R.id.text_alert2);
        etEmail = (EditText) findViewById(R.id.etEmail);
        findEmail = (TextView) findViewById(R.id.findEmail);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                equalsMail();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void equalsMail() {
        //이메일이 맞다면
        if (etEmail.getText().toString().equals("zoz7")) {

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

            // 이메일로 인증 번호 보내기

            try {
                Log.d("MAIL TESt", "try-in");
                GmailSender sender = new GmailSender("seong9806@gmail.com","scmk0718!"); // SUBSTITUTE HERE

                sender.sendMail(

                        "Guardear 비밀번호 재설정",   //subject.getText().toString(),
                        "비밀번호 재설정 인증메일입니다.\n 인증번호 \'5234\'를 입력해주세요",  //body.getText().toString(),
                        "seong9806@gmail.com",          //from.getText().toString(),
                        etEmail.getText().toString()+"@naver.com"         //to.getText().toString()
                );

                changePW();

            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FindpwActivity.this);

            //builder.setTitle("제목 설정");
            builder.setMessage(getString(R.string.diolog_mail));
            //확인 버튼 클릭 시 설정
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
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
    private void changePW() {

        //새로운 인증번호 재 전송
        AlertDialog.Builder builder = new AlertDialog.Builder(FindpwActivity.this);
        builder.setTitle(getString(R.string.diolog_once1));
        builder.setMessage(getString(R.string.diolog_once2));
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                return;
            }
        });
        //알림창 객체 설정
        AlertDialog dialog = builder.create();
        dialog.show();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //인증번호가 일치할 경우
                if (etNumber.getText().toString().equals("5235")) {

                    Intent intent = new Intent(getApplicationContext(), ChangePWActivity.class);

                    startActivity(intent);

                    finish(); // findPW창 종료
                } else {
                    //새로운 인증번호 재 전송
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindpwActivity.this);
                    builder.setTitle("인증번호가 일치하지 않습니다");
                    builder.setMessage("다시 입력해주세요");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

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

    public void timeTread() {

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("의견을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    sender.sendMail("의견보내기", // subject.getText().toString(),
                            "메일에 들어가는 본문 내용", // body.getText().toString(),
                            "seong9806@gmail.com", // from.getText().toString(),
                            "seong9806@gmail.com" // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(getApplicationContext(), "신청 실패", Toast.LENGTH_SHORT).show();

                }
                dialog.dismiss();
            }

            private void sleep(int i) {
                // TODO Auto-generated method stub

            }

        }).start();

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Findpw Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
