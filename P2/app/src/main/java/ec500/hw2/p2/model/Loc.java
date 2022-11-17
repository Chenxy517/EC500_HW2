package ec500.hw2.p2.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "loc")
public class Loc {

    @NonNull
    @PrimaryKey
    public String id;

    @ColumnInfo(name = "speed")
    public double speed;

    @ColumnInfo(name = "height")
    public double height;
}
