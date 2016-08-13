package com.codeburrow.build_open_gl_es_environment_demo_5;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.codeburrow.build_open_gl_es_environment_demo_5.helloopengles10.OpenGLES10Activity;
import com.codeburrow.build_open_gl_es_environment_demo_5.helloopengles20.OpenGLES20Activity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openGLES20(View view) {
        startActivity(new Intent(MainActivity.this, OpenGLES20Activity.class));
    }

    public void openGLES10(View view) {
        startActivity(new Intent(MainActivity.this, OpenGLES10Activity.class));
    }
}
