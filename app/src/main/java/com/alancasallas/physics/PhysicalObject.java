package com.alancasallas.physics;

import android.opengl.Matrix;
import android.opengl.GLES20;

import com.alancasallas.graphicals.DrawParameters;
import com.alancasallas.graphicals.Graphical;

import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Alan on 8/12/2015.
 */
public abstract class PhysicalObject{

    protected Vector3D position;
    protected Graphical graphical;

    public PhysicalObject() {
        position = new Vector3D(0f,0f,0f);
    }

    public final void setPosition(float x, float y, float z) {
        position.setXYZ(x,y,z);
    }

    public final Vector3D getPosition(float x, float y, float z) {
        return position;
    }

    public abstract void localTransforms(float[] modelMatrix);

    public abstract void activateShaderProgram(float[] modelViewProjectionMatrix);

    public final void draw(float[] modelMatrix, float[] viewProjectionMatrix, float[] modelViewProjectionMatrix) {
        setIdentityM(modelMatrix, 0); //reset modelMatrix
        localTransforms(modelMatrix); //the inheritor class must define its local transforms, however it should maintain its posiiton in the origin
        translateM(modelMatrix, 0, position.getX(), position.getY(), position.getZ()); //move object into position
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0); //create full matrix

        activateShaderProgram(modelViewProjectionMatrix); //tell OpenGL to use shader for object

        //now draw object
        for (DrawParameters parameters : graphical.drawParameters) {
            GLES20.glDrawArrays(parameters.DRAW_TYPE, parameters.START_VERTEX, parameters.NUM_VERTICES);
        }





    }

}
