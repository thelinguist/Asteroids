package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.Random;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.database.DAO;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.InputManager;

/**
 * Created by Bryce on 10/21/16.
 */

public class Ship {
    private static Ship instance;


    private Cannon cannon;
    private Part extraPart;
    private MainBody mainBody;
    private Engine engine;
    private PowerCore powerCore;
    private Integer baseTurnRate;
    private PointF emmitPoint = new PointF();
    private String sound;
    private Integer damage;     //current damage/health
    private Integer hitPoints;  //dish-out capability
    private boolean invincible = false;


    //for draw function
    private float scale = DrawingHelper.getGameViewWidth()/3000f;
    private float rotation = 0; //0 - 359;
    private PointF center = new PointF();
    private int alpha = 255;
    private RectF bounds;
    double isRotating = 0.0;
    double currentVelocity = 0.0;



    public static Ship getInstance() {
        if (instance == null) {
            instance = new Ship();
        }
        return instance;
    }

    //************************SETTERS************************

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public void setExtraPart(Part extraPart) {
        this.extraPart = extraPart;
    }

    public void setMainBody(MainBody mainBody) {
        this.mainBody = mainBody;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setPowerCore(PowerCore powerCore) {
        this.powerCore = powerCore;
    }

    public void setCenter(PointF center) {
        this.center = center;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    //************************GETTERS************************

    public Cannon getCannon() {
        return cannon;
    }

    public Part getExtraPart() {
        return extraPart;
    }

    public MainBody getMainBody() {
        return mainBody;
    }

    public Engine getEngine() {
        return engine;
    }

    public PowerCore getPowerCore() {
        return powerCore;
    }

    public PointF getCenter() {
        return center;
    }

    public double getSpeed() {
        return engine.getBaseSpeed()*2;
    }

    public double getTurnSpeed() {
        return engine.getBaseTurnRate();
    }

    public double getRotate() {
        return rotation;
    }

    public PointF getPosition() {
        return center;
    }

    public Integer getDamage() { return damage; }

    public void takeHit() {
        damage--;
    }
    /**
     * give me the bounds for each of the parts' sides
     * @return
     */
    public RectF getBounds() {
        // TODO: 11/16/16 bounds are off
                        //World         //
        float left = ViewPort.getInstance().convertWorldToViewPort(center).x - extraPart.getIWidth()/2.5f;
        float right = ViewPort.getInstance().convertWorldToViewPort(center).x + cannon.getIWidth()/2.5f;
        float top = ViewPort.getInstance().convertWorldToViewPort(center).y - mainBody.getIHeight()/4;
        float bottom = ViewPort.getInstance().convertWorldToViewPort(center).y + engine.getIHeight()/2;
        if (ViewPort.getInstance().getTopLeft() == new PointF(0,0)) {
            //left =
        }

        bounds = new RectF(left, top, right, bottom);
        //VIEWPORT COORDINATES
        DrawingHelper.drawRectangle(new Rect((int)left, (int)top,(int) right, (int)bottom), 200, 255);
        return bounds;
    }

    //*********************FUNCTIONALITY*********************

    public void buildRandomShip() {
        Random rand = new Random();
        //when min val is zero, set just the max bound
        int index = rand.nextInt(DAO.getInstance().getMainBodies().size());

        mainBody = DAO.getInstance().getMainBodies().get(index);
        powerCore = DAO.getInstance().getPowerCores().get(index);
        extraPart = DAO.getInstance().getExtraParts().get(index);
        cannon = DAO.getInstance().getCannons().get(index);
        engine = DAO.getInstance().getEngines().get(index);
        //probably don't need this line for each part:
        //engine.setIndex(ContentManager.getInstance().getImageId(engine.getImage()));
    }


    public void draw(float x, float y) {
        if (mainBody != null) {
            mainBody.draw(x, y, scale, rotation, alpha);
            if (extraPart != null) {
                extraPart.draw(mainBody.getCenter(), mainBody.getPosition(), mainBody.getExtraP(), scale, rotation, alpha);
            }
            if (cannon != null) {
                cannon.draw(mainBody.getCenter(), mainBody.getPosition(), mainBody.getCannonP(), scale, rotation, alpha);
            }
            if (engine != null) {
                engine.draw(mainBody.getCenter(), mainBody.getPosition(), mainBody.getEngineP(), scale, rotation, alpha);
            }
        }
        //DrawingHelper.drawPoint(new PointF(x, y), 80, Color.CYAN, 255);


    }

    /**
     * update the position based on the trajectory and rotation values
     */
    public void update(double elapsedTime, Integer invinceTimer) {
        double rotate;
        invincible(invinceTimer);
        if (InputManager.movePoint != null) {
            PointF ship = ViewPort.getInstance().convertWorldToViewPort(center);
            //subtract shipV center from move(V)
            PointF move = GraphicsUtils.subtract(InputManager.movePoint, ship);

            //angle from ship (R)
            rotate = Math.atan2(move.y, move.x);
            rotation = (float) GraphicsUtils.radiansToDegrees(rotate) + 90;
            //save where my ship is being drawn

            // TODO: 11/15/16 center is off.
            //world Coordinates
            // TODO: 11/16/16 replace with getBounds()
            GraphicsUtils.MoveObjectResult moved = GraphicsUtils.moveObject(center, bounds, getSpeed(), rotate, elapsedTime);
            center = moved.getNewObjPosition();
        }
    }


    /**
     * this helps the ship slowly rotate, and it's for extra credit so it doesn't need to be
     * implemented.
     * @param rotate
     */
    private void turningAlgor(double rotate) {
        if (rotation < GraphicsUtils.radiansToDegrees(rotate)) {
            isRotating += engine.getBaseTurnRate();
            rotation = ((float) GraphicsUtils.radiansToDegrees(isRotating));
        }
        else if (GraphicsUtils.radiansToDegrees(rotate) < 0) {
            if (rotation > GraphicsUtils.radiansToDegrees(rotate)){
                isRotating -= engine.getBaseTurnRate();
                rotation = ((float) GraphicsUtils.radiansToDegrees(isRotating));
            }
            else {
                isRotating = 0;
                rotation = ((float) GraphicsUtils.radiansToDegrees(rotate));
            }
        }
        else {
            isRotating = 0;
            rotation = ((float) GraphicsUtils.radiansToDegrees(rotate));
        }
    }

    /**
     * shoot a projectile with the right trajectory from the emmit point. It will also carry the hitPoint value.
     * @return Projectile
     */
    public Projectile fireCannon() {
        // TODO: 11/16/16 some parameter is wrong here, used to be main body. get center()
        //ViewPort.getInstance().convertWorldToViewPort(Ship.getInstance().getCenter())
        return cannon.fireCannon(mainBody.getCenter(), mainBody.getPosition(), mainBody.getCannonP(), scale, rotation);
    }

    /**
     * flash the ship if in invicible mode
     * @param invinceTimer
     */
    public void invincible(Integer invinceTimer) {
        //60 = 1 sec
        //max is 100
        if (invinceTimer < 20) {
            if (invinceTimer % 2 > 0) {
                alpha = 80;
            } else alpha = 255;
        }
        //flash faster
        else if (invinceTimer < 60) {
            if (invinceTimer % 4 > 1) {
                alpha = 80;
            } else alpha = 255;
        }

        //flash slower
        else {
            if (invinceTimer % 8 > 3) {
                alpha = 80;
            } else alpha = 255;
        }

        if (invinceTimer == 0) {
            invincible = false;
        }
        else {
            invincible = true;
        }
    }

    /**
     * if time permits, implement getEngineBoost from PowerCore
     */
    public void setCurrentVelocity() {
    }

    public boolean inSafeMode() {
        return invincible;
    }
}
