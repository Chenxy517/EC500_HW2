package ec500.hw2.p2.average;

import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ec500.hw2.p2.database.GPSDatabase;
import ec500.hw2.p2.model.Loc;

public class ClosestAverageTest {
    private GPSDatabase db;


    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, GPSDatabase.class).build();
        Loc loc1 = new Loc();
        loc1.id = "speed1";
        loc1.speed = 1;

        Loc loc2 = new Loc();
        loc1.id = "speed2";
        loc1.speed = 2;

        Loc loc3 = new Loc();
        loc1.id = "speed3";
        loc1.speed = 3;
        db.locDao().insertAll(loc1, loc2, loc3);
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void getAverage() {
        ClosestAverage test = new ClosestAverage(db);
        assertEquals(test.getAverage(), 2, 0.001);
    }
}