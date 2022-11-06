package ec500.hw2.p0.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "speed")
public class Speed {

    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "value")
    public double val;

}
