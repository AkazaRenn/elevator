package ca.carleton.winter2020.sysc3303a.group8.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SortedDequeTest {

    private SortedDeque<Integer> queue;
    
    @BeforeEach
    void setup() {
        queue = new SortedDeque<>();
        for(int i = -10; i < 20; i += 3) {
            queue.add(i);
        }
    }
    
    @Test
    void getHigher() {
        assertEquals(-4, queue.getHigher(-5));
        assertEquals(-4, queue.getHigher(-6));
        assertEquals(-4, queue.getHigher(-7));
        assertEquals(2, queue.getHigher(0));
        assertEquals(2, queue.getHigher(1));
        assertEquals(5, queue.getHigher(2));
    }
    
    @Test
    void getLower() {
        assertEquals(-7, queue.getLower(-5));
        assertEquals(-7, queue.getLower(-6));
        assertEquals(-10, queue.getLower(-7));
        assertEquals(-1, queue.getLower(0));
        assertEquals(-1, queue.getLower(1));
        assertEquals(-1, queue.getLower(2));
    }

}
