package com.gerefi;

import com.devexperts.logging.FileLogger;
import com.opensr5.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 * what the hell is this anyway? rename this utility class?
 * 6/30/13
 * Andrey Belomutskiy, (c) 2013-2020
 */
public class FileLog {

    public static final String LOG_INFO_TEXT = "Writing logs to '" + FileLogger.DIR + "'";
    public static final String OS_VERSION = "os.version";

    FileLog() {
    }

    public static String getDate() {
        return FileLogger.getDate();
    }

    public static boolean isLinux() {
        return getOsName().equalsIgnoreCase("Linux");
    }

    public static String getOsName() {
        return System.getProperty("os.name");
    }

    public static boolean is32bitJava() {
        return System.getProperty("os.arch").contains("86");
    }

    public static boolean isWindows() {
        return getOsName().contains("Windows");
    }
}
