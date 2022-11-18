package ec500.hw2.p2;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MainActivityTest {

    @Test
    public void speed_unit_transfer_value() {
        assertEquals(3.6, MainActivity.speed_unit_transfer_value(1.0, 1), 0.001);
    }

    @Test
    public void speed_unit_transfer_unit() {
        assertEquals("Mps", MainActivity.speed_unit_transfer_unit(0));
        assertEquals("Kph", MainActivity.speed_unit_transfer_unit(1));
        assertEquals("Mph", MainActivity.speed_unit_transfer_unit(2));
        assertEquals("Mpm", MainActivity.speed_unit_transfer_unit(3));
    }

    @Test
    public void distance_unit_transfer_value() {
        assertEquals(0.125, MainActivity.distance_unit_transfer_value(125, 1), 0.001);
    }

    @Test
    public void distance_unit_transfer_unit() {
        assertEquals("m", MainActivity.distance_unit_transfer_unit(0));
        assertEquals("km", MainActivity.distance_unit_transfer_unit(1));
        assertEquals("miles", MainActivity.distance_unit_transfer_unit(2));
        assertEquals("feet", MainActivity.distance_unit_transfer_unit(3));
    }

    @Test
    public void time_unit_transfer_value() {
        assertEquals(1.6667, MainActivity.time_unit_transfer_value(100, 1), 0.001);
    }

    @Test
    public void time_unit_transfer_unit() {
        assertEquals("s", MainActivity.time_unit_transfer_unit(0));
        assertEquals("mins", MainActivity.time_unit_transfer_unit(1));
        assertEquals("hrs", MainActivity.time_unit_transfer_unit(2));
        assertEquals("days", MainActivity.time_unit_transfer_unit(3));
    }
}