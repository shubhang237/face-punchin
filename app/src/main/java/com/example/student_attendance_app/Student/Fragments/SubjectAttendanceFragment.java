package com.example.student_attendance_app.Student.Fragments;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.student_attendance_app.Student.Adapter.CustomAdapter;
import com.example.student_attendance_app.Student.Model.SubjectAttendance;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;
import com.example.student_attendance_app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectAttendanceFragment extends Fragment {

    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDoalog;
    public SubjectAttendanceFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token",null);
        String email = sPref.getString("email",null);
        String base_url = sPref.getString("base_url","");
        getData("Token "+token,email,base_url);
        return inflater.inflate(R.layout.fragment_subject_attendance, container, false);
    }

    public void getData(String token,String email,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Attendance....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
        Call<SubjectAttendance> call = service.getAllAttendance(token,email);
        call.enqueue(new Callback<SubjectAttendance>() {

            @Override
            public void onResponse(Call<SubjectAttendance> call, Response<SubjectAttendance> response) {
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
            public void onFailure(Call<SubjectAttendance> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(SubjectAttendance subjectAttendance) {
        recyclerView = getView().findViewById(R.id.customRecyclerView);
        adapter = new CustomAdapter(getContext(),subjectAttendance);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
