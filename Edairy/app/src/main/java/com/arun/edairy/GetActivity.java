package com.arun.edairy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;

public class GetActivity extends AppCompatActivity {

    private EditText phoneedt;
    private TextView nameTxt,phoneTxt;
    private Button getBtn,qrbtn;
    private ImageView imageView;
    private DatabaseHelper databaseHelper;
    private int REQUEST_CODE = 109;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, GetActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        initView();
    }

    private void initView() {
        databaseHelper = DatabaseHelper.getInstance(this);
        phoneedt = findViewById(R.id.phoneedt);
        nameTxt = findViewById(R.id.nameTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        imageView = findViewById(R.id.imageView);
        qrbtn = findViewById(R.id.qrbtn);
        getBtn = findViewById(R.id.getBtn);

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()){
                    String phone = phoneedt.getText().toString();
                    User user = databaseHelper.getUser(phone);
                    if (user.getName() != null && user.getPhone()!= null && user.getPictureUrl() != null) {
                        nameTxt.setText("Name : " + user.getName());
                        phoneTxt.setText("Phone : " + user.getPhone());
                        imageView.setImageBitmap(getImage(user.getPictureUrl()));
                    }else {
                        Toast.makeText(GetActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        qrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GetActivity.this, ScannerQR.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String phone = data.getData().toString();
            if (phone != null){
                User user = databaseHelper.getUser(phone);
                if (user.getName() != null && user.getPhone()!= null && user.getPictureUrl() != null) {
                    nameTxt.setText("Name : " + user.getName());
                    phoneTxt.setText("Phone : " + user.getPhone());
                    imageView.setImageBitmap(getImage(user.getPictureUrl()));
                }else {
                    Toast.makeText(GetActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Invallid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public boolean isValid(){
        if(phoneedt.getText().toString().length() < 10)
        {
            phoneedt.setError("Invalid Phone Number");
            return false;
        }
        return true;
    }
}