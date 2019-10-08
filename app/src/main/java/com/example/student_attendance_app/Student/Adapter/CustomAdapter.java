package com.example.student_attendance_app.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_attendance_app.Student.Model.Section;
import com.example.student_attendance_app.Student.Model.Subject;
import com.example.student_attendance_app.Student.Model.SubjectAttendance;
import com.example.student_attendance_app.R;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;
    private SubjectAttendance data;

    public CustomAdapter(Context context, SubjectAttendance data) {
        this.context = context;
        this.data = data;

    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subject_card,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Section sec = data.sections.get(position);
        Subject subj =  sec.getSubject();
        String total = String.valueOf(sec.getTotal());
        String present = String.valueOf(sec.getPresent());
        String slot = sec.getSlot();

        float percentage = ((float)(sec.getPresent())/sec.getTotal())*100;
        holder.subjectName.setText(subj.getName());
        holder.total.setText("Total : "+total);
        holder.present.setText("Present : "+present);
        holder.code.setText(subj.getCode());
        holder.slotText.setText( "Slot : "+slot);
        holder.percent.setText(String.valueOf(String.format("%.2f",percentage))+"%");
    }

    @Override
    public int getItemCount() {
        return data.sections.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName,present,total,code,percent,slotText;
        CustomViewHolder(View itemView){
            super(itemView);
            percent = itemView.findViewById(R.id.percentage);
            subjectName = itemView.findViewById(R.id.subjectName);
            slotText = itemView.findViewById(R.id.slot);
            present = itemView.findViewById(R.id.present);
            total = itemView.findViewById(R.id.total);
            code = itemView.findViewById(R.id.subjectCode);
        }
    }
}


