package edu.byu.cs.superasteroids.main_menu;

import java.util.Random;

import edu.byu.cs.superasteroids.base.IView;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.database.DAO;
import edu.byu.cs.superasteroids.models.Cannon;
import edu.byu.cs.superasteroids.models.Engine;
import edu.byu.cs.superasteroids.models.Game;
import edu.byu.cs.superasteroids.models.MainBody;
import edu.byu.cs.superasteroids.models.Part;
import edu.byu.cs.superasteroids.models.PowerCore;
import edu.byu.cs.superasteroids.ship_builder.ShipBuildingActivity;

import static edu.byu.cs.superasteroids.models.Game.*;

/**
 * Created by Bryce on 11/9/16.
 */

public class MainMenuController implements IMainMenuController {
    private MainActivity mainActivity;

    public MainMenuController(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    /**
     * The MainActivity calls this function when the "quick play" button is pressed.
     */
    public void onQuickPlayPressed() {
        Game.getInstance().getPlayerShip().buildRandomShip();

        mainActivity.startGame();
        //start game with all the info loaded into game
    }

    @Override
    public IView getView() {
        return null;
    }

    @Override
    public void setView(IView view) {

    }
}
