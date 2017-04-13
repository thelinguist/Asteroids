package edu.byu.cs.superasteroids.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by Bryce on 10/28/16.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "asteroids.sqlite";
    public static int DB_VERSION = 1;



    private DAO dao = DAO.getInstance();

    public DBOpenHelper(Context context)  {super(context, DB_NAME, null, DB_VERSION); }

    public static String createObjects = "CREATE TABLE objects (" +
            "  image text NOT NULL," +
            "  objectId integer NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ")";

    public static String createAsteroids = "CREATE TABLE asteroids (" +
            "  name text NOT NULL," +
            "  image text NOT NULL," +
            "  imageWidth integer NOT NULL," +
            "  imageHeight integer NOT NULL," +
            "  type text NOT NULL," +
            "  asteroidId integer NOT NULL PRIMARY KEY AUTOINCREMENT" +
            ")";

    public static String createLevels = "CREATE TABLE levels (" +
            "  number integer NOT NULL PRIMARY KEY," +
            "  title text NOT NULL," +
            "  hint text NOT NULL," +
            "  width integer NOT NULL," +
            "  height integer NOT NULL," +
            "  music text NOT NULL" +
            ")";

    public static String createLevelAsteroids = "CREATE TABLE levelAsteroids (" +
            "  number integer NOT NULL," +
            "  asteroidId integer NOT NULL," +
            "  levelId integer NOT NULL," +
            "  FOREIGN KEY(asteroidId) REFERENCES asteroids(asteroidId)," +
            "  FOREIGN KEY(levelId) REFERENCES levels(number)" +
            ")";

    public static String createLevelObjects = "CREATE TABLE levelObjects (" +
            "  position string NOT NULL," +
            "  objectId integer NOT NULL," +
            "  scale integer NOT NULL," +
            "  levelId integer NOT NULL," +
            "  FOREIGN KEY(objectId) REFERENCES objects(objectId)," +
            "  FOREIGN KEY(levelId) REFERENCES levels(number)" +
            ")";

    public static String createBackgroundObjectImages = "CREATE TABLE backgroundObjectImages (" +
            "  id integer NOT NULL," +
            "  image string NOT NULL," +
            "  FOREIGN KEY(id) REFERENCES levelObjects(objectId)" +
            ")";

    public static String createMainBodies = "CREATE TABLE mainBodies (" +
            "  cannonAttach  text NOT NULL," +
            "  engineAttach  text NOT NULL," +
            "  extraAttach  text NOT NULL," +
            "  image text NOT NULL," +
            "  imageWidth integer NOT NULL," +
            "  imageHeight integer NOT NULL" +
            ")";

    public static String createCannons = "CREATE TABLE cannons (" +
            "  attachPoint  text NOT NULL," +
            "  emitPoint  text NOT NULL," +
            "  image text NOT NULL," +
            "  imageWidth integer NOT NULL," +
            "  imageHeight integer NOT NULL," +
            "  attackImage text NOT NULL," +
            "  attackImageWidth integer NOT NULL," +
            "  attackImageHeight integer NOT NULL," +
            "  attackSound text NOT NULL," +
            "  damage integer NOT NULL" +
            ")";

    public static String createExtraParts = "CREATE TABLE extraParts (" +
            "  attachPoint  text NOT NULL," +
            "  image text NOT NULL," +
            "  imageWidth integer NOT NULL," +
            "  imageHeight integer NOT NULL" +
            ")";

    public static String createEngines = "CREATE TABLE engines (" +
            "  baseSpeed integer NOT NULL," +
            "  baseTurnRate integer NOT NULL," +
            "  attachPoint  text NOT NULL," +
            "  image text NOT NULL," +
            "  imageWidth integer NOT NULL," +
            "  imageHeight integer NOT NULL" +
            ")";

    public static String createPowerCores = "CREATE TABLE powerCores (" +
            "  cannonBoost integer NOT NULL," +
            "  engineBoost integer NOT NULL," +
            "  image text NOT NULL" +
            ")";

    public static void delete(SQLiteDatabase db) {
        String[] tables = {"objects", "asteroids", "levels", "levelAsteroids", "levelObjects",
                "backgroundObjectImages", "mainBodies", "cannons", "extraParts", "engines", "powerCores"};
        for (String item : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + item + ";");
        }
        db.execSQL(createObjects);
        db.execSQL(createAsteroids);
        db.execSQL(createLevels);
        db.execSQL(createLevelObjects);
        db.execSQL(createBackgroundObjectImages);
        db.execSQL(createLevelAsteroids);
        db.execSQL(createMainBodies);
        db.execSQL(createCannons);
        db.execSQL(createExtraParts);
        db.execSQL(createEngines);
        db.execSQL(createPowerCores);
    }

        @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createObjects);
        db.execSQL(createAsteroids);
        db.execSQL(createLevels);
        db.execSQL(createLevelObjects);
        db.execSQL(createBackgroundObjectImages);
        db.execSQL(createLevelAsteroids);
        db.execSQL(createMainBodies);
        db.execSQL(createCannons);
        db.execSQL(createExtraParts);
        db.execSQL(createEngines);
        db.execSQL(createPowerCores);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        return;
    }
}
