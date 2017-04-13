package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

/**
 * Created by Bryce on 11/8/16.
 */

public class Engine extends Part {
    private Integer baseSpeed;
    private Integer baseTurnRate;

    public Engine(String image, Integer imageWidth, Integer imageHeight, PointF attachPoint, Integer baseSpeed, Integer baseTurnRate) {
        super(image, imageWidth, imageHeight, attachPoint);
        this.baseSpeed = baseSpeed;
        this.baseTurnRate = baseTurnRate;
    }

    /*
    public Engine() {

    }
    */

    public Integer getBaseSpeed() {
        return baseSpeed;
    }

    public Integer getBaseTurnRate() {
        return baseTurnRate;
    }
}
