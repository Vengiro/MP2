package ch.epfl.cs107.play.game.icrogue.area;


import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.Connector;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.game.tutosSolution.actor.GhostPlayer;
import ch.epfl.cs107.play.game.tutosSolution.actor.Player;
import ch.epfl.cs107.play.game.tutosSolution.area.Tuto2Area;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Ferme;
import ch.epfl.cs107.play.game.tutosSolution.area.tuto2.Village;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ICRogueRoom extends Area {
    private ICRogueBehavior behavior;

    protected String behaviorName;
    protected DiscreteCoordinates roomCoordinates;

    protected ArrayList<Connector> setOfConnector = new ArrayList<>();

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

    }
    protected  void createArea(){
        for (Connector connector : setOfConnector) {
            this.registerActor(connector);
        }
    }


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

    public void update(float deltatime){
        super.update(deltatime);
        final Keyboard keyboard = this.getKeyboard();
        changeState(keyboard.get(Keyboard.O), Connector.State.OPEN);
        changeState(keyboard.get(Keyboard.T), Connector.State.CLOSED);
        changeState(keyboard.get(Keyboard.L), Connector.State.LOCKED, Level0Room.Level0Connectors.W);
        /*for(Level0Room.Level0Connectors place :Level0Room.Level0Connectors.values()) {
            if (place == )
            changeState(keyboard.get(Keyboard.L), Connector.State., place);

        }*/


    }
    public void changeState(Button b, Connector.State s, Level0Room.Level0Connectors level0Connectors){
        if (b.isDown()){
            setOfConnector.get(level0Connectors.getIndex()).setState(s);
        }
    }


    public void changeState(Button b, Connector.State s){
        if (b.isDown()){
            for(Level0Room.Level0Connectors l: Level0Room.Level0Connectors.values()){
                changeState(b, s, l);
            }
        }
    }



}

