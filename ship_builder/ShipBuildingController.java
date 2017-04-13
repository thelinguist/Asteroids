package edu.byu.cs.superasteroids.ship_builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.byu.cs.superasteroids.base.ActionBarActivityView;
import edu.byu.cs.superasteroids.models.Game;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.database.DAO;
import edu.byu.cs.superasteroids.models.Cannon;
import edu.byu.cs.superasteroids.models.Engine;
import edu.byu.cs.superasteroids.models.MainBody;
import edu.byu.cs.superasteroids.models.Part;
import edu.byu.cs.superasteroids.models.PowerCore;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

import static edu.byu.cs.superasteroids.models.Game.*;
import static edu.byu.cs.superasteroids.ship_builder.IShipBuildingView.*;


/**
 *
 * Created by Bryce on 11/3/16.
 */


public class ShipBuildingController implements IShipBuildingController {

    private ShipBuildingActivity sba;
    private PartSelectionView state;

    public ShipBuildingController(ShipBuildingActivity sba) {
        this.sba = sba;
    }

    /**
     * a helper function to check the ship to see if it's ready
     * @return true if all parts are set
     */
    private boolean isComplete() {
        if ((getInstance().getPlayerShip().getMainBody() == null) ||
                (getInstance().getPlayerShip().getCannon() == null) ||
                (getInstance().getPlayerShip().getEngine() == null) ||
                (getInstance().getPlayerShip().getExtraPart() == null) ||
                (getInstance().getPlayerShip().getPowerCore() == null)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * The ship building view calls this function when a part selection view is loaded. This function
     * should be used to configure the part selection view. Example: Set the arrows for the view in
     * this function. (views are OPPOSITE?)
     * @param partView
     */
    public void onViewLoaded(PartSelectionView partView) {

        state = partView;
        switch (state) {
            case MAIN_BODY:
                // sba.setArrow(Direction, bool, string);
                sba.setArrow(PartSelectionView.MAIN_BODY, ViewDirection.LEFT, true, "Power Core");
                sba.setArrow(PartSelectionView.MAIN_BODY, ViewDirection.RIGHT, true, "Engine");
                sba.setArrow(PartSelectionView.MAIN_BODY, ViewDirection.DOWN, false, "");
                sba.setArrow(PartSelectionView.MAIN_BODY, ViewDirection.UP, false, "");
                break;
            case POWER_CORE:
                sba.setArrow(PartSelectionView.POWER_CORE, ViewDirection.LEFT, true, "Extra Part");
                sba.setArrow(PartSelectionView.POWER_CORE, ViewDirection.RIGHT, true, "Main Body");
                sba.setArrow(PartSelectionView.POWER_CORE, ViewDirection.DOWN, false, "");
                sba.setArrow(PartSelectionView.POWER_CORE, ViewDirection.UP, false, "");
                break;
            case EXTRA_PART:
                sba.setArrow(PartSelectionView.EXTRA_PART, ViewDirection.LEFT, true, "Cannon");
                sba.setArrow(PartSelectionView.EXTRA_PART, ViewDirection.RIGHT, true, "Power Core");
                sba.setArrow(PartSelectionView.EXTRA_PART, ViewDirection.DOWN, false, "");
                sba.setArrow(PartSelectionView.EXTRA_PART, ViewDirection.UP, false, "");
                break;
            case CANNON:
                sba.setArrow(PartSelectionView.CANNON, ViewDirection.LEFT, true, "Engine");
                sba.setArrow(PartSelectionView.CANNON, ViewDirection.RIGHT, true, "Extra Part");
                sba.setArrow(PartSelectionView.CANNON, ViewDirection.DOWN, false, "");
                sba.setArrow(PartSelectionView.CANNON, ViewDirection.UP, false, "");
                break;
            case ENGINE:
                sba.setArrow(PartSelectionView.ENGINE, ViewDirection.LEFT, true, "Main Body");
                sba.setArrow(PartSelectionView.ENGINE, ViewDirection.RIGHT, true, "Cannon");
                sba.setArrow(PartSelectionView.ENGINE, ViewDirection.DOWN, false, "");
                sba.setArrow(PartSelectionView.ENGINE, ViewDirection.UP, false, "");
                break;
        }
    }

    @Override
    public void update(double elapsedTime) {
        if (isComplete()) {
            sba.setStartGameButton(true);
        }
    }

    /**
     * The ShipBuildingView calls this function as it is created. Load ship building content in this
     * function. see p. 11
     *  Use this function to extract ship part data from your database. Then, use this data to load
     * ship part images. Once the images are loaded, pass the image IDs to the ShipBuildingActivity
     * using the setPartViewImageList() function.
     * @param content An instance of the content manager. This should be used to load images and sound.
     */
    public void loadContent(ContentManager content) {

        //list of integers for each object. make a list for each part

        List<Integer> bodies = new ArrayList<>();
        for (MainBody body : DAO.getInstance().getMainBodies()) {
            Integer index = content.loadImage(body.getImage());
            bodies.add(index); //returns and ID, and also saves image path. use the ID for view or something
        }
        sba.setPartViewImageList(PartSelectionView.MAIN_BODY, bodies);

        List<Integer> engines = new ArrayList<>();
        for (Engine engine : DAO.getInstance().getEngines()) {
            engines.add(content.loadImage(engine.getImage())); //returns and ID, and also saves image path. use the ID for view or something
        }
        sba.setPartViewImageList(PartSelectionView.ENGINE,engines);

        List<Integer> extraParts = new ArrayList<>();
        for (Part extraPart : DAO.getInstance().getExtraParts()) {
            extraParts.add(content.loadImage(extraPart.getImage())); //returns and ID, and also saves image path. use the ID for view or something
        }
        sba.setPartViewImageList(PartSelectionView.EXTRA_PART,extraParts);

        List<Integer> cannons = new ArrayList<>();
        for (Cannon cannon : DAO.getInstance().getCannons()) {
            cannons.add(content.loadImage(cannon.getImage())); //returns and ID, and also saves image path. use the ID for view or something
        }
        sba.setPartViewImageList(PartSelectionView.CANNON,cannons);

        //powerCores are not "Part"
        List<Integer> powerCores = new ArrayList<>();
        for (PowerCore powerCore : DAO.getInstance().getPowerCores()) {
            powerCores.add(content.loadImage(powerCore.getImage())); //returns and ID, and also saves image path. use the ID for view or something
        }
        sba.setPartViewImageList(PartSelectionView.POWER_CORE,powerCores);

    }

    @Override
    public void unloadContent(ContentManager content) {
        //ignore
    }

    @Override
    public void draw() {
        getInstance().getPlayerShip().draw((DrawingHelper.getGameViewWidth()/2), (DrawingHelper.getGameViewHeight()/2));
    }

    /**
     * The ShipBuildingView calls this function when the user makes a swipe/fling motion in the
     * screen. Respond to the user's swipe/fling motion in this function.
     * @param direction The direction of the swipe/fling. (swipe is NOT opposite of rotation??)
     */
    public void onSlideView(ViewDirection direction) {
        switch (state) {
            case MAIN_BODY:
                switch (direction) {
                    case RIGHT:
                        state = PartSelectionView.POWER_CORE;
                        sba.animateToView(state, ViewDirection.LEFT);
                        break;
                    case LEFT:
                        state = PartSelectionView.ENGINE;
                        sba.animateToView(state, ViewDirection.RIGHT);
                    default:
                        break;
                }
                break;
            case ENGINE:
                switch (direction) {
                    case RIGHT:
                        state = PartSelectionView.MAIN_BODY;
                        sba.animateToView(state, ViewDirection.LEFT);
                        break;
                    case LEFT:
                        state = PartSelectionView.CANNON;
                        sba.animateToView(state, ViewDirection.RIGHT);
                    default:
                        break;
                }
                break;
            case CANNON:
                switch (direction) {
                    case RIGHT:
                        state = PartSelectionView.ENGINE;
                        sba.animateToView(state, ViewDirection.LEFT);
                        break;
                    case LEFT:
                        state = PartSelectionView.EXTRA_PART;
                        sba.animateToView(state, ViewDirection.RIGHT);
                    default:
                        break;
                }
                break;
            case EXTRA_PART:
                switch (direction) {
                    case RIGHT:
                        state = PartSelectionView.CANNON;
                        sba.animateToView(state, ViewDirection.LEFT);
                        break;
                    case LEFT:
                        state = PartSelectionView.POWER_CORE;
                        sba.animateToView(state, ViewDirection.RIGHT);
                    default:
                        break;
                }
                break;
            case POWER_CORE:
                switch (direction) {
                    case RIGHT:
                        state = PartSelectionView.EXTRA_PART;
                        sba.animateToView(state, ViewDirection.LEFT);
                        break;
                    case LEFT:
                        state = PartSelectionView.MAIN_BODY;
                        sba.animateToView(state, ViewDirection.RIGHT);
                    default:
                        break;
                }
                break;
        }
    }

    /**
     * The part selection fragments call this function when a part is selected from the parts list. Respond
     * to the part selection in this function.
     * @param index The list index of the selected part.
     */
    public void onPartSelected(int index) {
        switch (state) {
            case MAIN_BODY:
                //get the main body from the SQL-index
                MainBody body = DAO.getInstance().getMainBodies().get(index);

                //the main body is set from what it found in the sql
                getInstance().getPlayerShip().setMainBody(body);

                //store an imageID based on what the Content Manager decides the index is (it gets the image from the object)
                getInstance().getPlayerShip().getMainBody().setIndex(ContentManager.getInstance().getImageId(body.getImage()));
                break;
            case POWER_CORE:
                PowerCore powerCore = DAO.getInstance().getPowerCores().get(index);
                getInstance().getPlayerShip().setPowerCore(powerCore);
                getInstance().getPlayerShip().getPowerCore().setIndex(ContentManager.getInstance().getImageId(powerCore.getImage()));
                break;
            case EXTRA_PART:
                Part extraPart = DAO.getInstance().getExtraParts().get(index);
                getInstance().getPlayerShip().setExtraPart(extraPart);
                getInstance().getPlayerShip().getExtraPart().setIndex(ContentManager.getInstance().getImageId(extraPart.getImage()));
                break;
            case CANNON:
                Cannon cannon = DAO.getInstance().getCannons().get(index);
                getInstance().getPlayerShip().setCannon(cannon);
                getInstance().getPlayerShip().getCannon().setIndex(ContentManager.getInstance().getImageId(cannon.getImage()));
                break;
            case ENGINE:
                Engine engine = DAO.getInstance().getEngines().get(index);
                getInstance().getPlayerShip().setEngine(engine);
                getInstance().getPlayerShip().getEngine().setIndex(ContentManager.getInstance().getImageId(engine.getImage()));
                break;
        }
    }

    /**
     * The ShipBuildingView calls this function is called when the start game button is pressed.
     */
    public void onStartGamePressed(){
        if (isComplete()) {
            sba.startGame();
        }
    }


    /**
     * The ShipBuildingView calls this function when ship building has resumed. Reset the Camera and
     * the ship position as needed when this is called.
     * --TAs say ignore this--
     */
    public void onResume(){ }

    @Override
    public IView getView() {
        return null;
    }

    @Override
    public void setView(IView view) {

    }
}
