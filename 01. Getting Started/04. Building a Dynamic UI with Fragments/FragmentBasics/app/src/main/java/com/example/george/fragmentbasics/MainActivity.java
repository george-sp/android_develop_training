package com.example.george.fragmentbasics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/*
 * While fragments are reusable, modular UI components, each instance of a Fragment class must
 * be associated with a parent FragmentActivity.
 * You can achieve this association by defining each fragment within your activity layout XML file.
 *
 * If you're using the v7 appcompat library, your activity should instead extend
 * AppCompatActivity, which is a subclass of FragmentActivity.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);
    }
}