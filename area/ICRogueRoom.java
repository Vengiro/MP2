package ch.epfl.cs107.play.game.icrogue.area;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogue;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area implements Logic {
    private ICRogueBehavior behavior;

    protected String behaviorName;
    protected DiscreteCoordinates roomCoordinates;

    private List<Connector> setOfConnector = new ArrayList<>();

    /**
     * Create the area by adding it all actors
     * called by begin method
     * Note it set the Behavior as needed !
     */

    protected ICRogueRoom(String behaviorName , DiscreteCoordinates roomCoordinates){
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
    }
    protected ICRogueRoom(List<DiscreteCoordinates > connectorsCoordinates , List<Orientation > orientations ,
                          String behaviorName , DiscreteCoordinates roomCoordinates){
        this.behaviorName = behaviorName;
        this.roomCoordinates = roomCoordinates;
        for(int i=0; i<connectorsCoordinates.size(); ++i){
            setOfConnector.add(new Connector(this , orientations.get(i).opposite(), connectorsCoordinates.get(i)));
        }
        assignPlayerSpawnPosition();
    }
    protected void createArea(){
        for (Connector connector : setOfConnector) {
            this.registerActor(connector);
        }
    }


    /// EnigmeArea extends Area

    /**
     * Method that returns CAMERA_SCALE_FACTOR, whose role is to position the camera so that the player can view the
     * game.
     * @return float
     */
    @Override
    public final float getCameraScaleFactor() {
        return ICRogue.CAMERA_SCALE_FACTOR;
    }
    // set the player spawn position in another room
    // Since connectors in setOfConnector are in the same order as the one in the enum it assings the right destination

    private void assignPlayerSpawnPosition(){
        for (int i=0; i<setOfConnector.size(); ++i){
            setOfConnector.get(i).setCoordinatesOfSpawn(Level0Room.Level0Connectors.values()[i].getDestination());
        }
    }
    public abstract DiscreteCoordinates getPlayerSpawnPosition();
    protected List<Connector> getSetOfConnectors(){return setOfConnector;}


    /// Demo2Area implements Playable

    /**
     * Method that begins the game by adding a window and a file system
     * @param window (Window): display context. Not null
     * @param fileSystem (FileSystem): given file system. Not null
     * @return boolean
     */
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

    private int counter = 0;

    /**
     * Method that evolves by moving.
     * @param deltatime elapsed time since last update, in seconds, non-negative
     */
    public void update(float deltatime){
        super.update(deltatime);
        final Keyboard keyboard = this.getKeyboard();
        open(keyboard.get(Keyboard.O));
        if(counter == 0){ close(keyboard.get(Keyboard.T));}
        if (counter > 0){switchState(keyboard.get(Keyboard.T));}
        lock(keyboard.get(Keyboard.L), Level0Room.Level0Connectors.W);
        if (keyboard.get(Keyboard.T).isReleased()){counter = 1;}
        isResolved();
    }
    public void open(Button b){
        if (b.isDown()){
            for (Connector connectors : setOfConnector){
                connectors.openState();
            }
        }
    }
    public void close(Button b){
        if (b.isDown()){
            for (Connector connectors : setOfConnector){
             connectors.closeState();
            }
        }
    }
    public void lock(Button b, Level0Room.Level0Connectors level0Connectors){
        if (b.isDown()){
            setOfConnector.get(level0Connectors.getIndex()).lockState();
        }
    }

    public void switchState(Button b){
        if (b.isDown() & !(b.wasDown())){
            for (Connector connectors : setOfConnector){
                connectors.switchState();
            }
        }
    }

    // If the player is in it returns directly true
    @Override
    public boolean isOn() {
        return true;
    }

    public void isResolved(){
        if(isOn()){
           for(Connector connector : setOfConnector){
               if(connector.getState() == Connector.State.CLOSED){
                   connector.openState();
               }
           }
        }
    }

}

