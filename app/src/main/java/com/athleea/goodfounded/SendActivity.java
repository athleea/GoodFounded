package com.athleea.goodfounded;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendActivity extends AppCompatActivity {

    Button sendBtn;
    EditText subjectEdit;
    EditText contentsEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        //이메일 사용권한
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        sendBtn = findViewById(R.id.send_btn);
        subjectEdit = findViewById(R.id.subject_Edit);
        contentsEdit = findViewById(R.id.content_Edit);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSecurityCode(getApplicationContext(), "athleea97@gmail.com");
                subjectEdit.setText("");
                contentsEdit.setText("");
            }
        });

    }


    public void sendSecurityCode(Context context, String sendTo) {
        try {
            GMailSender gMailSender = new GMailSender("athleea97@gmail.com", "asdf4791!@");
            gMailSender.sendMail(subjectEdit.getText().toString(), contentsEdit.getText().toString(), sendTo);
            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }


}
