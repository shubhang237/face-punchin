package com.example.student_attendance_app.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.student_attendance_app.Student.Fragments.SubjectAttendanceFragment;
import com.example.student_attendance_app.Student.Fragments.TeacherTimetableFragment;
import com.example.student_attendance_app.Student.Fragments.TeacherTodayFragment;
import com.example.student_attendance_app.Student.Fragments.TimetableFragment;
import com.example.student_attendance_app.R;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        SharedPreferences sPref = getApplicationContext().getSharedPreferences("MyPref",0);
        String username = sPref.getString("username","");
        String email = sPref.getString("email","");
        String role = sPref.getString("role","none");
        showUserInfo(username, email);

        if(role.equals("student")) {
            loadFragment(new TimetableFragment());
        }
        else if(role.equals("teacher")){
            navigationView.getMenu().removeItem(0);
            navigationView.getMenu().removeGroup(0);
            navigationView.inflateMenu(R.menu.activity_main_teacher_drawer);
            loadFragment(new TeacherTodayFragment());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.sign_out){
            SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("MyPref",0).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(MainActivity.this,SignInActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_attendance)
            loadFragment( new SubjectAttendanceFragment());
        else if(id == R.id.nav_timetable)
            loadFragment( new TimetableFragment());
        else if(id == R.id.nav_teacher_timetable){
            loadFragment(new TeacherTimetableFragment());
        }
        else if(id == R.id.nav_teacher_today_schedule){
            loadFragment(new TeacherTodayFragment());
        }
        else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showUserInfo(String userName,String Email){
        NavigationView navigationView = findViewById(R.id.nav_view);
        View mHeaderView = navigationView.getHeaderView(0);
        TextView username = mHeaderView.findViewById(R.id.username);
        username.setText(userName);
        TextView email = mHeaderView.findViewById(R.id.emailID);
        email.setText(Email);
    }

    void loadFragment(Fragment f){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,f);
        fragmentTransaction.commit();
    }

}
