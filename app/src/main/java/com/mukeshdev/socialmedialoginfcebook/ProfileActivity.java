package com.mukeshdev.socialmedialoginfcebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private ShareDialog shareDialog;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shareDialog = new ShareDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });
        Bundle inBundle = getIntent().getExtras();
        String name = inBundle.get("name").toString();
        String surname = inBundle.get("surname").toString();
        String imageUrl = inBundle.get("imageUrl").toString();

        TextView nameView = (TextView) findViewById(R.id.nameAndSurname);
        nameView.setText("" + name + " " + surname);
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LoginManager.getInstance().logOut();
                Intent login = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        });
        new ProfileActivity.DownloadImage((ImageView)findViewById(R.id.profileImage)).execute(imageUrl);
    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImage(ImageView bmImage){
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls){
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try{
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            }catch (Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result){
            bmImage.setImageBitmap(result);
        }

    }

}