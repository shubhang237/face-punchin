package com.example.student_attendance_app.Student.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.student_attendance_app.Student.Model.AttendanceList;
import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Adapter.SectionListAdapter;
import com.example.student_attendance_app.Student.Model.PostAttendance;
import com.example.student_attendance_app.Student.Model.SectionList;
import com.example.student_attendance_app.Student.Model.Status;
import com.example.student_attendance_app.Student.Model.Student;
import com.example.student_attendance_app.Student.Network.GetDataService;
import com.example.student_attendance_app.Student.Network.RetrofitClientInstance;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
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

    private static final int GALLERY_REQUEST_RESNET = 234;
    private static final int GALLERY_REQUEST_AGEIT = 158;
    private static final int CAMERA_REQUEST_RESNET = 987;
    private static final int CAMERA_REQUEST_AGEIT = 560;

    private String slot;
    private String date;
    private boolean isLab;
    private SectionListAdapter adapter;
    private RecyclerView recyclerView;
    private List<String> currentSelectedItems;
    private List<String> present;
    private ProgressDialog progressDoalog;
    private List<Student> studList;
//    private Button presentCount;
//    private Button absentCount;

    private Button markAttendance;
    private ImageButton photoAttendance;
    private ImageButton galleryAttendance;
    private ToggleButton modeButton;
    public StudentListFragment(String date,String slot,boolean isLab) {
        this.slot = slot;
        this.date = date;
        this.isLab = isLab;
        currentSelectedItems = new ArrayList<>();
        present = new ArrayList<>();
        studList = new ArrayList<>();
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
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        final String token = sPref.getString("access_token","");
        final String base_url = sPref.getString("base_url","");
//        presentCount = view.findViewById(R.id.presentCount);
//        presentCount.setText(String.valueOf(currentSelectedItems.size()));
//        absentCount = view.findViewById(R.id.absentCount);
//        absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
        modeButton = view.findViewById(R.id.mode);
        modeButton.setText("PRO");
        modeButton.setTextOn("LITE");
        modeButton.setTextOff("PRO");
        markAttendance = view.findViewById(R.id.markAttendance);
        markAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAttendance("Token "+token,new PostAttendance(date,currentSelectedItems,slot,isLab),base_url);
            }
        });
        photoAttendance = view.findViewById(R.id.photoAttendance);
        photoAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String mode = modeButton.getText().equals("PRO") ? "ageit" : "resnet";
                    captureFromCamera(mode);
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
                    String mode = modeButton.getText().equals("PRO") ? "ageit" : "resnet";
                    pickFromGallery(mode);
                }
                catch (Exception e){
                    Toast.makeText(getContext(), "Something went wrong with gallery!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void captureFromCamera(String type) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                if(type.equals("resnet"))
                    startActivityForResult(intent, CAMERA_REQUEST_RESNET);
                else if(type.equals("ageit")){
                    startActivityForResult(intent,CAMERA_REQUEST_AGEIT);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pickFromGallery(String type){
        try {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            if (type.equals("resnet")) {
                startActivityForResult(intent, GALLERY_REQUEST_RESNET);
            } else if (type.equals("ageit")) {
                startActivityForResult(intent, GALLERY_REQUEST_AGEIT);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK) {

            if(requestCode == CAMERA_REQUEST_AGEIT){
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);
                    markAttendanceWithPhoto(tempUri, "ageit");
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == CAMERA_REQUEST_RESNET){
                try {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri tempUri = getImageUri(getActivity().getApplicationContext(), photo);
                    markAttendanceWithPhoto(tempUri,"resnet");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == GALLERY_REQUEST_AGEIT){
                try {
                    final Uri imageUri = data.getData();
                    markAttendanceWithPhoto(imageUri,"ageit");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == GALLERY_REQUEST_RESNET){
                try {
                    final Uri imageUri = data.getData();
                    markAttendanceWithPhoto(imageUri,"resnet");
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Something went wrong !", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // CREATE A MATRIX FOR THE MANIPULATION
//        Matrix matrix = new Matrix();
//        // RESIZE THE BIT MAP
//        matrix.postScale(scaleWidth, scaleHeight);
//
//        // "RECREATE" THE NEW BITMAP
//        Bitmap resizedBitmap = Bitmap.createBitmap(
//                bm, 0, 0, width, height, matrix, false);
//        bm.recycle();
//        return resizedBitmap;
//    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void markAttendanceWithPhoto(Uri selectedImage,String type){
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String imgPath = cursor.getString(columnIndex);
        cursor.close();
        File file = new File(imgPath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), fileReqBody);
        RequestBody slot = RequestBody.create(MediaType.parse("multipart/form-data"), this.slot);
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        String token = sPref.getString("access_token","");
        String base_url = sPref.getString("base_url","");
        if(type.equals("resnet")) {
            getPresentStudentList("Token " + token, slot, image, base_url);
        }
        else if(type.equals("ageit")){
            getPresentStudentListAgeit("Token "+token,slot,image,base_url);
        }
    }

    public void getPresentStudentListAgeit(String token, RequestBody slot, MultipartBody.Part image,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        progressDoalog.setMessage("Fetching Present Students ....");
        progressDoalog.show();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(base_url).create(GetDataService.class);
        Call<AttendanceList> call = service.getPresentListAgeit(token,slot,image);
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
//                        absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
//                        presentCount.setText(String.valueOf(currentSelectedItems.size()));
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
                Toast.makeText(getContext(), "request failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getPresentStudentList(String token, RequestBody slot, MultipartBody.Part image,String base_url){
        progressDoalog = new ProgressDialog(getContext());
        SharedPreferences sPref = getContext().getSharedPreferences("MyPref",0);
        final String mode = sPref.getString("mode","ageit");
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
//                        absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
//                        presentCount.setText(String.valueOf(currentSelectedItems.size()));
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
        studList = sectionList.getStudents();
        recyclerView = getView().findViewById(R.id.studentListRecyclerView);
//        absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
//        presentCount.setText(String.valueOf(currentSelectedItems.size()));
        adapter = new SectionListAdapter(getContext(),studList,present,new SectionListAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(Student student) {
                currentSelectedItems.add(student.getRollno());
//                absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
//                presentCount.setText(String.valueOf(currentSelectedItems.size()));
            }

            @Override
            public void onItemUncheck(Student student) {
                currentSelectedItems.remove(student.getRollno());
//                absentCount.setText(String.valueOf(studList.size()-currentSelectedItems.size()));
//                presentCount.setText(String.valueOf(currentSelectedItems.size()));
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
