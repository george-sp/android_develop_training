package com.codeburrow.displaygraphicswithopengles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * A two-dimensional triangle for use as a drawn object in OpenGL ES 1.0/1.1.
 * <p/>
 * OpenGL ES allows you to define drawn objects using coordinates in three-dimensional space.
 * So, before you can draw a triangle, you must define its coordinates.
 * In OpenGL, the typical way to do this is to define a vertex array of floating point numbers for the coordinates.
 * For maximum efficiency, you write these coordinates into a ByteBuffer,
 * that is passed into the OpenGL ES graphics pipeline for processing.
 * <p/>
 * Drawing a defined shape using OpenGL ES 2.0 requires a significant amount of code,
 * because you must provide a lot of details to the graphics rendering pipeline.
 * Specifically, you must define the following:
 * - Vertex Shader: OpenGL ES graphics code for rendering the vertices of a shape.
 * - Fragment Shader: OpenGL ES code for rendering the face of a shape with colors or textures.
 * - Program: An OpenGL ES object that contains the shaders you want to use for drawing one or more shapes.
 * You need at least one vertex shader to draw a shape
 * and one fragment shader to color that shape.
 * These shaders must be complied and then added to an OpenGL ES program,
 * which is then used to draw the shape.
 */
public class Triangle {

    private static final String LOG_TAG = Triangle.class.getSimpleName();

    /*
     * Shaders contain OpenGL Shading Language (GLSL) code
     * that must be compiled prior to using it in the OpenGL ES environment.
     */
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private final int mProgram;
    private FloatBuffer vertexBuffer;

    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

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

        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        /*
         * Note:
         *      Compiling OpenGL ES shaders and linking programs is expensive
         *      in terms of CPU cycles and processing time,
         *      so you should avoid doing this more than once.
         *      If you do not know the content of your shaders at runtime,
         *      you should build your code such that they only get created once
         *      and then cached for later use.
         */
        // Create empty OpenGL ES Program.
        mProgram = GLES20.glCreateProgram();
        // Add the vertex shader to program.
        GLES20.glAttachShader(mProgram, vertexShader);
        // Add the fragment shader to program.
        GLES20.glAttachShader(mProgram, fragmentShader);
        // Creates OpenGL ES program executables.
        GLES20.glLinkProgram(mProgram);
    }

    /**
     * Sets the position and color values to the shape's vertex shader and fragment shader,
     * and the executes the drawing function.
     */
    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
        // Add program to OpenGL ES environment.
        GLES20.glUseProgram(mProgram);
        // Get handle to vertex shader's vPosition member.
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // Enable a handle to the triangle vertices.
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        // Prepare the triangle coordinate data.
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
        // Get handle to fragment shader's vColor member.
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // Set color for drawing the triangle.
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        // Pass the projection and view transformation to the shader.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        // Draw the triangle.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        // Disable vertex array.
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
