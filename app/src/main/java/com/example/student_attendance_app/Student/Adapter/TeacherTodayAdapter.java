package com.example.student_attendance_app.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Model.Schedule;
import java.util.List;

public class TeacherTodayAdapter extends RecyclerView.Adapter<TeacherTodayAdapter.TeacherTodayViewHolder> {

    private Context context;
    private List<Schedule> classes;
    private Fragment fragment;

    public TeacherTodayAdapter(Context context, List<Schedule> classes, Fragment fragment) {
        this.context = context;
        this.classes = classes;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public TeacherTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.daily_card, parent, false);
        return new TeacherTodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherTodayViewHolder holder, int position) {

        if(classes.get(position).getSection() != null) {
            holder.subjectName.setText(classes.get(position).getSection().getSubject().getName());
            holder.subjectSlot.setText(classes.get(position).getSection().getSlot());
        }
        else {
            holder.subjectName.setText(classes.get(position).getLab().getSubject().getName());
            holder.subjectSlot.setText(classes.get(position).getLab().getSlot());
        }

        holder.location.setText(classes.get(position).getLocation());
        holder.startTime.setText(classes.get(position).getStartTime());
        holder.endTime.setText(classes.get(position).getEndTime());


    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    class TeacherTodayViewHolder extends RecyclerView.ViewHolder{


        TextView subjectName,subjectSlot,startTime,endTime,location;

        public TeacherTodayViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectName);
            subjectSlot = itemView.findViewById(R.id.subjectSlot);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            location = itemView.findViewById(R.id.venue);
        }
    }

//    public void setViewAdapter(final List<Schedule> schedule, RecyclerView recyclerView, final Context context) {
//        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(schedule);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(scheduleAdapter);
//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context,
//                recyclerView, new ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//                //Values are passing to activity & to fragment as well
//                TextView slotTextView = view.findViewById(R.id.subjectSlot);
//                boolean isLab = schedule.get(position).getCategory().equals("Lab") ? true : false;
////                Toast.makeText(context, "Single Click on position        :"+position+" "+slotTextView.getText().toString()+" "+email,
////                        Toast.LENGTH_SHORT).show();
//                loadFragment(new StudentListFragment(slotTextView.getText().toString(),isLab));
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                Toast.makeText(context, "Long press on position :"+position,
//                        Toast.LENGTH_LONG).show();
//            }
//        }));
//    }
}
