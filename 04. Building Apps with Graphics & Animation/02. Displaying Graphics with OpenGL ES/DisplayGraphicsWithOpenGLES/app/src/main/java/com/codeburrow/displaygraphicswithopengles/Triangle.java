package com.codeburrow.displaygraphicswithopengles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 1.0/1.1.
 *
 * OpenGL ES allows you to define drawn objects using coordinates in three-dimensional space.
 * So, before you can draw a triangle, you must define its coordinates.
 * In OpenGL, the typical way to do this is to define a vertex array of floating point numbers for the coordinates.
 * For maximum efficiency, you write these coordinates into a ByteBuffer,
 * that is passed into the OpenGL ES graphics pipeline for processing.
 */
public class Triangle {

    private static final String LOG_TAG = Triangle.class.getSimpleName();

    private FloatBuffer vertexBuffer;

    // Number of coordinates per vertex in this array.
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values.
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    public Triangle() {
        // Initialize vertex byte buffer for shape coordinates.
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // Use the device hardware's native byte order.
        bb.order(ByteOrder.nativeOrder());

        // Create a floating point buffer from the ByteBuffer.
        vertexBuffer = bb.asFloatBuffer();
        // Add the coordinates to the FloatBuffer.
        vertexBuffer.put(triangleCoords);
        // Set the buffer to read the first coordinate.
        vertexBuffer.position(0);
    }
}
