package edu.byu.cs.superasteroids.models;

import android.graphics.PointF;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 10/21/16.
 */

public class Asteroid extends MovingObject {
    private Integer asteroidId;
    private String name;
    private String type;
    private float scale;
    private Integer levelId;
    private float rotate;
    private double rotateSpeed;
    private int speed;
    private int direction;
    private int damage;
    private boolean isLittle;


    /**
     * for copies
     *
     * @param another
     */
    public Asteroid(Asteroid another, boolean random) {
        this.asteroidId = another.asteroidId;
        this.name = another.name;
        this.type = another.type;
        this.scale = another.scale;
        this.levelId = another.levelId;
        this.rotate = another.rotate;
        this.rotateSpeed = another.rotate;
        this.speed = another.speed;
        this.direction = another.direction;
        damage = 1;
        this.isLittle = another.isLittle;
        //visible object
        this.image = another.image;
        this.imageIndex = another.imageIndex;
        //moving object
        this.iHeight = another.iHeight;
        this.iWidth = another.iWidth;
        this.position = another.position;
        this.bounds = another.bounds;

        if (random) {
            randomize();
        }
    }


    public Asteroid(Integer id, String name, String image, Integer imageWidth, Integer imageHeight, String type) {
        super(image, imageHeight, imageWidth);
        asteroidId = id;
        this.name = name;
        this.type = type;
        randomize();
        damage = 1;
        if (name == "little") {
            isLittle = true;
        }
        else isLittle = false;

    }

    public void setID(Integer ID) {
        asteroidId = ID;
    }


    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Integer getID() {
        return asteroidId;
    }

    public float getRotate() {
        rotate += rotateSpeed;
        return rotate;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDirection() {
        return direction;
    }

    public boolean isLittle() {
        return isLittle;
    }

    public boolean takeHit() {
        damage--;
        if (damage <= 0) {
            return true;
        }
        else {
            System.out.print("not less than or equal to zero");
        }
        return false;
    }

    /**
     * randomly generate speed, rotation, rotation speed, and direction
     * and scale too
     */
    private void randomize() {
        Random rand = new Random();
        //when min val is zero, set just the max bound
        rotate = rand.nextInt(360);

        // -8 thru 8 degrees
        Random randRotateS = new Random();
        rotateSpeed = randRotateS.nextInt(16) - 8;

        Random randSpeed = new Random();
        speed = randSpeed.nextInt(400) + 100;

        Random randDir = new Random();
        direction = randDir.nextInt(360 + 1);

        Random randScale = new Random();
        scale = randScale.nextFloat() % 3 + 1;

    }

    public void draw() {
        //PointF coordinatesV = ViewPort.getInstance().convertWorldToViewPort(position);
        if (imageIndex == null) {
            Game.getInstance().getCurrentLevel().getW();
        }
        DrawingHelper.drawImage(imageIndex, position.x, position.y, getRotate(), scale, scale, 255);
    }

    public void update(double elapsedTime) {
        GraphicsUtils.MoveObjectResult moved = GraphicsUtils.moveObject(position, getBounds(), speed, direction, elapsedTime);
        bounds = moved.getNewObjBounds();



        setPosition(moved.getNewObjPosition());
        GraphicsUtils.RicochetObjectResult bounce = GraphicsUtils.ricochetObject(
                position,
                bounds,
                GraphicsUtils.degreesToRadians(direction),
                Game.getInstance().getCurrentLevel().getW(),
                Game.getInstance().getCurrentLevel().getH());

        bounds = bounce.getNewObjBounds();
        position = bounce.getNewObjPosition();
        direction =(int) GraphicsUtils.radiansToDegrees(bounce.getNewAngleRadians());

        /*
        if (position.x > Game.getInstance().getCurrentLevel().getW() ||
                position.x < 0) {
            direction
        }
        if (position.y > Game.getInstance().getCurrentLevel().getH() ||
                position.y < 0) {
                direction *= -1;
        }
        }
        */
        // TODO: 11/15/16 match bounds
        if (type.equals("growing")) {
            if (scale <= 2) {
                scale *= 1.001;
            }
            //scaleBounds();
        }
    }

    private void scaleBounds() {
        bounds.bottom = getBounds().bottom * scale;
        bounds.top = getBounds().top * scale;
        bounds.right = getBounds().right * scale;
        bounds.left = getBounds().left * scale;
    }

    public String checkCollision() {
        //scaleBounds();
        Projectile toDel = new Projectile();
        boolean delProjectile = false;
        for (Projectile projectile : Game.getInstance().getProjectiles()) {
            if (getBounds().intersect(projectile.getBounds())) {
                toDel = projectile;
                delProjectile = true;
            }
        }
        if (delProjectile) {
            Game.getInstance().getProjectiles().remove(toDel);
            return "projectile";
        }
        if (getBounds().intersect(Ship.getInstance().getBounds())) {
            return "ship";
        }
        return "";
    }

    public ArrayList<Asteroid> split() {
        ArrayList<Asteroid> set = new ArrayList<>();
        isLittle = true;
        scale = scale / 2;
        //scaleBounds();

        if (type.equals("octeroid")) {
            scale = scale / 2;
            //scaleBounds();

            //add six more
            set.addAll(createTwoSubAsteroid());
            set.addAll(createTwoSubAsteroid());
            set.addAll(createTwoSubAsteroid());
        }
        set.addAll(createTwoSubAsteroid());

    return set;
    }

    private ArrayList<Asteroid> createTwoSubAsteroid() {
        ArrayList<Asteroid> set = new ArrayList<>();
        Random randSpeed = new Random();
        speed = randSpeed.nextInt(400) + 100;

        Random randDir = new Random();
        direction = randDir.nextInt(360 + 1);
        set.add(new Asteroid(this, false));

        direction -= 180;
        Random rSpeed2 = new Random();
        speed = rSpeed2.nextInt(400) + 100;
        set.add(new Asteroid(this, false));

        return set;
    }

    public boolean killGrowing() {
        if (scale <= 0.6) {
            isLittle = true;
            return true;
        }
        else {
            scale -= 0.5;
            return false;
        }
    }
}
