package ec500.hw2.p2.average;

import ec500.hw2.p2.database.GPSDatabase;
import ec500.hw2.p2.model.Loc;

public class HistoricalAverage {
    private GPSDatabase database;
    private double prevAverageSpeed, prevAverageHeight, dataNum;
    private String[] ids;

    public HistoricalAverage(GPSDatabase db) {
        database = db;
    }

    public void updateAverage(double curSpeed, double curHeight) {
        ids = new String[1];
        ids[0] = "average";
        if (database.locDao().loadAllByIds(ids).size() > 0) {
            prevAverageSpeed = database.locDao().loadById("average").speed;
            prevAverageHeight = database.locDao().loadById("average").height;
        } else {
            prevAverageSpeed = 0;
            prevAverageHeight = 0;
            Loc loc = new Loc();
            loc.id = "average";
            loc.speed = 0;
            loc.height = 0;
            database.locDao().insertAll(loc);
        }

        ids = new String[1];
        ids[0] = "data-num";
        if (database.locDao().loadAllByIds(ids).size() > 0) {
            dataNum = database.locDao().loadById("data-num").speed;
        } else {
            dataNum = 0;
            Loc loc = new Loc();
            loc.id = "data-num";
            loc.speed = 0;
            database.locDao().insertAll(loc);
        }

        double curAverageSpeed = (prevAverageSpeed * dataNum + curSpeed) / (dataNum + 1);
        double curAverageHeight = (prevAverageHeight * dataNum + curHeight) / (dataNum + 1);
        dataNum = dataNum + 1;

        database.locDao().deleteById("average");
        Loc newAverage = new Loc();
        newAverage.id = "average";
        newAverage.speed = curAverageSpeed;
        newAverage.height = curAverageHeight;
        database.locDao().insertAll(newAverage);

        database.locDao().deleteById("data-num");
        Loc newDataNum = new Loc();
        newDataNum.id = "data-num";
        newDataNum.speed = dataNum;
        database.locDao().insertAll(newDataNum);
    }

    public double getAverageSpeed() {
        return database.locDao().loadById("average").speed;
    }

    public double getAverageHeight() {
        return database.locDao().loadById("average").height;
    }

    public GPSDatabase getDatabase() {
        return database;
    }
}
