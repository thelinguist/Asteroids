package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;

/**
 * Created by Bryce on 11/8/16.
 */

public class PowerCore extends VisibleObject {
    Integer cannonBoost;
    Integer engineBoost;

    public PowerCore(String image, Integer cannonBoost, Integer engineBoost) {
        super(image);
        this.cannonBoost = cannonBoost;
        this.engineBoost = engineBoost;
    }

    public PowerCore() {

    }


    public Integer getCannonBoost() {
        return cannonBoost;
    }

    public Integer getEngineBoost() { return engineBoost;
    }

}
