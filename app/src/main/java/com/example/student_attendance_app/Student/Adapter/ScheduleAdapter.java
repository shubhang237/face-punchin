package com.example.student_attendance_app.Student.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_attendance_app.Student.Model.Schedule;
import com.example.student_attendance_app.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {


    private List<Schedule> schedule;

    public ScheduleAdapter(List<Schedule> schedule) {
        this.schedule = schedule;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.schedule_card,parent,false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        if(schedule.get(position).getSection() != null) {
            holder.subjectName.setText(schedule.get(position).getSection().getSubject().getName());
            holder.subjectSlot.setText(schedule.get(position).getSection().getSlot());
        }
        else {
            holder.subjectName.setText(schedule.get(position).getLab().getSubject().getName() + " Lab");
            holder.subjectSlot.setText(schedule.get(position).getLab().getSlot());
        }
        holder.startTime.setText(schedule.get(position).getStartTime());
        holder.endTime.setText(schedule.get(position).getEndTime());
        holder.location.setText(schedule.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return schedule.size();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName,subjectSlot,startTime,endTime,location;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectSlot = itemView.findViewById(R.id.subjectSlot);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            location = itemView.findViewById(R.id.venue);

        }
    }
}
