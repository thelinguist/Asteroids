package edu.byu.cs.superasteroids.importer;

import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;

import edu.byu.cs.superasteroids.database.DAO;
import edu.byu.cs.superasteroids.database.DBOpenHelper;

/**
 * Created by Bryce on 10/26/16.
 */

/**
 * put the json stuff in the SQL, then build classes from that
 */
public class GameDataImporter implements IGameDataImporter {
    public static final GameDataImporter SINGLETON = new GameDataImporter();
    JSONObject myJSON;

    //can I grab this function from DAO?
    private PointF toPointF(String value) {
        String[] out = value.split(",");
        return new PointF(Float.parseFloat(out[0]), Float.parseFloat(out[1]));
    }

    private static String makeString(InputStreamReader reader) throws IOException {

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[512];

        int n = 0;
        while ((n = reader.read(buf)) > 0) {
            sb.append(buf, 0, n);
        }

        return sb.toString();
    }

    /**
     * read the JSON file, parse it, and load via the DAO
     * @param dataInputReader The InputStreamReader connected to the .json file needing to be imported.
     * @return true if it worked
     */
    public boolean importData(InputStreamReader dataInputReader) throws IOException {

        try {
            JSONObject rootObject = new JSONObject(makeString(dataInputReader));
            myJSON = rootObject.getJSONObject("asteroidsGame");
            //logd("JSON", "inst working")


            DBOpenHelper.delete(DAO.getInstance().getDb());

            parseJSON();
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * parse JSON, and load via DAO
     * @throws JSONException
     */
    public void parseJSON() throws JSONException {
        putObjects(myJSON.getJSONArray("objects"));
        putAsteroids(myJSON.getJSONArray("asteroids"));
        putLevels(myJSON.getJSONArray("levels"));
        putMainBodies(myJSON.getJSONArray("mainBodies"));
        putCannons(myJSON.getJSONArray("cannons"));
        putExtraParts(myJSON.getJSONArray("extraParts"));
        putEngines(myJSON.getJSONArray("engines"));
        putPowerCores(myJSON.getJSONArray("powerCores"));
    }


    /**
     * get a list of objects from the JSON and put into DAO's SQL
     * @param objects
     * @throws JSONException
     */
    private void putObjects(JSONArray objects) throws JSONException {
        for (int i = 0; i < objects.length(); i++) {
            String image = objects.getString(i);
            DAO.getInstance().putObject(image);
        }
    }


    /**
     * read the JSON pertaining to the asteroids array, and put them in the DAO's SQL
     * @param asteroids
     * @throws JSONException
     */
    private void putAsteroids(JSONArray asteroids) throws JSONException {
        for (int i = 0; i < asteroids.length(); i++) {
            JSONObject asteroid = asteroids.getJSONObject(i);
            String name = asteroid.getString("name");
            String image = asteroid.getString("image");
            Integer iW = asteroid.getInt("imageWidth");
            Integer iH = asteroid.getInt("imageHeight");
            String type = asteroid.getString("type");
            DAO.getInstance().putAsteroid(name, image, iW, iH, type);
        }
    }

    /**
     * read the JSON pertaining to levels, and add them to the DAO's SQL
     * @param levels
     * @throws JSONException
     */
    private void putLevels(JSONArray levels) throws JSONException {
        for (int i = 0; i < levels.length(); i++) {
            JSONObject level = levels.getJSONObject(i);
            Integer number = level.getInt("number");
            String title = level.getString("title");
            String hint = level.getString("hint");
            Integer width = level.getInt("width");
            Integer height = level.getInt("height");
            String music = level.getString("music");
            putLevelObjects(level.getJSONArray("levelObjects"), number);
            putLevelAsteroids(level.getJSONArray("levelAsteroids"), number);
            DAO.getInstance().putLevel(number, title, hint, width, height, music);
        }
    }

    /**
     * read the JSON pertaining to mainBodies, and put them in the DAO's SQL
     * @param mainbodies
     * @throws JSONException
     */
    private void putMainBodies(JSONArray mainbodies) throws JSONException {
        for (int i = 0; i < mainbodies.length(); i++) {
            JSONObject mainbody = mainbodies.getJSONObject(i);
            String cannonAttach = mainbody.getString("cannonAttach");
            String engineAttach = mainbody.getString("engineAttach");
            String extraAttach = mainbody.getString("extraAttach");
            String image = mainbody.getString("image");
            Integer iW = mainbody.getInt("imageWidth");
            Integer iH = mainbody.getInt("imageHeight");
            DAO.getInstance().putMBody(cannonAttach, engineAttach, extraAttach, image, iW, iH);
        }
    }

    /**
     * read the JSON pertaining to cannons, and put them in the DAO's SQL
     * @param cannons
     * @throws JSONException
     */
    private void putCannons(JSONArray cannons) throws JSONException {
        for (int i = 0; i < cannons.length(); i++) {
            JSONObject cannon = cannons.getJSONObject(i);
            String attachPoint = cannon.getString("attachPoint");
            String image = cannon.getString("image");
            Integer iW = cannon.getInt("imageWidth");
            Integer iH = cannon.getInt("imageHeight");
            String emitPoint = cannon.getString("emitPoint");
            String attackImage = cannon.getString("attackImage");
            Integer attackIW = cannon.getInt("attackImageWidth");
            Integer attackIH = cannon.getInt("attackImageHeight");
            String attackSound = cannon.getString("attackSound");
            Integer damage = cannon.getInt("damage");
            DAO.getInstance().putCannon(attachPoint, image, iW, iH, emitPoint, attackImage, attackIW, attackIH, attackSound, damage);
        }
    }

    /**
     * read the JSON pertaining to extra parts, and put them in the DAO's SQL
     * @param extraParts
     * @throws JSONException
     */
    private void putExtraParts(JSONArray extraParts) throws JSONException {
        for (int i = 0; i < extraParts.length(); i++) {
            JSONObject extraPart = extraParts.getJSONObject(i);
            String attachPoint = extraPart.getString("attachPoint");
            String image = extraPart.getString("image");
            Integer iW = extraPart.getInt("imageWidth");
            Integer iH = extraPart.getInt("imageHeight");
            DAO.getInstance().putExtraPart(attachPoint, image, iW, iH);
        }
    }

    /**
     * read the JSON pertaining to engines, and put them in the DAO's SQL
     * @param engines
     * @throws JSONException
     */
    private void putEngines(JSONArray engines) throws JSONException {
        for (int i = 0; i < engines.length(); i++) {
            JSONObject engine = engines.getJSONObject(i);
            Integer baseSpeed = engine.getInt("baseSpeed");
            Integer baseTurnRate = engine.getInt("baseTurnRate");
            String attachPoint = engine.getString("attachPoint");
            String image = engine.getString("image");
            Integer iW = engine.getInt("imageWidth");
            Integer iH = engine.getInt("imageHeight");
            DAO.getInstance().putEngine(attachPoint, image, iW, iH, baseSpeed, baseTurnRate);
        }
    }

    /**
     * read the JSON pertaining to powerCores, and put them in the DAO's SQL
     * @param powerCores
     * @throws JSONException
     */
    private void putPowerCores(JSONArray powerCores) throws JSONException {
        for (int i = 0; i < powerCores.length(); i++) {
            JSONObject powerCore = powerCores.getJSONObject(i);
            Integer cannonBoost = powerCore.getInt("cannonBoost");
            Integer engineBoost = powerCore.getInt("engineBoost");
            String image = powerCore.getString("image");
            DAO.getInstance().putPowerCore(image, cannonBoost, engineBoost);
        }
    }

    /**
     * Read the JSON pertaining to levelObjects, and put them in the DAO's SQL
     * @param objects
     * @throws JSONException
     */
    private void putLevelObjects(JSONArray objects, Integer levelId) throws JSONException {
        for (int i = 0; i < objects.length(); i++) {
            JSONObject object = objects.getJSONObject(i);
            String position = object.getString("position");
            Integer objectId = object.getInt("objectId");
            Integer scale = object.getInt("scale");

            DAO.getInstance().putLevelObject(position, objectId, scale, levelId);
        }
    }

    /**
     * Read the JSON pertaining to LevelAsteroids, and put in the DAO's SQL
     * @param asteroids
     * @param number
     * @throws JSONException
     */
    private void putLevelAsteroids(JSONArray asteroids, Integer number) throws JSONException {
        for (int i = 0; i < asteroids.length(); i++) {
            JSONObject asteroid = asteroids.getJSONObject(i);
            Integer asteroidNumber = asteroid.getInt("number");
            Integer asteroidId = asteroid.getInt("asteroidId");
            DAO.getInstance().putLevelAsteroid(asteroidNumber, asteroidId, number);
        }
    }

}
