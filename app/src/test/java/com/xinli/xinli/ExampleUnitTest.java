package com.xinli.xinli;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Calendar tmp = Calendar.getInstance();
        tmp.set(2010, 11, 12);
        Date enrollmentDate = tmp.getTime();
        System.out.println(enrollmentDate);
        assertEquals(4, 2 + 2);
    }
}