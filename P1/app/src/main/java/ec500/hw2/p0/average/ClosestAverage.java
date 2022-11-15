package ec500.hw2.p0.average;

import ec500.hw2.p0.database.GPSDatabase;
import ec500.hw2.p0.model.Loc;

public class ClosestAverage {
    private GPSDatabase database;

    public ClosestAverage(GPSDatabase db) {
        database = db;
    }

    public double getAverage() {
        double speed1 = database.locDao().loadById("speed1").speed;
        double speed2 = database.locDao().loadById("speed2").speed;
        double speed3 = database.locDao().loadById("speed3").speed;
        return (speed1 + speed2 + speed3) / 3;
    }

    public void updateClosest(double currentSpeed) {
        double speed2 = database.locDao().loadById("speed2").speed;
        double speed3 = database.locDao().loadById("speed3").speed;
        database.locDao().deleteById("speed1");
        Loc loc1 = new Loc();
        loc1.id = "speed1";
        loc1.speed = speed2;
        database.locDao().insertAll(loc1);

        database.locDao().deleteById("speed2");
        Loc loc2 = new Loc();
        loc1.id = "speed2";
        loc1.speed = speed3;
        database.locDao().insertAll(loc2);

        database.locDao().deleteById("speed3");
        Loc loc3 = new Loc();
        loc1.id = "speed3";
        loc1.speed = currentSpeed;
        database.locDao().insertAll(loc3);
    }

    public GPSDatabase getDatabase() {
        return database;
    }
}
