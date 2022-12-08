package ch.epfl.cs107.play.game.icrogue.area;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.actor.Player;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public abstract class ICRogueRoom extends Area {
    private ICRogueBehavior behavior;

    protected String behaviorName;
    protected DiscreteCoordinates roomCoordinates;

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */

    protected ICRogueRoom(String behaviorName, DiscreteCoordinates roomCoordinates){
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
    }
    protected abstract void createArea();

    /// EnigmeArea extends Area

    @Override
    public final float getCameraScaleFactor() {
        return Tuto2.CAMERA_SCALE_FACTOR;
    }


    public abstract DiscreteCoordinates getPlayerSpawnPosition();

    /// Demo2Area implements Playable

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        if (super.begin(window, fileSystem)) {
            // Set the behavior map
            behavior = new ICRogueBehavior(window, behaviorName);
            setBehavior(behavior);
            createArea();
            return true;
        }
        return false;
    }

}

