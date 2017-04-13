package edu.byu.cs.superasteroids.models;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.IOException;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

import static edu.byu.cs.superasteroids.core.GraphicsUtils.degreesToRadians;
import static edu.byu.cs.superasteroids.core.GraphicsUtils.rotate;
import static edu.byu.cs.superasteroids.core.GraphicsUtils.scale;
import static edu.byu.cs.superasteroids.core.GraphicsUtils.subtract;

/**
 * Created by Bryce on 11/8/16.
 */

public class Cannon extends Part {
    private PointF emitPoint = new PointF();
    private Projectile projectile;
    private String attackSound;

    public Cannon(String image, Integer imageWidth, Integer imageHeight, PointF attachPoint, PointF emitPoint,
                  String attackImage, Integer attackImageWidth, Integer attackImageHeight, String attackSound,
                  Integer hitPoints) {
        super(image, imageWidth, imageHeight, attachPoint);
        this.emitPoint = emitPoint;
        this.projectile = new Projectile(attackImage, attackImageHeight, attackImageWidth, hitPoints);
        this.attackSound = attackSound;
    }

    public String getAttackSound() {
        return attackSound;
    }

    public PointF getEmitPoint() {
        return emitPoint;
    }

    public Projectile fireCannon(PointF hostCenterW,  PointF hostLoc, PointF hostAttach, float scale, float rotationD) {
        //Just like the part offset, just it's an emit point

        PointF partOffset = GraphicsUtils.add(subtract(hostAttach, hostCenterW),
                (subtract(emitPoint, attachPoint)));

        partOffset = scale(partOffset, scale);


        PointF rotPartOffset = rotate(partOffset, degreesToRadians(rotationD));
        //rotPartOffset = scale(rotPartOffset, scale);

        PointF location = GraphicsUtils.add(hostLoc, rotPartOffset);

        //location = GraphicsUtils.add(Ship.getInstance().getPosition(), ViewPort.getInstance().convertViewPortToWorld(location));
        //convert to Viewport
        return new Projectile(projectile, rotationD, scale, location);
    }

}
