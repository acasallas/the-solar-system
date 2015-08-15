package com.alancasallas.physics;

import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.Matrix;

import com.alancasallas.graphicals.DrawParameters;
import com.alancasallas.graphicals.DrawUtils;
import com.alancasallas.graphicals.Graphical;
import com.alancasallas.graphicals.SolidColorSphere;
import com.alancasallas.graphicals.VertexArray;
import com.alancasallas.shaderprograms.ColorShaderProgram;
import com.alancasallas.utils.IntRef;

/**
 * Created by Alan on 8/14/2015.
 */
public class Sphere extends PhysicalObject {

    private final int SMOOTHNESS = 10;

    float rotationVelocity; //in degrees
    float rotationAngle; //in degrees/frame

    float orbitalRadius; //in radians
    float orbitalVelocity; //in radians/frame
    float orbitalAngle;

    float radius;

    SolidColorSphere graphicalSphere;
    float r;
    float g;
    float b;

    public Sphere(float radius, float rotationVelocity, float orbitalRadius, float orbitalVelocity, float r, float g, float b) {
        super();

        this.radius = radius;

        this.rotationVelocity = rotationVelocity;
        this.rotationAngle = 0;

        this.orbitalRadius = orbitalRadius;
        this.orbitalVelocity = orbitalVelocity;
        this.orbitalAngle = 0;

        this.r = r;
        this.g = g;
        this.b = b;

    }

    public void prepareGraphics(ColorShaderProgram colorShaderProgram) {
        graphical = graphicalSphere = new SolidColorSphere(radius, SMOOTHNESS,SMOOTHNESS, colorShaderProgram);
    }

    public void orbit() {
        orbitalAngle += orbitalVelocity;
        if (orbitalAngle > 360f) {
            orbitalAngle = orbitalAngle - 360f;
        }
        setPosition(orbitalRadius * (float) Math.cos(orbitalAngle), 0,  orbitalRadius * (float) Math.sin(orbitalAngle));
    }

    public void spin() {
        rotationAngle += rotationVelocity;
        if (rotationAngle > 360f) {
            rotationAngle = rotationAngle - 360f;
        }
    }

    @Override
    public void localTransforms(float[] modelMatrix) {
        Matrix.rotateM(modelMatrix, 0, rotationAngle, 0f, 1f, 0f);
    }

    @Override
    public void activateShaderProgram(float[] modelViewProjectionMatrix) {
        graphicalSphere.activateColorShader(modelViewProjectionMatrix,r,g,b);
    }
}
