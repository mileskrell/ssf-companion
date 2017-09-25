package org.cpsscifair.ssfcompanion;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import org.cpsscifair.ssfcompanion.databinding.ActivityNavDrawerBinding;

public class NavDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ActivityNavDrawerBinding binding;

    private static final String TITLE = "action_bar_title";

    static final String CHECKLIST_FRAGMENT = "checklist_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_drawer);

        setSupportActionBar(binding.appBarNavDrawer.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarNavDrawer.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // Add the HomeFragment
            onNavigationItemSelected(binding.navView.getMenu().findItem(R.id.nav_home));
        } else {
            // Set the title to whatever it was before
            getSupportActionBar().setTitle(savedInstanceState.getString(TITLE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE, (String) getSupportActionBar().getTitle());
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment.newInstance())
                        .commit();
                getSupportActionBar().setTitle(R.string.app_name);
                break;
            case R.id.nav_checklist:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ChecklistFragment.newInstance(), CHECKLIST_FRAGMENT)
                        .commit();
                getSupportActionBar().setTitle(R.string.checklist);
                break;
            case R.id.nav_unit_converter:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, UnitConverterFragment.newInstance())
                        .commit();
                getSupportActionBar().setTitle(R.string.unit_converter);
                break;
            case R.id.nav_advise_a_student:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, AdviseAStudentFragment.newInstance())
                        .commit();
                getSupportActionBar().setTitle(R.string.advise_a_student_program);
                break;
            case R.id.nav_calendar:
                safelyInvokeIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.calendar_url))));
                break;
            case R.id.nav_exhibit_categories:
                safelyInvokeIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.exhibit_categories_url))));
                break;
            case R.id.nav_safety_guidelines:
                safelyInvokeIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.safety_guidelines_url))));
                break;
            case R.id.nav_send_feedback:
                Intent sendFeedbackIntent = new Intent(Intent.ACTION_SENDTO,
                        Uri.parse("mailto:" + getString(R.string.my_email_address)));
                sendFeedbackIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                safelyInvokeIntent(sendFeedbackIntent);
                break;
            case R.id.nav_view_the_code:
                safelyInvokeIntent(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.app_github_url))));
                break;
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void viewSsfIncSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://cpsstudentsciencefair.squarespace.com/")));
    }

    public void viewCpsSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cps.edu/")));
    }

    public void viewMsiSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.msichicago.org/")));
    }

    public void viewTakedaSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.takeda.com/")));
    }

    public void viewBpSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.bp.com/")));
    }

    public void viewComedSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.comed.com/")));
    }

    public void viewMotorolaSolutionsFoundationSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.motorolasolutions.com/en_us/about/company-overview/corporate-responsibility/motorola-solutions-foundation.html")));
    }

    public void viewPeoplesGasSite(View view) {
        safelyInvokeIntent(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.peoplesgasdelivery.com/")));
    }

    void safelyInvokeIntent(Intent intent) {
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            if (intent.getAction().equals(Intent.ACTION_SENDTO)) {
                Snackbar.make(binding.drawerLayout, R.string.unable_to_compose_email, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(binding.drawerLayout, R.string.unable_to_view_site, Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
