package com.himanshu.nautiyal.studyguru.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.himanshu.nautiyal.studyguru.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EachDayDatabase implements Constants {
    public static final String TABLE_NAME="SingleDay";
    public static final String TABLE_B_NAME="TeamB";
    public static final String CommonTableName="Common";
    public static String ID="ID";
    public interface ColumnCommon
    {
        String DAY="Day ";
        String DATE="Date ";
        String RATING="Rating ";
        String TOTAL_TIME="TotalTime ";

    }
    int Sum=0;
//    public final static String Foul="Fouls ";
//    public static final String Player="Player ";
//    public static final String ThemeTableName="Theme";

//    public static final String[] All_Column={ID,Column.Name,Column.Sum,Foul,Player};
//    public static final String[] AllTheme={ ThemeColumn.Theme};
    public static final String[] All={ID,ColumnCommon.DAY, ColumnCommon.DATE,ColumnCommon.RATING,ColumnCommon.TOTAL_TIME};
//    public interface Column{ String Name="Name ";String Sum="Sum ";}
//    public interface ThemeColumn { String Theme="Theme";}
    public static final String CMD_CREATE_SINGLE_TABLE=
            Create+TABLE_NAME+LBR+
                    ID +INT+"PRIMARY KEY "+
                    COMMA+
                    ColumnCommon.DAY+TEXT+
                    COMMA+
                    ColumnCommon.DATE+TEXT+
                    COMMA+
                    ColumnCommon.RATING+TEXT+
                    COMMA+
                    ColumnCommon.TOTAL_TIME+ TEXT +
                    RBR+SEMI;


//    public static final String CMD_CREATE_THEME= Create+ThemeTableName+LBR+ ThemeColumn.Theme+INT+ RBR+SEMI;
//    public static final String insert="insert into "+ThemeTableName+" values(2);";
    public static void insertPlayer(SQLiteDatabase db, DataList team){
        ContentValues row=new ContentValues();
        row.put(ColumnCommon.DAY,team.getDay());
        row.put(ColumnCommon.DATE,team.getDate());
        row.put(ColumnCommon.RATING,team.getRatings());
        row.put(ColumnCommon.TOTAL_TIME,team.getTotolTime());
        db.insert(TABLE_NAME,null,row);
    }


    public static ArrayList<DataList> readAll(SQLiteDatabase db)
    {
        ArrayList<DataList> arrayList=new ArrayList<>();
        Cursor c=db.query(TABLE_NAME,All,null,null,
                null,null,null);
        while(c.moveToNext())
        {
            DataList teamAList=new DataList();
            teamAList.setDay(c.getString(1));
            teamAList.setDate(c.getString(2));
            teamAList.setRatings(c.getString(3));
            teamAList.setTotolTime(c.getString(4));

            arrayList.add(teamAList);
        }
        return arrayList;
    }



//
//
//    public static void DeleteTable(SQLiteDatabase db,int position)
//    {
//        db.delete(TABLE_NAME,ID+"="+(position+1),null);
//    }
//    public  static void UpdateScore(SQLiteDatabase db,TeamAList teamA,int position){
//        ContentValues values=new ContentValues();
//        values.put(Column.Sum,teamA.getSum());
//        db.update(TABLE_NAME,values,ID+"="+(position+1),null);
//
//    }
//






}
