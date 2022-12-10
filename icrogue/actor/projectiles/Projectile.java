package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.Collections;
import java.util.List;

public abstract class Projectile extends ICRogueActor implements Consumable, Interactor {

    protected Sprite sprite;
    protected int MOVE_DURATION;
    protected int damage;
    protected boolean isConsumed;

    final private int DEFAULT_DAMAGE = 1;
    final private int DEFAULT_MOVE_DURATION = 10;

    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION, int damage) {
        super(area, orientation, position);
        this.MOVE_DURATION = MOVE_DURATION;
        this.damage = damage;
    }
    public Projectile(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        MOVE_DURATION = DEFAULT_MOVE_DURATION;
        damage = DEFAULT_DAMAGE;
    }

    public abstract void setSprite();
    @Override
    public void consume() {
        this.getOwnerArea().unregisterActor(this);
        isConsumed = true;
    }

    @Override
    public boolean isConsumed() {
        return isConsumed;
    }

    public  void update(float deltatime){
        super.update(deltatime);
        throwFireBall();
    }

    public void throwFireBall(){
            move(MOVE_DURATION);
    }


    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList
                (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return true;
    }
}
