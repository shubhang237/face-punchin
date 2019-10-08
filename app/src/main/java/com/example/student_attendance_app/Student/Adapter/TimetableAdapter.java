package com.example.student_attendance_app.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_attendance_app.Student.Model.Schedule;
import com.example.student_attendance_app.Student.Model.Timetable;
import com.example.student_attendance_app.R;

import java.util.List;


public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private Context context;
    private Timetable data;

    public TimetableAdapter(Context context, Timetable data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.timetable_card,parent,false);
        return new TimetableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        switch (position){
            case 0: holder.day.setText("Monday");
                    setViewAdapter(data.getMonday(),holder.classView,holder.context);
                    break;
            case 1: holder.day.setText("Tuesday");
                    setViewAdapter(data.getTuesday(),holder.classView,holder.context);
                    break;
            case 2: holder.day.setText("Wednesday");
                    setViewAdapter(data.getWednesday(),holder.classView,holder.context);
                    break;
            case 3: holder.day.setText("Thursday");
                    setViewAdapter(data.getThursday(),holder.classView,holder.context);
                    break;
            case 4: holder.day.setText("Friday");
                    setViewAdapter(data.getFriday(),holder.classView,holder.context);
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class TimetableViewHolder extends RecyclerView.ViewHolder{
        RecyclerView classView;
        TextView day;
        Context context;
        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            classView = itemView.findViewById(R.id.scheduleRecyclerView);
            context = itemView.getContext();
        }
    }

    public void setViewAdapter(List<Schedule> schedule, RecyclerView recyclerView, Context context){
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedule);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(scheduleAdapter);
    }
}
