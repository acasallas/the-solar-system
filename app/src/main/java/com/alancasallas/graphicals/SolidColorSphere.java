package com.alancasallas.graphicals;

import android.opengl.GLES20;

import com.alancasallas.physics.Vector3D;
import com.alancasallas.shaderprograms.ColorShaderProgram;
import com.alancasallas.utils.IntRef;

/**
 * Created by Alan on 8/15/2015.
 */
public class SolidColorSphere extends Graphical {
    ColorShaderProgram colorShaderProgram;
    final static int POS_COMPONENTS = 3;
    final static int VERTEX_COMPONENTS = 3;
    final int NUM_COORDINATES;
    VertexArray vertexArray;

    public SolidColorSphere(float radius, int levels, int points, ColorShaderProgram colorShaderProgram) {
        if (levels < 1) {
            throw new RuntimeException("Need at least 2 levels!");
        }
        if (points < 3) {
            throw new RuntimeException("Need at least 3 points!");
        }
        if (radius <= 0f) {
            throw new RuntimeException("Radius must be positive!");
        }

        this.colorShaderProgram = colorShaderProgram;

        NUM_COORDINATES =  (levels-1) * 2 * DrawUtils.numVertClosedStrip(points) // there are 1 less strips than levels
                + DrawUtils.numVertClosedFan(points)*2; //top and bottom caps

        drawParameters = new DrawParameters[levels*2];

        float[] coordinates = new float[NUM_COORDINATES * POS_COMPONENTS];

        IntRef coorIndex = new IntRef();
        IntRef drawParamIndex = new IntRef();

        float pi = (float)Math.PI;
        //loop through phi values, creating spherical strip pairs at each phi
        for (int n = 1; n < (levels*2-1); n++) {
            float topPhi = pi*(float)(n+1)/(levels*2);
            float bottomPhi = pi*(float) n/(levels*2);
            addSphericalStrip(radius, topPhi, bottomPhi, points, coorIndex, drawParamIndex, coordinates, drawParameters);
        }

        float frontCapPhi = 1f/(levels*2);
        addCap(radius,0,frontCapPhi,points,coorIndex,drawParamIndex,coordinates,drawParameters);
        float backCapPhi = (float)((levels*2)-1)/(levels*2);
        addCap(radius,(float)Math.PI,backCapPhi,points,coorIndex,drawParamIndex,coordinates,drawParameters);

        vertexArray = new VertexArray(coordinates);
    }

    void addCartesianCoor(float x, float y, float z, IntRef coorIndex, float[] coordinates) {
        coordinates[coorIndex.integer++] = x; //x
        coordinates[coorIndex.integer++] = y; //y
        coordinates[coorIndex.integer++] = z; //z
    }

    void addSphericalCoor(float radius, float theta, float phi, IntRef coorIndex, float[] coordinates) {
        coordinates[coorIndex.integer++] = (float)(radius * Math.cos(theta) * Math.sin(phi)); //x
        coordinates[coorIndex.integer++] = (float)(radius * Math.sin(theta) * Math.sin(phi)); //y
        coordinates[coorIndex.integer++] = (float)(radius * Math.cos(phi)); //z
    }

    void addSphericalStrip(float radius,float topPhi, float bottomPhi, int points,IntRef coorIndex, IntRef drawParamIndex,  float[] coordinates, DrawParameters[] drawParameters) {
        float twoPi = (float)Math.PI*2f;
        //Add top strip
        int startCoordinate = coorIndex.integer;
        float theta = 0;
        for (int n = 0; n <= points; n++) {
            theta = twoPi*(float)n/points;
            addSphericalCoor(radius,theta,topPhi,coorIndex,coordinates);
            addSphericalCoor(radius,theta,bottomPhi,coorIndex,coordinates);
        }
        drawParameters[drawParamIndex.integer++] =
                new DrawParameters(GLES20.GL_TRIANGLE_STRIP,startCoordinate/VERTEX_COMPONENTS,(coorIndex.integer-startCoordinate)/VERTEX_COMPONENTS);
    }

    void addCap(float radius, float tipPhi, float phi, int points, IntRef coorIndex, IntRef drawParamIndex, float[] coordinates, DrawParameters[] drawParameters) {
        //add Coordinates
        int startCoordinate = coorIndex.integer;
        addSphericalCoor(radius, 0, tipPhi, coorIndex, coordinates);
        for (int n = 0; n <= points; n++) {
            float theta = (float)(Math.PI*2)* ((float)n/points);
            addSphericalCoor(radius, theta, phi, coorIndex, coordinates);
        }
        //add DrawParameters
        drawParameters[drawParamIndex.integer++] =
                new DrawParameters(GLES20.GL_TRIANGLE_FAN,startCoordinate/VERTEX_COMPONENTS,(coorIndex.integer-startCoordinate)/VERTEX_COMPONENTS);

    }

    public void activateColorShader(float[] modelViewProjectionMatrix,float r, float g, float b) {
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(modelViewProjectionMatrix, r, g, b);
        bindData();
    }

    @Override
    protected void bindData() {
        vertexArray.setVertexAttribPointer(0,
                colorShaderProgram.getPositionAttributeLocation(),
                POS_COMPONENTS, 0);
    }
}
