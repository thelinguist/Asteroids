package edu.byu.cs.superasteroids.models;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Set;

import edu.byu.cs.superasteroids.core.GraphicsUtils;

/**
 * Created by Bryce on 10/28/16.
 */

public class Level {
    private Integer number;
    private String title;
    private String hint;
    private Integer width;
    private Integer height;
    private String musicFile;
    private Set<PositionedObject> objects;
    private ArrayList<Asteroid> levelAsteroids;
    private RectF bounds;


    public Level(Integer number, String title, String hint, Integer width, Integer height, String musicFile, Set<PositionedObject> objects, ArrayList<Asteroid> levelAsteroids) {
        this.number = number;
        this.title = title;
        this.hint = hint;
        this.width = width;
        this.height = height;
        this.musicFile = musicFile;
        this.objects = objects;
        this.levelAsteroids = levelAsteroids;
    }

    public Integer getNum() {
        return number;
    }
    public void setNum(Integer newNumber) {
        number = newNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public Integer getW() {
        return width;
    }

    public Integer getH() {
        return height;
    }

    public String getMusic() {
        return musicFile;
    }

    public ArrayList<Asteroid> getLevelAsteroids() {
        return levelAsteroids;
    }

    public Set<PositionedObject> getLevelObjects() {
        return objects;
    }

    public RectF getBoundaries() {
        int buffer = 10;
        if (bounds == null) {
        bounds = new RectF((ViewPort.getInstance().getTopLeft().x + buffer),
                (ViewPort.getInstance().getTopLeft().y + buffer),
                (ViewPort.getInstance().getTopLeft().x + width - buffer),
                (ViewPort.getInstance().getTopLeft().y + height - buffer));
    }
        bounds = new RectF((ViewPort.getInstance().getTopLeft().x + buffer),
                (ViewPort.getInstance().getTopLeft().y + buffer),
                (ViewPort.getInstance().getTopLeft().x + width - buffer),
                (ViewPort.getInstance().getTopLeft().y + height - buffer));
        return bounds;
    }
}