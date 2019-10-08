package com.example.student_attendance_app.Student.Network;

import com.example.student_attendance_app.Student.Model.DailySchedule;
import com.example.student_attendance_app.Student.Model.PostAttendance;
import com.example.student_attendance_app.Student.Model.SectionList;
import com.example.student_attendance_app.Student.Model.Status;
import com.example.student_attendance_app.Student.Model.SubjectAttendance;
import com.example.student_attendance_app.Student.Model.Timetable;
import com.example.student_attendance_app.Student.Model.TokenData;
import com.example.student_attendance_app.Student.Model.credential;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("accounts/login/")
    Call<TokenData> getToken(@Body credential cred);

    @GET("api/student/")
    Call<SubjectAttendance> getAllAttendance(@Header("Authorization") String token,@Query("email") String email);

    @GET("api/student/timetable/")
    Call<Timetable> getTimetable(@Header("Authorization") String token,@Query("email") String email);


    @GET("api/teacher/timetable/")
    Call<Timetable> getTeacherTimetable(@Header("Authorization") String token,@Query("email") String email);

    @GET("api/teacher/todaysclasses/")
    Call<DailySchedule> getTeacherTodayTimetable(@Header("Authorization") String token, @Query("email") String email);


    @GET("api/section_students/")
    Call<SectionList> getStudentList(@Header("Authorization") String token,@Query("email") String email, @Query("slot") String slot);

    @POST("api/attendances/")
    Call<Status> markAttendances(@Header("Authorization") String token, @Header("Content-Type") String type, @Body PostAttendance postAttendance);
}
