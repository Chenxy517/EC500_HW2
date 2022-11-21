package ec500.hw2.p2.average;

import java.util.ArrayList;
import java.util.Random;

import ec500.hw2.p2.JKalman.jama.Matrix;
import ec500.hw2.p2.JKalman.jkalman.JKalman;
import ec500.hw2.p2.database.GPSDatabase;

public class KalmanFilter {

    private static GPSDatabase database;
    private static JKalman kalman;
    private static Matrix my_Matrix;
    private static Matrix predicted;
    private static Matrix correct;
    private static Random rand;

    private double pdelt;
    private double mdelt;
    private double Gauss;
    private double kalmanGain;
    private final static double Q = 0.00001;
    private final static double R = 0.1;

    public KalmanFilter() throws Exception {
        // Constructor, for Initialize only.
        KalmanInit();
    }

    public static void KalmanInit() throws Exception {

        // For speed and distance, One dimension only (Without 2'd coordinate)

        // dynam_params is "the number of measurement vector dimensions",
        // measure_params is "the number of state vector dimensions".
        kalman = new JKalman(2, 1);

        // initialize for the state matrix from 2 dimension: One is Distance, one is Speed.
        predicted = new Matrix(2, 1); // Average Data: Current state [x, dx]
        correct = new Matrix(2, 1); // Sensor data: corrected state [x', dx']

        my_Matrix = new Matrix(1,1);

        // TODO: find time stamp for this APP
        rand = new Random(System.currentTimeMillis() % 2011);
    }

    public static ArrayList<Double> Filter(ArrayList<Double> speedDistance) throws Exception
    {

        // transitions for x, dx

        double[][] transition_Matrix = {{1, 0}, {0, 1}};

        kalman.setTransition_matrix(new Matrix(transition_Matrix));

        // 1s somewhere?
        kalman.setError_cov_post(kalman.getError_cov_post().identity());

        ArrayList<Double> estimated = new ArrayList<>();

        // Using Java stream to calculate and update the value of speedDistance
        speedDistance.stream().forEach(value -> {
            // Set the current [Distance, speed] to F(x) matrix and append Gaussian noises for each state variable;
            // Note x: state matrix, state variable: Distance, Speed.
            my_Matrix.set(0, 0, value + rand.nextGaussian());

            // According to the current state, Using Newton's second law of motion to calculate / predict the next state' [next_distance, next_speed]
            predicted = kalman.Predict();

            // According to the accurate state's information from sensor Data, [Distance, Speed], append sensor's noise,
            // adjusts stochastic model state on the basis of the given measurement of the model state: [correct_distance, dx]
            correct = kalman.Correct(my_Matrix);

            // Using Correct State to make better approach
            estimated.add((double) predicted.get(0, 0));

        });

        return estimated;
    }

    public static ArrayList<Double> Updating_State(ArrayList<Double> input_stream) throws Exception {

        ArrayList<Double> state_matrix = input_stream;
        state_matrix = Filter(state_matrix);
        return state_matrix;
    }

}



//public class KalmanFilter {
//
//    /**Kalman Filter*/
//    private Integer predict;
//    private Integer current;
//    private Integer estimate;
//    private double pdelt;
//    private double mdelt;
//    private double Gauss;
//    private double kalmanGain;
//    private final static double Q = 0.00001;
//    private final static double R = 0.1;
//
//    public void initial(){
//        pdelt = 4;    //系统测量误差
//        mdelt = 3;
//    }
//    public Integer KalmanFilter(Integer oldValue,Integer value){
//        //(1)第一个估计值
//        predict = oldValue;
//        current = value;
//        //(2)高斯噪声方差
//        Gauss = Math.sqrt(pdelt * pdelt + mdelt * mdelt) + Q;
//        //(3)估计方差
//        kalmanGain = Math.sqrt((Gauss * Gauss)/(Gauss * Gauss + pdelt * pdelt)) + R;
//        //(4)估计值
//        estimate = (int) (kalmanGain * (current - predict) + predict);
//        //(5)新的估计方差
//        mdelt = Math.sqrt((1-kalmanGain) * Gauss * Gauss);
//
//        return estimate;
//    }
//
//}
//
//
//public class Main {
//    private static kalmanfilter = new KalmanFilter();
//    public static void main(String[] arg){
//
//        kalmanfilter.initial();
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        list.add(-75);
//        list.add(-76);
//        list.add(-81);
//        list.add(-75);
//        list.add(-77);
//        list.add(-76);
//        list.add(-86);
//
//        int oldvalue = list.get(0);
//        ArrayList<Integer> alist = new ArrayList<Integer>();
//        for(int i = 0; i < list.size(); i++){
//            int value = list.get(i);
//            oldvalue = kalmanfilter.KalmanFilter(oldvalue,value);
//            alist.add(oldvalue);
//        }
//
//        System.out.println(list);
//        System.out.println(alist);
//
//    }
//}