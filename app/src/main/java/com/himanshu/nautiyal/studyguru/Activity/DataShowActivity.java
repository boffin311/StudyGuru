package com.himanshu.nautiyal.studyguru.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.himanshu.nautiyal.studyguru.Adapter.DataShowAdapter;
import com.himanshu.nautiyal.studyguru.Database.DataList;
import com.himanshu.nautiyal.studyguru.Database.EachDayDatabase;
import com.himanshu.nautiyal.studyguru.Database.MyDatabaseHelper;
import com.himanshu.nautiyal.studyguru.R;

import java.util.ArrayList;

public class DataShowActivity extends AppCompatActivity {
SQLiteDatabase db;
Button btnInsert;
RecyclerView rvAllDayDate;
ArrayList<DataList> dataLists;
public static final String TAG="DSA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_show);
        rvAllDayDate =findViewById(R.id.rvAllDayData);

        MyDatabaseHelper myDataBaseHelper=new MyDatabaseHelper(this);
        db = myDataBaseHelper.getWritableDatabase();
        dataLists=EachDayDatabase.readAll(db);

        rvAllDayDate.setLayoutManager(new LinearLayoutManager(this));
        final DataShowAdapter dataShowAdapter=new DataShowAdapter(dataLists);
        rvAllDayDate.setAdapter(dataShowAdapter);
        btnInsert=findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EachDayDatabase.insertPlayer(db,new DataList("mon","22/03/2020","3 hr. 45 min.","65"));
                dataLists=EachDayDatabase.readAll(db);
                dataShowAdapter.notifyDataSetChanged();
                Log.d(TAG, "onClick: "+EachDayDatabase.readAll(db).get(0).getDate());
            }
        });


    }
}
