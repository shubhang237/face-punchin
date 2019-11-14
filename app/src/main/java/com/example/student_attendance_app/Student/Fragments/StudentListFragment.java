package com.example.student_attendance_app.Student.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_attendance_app.BuildConfig;
import com.example.student_attendance_app.Student.Model.AttendanceList;
import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Adapter.SectionListAdapter;
import com.example.student_attendance_app.Student.Model.PostAttendance;
import com.example.student_attendance_app.Student.Model.SectionList;
import com.example.student_attendance_app.Student.Model.Status;
import com.example.student_attendance_app.Student.Model.Student;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentListFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 234;
    private static final int CAMERA_REQUEST_CODE = 987;
    private String slot;
    private String date;
    private boolean isLab;
    private SectionListAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> currentSelectedItems;
    private List<String> present;
    private ProgressDialog progressDoalog;
    private List<Student> list;
    private Button markAttendance;
    private ImageButton photoAttendance;
    private ImageButton galleryAttendance;
    public StudentListFragment(String date,String slot,boolean isLab) {
        this.slot = slot;
        this.date = date;
        this.isLab = isLab;
        currentSelectedItems = new ArrayList<>();
        present = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String email = sPref.getString("email","");
        String base_url = sPref.getString("base_url","");
        getData("Token "+token,email,base_url);
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
                String base_url = sPref.getString("base_url","");
                postAttendance("Token "+token,new PostAttendance(date,currentSelectedItems,slot,isLab),base_url);
            }
        });
        photoAttendance = view.findViewById(R.id.photoAttendance);
        photoAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    captureFromCamera();
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Something went wrong with camera!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        galleryAttendance = view.findViewById(R.id.galleryAttendance);
        galleryAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pickFromGallery();
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Something went wrong with gallery!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void pickFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }


    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                markAttendanceWithPhoto(selectedImage);
            }
            catch (Exception e){
                Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void markAttendanceWithPhoto(Uri selectedImage){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        File file = new File(imgPath);
        Log.d("file",file.getAbsolutePath());
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        RequestBody slot = RequestBody.create(MediaType.parse("multipart/form-data"), this.slot);
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String base_url = sPref.getString("base_url","");
        getPresentStudentList("Token "+token,slot,image,base_url);
    }


    public void getPresentStudentList(String token, RequestBody slot, MultipartBody.Part image,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Present Students ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
        Call<AttendanceList> call = service.getPresentList(token,slot,image);
        call.enqueue(new Callback<AttendanceList>() {
            @Override
            public void onResponse(Call<AttendanceList> call, Response<AttendanceList> response) {
                try {
                    progressDoalog.dismiss();
                    AttendanceList list = response.body();
                    String status = list.getStatus();
                    if(status.equals("1")){
                        List<String> studentList = list.getRollnos();
                        currentSelectedItems.addAll(studentList);
                        present.addAll(studentList);
                        adapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e){
                    progressDoalog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getContext(), "could not parse", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AttendanceList> call, Throwable t) {
                progressDoalog.dismiss();
                Log.d("error",t.toString());
                Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getData(String token, String email,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Fetching Student List ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
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

    public void postAttendance(String token, PostAttendance postAttendance,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage("Marking Attendance ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
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
        adapter = new SectionListAdapter(getContext(),list,present,new SectionListAdapter.OnItemCheckListener() {
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
