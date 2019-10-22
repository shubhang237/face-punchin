package com.example.student_attendance_app.Student.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.student_attendance_app.R;
import com.example.student_attendance_app.Student.Model.Student;

import java.util.List;


public class SectionListAdapter extends RecyclerView.Adapter<SectionListAdapter.SectionListViewHolder> {

    public interface OnItemCheckListener {
        void onItemCheck(Student item);
        void onItemUncheck(Student item);
    }

    @NonNull
    private OnItemCheckListener onItemClick;

    Context context;
    List<Student> data;
    List<String> present;

    public SectionListAdapter(Context context, List<Student> data,List<String> present,@NonNull OnItemCheckListener onItemCheckListener) {
        this.context = context;
        this.data = data;
        this.present = present;
        this.onItemClick = onItemCheckListener;
    }

    @NonNull
    @Override
    public SectionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.student_card,parent,false);
        return new SectionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SectionListViewHolder holder, int position) {
           if(holder instanceof SectionListViewHolder){
               final Student currentItem  = data.get(position);
               holder.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       holder.checkBox.setChecked(!holder.checkBox.isChecked());
                       if(holder.checkBox.isChecked()){
                           onItemClick.onItemCheck(currentItem);
                       }
                       else {
                           onItemClick.onItemUncheck(currentItem);
                       }
                   }
               });
               for(int i=0;i<present.size();i++){
                   if(data.get(position).getRollno().equals(present.get(i))){
                       holder.checkBox.setChecked(true);
                   }
               }
               holder.rollNo.setText(data.get(position).getRollno());
               holder.studentName.setText(data.get(position).getStudentData().getFirstName()+" "+data.get(position).getStudentData().getLastName());
           }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SectionListViewHolder extends RecyclerView.ViewHolder{

        View itemView;
        TextView studentName,rollNo;
        CheckBox checkBox;

        public SectionListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            studentName = itemView.findViewById(R.id.student_name);
            rollNo = itemView.findViewById(R.id.student_rollNo);
            checkBox = itemView.findViewById(R.id.attendance);
            checkBox.setClickable(false);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
