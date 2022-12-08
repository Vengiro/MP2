package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class Fire extends Projectile {

    ICRogueFireInteractionHandler handler = new ICRogueFireInteractionHandler();
    public Fire(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION, int damage) {
        super(area, orientation, position, MOVE_DURATION, damage);
    }


    @Override
    public void draw(Canvas canvas) {
        sprite.draw(canvas);

    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    @Override
    public void setSprite(){sprite = new Sprite("zelda/fire", 1f, 1f, this ,
            new RegionOfInterest(0, 0, 16, 16),
            new Vector(0, 0));}

    public void appear(){
        this.getOwnerArea().registerActor(this);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }

    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler{
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction){
            boolean wallOrHole = (cell.getType().equals(ICRogueBehavior.ICRogueCell.ICRogueCellType.HOLE))  ||
                    (cell.getType().equals(ICRogueBehavior.ICRogueCell.ICRogueCellType.WALL));

            if(wantsViewInteraction() && wallOrHole){
                consume();

            }
        }
    }
}
