package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

import static edu.byu.cs.superasteroids.core.GraphicsUtils.*;

/**
 * Created by Bryce on 10/21/16.
 */

public class Part extends MovingObject {
    protected PointF attachPoint = new PointF();     //this is relative to image
    protected PointF partCenter;

    public Part(String image, Integer imageWidth, Integer imageHeight, PointF attachPoint) {
        super(image, imageHeight, imageWidth);
        this.attachPoint = attachPoint;
        partCenter = new PointF(imageWidth/2, imageHeight/2);

    }


    /**
     * @return attachpoint
     */
    public PointF getAttachPoint() {
        return attachPoint;
    }

    public void draw(PointF hostCenter, PointF hostLoc, PointF hostAttach, float scale, float rotationD, int alpha) {

        PointF partOffset = GraphicsUtils.add(subtract(hostAttach, hostCenter),
                                            (subtract(partCenter, attachPoint)));

        partOffset = scale(partOffset, scale);

        //PointF location = GraphicsUtils.add(hostLoc, partOffset);


        PointF rotPartOffset = rotate(partOffset, degreesToRadians(rotationD));
        //rotPartOffset = scale(rotPartOffset, scale);

        PointF location = GraphicsUtils.add(hostLoc, rotPartOffset);
        //location = add(partOffset, location);
        //location = add(partOffset, hostLoc);

        position = location;
        DrawingHelper.drawImage(getIndex(), location.x, location.y, rotationD, scale, scale, alpha);

    }
}
