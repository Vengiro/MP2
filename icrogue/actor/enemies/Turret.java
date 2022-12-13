package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.ICRogueActor;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Arrow;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Turret extends ICRogueActor {

    private Sprite sprite;
    private Orientation[] orientations;
    private final float COOLDOWN = 2.f;
    private final float dt = .1f;
    private float counter = 0;
    private boolean isDead = false;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public Turret(Area area, Orientation orientation, DiscreteCoordinates position, Orientation[] orientationsOfArrows) {
        super(area, orientation, position);
        sprite = new Sprite("icrogue/static_npc", 1.5f, 1.5f, this , null , new Vector(-0.25f, 0));
        orientations = orientationsOfArrows;
    }

    public void update(float deltatime){
        super.update(deltatime);
        if(counter >= COOLDOWN){
            throwArrow();
            counter = (counter)%COOLDOWN;
        }
        counter += dt;
    }

    private void throwArrow(){
        for(Orientation orientation : orientations){
            Arrow arrow = new Arrow(getOwnerArea(), orientation, getCurrentCells().get(0));
            arrow.appear();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.sprite.draw(canvas);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
    }

    public void die(){
        this.getOwnerArea().unregisterActor(this);
        isDead = true;
    }
    public boolean isDead(){return isDead;}
}
