package com.alancasallas.graphicals;

/**
 * Created by Alan on 8/12/2015.
 */
public abstract class Graphical {

    public static final int BYTES_PER_FLOAT = 4;

    public DrawParameters[] drawParameters;

    protected abstract void bindData();


}
