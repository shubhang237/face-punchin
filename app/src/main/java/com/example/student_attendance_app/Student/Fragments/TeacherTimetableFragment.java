package com.example.student_attendance_app.Student.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Adapter.TeacherTimetableAdapter;
import com.example.student_attendance_app.Student.Model.Timetable;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TeacherTimetableFragment extends Fragment {
    private TeacherTimetableAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    public TeacherTimetableFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String email = sPref.getString("email","");
        getData(email,"Token "+token);
        return inflater.inflate(R.layout.fragment_teacher_timetable, container, false);
    }

    public void getData(final String email, String token){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Timetable....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Timetable> call = service.getTeacherTimetable(token,email);
        call.enqueue(new Callback<Timetable>() {

            @Override
            public void onResponse(Call<Timetable> call, Response<Timetable> response) {
                try {
                    progressDoalog.dismiss();
                    Timetable timetable = response.body();
                    generateDataList(timetable);
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
        adapter = new TeacherTimetableAdapter(timetable);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
