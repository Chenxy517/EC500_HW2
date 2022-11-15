package ec500.hw2.p0.average;

import ec500.hw2.p0.database.GPSDatabase;
import ec500.hw2.p0.model.Loc;

public class HistoricalAverage {
    private GPSDatabase database;

    public HistoricalAverage(GPSDatabase db) {
        database = db;
    }

    public void updateAverage(double curSpeed, double curHeight) {
        double prevAverageSpeed = database.locDao().loadById("average").speed;
        double prevAverageHeight = database.locDao().loadById("average").height;
        double dataNum = database.locDao().loadById("data-num").speed;

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
