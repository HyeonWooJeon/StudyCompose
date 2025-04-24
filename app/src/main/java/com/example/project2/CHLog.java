package com.example.project2;

import android.util.Log;

import androidx.compose.runtime.State;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CHLog {
    private static boolean SHOW = true;
    private static boolean DEBUG_FLAG = true;
    private static boolean ERROR_FLAG = true;
    private static boolean INFO_FLAG = true;
    private static boolean WARNING_FLAG = true;
    private static boolean VERBOS_FLAG = true;

    public CHLog(@NotNull String s, @NotNull State<? extends List<SearchWord>> saveSearchWordWord)
    {}

    public static void d(String tag, String message) {
        if (DEBUG_FLAG && SHOW) {
            String log = buildLogMsg(message);
            Log.d(tag, log);
        }
    }

    public static void e(String tag, String message) {
        if (ERROR_FLAG && SHOW) {
            String log = buildLogMsg(message);
            Log.e(tag, log);
        }
    }

    public static void i(String tag, String message) {
        if (INFO_FLAG && SHOW) {
            String log = buildLogMsg(message);
            Log.i(tag, log);
        }
    }

    public static void w(String tag, String message) {
        if (WARNING_FLAG && SHOW) {
            String log = buildLogMsg(message);
            Log.w(tag, log);
        }
    }

    public static void v(String tag, String message) {
        if (VERBOS_FLAG && SHOW) {
            String log = buildLogMsg(message);
            Log.v(tag, log);
        }
    }

    private static String buildLogMsg(String message) {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
        return "[" +
                ste.getFileName() +
                " > " +
                ste.getMethodName() +
                " > #" +
                ste.getLineNumber() +
                "] " +
                message;
    }

}
