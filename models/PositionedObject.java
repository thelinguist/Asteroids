package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 10/21/16.
 */

public class PositionedObject extends VisibleObject {
    private PointF coordinates;
    private Integer scale;
    private Integer objectId;
    private Integer levelId;



    public PositionedObject(String image, PointF coordinates, Integer scale, Integer objectId, Integer levelId) {
        super(image);
        this.coordinates = coordinates;
        this.scale = scale;
        this.objectId = objectId;
        this.levelId = levelId;

    }

    /**
     * This will just update the coordinates (which won't change)
     */
    public void update() {
    }

    public void draw() {
        PointF coordinatesV = ViewPort.getInstance().convertWorldToViewPort(coordinates);
        DrawingHelper.drawImage(imageIndex, coordinatesV.x, coordinatesV.y, 0, scale, scale, 255);
    }

}
