package com.example.student_attendance_app.Student.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Adapter.ClickListener;
import com.example.student_attendance_app.Student.Adapter.RecyclerTouchListener;
import com.example.student_attendance_app.Student.Adapter.TeacherTodayAdapter;
import com.example.student_attendance_app.Student.Model.DailySchedule;
import com.example.student_attendance_app.Student.Model.Schedule;
import com.example.student_attendance_app.Student.Model.Timetable;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherTodayFragment extends Fragment {


    private TeacherTodayAdapter teacherTodayAdapter;
    private TextView todayDate;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    public TeacherTodayFragment() { }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String email = sPref.getString("email","");
        String base_url = sPref.getString("base_url","");
        getData(email,"Token "+token,base_url);
        return inflater.inflate(R.layout.fragment_teacher_today, container, false);
    }

    public void getData(final String email, String token,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Timetable....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
        Call<DailySchedule> call = service.getTeacherTodayTimetable(token,email);
        call.enqueue(new Callback<DailySchedule>() {
            @Override
            public void onResponse(Call<DailySchedule> call, Response<DailySchedule> response) {
                try {

                    progressDoalog.dismiss();
                    // Log.d("Resp",response.body().toString());
                    DailySchedule dailySchedule = response.body();
                    generateDataList(dailySchedule);
                }
                catch (Exception e){
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DailySchedule> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(final DailySchedule dailySchedule) {
        todayDate = getView().findViewById(R.id.date);
        todayDate.setText(dailySchedule.getDate());
        recyclerView = getView().findViewById(R.id.todayRecyclerView);
        teacherTodayAdapter = new TeacherTodayAdapter(getContext(),dailySchedule.getClasses(),TeacherTodayFragment.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(teacherTodayAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                String date = dailySchedule.getDate();
                String slot = dailySchedule.getClasses().get(position).getSection() != null
                        ? dailySchedule.getClasses().get(position).getSection().getSlot()
                        : dailySchedule.getClasses().get(position).getLab().getSlot();
                boolean isLab = dailySchedule.getClasses().get(position).getCategory().equals("Lab") ? true : false;
                loadFragment(new StudentListFragment(date,slot,isLab));
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(), "Long press on position :"+position,
                        Toast.LENGTH_LONG).show();
            }

        }));
    }

    void loadFragment(Fragment f){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,f);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
