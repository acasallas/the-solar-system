package com.alancasallas.physics;

/**
 * Created by Alan on 8/15/2015.
 */
/**
 * Convenience library to do vector calculations
 *
 * @author garysoed
 */
public class Vector3D {

    private float x;
    private float y;
    private float z;

    public Vector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setXYZ(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float length() {
        return (float)Math.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        return this.dot(this);
    }

    public float dot(Vector3D v3) {
        return x * v3.x + y * v3.y + z * v3.z;
    }

    public Vector3D add(Vector3D v3) {
        setXYZ(x + v3.x, y + v3.y, z + v3.z);
        return this;
    }

    public void subtractSet(Vector3D v3) {
        setXYZ(x - v3.x, y - v3.y, z - v3.z);
    }

    public Vector3D subtractNew(Vector3D v3) {
        return new Vector3D(x - v3.x, y - v3.y, z - v3.z);
    }

    public Vector3D multiply(float constant) {
        setXYZ(x * constant, y * constant, z * constant);
        return this;
    }

    public Vector3D unitVector() {
        float length = length();
        return this.multiply(1 / length);
    }

    /*
    public float bearing() {
        return (float) Math.atan2(y, x);
    }*/

    public static Vector3D copyOf(Vector3D vector) {
        return new Vector3D(vector.x, vector.y, vector.z);
    }
}