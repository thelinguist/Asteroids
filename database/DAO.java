package edu.byu.cs.superasteroids.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import edu.byu.cs.superasteroids.models.Asteroid;
import edu.byu.cs.superasteroids.models.Cannon;
import edu.byu.cs.superasteroids.models.Engine;
import edu.byu.cs.superasteroids.models.Level;
import edu.byu.cs.superasteroids.models.MainBody;
import edu.byu.cs.superasteroids.models.Part;
import edu.byu.cs.superasteroids.models.PositionedObject;
import edu.byu.cs.superasteroids.models.PowerCore;

/**
 * Created by Bryce on 10/21/16.
 */

public class DAO {


    private SQLiteDatabase db;
    private static DAO instance;

    private DAO() {
        db = null;
    }

    public static DAO getInstance() {
        if (instance == null) {
            instance = new DAO();
        }
        return instance;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    /**
     * a quickie, convert string to PointF
     * @param value to be converted
     * @return a pointF
     */
    private PointF toPointF(String value) {
        String[] out = value.split(",");
        return new PointF(Float.parseFloat(out[0]), Float.parseFloat(out[1]));
    }


    //*******************POSITIONED-OBJECTS*****************************

    /**
     * add an object
     * @param image the file location of the image
     */
    public void putObject(String image) {
        ContentValues values = new ContentValues();
        values.put("image", image);

        //add object to db
        db.insert("objects", null, values);
    }

    /**
     * give me an image based on the objectID
     * @param objectId a key in the SQL, to find the image
     * @return the location of the image
     */
    private String getObjectImage(Integer objectId) {
        Cursor cursor = db.rawQuery("select * from objects where objectId = " + objectId, new String[]{});
        try {
            cursor.moveToFirst();
            return cursor.getString(0);
        } finally {
            cursor.close();
        }
    }

    /**
     * This gets the level objects based on the level number, as a set
     * @param levelId the level number
     * @return all the objects associated with that level
     */
    public Set<PositionedObject> getLevelObjects(Integer levelId) {
        Set<PositionedObject> result = new HashSet<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from levelObjects where levelId = " + levelId, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;
                PointF position = toPointF(cursor.getString(cursorPos++));
                Integer objectId = cursor.getInt(cursorPos++);
                Integer scale = cursor.getInt(cursorPos++);
                PositionedObject object = new PositionedObject(getObjectImage(objectId), position, scale, objectId, levelId);
                result.add(object);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    /**
     * add a single level object to the SQL database
     * @param position as a string
     * @param objectId
     * @param scale
     * @return
     */
    public boolean putLevelObject(String position, Integer objectId, Integer scale, Integer levelId) {
        ContentValues values = new ContentValues();
        values.put("position", position);
        values.put("objectId", objectId);
        values.put("scale", scale);
        values.put("levelId", levelId);


        //add object to db
        db.insert("levelObjects", null, values);
        return true;
    }


    //**********************ASTEROIDS*********************************

    /**
     * get data (from JSON, etc) and add to SQL
     *
     * @param name        asteroid Name
     * @param image       asteroid Image
     * @param imageWidth  image width
     * @param imageHeight image height
     * @param type        type of asteroid
     * @return true if it worked
     */
    public boolean putAsteroid(String name, String image, Integer imageWidth, Integer imageHeight, String type) {

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("imageWidth", imageWidth);
        values.put("imageHeight", imageHeight);
        values.put("type", type);

        //add asteroid to db
        db.insert("asteroids", null, values);
        return true;
    }

    /**
     * This gets all the asteroids from the SQL database.
     *
     * @return Set of asteroids
     */
    public ArrayList<Asteroid> getAsteroids() {
        ArrayList<Asteroid> result = new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from asteroids", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer cursorPos = 0;
                int id = cursor.getInt(cursorPos++);
                String name = cursor.getString(cursorPos++);
                String image = cursor.getString(cursorPos++);
                Integer imageWidth = Integer.parseInt(cursor.getString(cursorPos++));
                Integer imageHeight = Integer.parseInt(cursor.getString(cursorPos++));
                String type = cursor.getString(cursorPos++);

                Asteroid asteroid = new Asteroid(id, name, image, imageWidth, imageHeight, type);
                result.add(asteroid);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    /**
     * get one asteroid from SQL database with asteroidID
     * @param asteroidId
     * @return
     */
    public Asteroid getAsteroid(Integer asteroidId) {
        Cursor cursor = db.rawQuery("select * from asteroids where asteroidId = " + asteroidId, new String[]{});
        try {
            Integer cursorPos = 0;
            cursor.moveToFirst();
            //cursorPos++; //id
            String name = cursor.getString(cursorPos++);
            String image = cursor.getString(cursorPos++);
            Integer imageWidth = cursor.getInt(cursorPos++);
            Integer imageHeight = cursor.getInt(cursorPos++);
            String type = cursor.getString(cursorPos++);

            return new Asteroid(asteroidId, name, image, imageWidth, imageHeight, type);
        } finally {
            cursor.close();
        }
    }

    /**
     * change the asteroid values in the SQL database
     * @param asteroid to be changed
     * @return true if it worked
     */
    public boolean updateAsteroid(Asteroid asteroid) {
        ContentValues values = new ContentValues();
        values.put("ID", asteroid.getID());
        values.put("name", asteroid.getName());
        values.put("image", asteroid.getImage());
        values.put("imageHeight", asteroid.getIHeight());
        values.put("imageWidth", asteroid.getIWidth());
        values.put("type", asteroid.getType());

        int rows = db.update("Asteroid", values, "id = " + asteroid.getID(), null);

        return (rows == 1);
    }

    /**
     * remove an asteroid from play
     * @param asteroid to be removed
     * @return true if it worked
     */
    public boolean delAsteroid(Asteroid asteroid) {
        int rows = db.delete("Asteroid", "id = " + asteroid.getID(), null);
        if (rows == 1) {
            asteroid.setID(-1);
            return true;
        } else {
            return false;
        }
    }
    

    //*******************LEVEL-ASTEROIDS************************

    /**
     * load single Asteroid into SQL Table: levelAsteroids
     * @param asteroidNumber
     * @param asteroidId
     * @param levelNumber
     */
    public void putLevelAsteroid(Integer asteroidNumber, Integer asteroidId, Integer levelNumber) {
        ContentValues values = new ContentValues();
        values.put("number", asteroidNumber);
        values.put("asteroidId", asteroidId);
        values.put("levelId", levelNumber);

        //add asteroid to db
        db.insert("levelAsteroids", null, values);
    }

    /**
     * get all asteroids (for the get Level function) by finding the right asteroid in the database and making the right amount of copies
     * @param levelId
     * @return the set of asteroids
     */
    private ArrayList<Asteroid> getLevelAsteroids(Integer levelId) {
        ArrayList<Asteroid> result = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from levelAsteroids where levelId = " + levelId, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Integer cursorPos = 0;
                Integer number = cursor.getInt(cursorPos++);
                Integer asteroidId = cursor.getInt(cursorPos++);
                //Asteroid model = getAsteroid(asteroidId);
                for (int i = 0; i < number; i++) {
                    result.add(getAsteroid(asteroidId));
                }
                cursor.moveToNext();
            }
        } finally {
        cursor.close();
    }
    return result;

    }


    //**********************LEVELS*********************************

    /**
     * put a level into the SQL database (like from a JSON)
     * @param number level number
     * @param title level title
     * @param hint level hint
     * @param width level-arena width
     * @param height level-arena height
     * @param musicFile location of music file
     * @return true if it worked
     */
    public boolean putLevel(Integer number, String title, String hint, Integer width, Integer height, String musicFile) {

        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("title", title);
        values.put("hint", hint);
        values.put("width", width);
        values.put("height", height);
        values.put("music", musicFile);

        //add asteroid to db
        db.insert("levels", null, values);
        return true;
    }

    /**
     * This gets all the levels from the SQL database, including the level objects and asteroids
     *
     * @return an Array of levels
     */
    public ArrayList<Level> getLevels() {
        ArrayList<Level> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from levels", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;
                Integer number = cursor.getInt(cursorPos++);
                String title = cursor.getString(cursorPos++);
                String hint = cursor.getString(cursorPos++);
                Integer width = Integer.parseInt(cursor.getString(cursorPos++));
                Integer height = Integer.parseInt(cursor.getString(cursorPos++));
                String musicFile = cursor.getString(cursorPos++);
                Set<PositionedObject> objects = getLevelObjects(number);
                ArrayList<Asteroid> asteroids = getLevelAsteroids(number);

                Level level = new Level(number, title, hint, width, height, musicFile, objects, asteroids);
                result.add(level);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }

    /**
     * Gimme a level from the SQL database based on the level number
     * @param index this is the level number
     * @return the (correct) level object
     */
    public Level getLevel(int index) {
        Level level = null;
        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("SELECT * FROM levels WHERE number = " + index, new String[]{});
        try {
            cursor.moveToFirst();
            //int count = cursor.getCount();
            //int ColumnCount = cursor.getColumnCount();

            int cursorPos = 0;
            cursorPos++; //number
            String title = cursor.getString(cursorPos++);
            String hint = cursor.getString(cursorPos++);
            Integer width = Integer.parseInt(cursor.getString(cursorPos++));
            Integer height = Integer.parseInt(cursor.getString(cursorPos++));
            String musicFile = cursor.getString(cursorPos++);
            Set<PositionedObject> objects = getLevelObjects(index);
            ArrayList<Asteroid> asteroids = getLevelAsteroids(index);

            level = new Level(index, title, hint, width, height, musicFile, objects, asteroids);
        } finally {
            cursor.close();
        }
        return level;
    }

    // ----These functions aren't used (but this is how you do it)----
    /**
     * change the level values in the SQL database
     * @param level to be changed
     * @return true if it worked
     */
    public boolean updateLevel(Level level) {
        ContentValues values = new ContentValues();
        values.put("number", level.getNum());
        values.put("title", level.getTitle());
        values.put("hint", level.getHint());
        values.put("width", level.getW());
        values.put("height", level.getH());
        values.put("music", level.getMusic());
        //grab values in objects and asteroids that match level


        int rows = db.update("Asteroid", values, "id = " + level.getNum(), null);

        return (rows == 1);
    }

    /**
     * remove an level from db
     * @param level to be removed
     * @return true if it worked
     */
    public boolean delLevel(Level level) {
        int rows = db.delete("Level", "id = " + level.getNum(), null);
        if (rows == 1) {
            level.setNum(-1);
            return true;
        } else {
            return false;
        }
    }


    //**********************MAIN-BODIES******************************

    /**
     * add a Main Body to the database (such as from a JSON)
     * @param cannonAttach where the cannon attaches
     * @param engineAttach where the engine attaches
     * @param extraAttach where the extra part attaches
     * @param image where the image is located (directory)
     * @param imageWidth image width
     * @param imageHeight image height
     * @return true if it worked
     */
    public boolean putMBody(String cannonAttach, String engineAttach, String extraAttach, String image, Integer imageWidth, Integer imageHeight) {
        ContentValues values = new ContentValues();
        values.put("cannonAttach", cannonAttach);
        values.put("engineAttach", engineAttach);
        values.put("extraAttach", extraAttach);
        values.put("image", image);
        values.put("imageWidth", imageWidth);
        values.put("imageHeight", imageHeight);


        //add asteroid to db
        db.insert("mainBodies", null, values);
        return true;
    }

    /**
     * This gets all the mainBodies from the SQL database.
     *
     * @return Set of mainBodies
     */
    public ArrayList<MainBody> getMainBodies() {
        ArrayList<MainBody> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from mainBodies", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;
                PointF cannonAttach = toPointF(cursor.getString(cursorPos++));
                PointF engineAttach = toPointF(cursor.getString(cursorPos++));
                PointF extraAttach = toPointF(cursor.getString(cursorPos++));
                String image = cursor.getString(cursorPos++);
                Integer width = Integer.parseInt(cursor.getString(cursorPos++));
                Integer height = Integer.parseInt(cursor.getString(cursorPos++));
                MainBody mainBody = new MainBody(image, height, width, extraAttach, cannonAttach, engineAttach);
                result.add(mainBody);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }


    //**********************CANNONS*********************************

    /**
     * get data (from JSON, etc) and add to SQL
     * @param attachPoint for cannon
     * @param image of cannon
     * @param iW image width
     * @param iH image height
     * @param emitPoint where projectile comes out
     * @param attackImage image of projectile
     * @param attackIW width of attackImage
     * @param attackIH height of attackImage
     * @param attackSound sound of attackImage
     */
    public void putCannon(String attachPoint, String image, Integer iW, Integer iH, String emitPoint, String attackImage, Integer attackIW, Integer attackIH, String attackSound, Integer damage) {
        ContentValues values = new ContentValues();
        values.put("attachPoint", attachPoint);
        values.put("image", image);
        values.put("imageWidth", iW);
        values.put("imageHeight", iH);
        values.put("emitPoint", emitPoint);
        values.put("attackImage", attackImage);
        values.put("attackImageWidth", attackIW);
        values.put("attackImageHeight", attackIH);
        values.put("attackSound", attackSound);
        values.put("damage", damage);

        //add cannons to db
        db.insert("cannons", null, values);
    }

    /**
     * gimme the cannon objects from the SQL database
     * @return all the cannons
     */
    public ArrayList<Cannon> getCannons() {
        ArrayList<Cannon> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from cannons", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;


                PointF attachPoint = toPointF(cursor.getString(cursorPos++));
                PointF emitPoint = toPointF(cursor.getString(cursorPos++));
                String image = cursor.getString(cursorPos++);
                Integer iW = cursor.getInt(cursorPos++);
                Integer iH = cursor.getInt(cursorPos++);
                String attackImage = cursor.getString(cursorPos++);
                Integer attackiW = cursor.getInt(cursorPos++);
                Integer attackiH = cursor.getInt(cursorPos++);
                String attackSound = cursor.getString(cursorPos++);
                Integer damage = cursor.getInt(cursorPos++);

                result.add(new Cannon(image, iW, iH, attachPoint, emitPoint, attackImage, attackiW, attackiH, attackSound, damage));

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }


    //**********************EXTRA-PARTS****************************

    /**
     * put information into the SQL
     * @param attachPoint of extra part
     * @param image of extra part
     * @param iW width of image
     * @param iH height of image
     */
    public void putExtraPart(String attachPoint, String image, Integer iW, Integer iH) {
        ContentValues values = new ContentValues();
        values.put("attachPoint", attachPoint);
        values.put("image", image);
        values.put("imageWidth", iW);
        values.put("imageHeight", iH);

        //add extraParts to db
        db.insert("extraParts", null, values);
    }

    /**
     * gimme all the extraParts
     * @return all the extraParts from the SQL database
     */
    public ArrayList<Part> getExtraParts() {
        ArrayList<Part> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from extraParts", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;

                PointF attachPoint = toPointF(cursor.getString(cursorPos++));
                String image = cursor.getString(cursorPos++);
                Integer iW = cursor.getInt(cursorPos++);
                Integer iH = cursor.getInt(cursorPos++);

                result.add(new Part(image, iW, iH, attachPoint));

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }


    //**************************ENGINES***************************

    /**
     * take info and put it into the SQL
     * @param attachPoint
     * @param image
     * @param iW
     * @param iH
     * @param baseSpeed
     * @param baseTurnRate
     */
    public void putEngine(String attachPoint, String image, Integer iW, Integer iH, Integer baseSpeed, Integer baseTurnRate) {
        ContentValues values = new ContentValues();
        values.put("attachPoint", attachPoint);
        values.put("image", image);
        values.put("imageWidth", iW);
        values.put("imageHeight", iH);
        values.put("baseSpeed", baseSpeed);
        values.put("baseTurnRate", baseTurnRate);

        //add engine to db
        db.insert("engines", null, values);
    }

    /**
     * gimme all the Engines in the SQL database
     * @return all the engines
     */
    public ArrayList<Engine> getEngines() {
        ArrayList<Engine> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from engines", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;
                Integer baseSpeed = cursor.getInt(cursorPos++);
                Integer baseTurnRate = cursor.getInt(cursorPos++);
                PointF attachPoint = toPointF(cursor.getString(cursorPos++));
                String image = cursor.getString(cursorPos++);
                Integer iW = cursor.getInt(cursorPos++);
                Integer iH = cursor.getInt(cursorPos++);

                result.add(new Engine(image, iW, iH, attachPoint, baseSpeed, baseTurnRate));

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }


    //**********************POWER-CORES*****************************

    /**
     * add a powercore to SQL database
     * @param image as a string
     * @param cannonBoost
     * @param engineBoost
     */
    public void putPowerCore(String image, Integer cannonBoost, Integer engineBoost) {
        ContentValues values = new ContentValues();
        values.put("image", image);
        values.put("cannonBoost", cannonBoost);
        values.put("engineBoost", engineBoost);

        //add powerCore to db
        db.insert("powerCores", null, values);
    }

    /**
     * get a list of Power Cores from the SQL database
     * @return Hashset of PowerCores
     */
    public ArrayList<PowerCore> getPowerCores() {
        ArrayList<PowerCore> result = new ArrayList<>();

        //The rawQuery command assumes that normally the sql statement will have '?' in it.
        //It thus expect an array of values to fill the '?'.  However, if the sql statement
        //does not have any '?' in it.  You must still pass an empty array of strings.
        Cursor cursor = db.rawQuery("select * from powerCores", new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int cursorPos = 0;
                Integer cannonBoost = cursor.getInt(cursorPos++);
                Integer engineBoost = cursor.getInt(cursorPos++);
                String image = cursor.getString(cursorPos++);

                result.add(new PowerCore(image, cannonBoost, engineBoost));

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return result;
    }

}
