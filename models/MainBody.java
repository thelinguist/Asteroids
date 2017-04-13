package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 10/21/16.
 */
public class MainBody extends MovingObject {
    private PointF cannonAttach = new PointF();
    private PointF engineAttach = new PointF();
    private PointF extraAttach = new PointF();
    //private PointF location;
    private PointF center;

    public MainBody(String image, Integer iHeight, Integer iWidth, PointF extraAttach, PointF cannonAttach, PointF engineAttach) {
        super(image, iHeight, iWidth);
        this.cannonAttach = cannonAttach;
        this.engineAttach = engineAttach;
        this.extraAttach = extraAttach;
        center = new PointF(iWidth/2, iHeight/2);

    }

    public PointF getCannonP() {
        return cannonAttach;
    }

    public PointF getEngineP() {
        return engineAttach;
    }

    public PointF getExtraP() {
        return extraAttach;
    }

    public PointF getCenter() { return center; }

    /**
     * override draw to account for multiple parts
     */
    public void draw(float x, float y, float scale, float rotation, int alpha) {
        //location = new PointF(x,y);
        position = new PointF(x,y);
        DrawingHelper.drawImage(imageIndex, x, y, rotation, scale, scale, alpha);
        //DrawingHelper.drawImage(imageIndex, x, y, rotation, 100, 100, 255);

    }
}
