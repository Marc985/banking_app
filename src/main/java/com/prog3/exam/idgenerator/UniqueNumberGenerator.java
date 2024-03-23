package com.prog3.exam.idgenerator;

import java.util.Random;

public class UniqueNumberGenerator {
    public static long generateUniqueId() {
        long timestamp = System.currentTimeMillis();
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        long uniqueNumber = timestamp + randomNumber;
    return  uniqueNumber;
    }
}
