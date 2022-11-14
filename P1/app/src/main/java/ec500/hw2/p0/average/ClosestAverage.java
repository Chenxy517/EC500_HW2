package ec500.hw2.p0.average;

import ec500.hw2.p0.database.GPSDatabase;

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
        database.locDao().updateById("speed1", speed2);
        database.locDao().updateById("speed2", speed3);
        database.locDao().updateById("speed3", currentSpeed);
    }

    public GPSDatabase getDatabase() {
        return database;
    }
}
