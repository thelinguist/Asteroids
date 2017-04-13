package edu.byu.cs.superasteroids.models;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 11/12/16.
 */

public class MainBackground extends VisibleObject {
    private static MainBackground instance;

    public MainBackground() {

    }
    public static MainBackground getInstance() {
        if (instance == null) {
            instance = new MainBackground();
        }
        return instance;
    }

    public void draw(Level level) {
        // (int) means that whatever that is, gets cast as an int
        int x = (int) (level.getW()/2 - ViewPort.getInstance().getTopLeft().x);
        int y = (int) (level.getH()/2 - ViewPort.getInstance().getTopLeft().y);

        //DrawingHelper.drawImage(imageIndex, x, y, 0, level.getW()/2048f, level.getH()/2048f, 255);

        DrawingHelper.drawImage(imageIndex, x, y, 0, level.getW()/2048f, level.getH()/2048f, 255);

        //DrawingHelper.drawPoint(new PointF(x,y),200, Color.GREEN,255);

    }
}
