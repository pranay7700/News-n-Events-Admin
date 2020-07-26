package com.vaagdevi.newsneventsadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactDeveloper extends AppCompatActivity {

    ImageView DeveloperInstagram;
    ImageView DeveloperGmail;
    ImageView DeveloperPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developer);

        DeveloperInstagram = (ImageView) findViewById(R.id.developer_instagramBTN);
        DeveloperGmail = (ImageView) findViewById(R.id.developer_gmailBTN);
        DeveloperPhone = (ImageView) findViewById(R.id.developer_phoneBTN);


        DeveloperInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent instagramintent = new Intent(Intent.ACTION_VIEW);
                instagramintent.setData(Uri.parse("https://www.instagram.com/pranay_7700/?hl=en"));
                startActivity(instagramintent);

            }
        });

        DeveloperGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] recipients={"udayagiripranay7@gmail.com"};
                Intent gmailintent = new Intent(Intent.ACTION_SEND);
                gmailintent.putExtra(Intent.EXTRA_EMAIL, recipients);
                gmailintent.setType("text/plain");
                startActivity(gmailintent);


            }
        });

        DeveloperPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phoneintent = new Intent(Intent.ACTION_DIAL);
                phoneintent.setData(Uri.parse("tel:+918008763661"));
                startActivity(phoneintent);

            }
        });

    }
}
