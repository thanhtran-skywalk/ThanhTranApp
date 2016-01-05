package com.thanhtran.redstring.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.ParseException;
import com.thanhtran.redstring.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by admin on 10/21/15.
 */
public class SignUpActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    private String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void selectAvatar(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();

                ImageView imageView = (ImageView) findViewById(R.id.img_avatar);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            }

        }
    }

    public void signUpAction(View view) {
        showProgressDialog("Sigining In", "One moment please...");
        String email = ((EditText)findViewById(R.id.txt_signup_email_field)).getText().toString();
        String password = ((EditText)findViewById(R.id.txt_signup_password_field)).getText().toString();
        String rePassword = ((EditText)findViewById(R.id.txt_signup_repassword_field)).getText().toString();
        String firstName = ((EditText)findViewById(R.id.txt_signup_first_name_field)).getText().toString();
        String lastName = ((EditText)findViewById(R.id.txt_signup_last_name_field)).getText().toString();
        if(validateSignUpData(email, password, rePassword, firstName, lastName)){
            final ParseUser newUser = new ParseUser();
            newUser.setUsername(email);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.put("firstName", firstName);
            newUser.put("lastName", lastName);

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] image = stream.toByteArray();
            final ParseFile file = new ParseFile(email+".png", image);
            file.saveInBackground();
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    newUser.put("avatar", file);
                    newUser.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            hideProgressDialog();
                            if (e == null) {
                                startMainActivity();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }
            });


        }
    }

    private Boolean validateSignUpData(String Email, String password, String repassword, String firstName, String lastName){
        return true;
    }

    private void showProgressDialog(String title, String message) {
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.hide();
    }
    private void startMainActivity() {
        Intent messagingActivity = new Intent(this, MainActivity.class);
        startActivity(messagingActivity);
        finish();
    }
}
