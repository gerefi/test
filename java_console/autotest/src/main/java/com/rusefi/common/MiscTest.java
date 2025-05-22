package com.gerefi.common;

import com.gerefi.GerefiTestBase;
import com.gerefi.binaryprotocol.BinaryProtocol;
import com.gerefi.core.Sensor;
import com.gerefi.core.SensorCentral;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.devexperts.util.TimeUtil.SECOND;
import static com.gerefi.IoUtil.sleepSeconds;
import static org.junit.Assert.assertTrue;

public class MiscTest extends GerefiTestBase {
    @Test
    public void burn() {
        BinaryProtocol bp = ecu.getLinkManager().getCurrentStreamState();
        // let's make sure 'burn' command works since sometimes it does not
        bp.burn();
    }

    @Test
    public void testGetAllOutputs() throws InterruptedException {
        CountDownLatch responseLatch = new CountDownLatch(1);
        ecu.getLinkManager().execute(() -> {
            boolean result = ecu.getLinkManager().getBinaryProtocol().requestOutputChannels();
            System.out.println("requestOutputChannels=" + result);
            responseLatch.countDown();
        });
        responseLatch.await(1, TimeUnit.MINUTES);
    }

    @Test
    public void testMcuTemperature() throws InterruptedException {
        double mcuTemp = Double.NaN;
        long start = System.currentTimeMillis();
        while (Double.isNaN(mcuTemp) && (System.currentTimeMillis() - start) < 5 * SECOND) {
            Thread.sleep(100);
            mcuTemp = SensorCentral.getInstance().getValue(Sensor.INT_TEMP);
        }

        System.out.println("MCU temperature is " + mcuTemp + " deg C");
        BinaryProtocol bp = ecu.getLinkManager().getCurrentStreamState();
        assertTrue("Happy OutputChannels expected", bp.isGoodOutputChannels);

        // You are probably indoors and not on fire
        String message = "mcuTemp is " + mcuTemp;
        assertTrue(message, mcuTemp > 10);
        // wow sometimes my utility closet is pretty warm?
        assertTrue(message, mcuTemp < 52);
    }

    @Test
    public void testSetDate() {
        // set some random time
        ecu.sendCommand("set date 2022-12-07T11:14:22");

        // If things are going to crash, let it happen in this test
        sleepSeconds(5);
    }
}
