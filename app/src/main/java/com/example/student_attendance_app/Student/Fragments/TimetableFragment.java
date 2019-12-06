package com.example.student_attendance_app.Student.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.student_attendance_app.Student.Adapter.TimetableAdapter;
import com.example.student_attendance_app.Student.Model.Timetable;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;
import com.example.student_attendance_app.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TimetableFragment extends Fragment {


    private TimetableAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;

    public TimetableFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token",null);
        String email = sPref.getString("email",null);
        String base_url = sPref.getString("base_url","");
        getData(email,"Token "+token,base_url);
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    public void getData(String email,String token,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Timetable....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
        Call<Timetable> call = service.getTimetable(token,email);
        call.enqueue(new Callback<Timetable>() {
            @Override
            public void onResponse(Call<Timetable> call, Response<Timetable> response) {
                try {
                    progressDoalog.dismiss();
                    generateDataList(response.body());
                }
                catch (Exception e){
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Timetable> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(Timetable timetable) {
        recyclerView = getView().findViewById(R.id.timetableRecyclerView);
        adapter = new TimetableAdapter(getContext(),timetable);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
