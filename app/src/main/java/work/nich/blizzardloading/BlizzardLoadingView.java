package work.nich.blizzardloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

/**
 * BlizzardLoadingView
 * @author nich
 * Guangzhou, China, Asia, Earth.
 */

public class BlizzardLoadingView extends FrameLayout {
    
    private static final int DURATION_TIME = 900;
    private static final int OUTER_CIRCLE_RADIUS = 40;
    private static final int INNER_CIRCLE_RADIUS = 35;
    private static final int RING_PADDING = 8;
    private static final int LEAN_ANGLE = 70;
    
    private RingView mRingA, mRingB, mRingC;
    
    public BlizzardLoadingView(Context context) {
        super(context);
        init();
    }
    
    public BlizzardLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public BlizzardLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mRingA = new RingView(getContext());
        mRingB = new RingView(getContext());
        mRingC = new RingView(getContext());
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        show();
    }
    
    public void show() {
        if (this.getChildCount() == 0) {
            this.addView(mRingA);
            this.addView(mRingB);
            this.addView(mRingC);
        }
    
        startRotateAnimation();
    }
    
    private void startRotateAnimation() {
        rotate(mRingA, LEAN_ANGLE, 0, true, 0f);
        rotate(mRingB, LEAN_ANGLE, 120, false, 70f);
        rotate(mRingC, LEAN_ANGLE, 60, true, 150f);
    }
    
    private void rotate(final View view, float leanAngle, float YDegrees, boolean clockwise, float offsetAngle) {
        if (offsetAngle != 0) {
            view.setRotation(offsetAngle);
        }
        
        AnimationSet set = new AnimationSet(false);
        Rotate3dAnimation angle = new Rotate3dAnimation(getContext(), leanAngle, leanAngle, YDegrees, YDegrees, 0, 0);
        angle.setRepeatCount(Animation.INFINITE);
        
        float startAngle;
        float endAngle;
        if (clockwise) {
            startAngle = 0f;
            endAngle = 360f;
        } else {
            startAngle = 360f;
            endAngle = 0f;
        }
        
        RotateAnimation rotate = new RotateAnimation(startAngle, endAngle, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(DURATION_TIME);
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE);
        set.addAnimation(rotate);
        set.addAnimation(angle);
        
        view.startAnimation(set);
    }
    
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    private class RingView extends View {
        private static final int COLOR = 0xFF1ec0f5;
        
        private Paint mCirclePaint;
        private Path mOuterCirclePath;
        private Path mInnerCirclePath;
        
        private int mWidth;
        private int mHeight;
        
        public RingView(Context context) {
            super(context);
            init();
        }
        
        public RingView(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }
        
        public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }
        
        private void init() {
            mOuterCirclePath = new Path();
            mInnerCirclePath = new Path();
            
            mCirclePaint = new Paint();
            mCirclePaint.setAntiAlias(true);
            mCirclePaint.setColor(COLOR);
        }
        
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            
            mWidth = w;
            mHeight = h;
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.save();
            canvas.translate(mWidth / 2, mHeight / 2);
            
            mOuterCirclePath.addCircle(0, 0, dp2px(OUTER_CIRCLE_RADIUS), Path.Direction.CW);
            mInnerCirclePath.addCircle(dp2px(RING_PADDING), 0, dp2px(INNER_CIRCLE_RADIUS), Path.Direction.CW);
            
            mOuterCirclePath.op(mInnerCirclePath, Path.Op.DIFFERENCE);
            
            canvas.drawPath(mOuterCirclePath, mCirclePaint);
            canvas.restore();
        }
    }
    
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
