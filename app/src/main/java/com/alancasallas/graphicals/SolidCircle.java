package com.alancasallas.graphicals;

import android.opengl.GLES20;

import com.alancasallas.shaderprograms.ColorShaderProgram;

/**
 * Created by Alan on 8/12/2015.
 */
public class SolidCircle extends Graphical {

    final static int POS_COMPONENTS = 2;
    final int NUM_COORDINATES;
    VertexArray vertexArray;
    ColorShaderProgram colorShaderProgram;

    public SolidCircle(float radius, int points, ColorShaderProgram colorShaderProgram) {
        if (points < 2) {
            throw new RuntimeException("Need at least 2 points!");
        }
        if (radius <= 0f) {
            throw new RuntimeException("Radius must be positive!");
        }

        this.colorShaderProgram = colorShaderProgram;

        //create float array with x and y position coordinates
        NUM_COORDINATES = points + 2;
        int numFloats = NUM_COORDINATES * POS_COMPONENTS;
        float[] coordinates = new float[numFloats];

        coordinates[0] = coordinates[1] = 0f;

        for (int n = 2; n < numFloats; n += 2) {
            int unitIndex = (n-2)/2;
            double angle = Math.PI * 2 * ((double)unitIndex/points);
            coordinates[n] = (float)Math.cos(angle) * radius; //x coordinate
            coordinates[n+1] = (float) Math.sin(angle) * radius; //y coordinate
        }

        vertexArray = new VertexArray(coordinates);

        drawParameters = new DrawParameters[1];
        drawParameters[0] = new DrawParameters(GLES20.GL_TRIANGLE_FAN,0,NUM_COORDINATES);
    }

    @Override
    public void bindData() {
        vertexArray.setVertexAttribPointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POS_COMPONENTS, 0);
    }

}
