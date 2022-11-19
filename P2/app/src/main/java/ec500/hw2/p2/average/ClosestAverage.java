package ec500.hw2.p2.average;

import ec500.hw2.p2.database.GPSDatabase;
import ec500.hw2.p2.model.Loc;

public class ClosestAverage {
    private GPSDatabase database;
    private double speed1, speed2, speed3;
    private String[] ids;

    public ClosestAverage(GPSDatabase db) {
        database = db;
    }

    // return the average speed of the three closest locations
    public double getAverage() {
        ids = new String[1];
        ids[0] = "speed1";
        if (database.locDao().loadAllByIds(ids).size() > 0) {
            speed1 = database.locDao().loadById("speed1").speed;
        } else {
            speed1 = 0;
            Loc loc = new Loc();
            loc.id = "speed1";
            loc.speed = 0;
            database.locDao().insertAll(loc);
        }

        ids = new String[1];
        ids[0] = "speed2";
        if (database.locDao().loadAllByIds(ids).size() > 0) {
            speed2 = database.locDao().loadById("speed2").speed;
        } else {
            speed2 = 0;
            Loc loc = new Loc();
            loc.id = "speed2";
            loc.speed = 0;
            database.locDao().insertAll(loc);
        }

        ids = new String[1];
        ids[0] = "speed3";
        if (database.locDao().loadAllByIds(ids).size() > 0) {
            speed3 = database.locDao().loadById("speed3").speed;
        } else {
            speed3 = 0;
            Loc loc = new Loc();
            loc.id = "speed3";
            loc.speed = 0;
            database.locDao().insertAll(loc);
        }
        return (speed1 + speed2 + speed3) / 3;
    }

    // update the average speed stored in the database
    public void updateClosest(double currentSpeed) {
        speed2 = database.locDao().loadById("speed2").speed;
        speed3 = database.locDao().loadById("speed3").speed;

        database.locDao().deleteById("speed1");
        Loc loc1 = new Loc();
        loc1.id = "speed1";
        loc1.speed = speed2;
        database.locDao().insertAll(loc1);

        database.locDao().deleteById("speed2");
        Loc loc2 = new Loc();
        loc2.id = "speed2";
        loc2.speed = speed3;
        database.locDao().insertAll(loc2);

        database.locDao().deleteById("speed3");
        Loc loc3 = new Loc();
        loc3.id = "speed3";
        loc3.speed = currentSpeed;
        database.locDao().insertAll(loc3);
    }

    // return database to MainActivity
    public GPSDatabase getDatabase() {
        return database;
    }
}
