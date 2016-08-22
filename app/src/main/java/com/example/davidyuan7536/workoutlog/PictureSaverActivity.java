package com.example.davidyuan7536.workoutlog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class PictureSaverActivity extends AppCompatActivity {


    private Upload upload;
    private File chosenFile;

    String userID;


    ImageButton savePicture;
    ImageButton cancel;
    EditText pictureCaption;
    ImageView picture;
    MLRoundedImageView pictureRounded;


    private Context context;


    private DatabaseReference mDatabase;

    boolean isUserAvatar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_saver);

        context = this;
        mDatabase = FirebaseDatabase.getInstance().getReference();



        picture = (ImageView) findViewById(R.id.pictureSaverPicture);
        pictureRounded = (MLRoundedImageView) findViewById(R.id.pictureSaverPictureRound);
        pictureCaption = (EditText) findViewById(R.id.pictureSaverPictureCaption);
        savePicture = (ImageButton) findViewById(R.id.pictureSaverSaveBtn);
        cancel = (ImageButton) findViewById(R.id.pictureSaverCancelBtn);

        isUserAvatar =false;
        Intent intent = getIntent();

        final Uri uri = intent.getParcelableExtra("imageUri");
        String filePath = DocumentHelper.getPath(this, uri);
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);

        userID = intent.getExtras().getString("userID");

        isUserAvatar = intent.getExtras().getBoolean("isUserAvatar");

        if(isUserAvatar){
            pictureRounded.setImageURI(uri);
            pictureRounded.setVisibility(View.VISIBLE);
            picture.setVisibility(View.GONE);
            pictureCaption.setVisibility(View.GONE);
            savePicture.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);

            ProgressDialog dialog = ProgressDialog.show(PictureSaverActivity.this, "", "Uploading. Please wait...", true);
            dialog.show();

            createUpload(chosenFile);
            Execute(upload, new UiCallback());

        }
        else{

            picture.setImageURI(uri);


            savePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ProgressDialog dialog = ProgressDialog.show(PictureSaverActivity.this, "", "Uploading. Please wait...", true);
                    dialog.show();

                    createUpload(chosenFile);
                    Execute(upload, new UiCallback());


                }
            });


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    kill_activity();
                }
            });

        }




    }

    void kill_activity()
    {
        finish();
    }



    private String savePhotoToDatabase(String uID, String caption, String link, String today, String date){

        String key = mDatabase.child("users").child(uID).child("pictures").push().getKey();
        mDatabase.child("users").child(uID).child("pictures").child(key).child("caption").setValue(caption);
        mDatabase.child("users").child(uID).child("pictures").child(key).child("date").setValue(today);
        mDatabase.child("users").child(uID).child("pictures").child(key).child("order").setValue(date);
        mDatabase.child("users").child(uID).child("pictures").child(key).child("key").setValue(key);
        mDatabase.child("users").child(uID).child("pictures").child(key).child("link").setValue(link);


        return key;


    }

    private void saveUserAvatar(String uID, String link){
        mDatabase.child("users").child(uID).child("avatar").setValue(link);

    }



    private void Execute(Upload upload, Callback<ImageResponse> callback) {
        final Callback<ImageResponse> cb = callback;


        RestAdapter restAdapter = buildRestAdapter();

        restAdapter.create(ImgurAPI.class).postImage(
                Constants.getClientAuth(),
                upload.title,
                upload.description,
                upload.albumId,
                null,
                new TypedFile("image/*", upload.image),
                new Callback<ImageResponse>() {
                    @Override
                    public void success(ImageResponse imageResponse, Response response) {

                        if (cb != null) cb.success(imageResponse, response);
                        if (response == null) {

                            Toast.makeText(context, "Picture could not be uploaded", Toast.LENGTH_SHORT);
                            kill_activity();
                            return;
                        }
                        /*
                        Notify image was uploaded successfully
                        */
                        if (imageResponse.success) {

                            if(isUserAvatar){
                                saveUserAvatar(userID, imageResponse.data.getLink());
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("uploaded", "avatar");
                                resultIntent.putExtra("link", imageResponse.data.getLink());
                                setResult(Activity.RESULT_OK, resultIntent);
                                kill_activity();
                            }
                            else{

                                final Calendar c = Calendar.getInstance();
                                int year = c.get(Calendar.YEAR);
                                int month = c.get(Calendar.MONTH);
                                int day = c.get(Calendar.DAY_OF_MONTH);
                                String leadingZeroMonth = String.format("%02d", month);
                                String leadingZeroDay = String.format("%02d", day);


                                final String today = month+ "/" + day +"/" + year;
                                final String date = ""+year+leadingZeroMonth+leadingZeroDay;

                                String photoID = savePhotoToDatabase(userID, pictureCaption.getText().toString(),imageResponse.data.getLink(), today, date);

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("uploaded", "picture");
                                resultIntent.putExtra("pictureCaption", pictureCaption.getText().toString());
                                resultIntent.putExtra("pictureToday", today);
                                resultIntent.putExtra("pictureDate", date);
                                resultIntent.putExtra("pictureLink", imageResponse.data.getLink());
                                resultIntent.putExtra("pictureID", photoID);
                                setResult(Activity.RESULT_OK, resultIntent);
                                kill_activity();
                            }

                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(context, "Picture could not be uploaded", Toast.LENGTH_SHORT);
                        if (cb != null) cb.failure(error);
                        kill_activity();

                    }
                });
    }


    private RestAdapter buildRestAdapter() {
        RestAdapter imgurAdapter = new RestAdapter.Builder()
                .setEndpoint(ImgurAPI.server)
                .build();
        if (Constants.LOGGING)
            imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        return imgurAdapter;
    }

    private void createUpload(File image) {
        upload = new Upload();

        upload.image = image;
    }




    private class UiCallback implements Callback<ImageResponse> {

        @Override
        public void success(ImageResponse imageResponse, Response response) {

        }

        @Override
        public void failure(RetrofitError error) {

        }
    }

}



