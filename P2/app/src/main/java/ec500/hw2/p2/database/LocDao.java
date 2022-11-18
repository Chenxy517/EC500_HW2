package ec500.hw2.p2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ec500.hw2.p2.model.Loc;

@Dao
public interface LocDao {
    @Insert
    void insertAll(Loc... loc);

    @Delete
    void delete(Loc loc);

    @Query("DELETE FROM loc WHERE id = :ID")
    void deleteById(String ID);

    @Query("SELECT * FROM loc WHERE id = :ID")
    Loc loadById(String ID);

    @Query("SELECT * FROM loc WHERE id IN (:ids)")
    List<Loc> loadAllByIds(String[] ids);

    @Query("SELECT * FROM loc")
    List<Loc> getAll();
}
