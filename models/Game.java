package edu.byu.cs.superasteroids.models;

import java.util.ArrayList;
import java.util.Set;

import edu.byu.cs.superasteroids.database.DAO;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * Created by Bryce on 11/8/16.
 * --Welcome to the game--
 */

public class Game {

    private static Ship playerShip = Ship.getInstance();
    private Level currentLevel;
    private int currentLevelID = 1;
    private ArrayList<Asteroid> currentAsteroids;
    private Set<PositionedObject> currentLevelObjects;
    private ArrayList<Projectile> projectiles;





    private static Game instance;

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public static Ship getPlayerShip() {
        return playerShip;
    }

    public ArrayList<Projectile> getProjectiles() {

        return projectiles;
    }

    public void setProjectiles(ArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }

    public int setCurrentLevel() {

        //on startup
        if (currentLevel == null) {
            currentLevel = DAO.getInstance().getLevel(currentLevelID);
            currentAsteroids = currentLevel.getLevelAsteroids();
            currentLevelObjects = currentLevel.getLevelObjects();
            return 0;
        }

        //on: killing all asteroids
        else if (currentAsteroids.isEmpty()) {
            //transition here
            currentLevelID++;
            if (currentLevelID > DAO.getInstance().getLevels().size()) {
                return 1;
            }
            else {
                currentLevel = DAO.getInstance().getLevel(currentLevelID);
                currentAsteroids = currentLevel.getLevelAsteroids();
                currentLevelObjects = currentLevel.getLevelObjects();
                return 0;
            }
        }
        return -1;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public ArrayList<Asteroid> getCurrentAsteroids() {
        return currentAsteroids;
    }

    public Set<PositionedObject> getCurrentObjects() {
        return currentLevelObjects;
    }
}
