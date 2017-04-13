package edu.byu.cs.superasteroids.base;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.InputManager;
import edu.byu.cs.superasteroids.models.Asteroid;
import edu.byu.cs.superasteroids.models.Game;
import edu.byu.cs.superasteroids.models.MainBackground;
import edu.byu.cs.superasteroids.models.PositionedObject;
import edu.byu.cs.superasteroids.models.Projectile;
import edu.byu.cs.superasteroids.models.Ship;
import edu.byu.cs.superasteroids.models.ViewPort;

/**
 * Controls(not delegates) the GameView
 * Created by Bryce on 10/26/16.
 */

public class GameDelegate implements IGameDelegate {
    Integer pewPewSound;
    Integer levelMusic;
    Integer invinceTimer;
    Integer impactSound;
    Integer textBalloonTimer;
    String levelMessage;


    public GameDelegate() {

    }

    /**
     * generate a random point in the level boundaries
     * @return the point that's random
     */
    private PointF getRandomPoint(int iW, int iH) {
        if (Game.getInstance().getCurrentLevel() == null) {
            return new PointF(1000,1000);
        }
        int width = Game.getInstance().getCurrentLevel().getW();
        int height = Game.getInstance().getCurrentLevel().getH();
        Random rand = new Random();

        //range = bounds thru (height - bounds)
        int n = (height - iH) - iH + 1;
        int i = rand.nextInt(n);
        float y =  iH + i;

        n = (width - iW) - iW + 1;
        i = rand.nextInt(n);
        float x =  iW + i;

        return new PointF(x,y);
    }

    /**
     * helper function (asteroids) for update
     * @param elapsedTime
     */
    private void updateAsteroids(double elapsedTime) {
        for (Asteroid asteroid : Game.getInstance().getCurrentAsteroids()) {
            asteroid.update(elapsedTime);
        }
    }

    /**
     * Updates the game delegate. The game engine will call this function 60 times a second
     * once it enters the game loop.
     * @param elapsedTime Time since the last update. For this game, elapsedTime is always
     *                    1/60th of second
     * a new position or bounding box
     */
    public void update(double elapsedTime) {
        invinceTimer--;
        //textBalloonTimer--;

        //Sanka, you dead mon?

        //check win, and update if necessary
        if (checkGameStatus()) {


            Ship.getInstance().update(elapsedTime, invinceTimer);

            //shoot
            if (InputManager.movePoint != null && InputManager.firePressed) {
                ContentManager.getInstance().playSound(pewPewSound, 1, 1);
                Game.getInstance().getProjectiles().add(Ship.getInstance().fireCannon());
            }

            /* not needed for pass-off
            else {
                Ship.getInstance().setCurrentVelocity();
            }*/

            //update asteroids
            updateAsteroids(elapsedTime);

            //update viewport location
            ViewPort.getInstance().update();


            //check asteroid hits
            AsteroidCollisions();

            //updates AND clears projectiles
            clearProjectiles(elapsedTime);
        }
    }

    /**
     * helps check the state of the game for the update function
     */
    private boolean checkGameStatus() {
        PointF textBalloon = ViewPort.getInstance().convertWorldToViewPort(Ship.getInstance().getCenter());
        if (Ship.getInstance().getDamage() != 0) {
            int gameStatus = Game.getInstance().setCurrentLevel();
            if (gameStatus == 1 || gameStatus == 0) {
                if (gameStatus == 1) {
                    Game.getInstance().getCurrentAsteroids().clear();
                    Game.getInstance().getProjectiles().clear();
                    levelMessage = "You Win!";
                } else {

                    levelMessage = Game.getInstance().getCurrentLevel().getTitle();
                }
                DrawingHelper.drawText(new Point((int)textBalloon.x,(int) textBalloon.y), levelMessage, Color.RED, 150);
            }
            return true;
        }
        else {
            Game.getInstance().getCurrentAsteroids().clear();
            Game.getInstance().getCurrentObjects().clear();
            Game.getInstance().getProjectiles().clear();

            DrawingHelper.drawText(new Point((int)textBalloon.x,(int) textBalloon.y), "Game Over", Color.RED, 100);
            return false;
        }
    }

    /**
     * go thru each asteroid and see if it got hit.
     *  IF it was hit by projectile: delete projectile, delete/break/damage asteroid.
     *  IF it was hit by ship and it's not invincible, damage the ship.
     * afterwards, update asteroid list.
     */
    private void AsteroidCollisions() {
        ArrayList<Asteroid> delIndex = new ArrayList<>();
        ArrayList<Asteroid> toAdd = new ArrayList<>();
        ArrayList<Projectile> delProjectiles = new ArrayList<>();


        //check collisions
        for (Asteroid asteroid : Game.getInstance().getCurrentAsteroids()) {
            String hit = asteroid.checkCollision();

            if (hit.equals("projectile")) {
                if (asteroid.takeHit()) {

                    if (asteroid.getType().equals("growing")) {
                        if (asteroid.killGrowing()) {
                            delIndex.add(asteroid);
                        }
                    }

                    else {
                        if (!asteroid.isLittle()) {
                            toAdd.addAll(asteroid.split());
                        }
                        delIndex.add(asteroid);
                    }

                }

            }

            else if (hit.equals("ship")) {
                if (invinceTimer <= 0) {
                    {
                        ContentManager.getInstance().playSound(impactSound, 1, 1);
                        Ship.getInstance().takeHit();
                        invinceTimer = 100;
                    }
                }

            }
        }

        //add or delete any asteroids
        Game.getInstance().getCurrentAsteroids().removeAll(delIndex);
        Game.getInstance().getCurrentAsteroids().addAll(toAdd);

    }

    /**
     * helper function to clear all projectiles that have left the bounds of the map
     * @param elapsedTime
     */
    private void clearProjectiles(double elapsedTime) {

        ArrayList<Integer> delIndex = new ArrayList<>();
        for (Projectile projectile : Game.getInstance().getProjectiles()) {
            projectile.update(elapsedTime);
            if (projectile.getBounds().intersect(Game.getInstance().getCurrentLevel().getBoundaries())) {
                delIndex.add(Game.getInstance().getProjectiles().indexOf(projectile));
            }
        }
        for (Integer index : delIndex) {
            Game.getInstance().getProjectiles().remove(index);
        }

    }


    /**
     * Loads content such as image and sounds files and other data into the game. The GameActivty will
     * call this once right before entering the game engine enters the game loop. The ShipBuilding
     * activity calls this function in its onCreate() function.
     * @param content An instance of the content manager. This should be used to load images and sound
     *                files.
     */
    public void loadContent(ContentManager content) {
        Game.getInstance().setCurrentLevel();

        //SOUNDS
        try {
            //ONLY WORKS ON DEVICE, and EMULATOR only after reboot
            levelMusic = content.loadLoopSound(Game.getInstance().getCurrentLevel().getMusic());
            pewPewSound = content.loadSound(Ship.getInstance().getCannon().getAttackSound());
            impactSound = content.loadSound("sounds/impact.wav");

        } catch (IOException e) {
            e.printStackTrace();
        }
        content.playLoop(levelMusic);

        //BACKGROUND IMAGES
        String[] images = {"images/space.bmp", "images/space_alt.jpg", "images/space_heart.jpg", "images/space_nebula.jpg", "images/space_nova.jpg", "images/space_pluto.jpg"};
        MainBackground.getInstance().setIndex(content.loadImage(images[3]));


        //SHIP
        Ship.getInstance().setScale(.25f);
        Ship.getInstance().setDamage(3);
        invinceTimer = 100;
        //gives WORLD COORDINATES
        PointF randomPoint = getRandomPoint(400, 400);
        Ship.getInstance().setCenter(randomPoint);


        //ASTEROIDS
        for (Asteroid asteroid : Game.getInstance().getCurrentAsteroids()) {
            //set the index and the initial position
            asteroid.setIndex(content.loadImage(asteroid.getImage()));
            asteroid.setPosition(getRandomPoint(asteroid.getIWidth(),asteroid.getIHeight()));
        }

        //LEVEL-OBJECTS
        for (PositionedObject object : Game.getInstance().getCurrentObjects()) {
            //set the index and the initial position
            object.setIndex(content.loadImage(object.getImage()));
        }

        //PROJECTILES
        Game.getInstance().setProjectiles(new ArrayList<Projectile>());

    }

    /**
     * Unloads content from the game. The GameActivity will call this function after the game engine
     * exits the game loop. The ShipBuildingActivity will call this function after the "Start Game"
     * button has been pressed.
     * @param content An instance of the content manager. This should be used to unload image and
     *                sound files.
     */
    public void unloadContent(ContentManager content) {
        content.pauseLoop(levelMusic);
    }

    /**
     * Draws the game delegate. This function will be 60 times a second.
     * image @ position
     */
    public void draw() {
        ViewPort.getInstance().initTopLeft();

        MainBackground.getInstance().draw(Game.getInstance().getCurrentLevel());
        for (PositionedObject object : Game.getInstance().getCurrentObjects()) {
            object.draw();
        }

        for (Asteroid asteroid : Game.getInstance().getCurrentAsteroids()) {
            asteroid.draw();
        }
        if (!Game.getInstance().getProjectiles().isEmpty()) {
            for (Projectile projectile : Game.getInstance().getProjectiles()) {
                projectile.draw();
            }
        }

        ViewPort.getInstance().draw();

    }
}
