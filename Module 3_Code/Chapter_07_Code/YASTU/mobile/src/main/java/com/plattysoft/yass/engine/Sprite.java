package com.plattysoft.yass.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.plattysoft.yass.YassActivity;

/**
 * Created by Raul Portales on 23/03/15.
 */
public abstract class Sprite extends ScreenGameObject {

    public double mRotation;

    protected final double mPixelFactor;

    private final Bitmap mBitmap;

    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    public int mAlpha = 255;
    public double mScale = 1;

    protected Sprite (GameEngine gameEngine, int drawableRes, BodyType bodyType) {
        Resources r = gameEngine.getContext().getResources();
        Drawable spriteDrawable = r.getDrawable(drawableRes);

        mPixelFactor = gameEngine.mPixelFactor;

        mHeight = (int) (spriteDrawable.getIntrinsicHeight() * mPixelFactor);
        mWidth = (int) (spriteDrawable.getIntrinsicWidth() * mPixelFactor);

        mBitmap = ((BitmapDrawable) spriteDrawable).getBitmap();

        mRadius = Math.max(mHeight, mWidth)/2;

        mBodyType = bodyType;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mX > canvas.getWidth()
                || mY > canvas.getHeight()
                || mX < -mWidth
                || mY < -mHeight) {
            return;
        }
        if (YassActivity.VISUAL_COLLISION_DEBUG) {
            mPaint.setColor(Color.YELLOW);
            if (mBodyType == BodyType.Circular) {
                canvas.drawCircle((int) (mX + mWidth / 2), (int) (mY + mHeight / 2), (int) mRadius, mPaint);
            } else if (mBodyType == BodyType.Rectangular) {
                canvas.drawRect(mBoundingRect, mPaint);
            }
        }
        float scaleFactor = (float) (mPixelFactor*mScale);
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor);
        mMatrix.postTranslate((float) mX, (float) mY);
        mMatrix.postRotate((float) mRotation, (float) (mX + mWidth*mScale / 2), (float) (mY + mHeight*mScale / 2));
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
