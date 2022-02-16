package com.example.pinchwithtwofingers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mInfoTextView;
    private ImageView mImageView;
    private Bitmap mBitmap;

    private int mBmpWidth, mBmpHeight;

    // Touch event related variables
    private int mTouchState;
    final int IDLE = 0;
    final int TOUCH = 1;
    final int PINCH = 2;
    private float mStartDistance, mCurrentDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfoTextView = (TextView) findViewById(R.id.textViewinfo);
        mImageView = (ImageView) findViewById(R.id.imageView4);

        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.omnemix);
        mBmpWidth = mBitmap.getWidth();
        mBmpHeight = mBitmap.getHeight();

        mCurrentDistance = 1; // значение по умолчанию (с потолка)
        mStartDistance = 1; // значение по умолчанию (с потолка)
        resizeImage();
        mImageView.setOnTouchListener(onTouchListener);
        mTouchState = IDLE;
    }

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float distX, distY;

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    mInfoTextView.setText("ACTION_DOWN");
                    mTouchState = TOUCH;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    // A non-primary pointer has gone down.
                    mInfoTextView.setText("ACTION_POINTER_DOWN");
                    mTouchState = PINCH;

                    // Get the distance when the second pointer touch
                    distX = event.getX(0) - event.getX(1);
                    distY = event.getY(0) - event.getY(1);
                    mStartDistance = (float) Math.sqrt(distX * distX + distY * distY);

                    break;
                case MotionEvent.ACTION_MOVE:
                    mInfoTextView.setText("ACTION_MOVE");

                    if (mTouchState == PINCH) {
                        // Get the current distance
                        distX = event.getX(0) - event.getX(1);
                        distY = event.getY(0) - event.getY(1);
                        mCurrentDistance = (float) Math.sqrt(distX * distX + distY
                                * distY);

                        resizeImage();
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    mInfoTextView.setText("ACTION_UP");
                    mTouchState = IDLE;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mInfoTextView.setText("ACTION_POINTER_UP");
                    mTouchState = TOUCH;
                    break;
            }
            /*
             * Возвращаем 'true', чтобы распознавать события ACTION_MOVE и ACTION_UP.
             * Если оставить генерируемый 'false', то код будет определять только
             * ACTION_DOWN
             */
            return true;
        }
    };

    private void resizeImage() {
        float curScale = mCurrentDistance / mStartDistance;
        if (curScale < 0.1) {
            curScale = 0.1f;
        }

        Bitmap resizedBitmap;
        int newHeight = (int) (mBmpHeight * curScale);
        int newWidth = (int) (mBmpWidth * curScale);
        resizedBitmap = Bitmap.createScaledBitmap(mBitmap, newWidth, newHeight,
                false);
        mImageView.setImageBitmap(resizedBitmap);
    }
}