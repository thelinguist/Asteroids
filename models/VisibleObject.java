package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 10/21/16.
 */

public class VisibleObject {
    protected String image;
    protected Integer imageIndex;



    public VisibleObject(String image) {
        this.image = image;
        this.imageIndex = ContentManager.getInstance().loadImage(image);
    }

    public VisibleObject() {

    }

    public String getImage() {
        return image;
    }

    public Integer getIndex() { return imageIndex; }

    public void setIndex(Integer index) {
        this.imageIndex = index;
    }


    /**
     * drawing function for non-ship objects only
     * @param x x value
     * @param y y value
     * @param scale scale
     */
    public void draw(float x, float y, float scale) {
        DrawingHelper.drawImage(getIndex(), x, y, 0, scale, scale, 1);
    }


}
