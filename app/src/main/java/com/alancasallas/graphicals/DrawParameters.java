package com.alancasallas.graphicals;

/**
 * Created by Alan on 8/14/2015.
 */
public class DrawParameters {
    public final int NUM_VERTICES;
    public final int DRAW_TYPE;
    public final int START_VERTEX;

    public DrawParameters(int drawType, int vertex_start, int vertices) {
        DRAW_TYPE = drawType;
        START_VERTEX = vertex_start;
        NUM_VERTICES = vertices;
    }

}
