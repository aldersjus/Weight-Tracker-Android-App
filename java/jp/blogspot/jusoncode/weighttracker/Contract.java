package jp.blogspot.jusoncode.weighttracker;


import android.provider.BaseColumns;

final class Contract {
    private Contract(){}

    static class DBCon implements BaseColumns{
        static final String TABLE_NAME = "weights";
        static final String COLUMN_WEIGHT = "weight";
        static final String COLUMN_DATE = "date";
        static final String COLUMN_MEASUREMENT = "measurement";
    }
}
