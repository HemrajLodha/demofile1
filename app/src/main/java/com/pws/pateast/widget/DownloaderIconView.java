package com.pws.pateast.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.pws.pateast.R;
import com.pws.pateast.enums.DownloadingStatus;
import com.pws.pateast.utils.ColorUtil;
import com.pws.pateast.utils.FontManager;

/**
 * Created by anshul on 6/2/17.
 */

public class DownloaderIconView extends View {

    private final int CIRCLE_STROKE_WIDTH = 10;
    private final double ANGLE_MULTIPLER = 3.6;
    private final int TOTAL_ANGLE = 360;
    private final int STARTING_ANGLE = -90;
    private final int RECTANGLE_SIZE = 60;
    private int rotateAngle = 0;
    private int progress = 0;
    private Paint mPaint;
    private String mCircle, mNotDownloaded, mWaiting, mCompleted;
    private int itemId;
    private int downloadingColor;

    private DownloadingStatus downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
    private Bitmap bitmap;

    public DownloaderIconView(Context context) {
        super(context);
        init();
    }

    public DownloaderIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DownloaderIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    public void init() {
        mPaint = new Paint();
        mCircle = getContext().getString(R.string.mdi_checkbox_blank_circle);
        mNotDownloaded = getContext().getString(R.string.mdi_attachment);
        mWaiting = getContext().getString(R.string.fa_wait);
        mCompleted = getContext().getString(R.string.fa_file);
        downloadingColor = ColorUtil.getAttrColor(getContext(), R.attr.colorAccent);
    }

    public void updateDownloadingStatus(DownloadingStatus downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
        invalidate();
    }

    public DownloadingStatus getDownloadingStatus() {
        return downloadingStatus;
    }

    public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
        this.downloadingStatus = downloadingStatus;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void updateProgress(Context context, int progress) {
        // DownloadItemHelper.setDownloadPercent(context, itemId, progress);
        this.progress = progress;
    }

    @Override
    public void onDraw(Canvas canvas) {

        Matrix matrix = new Matrix();
        // DownloadItemHelper.setDownloadStatus(getContext(), itemId, downloadingStatus);

        switch (downloadingStatus) {
            case NOT_DOWNLOADED:
                drawTextOnCanvas(canvas, FontManager.getMaterialDesignFont(getContext()), mNotDownloaded, true);
                break;
            case DOWNLOADED:
                drawTextOnCanvas(canvas, FontManager.getFontAwesomeFont(getContext()), mCompleted, false);
                break;
            case IN_PROGRESS:
                drawInProgressIconOnCanvas(canvas);
                break;
            case WAITING:
                matrix.postRotate(rotateAngle++);
                if (rotateAngle < TOTAL_ANGLE) {
                    invalidate();
                }
                drawTextOnCanvas(canvas, FontManager.getFontAwesomeFont(getContext()), mWaiting, false);
                break;
        }
    }

    private void drawInProgressIconOnCanvas(Canvas canvas) {
        //If the download percent is 50%, then the value of the arc should be 50*3.6 which is equal
        // to 180 degree which is half circle.
        double downloadedPercentAngle = progress * ANGLE_MULTIPLER;
        double notDownloadedPercentAngle = TOTAL_ANGLE - downloadedPercentAngle;
        float dX = (canvas.getWidth() - RECTANGLE_SIZE) / 2;
        float dY = (canvas.getHeight() - RECTANGLE_SIZE) / 2;
        RectF rectF = new RectF(dX, dY, RECTANGLE_SIZE + dX, RECTANGLE_SIZE + dY);

        //Draw the downloaded arc.
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(downloadingColor);
        mPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        canvas.drawArc(rectF, STARTING_ANGLE, (float) downloadedPercentAngle, false, mPaint);

        //Draw the not-downloaded arc.
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        canvas.drawArc(rectF, (float) (downloadedPercentAngle + STARTING_ANGLE),
                (float) notDownloadedPercentAngle, false, mPaint);

        // Draw text within the arc.
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(getResources().getDimension(R.dimen.text_14));
        mPaint.setTextAlign(Paint.Align.CENTER);
        dX = canvas.getWidth() / 2;
        dY = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        canvas.drawText(Integer.toString(progress), dX, dY, mPaint);
    }

    private void drawTextOnCanvas(Canvas canvas, Typeface typeface, String text, boolean bgCircle) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTypeface(typeface);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float dX = canvas.getWidth() / 2;
        float dY = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent())));
        if (bgCircle) {
            mPaint.setTextSize(getResources().getDimension(R.dimen.text_size_xx_large));
            mPaint.setColor(Color.parseColor("#AF64B7"));
            canvas.drawText(mCircle, dX, dY, mPaint);
        }
        mPaint.setTextSize(getResources().getDimension(R.dimen.text_size_medium));
        mPaint.setColor(Color.WHITE);
        canvas.drawText(text, dX, dY, mPaint);
    }
}
