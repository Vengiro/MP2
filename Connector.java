package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;


public class Connector extends ICRogueActor implements Interactable {
    public enum State{
        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE;
    }

    private State state;
    private Sprite sprite;
    private String nextAreaName;
    private DiscreteCoordinates coordinatesOfSpawn;
    private final Orientation orientation;
    private static final int NO_KEY_ID = 0;

    private int keyID = NO_KEY_ID;

    public Connector(Area area, Orientation orientation, DiscreteCoordinates position, int keyID){
        super(area, orientation, position);
        this.keyID = keyID;
        state = State.INVISIBLE;
        this.orientation = orientation;
    }

    public Connector(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        state = State.INVISIBLE;
        this.orientation = orientation;
    }


    public void setCoordinatesOfSpawn(DiscreteCoordinates coordinatesOfSpawn){this.coordinatesOfSpawn = coordinatesOfSpawn;}
    public DiscreteCoordinates getCoordinatesOfSpawn(){return coordinatesOfSpawn;}

    public void setDestination(String nextAreaName){
        this.nextAreaName = nextAreaName;
    }
    public String getDestination(){return nextAreaName;}
    public void setKeyID(int keyID){
        this.keyID = keyID;
    }

    public int getKeyID(){return keyID;}

    @Override
    public void draw(Canvas canvas) {
        switch (state){
            case INVISIBLE -> sprite = new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
            case CLOSED -> sprite = new Sprite("icrogue/door_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
            case LOCKED -> sprite = new Sprite("icrogue/lockedDoor_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        }
        if(state != State.OPEN){sprite.draw(canvas);}
    }
    public void switchState(){
        if (this.state == State.OPEN){
            this.state = State.CLOSED;
        }
        else if(this.state != State.LOCKED){
            this.state = State.OPEN;
        }
    }

    public State getState(){return state;}
    public void openState(){
            this.state = State.OPEN;
    }

    public void closeState(){
            this.state = State.CLOSED;
    }

    public void lockState(){this.state = State.LOCKED;}
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        DiscreteCoordinates coord = getCurrentMainCellCoordinates();
        return List.of(coord , coord.jump(new Vector((getOrientation().ordinal()+1)%2,
                getOrientation().ordinal()%2)));

    }

    @Override
    public boolean takeCellSpace() {
        return state != State.OPEN;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return state == State.LOCKED;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {}

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {}

}
