package ec500.hw2.p0.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "speed")
public class Speed {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "value")
    public int val;

}
