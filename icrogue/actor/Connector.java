package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

import java.util.List;

import static ch.epfl.cs107.play.game.icrogue.actor.Connector.State.INVISIBLE;

public class Connector extends ICRogueActor implements Interactable {
    public enum State{
        OPEN,
        CLOSED,
        LOCKED,
        INVISIBLE;
    }

    private State state;
    private Sprite sprite;
    private String nextArea;
    private DiscreteCoordinates coordinatesOfNewArea;
    private Orientation orientation;
    private final int NO_KEY_ID = 0;

    private int keyID = NO_KEY_ID;

    Connector(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
        state = INVISIBLE;
        this.orientation = orientation;
    }

    @Override
    public void draw(Canvas canvas) {
        switch (state){
            case INVISIBLE -> sprite = new Sprite("icrogue/invisibleDoor_"+orientation.ordinal(),
                    (orientation.ordinal()+1)%2+1, orientation.ordinal()%2+1, this);
        }

    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return null;
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {

    }

    @Override
    public void onLeaving(List<DiscreteCoordinates> coordinates) {

    }

    @Override
    public void onEntering(List<DiscreteCoordinates> coordinates) {

    }

}
