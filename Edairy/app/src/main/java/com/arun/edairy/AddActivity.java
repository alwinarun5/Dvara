package com.arun.edairy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 99;
    private EditText mName;
    private EditText mPhone;
    private Button mSaveBtn, pictureBtn;
    private DatabaseHelper databaseHelper;
    private ImageView imageView;
    private Bitmap bitmap = null;
    private byte[] image = null;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        initView();
        databaseHelper = DatabaseHelper.getInstance(this);
    }

    private void initView() {
        mName = findViewById(R.id.editTextTextPersonName);
        mPhone = findViewById(R.id.editTextPhone);
        pictureBtn = findViewById(R.id.pictureBtn);
        imageView = findViewById(R.id.imageScane);
        mSaveBtn = findViewById(R.id.saveBtn);

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid() && image != null){
                    User user = new User();
                    user.setName(mName.getText().toString());
                    user.setPhone(mPhone.getText().toString());
                    user.setPictureUrl(image);
                    addInfo(user);
                }
            }
        });

    }

    protected void startScan() {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                getContentResolver().delete(uri, null, null);
                imageView.setImageBitmap(bitmap);
                image = getBytes(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addInfo(User user) {
        long status = databaseHelper.addOrUpdateUser(user);

        Toast.makeText(this, "Status :::: "+ status, Toast.LENGTH_SHORT).show();
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public boolean isValid(){
        if(mPhone.getText().toString().length() < 10)
        {
            mPhone.setError("Invalid Phone Number");
            return false;
        }
        if(mName.getText().toString().length() == 0)
        {
            mName.setError("Whats name?");
            return false;
        }
        return true;
    }
}