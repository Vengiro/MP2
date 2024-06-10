package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public abstract class Enemy extends ICRogueActor {

    private boolean isDead = false;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Enemy(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
    }

    /**
     * Kill the enemy and unregister him from the current area
     */
    public void die(){
        isDead = true;
        getOwnerArea().unregisterActor(this);
    }

    /**
     * Return if the enemy is dead or not
     * @return boolean
     */
    public boolean isDead(){return isDead;}
}

