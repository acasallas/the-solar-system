package com.alancasallas.graphicals;

import android.opengl.GLES20;

/**
 * Created by Alan on 8/14/2015.
 */
public class DrawUtils {

    static public int numVertClosedStrip(int points) {
        return (points+1)*2;
    }

    static public int numVertClosedFan(int points) {
        return (points+2);
    }
}
