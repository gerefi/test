package com.gerefi.autodetect;

import com.devexperts.logging.Logging;
import com.gerefi.NamedThreadFactory;
import com.gerefi.io.LinkManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * Andrey Belomutskiy, (c) 2013-2020
 * @see SerialPortScanner what's the relationship?
 */
public class PortDetector {
    private final static Logging log = Logging.getLogging(PortDetector.class);

    private static final NamedThreadFactory AUTO_DETECT_PORT = new NamedThreadFactory("ECU AutoDetectPort", true);
    public static final String AUTO = "auto";

    /**
     * Connect to all serial ports and find out which one respond first
     * @param callback
     * @return port name on which gerEFI was detected or null if none
     */
    @NotNull
    public static SerialAutoChecker.AutoDetectResult autoDetectSerial(Function<SerialAutoChecker.CallbackContext, Void> callback) {
        String rusEfiAddress = System.getProperty("gerefi.address");
        if (rusEfiAddress != null) {
            return getSignatureFromPorts(callback, Collections.singleton(rusEfiAddress));
        }
        final Set<String> serialPorts = LinkManager.getCommPorts();
        if (serialPorts.isEmpty()) {
            log.error("No serial ports detected");
            return new SerialAutoChecker.AutoDetectResult(null, null);
        }
        log.info("Trying [" + String.join(", ", serialPorts) + "]");
        return getSignatureFromPorts(callback, serialPorts);
    }

    @NotNull
    private static SerialAutoChecker.AutoDetectResult getSignatureFromPorts(Function<SerialAutoChecker.CallbackContext, Void> callback, Set<String> serialPorts) {
        List<Thread> serialFinder = new ArrayList<>();
        CountDownLatch portFound = new CountDownLatch(1);
        AtomicReference<SerialAutoChecker.AutoDetectResult> result = new AtomicReference<>();
        for (String serialPort : serialPorts) {
            Thread thread = AUTO_DETECT_PORT.newThread(new Runnable() {
                @Override
                public void run() {
                    new SerialAutoChecker(serialPort, portFound).openAndCheckResponse(result, callback);
                }

                @Override
                public String toString() {
                    return serialPort + " " + super.toString();
                }
            });
            serialFinder.add(thread);
            thread.start();
        }
        try {
            portFound.await(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
        log.info("Now interrupting " + serialFinder);
        try {
            for (Thread thread : serialFinder) {
                log.info("Interrupting " + thread);
                thread.interrupt();
            }
        } catch (RuntimeException e) {
            log.error("Unexpected runtime", e);
        }
        log.info("Done interrupting!");

        SerialAutoChecker.AutoDetectResult autoDetectResult = result.get();
        if (autoDetectResult == null)
            autoDetectResult = new SerialAutoChecker.AutoDetectResult(null, null);
        log.debug("Found " + autoDetectResult + " now stopping threads");
//        log.info("Returning " + result.get());
        return autoDetectResult;
    }

    public static String autoDetectSerialIfNeeded(String port) {
        if (!isAutoPort(port))
            return port;
        return autoDetectSerial(null).getSerialPort();
    }

    public static boolean isAutoPort(String port) {
        return port.toLowerCase().startsWith(AUTO);
    }
}
