package aadithyabharadwaj.cs.niu.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class HelpActivity extends AppCompatActivity
{
    private Button moreBuutton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().setTitle("Help");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        moreBuutton = findViewById(R.id.wantToKnowMoreButton);

        moreBuutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(HelpActivity.this, "Know More", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), KnowMore.class);
                startActivity(intent);
            }  // End onClick
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected

    public void doPhone(View view)
    {
        Intent phoneIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:+15512004695"));

        startActivity(phoneIntent);
    } // End doPhone

    public void doMessage(View view)
    {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.setData(Uri.parse("sms:" + 100));
        startActivity(smsIntent);
    } // End doMessage


} // End HelpActivity
