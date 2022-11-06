package ec500.hw2.p0.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ec500.hw2.p0.model.Speed;

@Dao
public interface SpeedDao {

    @Insert
    void insertAll(Speed... speed);

    @Query("SELECT * FROM speed")
    Speed[] getAll();

    @Query("SELECT * FROM speed WHERE id IN (:userIds)")
    List<Speed> loadAllByIds(String[] userIds);

    @Delete
    void delete(Speed... speed);
}
