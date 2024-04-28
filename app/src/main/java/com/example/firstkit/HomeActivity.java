package com.example.firstkit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.firstkit.Prevelant.Prevelant;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /////////////////////// Recycler View
      //  recyclerView = findViewById(R.id.recycler_view_prod);
       // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //RecyclerView.LayoutManager layout = layoutManager;

        //recyclerView.setLayoutManager(layout);


        ////////////////////////Recycler View




        getSupportActionBar().getElevation();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("FirstKit");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00FFFFFF")));




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerview = navigationView.getHeaderView(0);
        TextView hiuser = headerview.findViewById(R.id.hi_user);



        if(Prevelant.currentOnlineUser.getName()!=null) {
            String hiname;
            hiname = "hi, " + Prevelant.currentOnlineUser.getName();
            hiuser.setText(hiname);
        }
        else{
            hiuser.setText("You dont have name!!!");
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ForHomeFragment fragment = new ForHomeFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment, "HOME");
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {  int count = getSupportFragmentManager().getBackStackEntryCount();
            switch(count){
                case 0:
                    new AlertDialog.Builder(this)
                            .setTitle("Exit")
                            .setMessage("Do you really want to exit?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    HomeActivity.super.onBackPressed();
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                    break;
                default:
                    super.onBackPressed();
                    break;
            }

        }
    }


    //////deleted three dots
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return false;
    }*/

    //for cart icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu, menu);
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }


    //for cart icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.submit_cart) {


            ///////////checking that the current fragment should not be cart
            String checkingcurrentfragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();

            /////////

            if(!checkingcurrentfragment.equals("CartFragment")) {


                if(getSupportFragmentManager().getBackStackEntryCount() >= 3){

                    ///if backstack entry is more then 3 then open new fragment by closing previous fragment.
                if(checkingcurrentfragment.equals("ProductDetailsFragment")){
                    CartFragment fragment = new CartFragment();

                    getSupportFragmentManager().popBackStack(null ,  getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);///i think removing all fragments
                    getSupportFragmentManager().beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentByTag("HOME"))
                            .add(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                }}
                else {
                    CartFragment fragment = new CartFragment();
                    getSupportFragmentManager().beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                            .add(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                }
            }



            //Your code to handle click

            return true;
        }
        if(id == R.id.submit_user){


            ///////////////////



            ///////////checking that the current fragment should not be UserAccountFragment
            String checkingcurrentfragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();

            /////////

            if(!checkingcurrentfragment.equals("UserAccountSettingFragment")) {


                if(getSupportFragmentManager().getBackStackEntryCount() >= 3){

                    if(checkingcurrentfragment.equals("ProductDetailsFragment")){
                        UserAccountSettingFragment fragment = new UserAccountSettingFragment();

                        ///goto back and start again therefore no increase in backstacks(fragments)

                        getSupportFragmentManager().popBackStack(null ,  getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
                        getSupportFragmentManager().beginTransaction()
                                .hide(getSupportFragmentManager().findFragmentByTag("HOME"))
                                .add(R.id.fragment_container, fragment)
                                .addToBackStack(null)
                                .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();

                    }}
                else {
                    UserAccountSettingFragment fragment = new UserAccountSettingFragment();
                    getSupportFragmentManager().beginTransaction()
                            .hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container))
                            .add(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();

                }
            }



            //Your code to handle click

            return true;


            //////////////////

        }
        return super.onOptionsItemSelected(item);
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(HomeActivity.this , delete.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.sign_out_navigation) {
            Paper.book().destroy();
            Intent intent = new Intent(HomeActivity.this , SignIn.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } else if (id == R.id.About_us) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
