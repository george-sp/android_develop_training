package com.codeburrow.displaygraphicswithopengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 * <p/>
 * Again, you should define the vertices in a counterclockwise order for both triangles
 * that represent this shape, and put the values in a ByteBuffer.
 * In order to avoid defining the two coordinates shared by each triangle twice,
 * use a drawing list to tell the OpenGL ES graphics pipeline how to draw these vertices.
 */
public class Square {

    private static final String LOG_TAG = Square.class.getSimpleName();

    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    // Number of coordinates per vertex in this array.
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right

    private short drawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    public Square() {
        // Initialize vertex byte buffer for shape coordinates.
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // Initialize byte buffer for the draw list.
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }
}