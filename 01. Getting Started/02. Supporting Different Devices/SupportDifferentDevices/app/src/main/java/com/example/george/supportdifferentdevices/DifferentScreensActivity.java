package com.example.george.supportdifferentdevices;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

public class DifferentScreensActivity extends AppCompatActivity {

    ImageView imageView;
    TextView dimensionsTextView;
    int imageViewHeight;
    int imageViewWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diferrent_screens);

        imageView = (ImageView) findViewById(R.id.imageView);
        dimensionsTextView = (TextView) findViewById(R.id.dimensionsTextView);

        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                imageViewHeight = imageView.getMeasuredHeight();
                imageViewWidth = imageView.getMeasuredWidth();
                dimensionsTextView.setText(imageViewHeight + " * " + imageViewWidth);
                return true;
            }
        });
    }

}
