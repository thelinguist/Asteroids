package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;


/**
 * Created by Bryce on 10/21/16.
 */

public class MovingObject extends VisibleObject {
    protected PointF position = new PointF();
    protected Integer iWidth;
    protected Integer iHeight;
    protected RectF bounds;

    public MovingObject(String image, Integer iHeight, Integer iWidth) {
        super(image);
        this.iWidth = iWidth;
        this.iHeight = iHeight;
    }

    public MovingObject() {

    }

    /**
     * This overrides the update function of positioned object in case there is a trajectory to keep track of
     */
    public void update() {

    }

    public int getIWidth() {
        return iWidth;
    }

    public int getIHeight() {
        return iHeight;
    }

    /**
     * get the bounds if the position is set. If bounds isn't set, set it.
     * @return
     */
    public RectF getBounds() {
        try {
            if (position == null) {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (bounds == null) {
            bounds = new RectF((position.x - iWidth / 2),
                        (position.y - iHeight / 2),
                        (position.x + iWidth / 2),
                        (position.y + iHeight / 2));
        }
        bounds = new RectF((position.x - iWidth / 2),
                (position.y - iHeight / 2),
                (position.x + iWidth / 2),
                (position.y + iHeight / 2));
        DrawingHelper.drawRectangle(new Rect((int)bounds.left, (int)bounds.top,(int) bounds.right, (int)bounds.bottom), 200, 255);

        return bounds;
    }


    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
    }
}
