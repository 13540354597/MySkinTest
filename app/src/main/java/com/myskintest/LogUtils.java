package com.myskintest;

import java.io.IOException;

/**
 * Created by TR 105 on 2017/8/2.
 */

class LogUtils {
    public static boolean DEBUG = false;

    public static void e(IOException ioex) {
        ioex.printStackTrace();
    }

    public static void e(Exception ioex) {
        ioex.printStackTrace();
    }


}
