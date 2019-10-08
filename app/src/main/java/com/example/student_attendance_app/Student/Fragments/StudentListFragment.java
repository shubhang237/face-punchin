package com.example.student_attendance_app.Student.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Adapter.SectionListAdapter;
import com.example.student_attendance_app.Student.Model.DailySchedule;
import com.example.student_attendance_app.Student.Model.PostAttendance;
import com.example.student_attendance_app.Student.Model.Schedule;
import com.example.student_attendance_app.Student.Model.SectionList;
import com.example.student_attendance_app.Student.Model.Status;
import com.example.student_attendance_app.Student.Model.Student;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends Fragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;

    private String slot;
    private String date;
    private boolean isLab;
    private SectionListAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> currentSelectedItems;
    private ProgressDialog progressDoalog;
    private List<Student> list;
    private Button markAttendance;
    private Button photoAttendance;

    public StudentListFragment(String date,String slot,boolean isLab) {
        this.slot = slot;
        this.date = date;
        this.isLab = isLab;
        currentSelectedItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String email = sPref.getString("email","");
        getData("Token "+token,email);
        return inflater.inflate(R.layout.fragment_student_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        markAttendance = view.findViewById(R.id.markAttendance);
        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
                String token = sPref.getString("access_token","");
                postAttendance("Token "+token,new PostAttendance(date,currentSelectedItems,slot,isLab));
            }
        });
        photoAttendance = view.findViewById(R.id.photoAttendance);
        photoAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,
                        CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);
                //imageView.setImageBitmap(bitmap);

            }
        }
    }

    public void getData(String token, String email){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Student List ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<SectionList> call = service.getStudentList(token,email,slot);
        call.enqueue(new Callback<SectionList>() {
            @Override
            public void onResponse(Call<SectionList> call, Response<SectionList> response) {
                try{
                    progressDoalog.dismiss();
                    generateDataList(response.body());
                }
                catch (Exception e){
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<SectionList> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postAttendance(String token, PostAttendance postAttendance){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Marking Attendance ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Status> call = service.markAttendances(token,"application/json",postAttendance);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                try {
                    progressDoalog.dismiss();
                    String status = response.body().getStatus();
                    if(status.equals("1")) {
                        Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                    else {
                        Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    progressDoalog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(SectionList sectionList) {
        list = sectionList.getStudents();
        recyclerView = getView().findViewById(R.id.studentListRecyclerView);
        adapter = new SectionListAdapter(getContext(), list, new SectionListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Student student) {
                currentSelectedItems.add(student.getRollno());
            }

            @Override
            public void onItemUncheck(Student student) {
                currentSelectedItems.remove(student.getRollno());
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
