package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class Arrow extends Projectile{
    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION, int damage) {
        super(area, orientation, position, MOVE_DURATION, damage);
        sprite = new Sprite("zelda/arrow", 1f, 1f, this ,
                new RegionOfInterest(32* orientation.ordinal(), 0, 32, 32), new Vector(0, 0));

    }

    public Arrow(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        sprite = new Sprite("zelda/arrow", 1f, 1f, this ,
                new RegionOfInterest(32* orientation.ordinal(), 0, 32, 32), new Vector(0, 0));

    }

    public void update(float deltatime){
        super.update(deltatime);
        throwArrow();
    }

    private void throwArrow(){
        move(MOVE_DURATION);
    }

    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    private final ICRogueArrowInteractionHandler handler = new ICRogueArrowInteractionHandler();
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    private class ICRogueArrowInteractionHandler implements ICRogueInteractionHandler{
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction){

            if(doesItStopProjectiles(cell)){
                consume();
            }
        }
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction){
            consume();
        }


    }
}
