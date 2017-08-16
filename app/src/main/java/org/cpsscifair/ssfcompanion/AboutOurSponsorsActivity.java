package org.cpsscifair.ssfcompanion;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class AboutOurSponsorsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_our_sponsors);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void viewSsfIncSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cpsstudentsciencefair.squarespace.com/")));
    }

    public void viewCpsSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cps.edu/")));
    }

    public void viewMsiSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.msichicago.org/")));
    }

    public void viewTakedaSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.takeda.com/")));
    }

    public void viewBpSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bp.com/")));
    }

    public void viewComedSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.comed.com/")));
    }

    public void viewMotorolaSolutionsFoundationSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.motorolasolutions.com/en_us/about/company-overview/corporate-responsibility/motorola-solutions-foundation.html")));
    }

    public void viewPeoplesGasSite(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.peoplesgasdelivery.com/")));
    }
}
