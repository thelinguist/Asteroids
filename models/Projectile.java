package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 10/21/16.
 */

public class Projectile extends MovingObject {
    private Integer hitPoints;
    private float direction;
    private double speed;
    private Float scale;


    public Projectile(String image, Integer iHeight, Integer iWidth, Integer hitPoints) {
        super(image, iHeight, iWidth);
        this.hitPoints = hitPoints;
    }

    public Projectile(Projectile another, float direction, float scale, PointF position) {
        this.hitPoints = another.hitPoints;
        this.image = another.image;
        this.imageIndex = another.imageIndex;
        this.iHeight = another.iHeight;
        this.iWidth = another.iWidth;
        this.direction = direction;
        speed = 600;
        this.scale = scale;
        this.position = position;

    }

    public Projectile() {}

    /**
     * @return hitPoints of projectile
     */
    public Integer getHP() {
        return hitPoints;
    }

    public void draw() {
        PointF coordinatesV = ViewPort.getInstance().convertWorldToViewPort(position);
        DrawingHelper.drawImage(imageIndex, position.x, position.y, direction, scale, scale, 255);
    }

    public void update(double elapsedTime) {
        GraphicsUtils.MoveObjectResult moved = GraphicsUtils.moveObject(position, getBounds(), speed, GraphicsUtils.degreesToRadians(direction-90), elapsedTime);
        setPosition(moved.getNewObjPosition());
        bounds = moved.getNewObjBounds();
    }
}
