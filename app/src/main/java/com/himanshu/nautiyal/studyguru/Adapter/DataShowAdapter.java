package com.himanshu.nautiyal.studyguru.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.himanshu.nautiyal.studyguru.Database.DataList;
import com.himanshu.nautiyal.studyguru.R;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;

public class DataShowAdapter extends RecyclerView.Adapter<DataShowAdapter.MyHolder> {
    ArrayList<DataList> arrayList;

    public DataShowAdapter(ArrayList<DataList> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li= (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=li.inflate(R.layout.adapter_single_day,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        DataList dataList=arrayList.get(position);
  holder.tvTotalTime.setText(dataList.getTotolTime());
        Log.d("DSA", "onBindViewHolder: "+dataList.getTotolTime()+"  "+dataList.getDate());
      holder.imageDay.setImageResource(getImageOfDay(dataList.getDay()));
      holder.circularRating.setProgress(Float.parseFloat(dataList.getRatings()));
      holder.tvRating.setText(dataList.getRatings()+"%");
  holder.tvDate.setText(dataList.getDate());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView tvDate,tvTotalTime,tvRating;
        ImageView imageDay;
        CircularProgressBar circularRating;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvRating=itemView.findViewById(R.id.tvRating);
            circularRating=itemView.findViewById(R.id.progressRating);
            imageDay=itemView.findViewById(R.id.imageDay);
            tvTotalTime=itemView.findViewById(R.id.tvTotalTime);
        }
    }
    int getImageOfDay(String day){
        int dataToReturn=R.drawable.monday_logo;
        switch (day){
            case "mon": dataToReturn= R.drawable.monday_logo;break;
            case "tue": dataToReturn= R.drawable.tuesday_logo;break;
            case "wed": dataToReturn= R.drawable.wednesday_logo;break;
            case "thur": dataToReturn= R.drawable.thrusday_logo;break;
            case "fri": dataToReturn= R.drawable.friday_logo;break;
            case "sat": dataToReturn= R.drawable.saturday_logo;break;
            case "sun": dataToReturn= R.drawable.sunday_logo;break;
        }
        return dataToReturn;
    }
}
