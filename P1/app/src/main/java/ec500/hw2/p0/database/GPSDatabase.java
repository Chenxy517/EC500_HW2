package ec500.hw2.p0.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import ec500.hw2.p0.model.Speed;

@Database(entities = {Speed.class}, version = 1, exportSchema = false)
public abstract class GPSDatabase extends RoomDatabase {

    public abstract SpeedDao speedDao();
}
