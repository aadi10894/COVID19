package aadithyabharadwaj.cs.niu.covid19;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setTitle("About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    } // End onCreate

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    } // End onOptionsItemSelected

    public void openGitHub(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/aadi10894")));
    } // End openGitHub

    public void openInstagram(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/aadi_vayuputra/?hl=en")));
    } // End openInstaagram

    public void openFacebook(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aadithya.bharadwaj.9/")));
    } // End openFacebook

    public void openLinkedIN(View view)
    {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/aadithyabharadwaj/")));
    } // End openLinkedIN

    public void openPhone(View view)
    {
        Intent phoneIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:5512004695"));

        startActivity(phoneIntent);
    } // End openPhone

} // End AboutActivity