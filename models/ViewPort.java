package edu.byu.cs.superasteroids.models;


import android.graphics.PointF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 11/11/16.
 */

public class ViewPort {
    private PointF locationW;
    private PointF topLeft;

    Integer width;
    Integer height;


    private static ViewPort instance;

    public static ViewPort getInstance() {
            if (instance == null) {
                instance = new ViewPort();
            }
            return instance;
        }

    private ViewPort() {
        locationW = Ship.getInstance().getCenter();
        //locationW = convertWorldToViewPort(locationW);

                //setTopLeft();
        //locationW = Ship.getInstance().getCenter();
        //locationW = convertWorldToViewPort(locationW);

    }

    private void reboundTopLeft() {
        if (topLeft.x < 0) {
            topLeft.x = 0;
        }
        if (topLeft.y < 0) {
            topLeft.y = 0;
        }
        if (topLeft.x > (Game.getInstance().getCurrentLevel().getW() - DrawingHelper.getGameViewWidth())) {
            topLeft.x = Game.getInstance().getCurrentLevel().getW() - DrawingHelper.getGameViewWidth();
        }
        if (topLeft.y > (Game.getInstance().getCurrentLevel().getW() - DrawingHelper.getGameViewHeight())) {
            topLeft.y = Game.getInstance().getCurrentLevel().getW() - DrawingHelper.getGameViewHeight();
        }
    }

    private PointF setTopLeft() {
        PointF out = GraphicsUtils.subtract(locationW, new PointF(width/2, height/2));
        PointF size = new PointF(width, height);
        GraphicsUtils.add(out, size);
        return out;
    }

    public PointF convertWorldToViewPort(PointF world) {
//        initTopLeft();
        return GraphicsUtils.subtract(world, topLeft);
    }

    public PointF convertViewPortToWorld(PointF viewportP) {
        //initTopLeft();
        return GraphicsUtils.subtract(viewportP, topLeft);
    }

    /*public void setLocation(PointF locationW) {
        this.locationW = locationW;
    }*/


    public void draw() {
        //ViewPort
        PointF move = convertWorldToViewPort(Ship.getInstance().getCenter());
        //VIEWPORT COORDINATES
        Ship.getInstance().draw(move.x, move.y);
    }

    public PointF getTopLeft() {
        return topLeft;
    }

    public boolean initTopLeft() {
        if (topLeft == null) {

            //VIEWPORT COORDINATES
            width = DrawingHelper.getGameViewWidth();
            height = DrawingHelper.getGameViewHeight();

            //WORLD COORDINATES             //WORLD           -                  //  1/2 VIEWPORT
        }
        topLeft = GraphicsUtils.subtract(Ship.getInstance().getCenter(), new PointF(width / 2, height / 2));
        reboundTopLeft();

        return true;
    }

    public void update() {
        locationW = Ship.getInstance().getCenter();
        setTopLeft();
    }
}
