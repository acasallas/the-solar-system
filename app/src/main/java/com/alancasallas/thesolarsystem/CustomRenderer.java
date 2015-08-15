package com.alancasallas.thesolarsystem;

import android.content.Context;
import android.opengl.Matrix;
import android.opengl.GLSurfaceView;
import android.opengl.GLES20;

import com.alancasallas.physics.PhysicalObject;
import com.alancasallas.physics.Sphere;
import com.alancasallas.physics.Vector3D;
import com.alancasallas.shaderprograms.ColorShaderProgram;
import com.alancasallas.shaderprograms.TextureShaderProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;


public class CustomRenderer implements GLSurfaceView.Renderer {

    //Solar System Bodies
    Sphere[] worldObjects;

    Vector3D posEye;

    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];

    TextureShaderProgram textureProgram;
    ColorShaderProgram colorProgram;

    Context context;

    public CustomRenderer(Context context) {
        this.context = context;
        posEye = new Vector3D(0f,0f,8f);

        //Sphere(float radius, float rotationVelocity, float orbitalRadius, float orbitalVelocity, float r, float g, float b)
        worldObjects = new Sphere[2];
        worldObjects[0] = new Sphere(2f,.5f,0,0,1f,1f,0f);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //Set Background Color
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);



        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);

        worldObjects[0].prepareGraphics(colorProgram);
        worldObjects[1].prepareGraphics(colorProgram);
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);
        perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 15f);

    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);

        //Create view matrix and multiply them together
        setLookAtM(viewMatrix, 0, posEye.getX(), posEye.getY(), posEye.getZ(), 0f, 0f, -1f, 0f, 1f, 0f);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        //Perform physics
        doPhysics();

        //draw
        render();
    }

    void doPhysics() {
        for (Sphere sphere : worldObjects) {
            sphere.orbit();
            sphere.spin();
        }
    }

    void render() {
        for (Sphere worldObject : worldObjects) {
            worldObject.draw(modelMatrix,viewProjectionMatrix,modelViewProjectionMatrix);
        }
    }




    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect,
                                    float n, float f) {
        final float angleInRadians = (float) (yFovInDegrees * Math.PI / 180.0);

        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));
        m[0] = a / aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f + n) / (f - n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f * f * n) / (f - n));
        m[15] = 0f;
    }
}
