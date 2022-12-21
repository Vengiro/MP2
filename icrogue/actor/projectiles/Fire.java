package ch.epfl.cs107.play.game.icrogue.actor.projectiles;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.ICRogueBehavior;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations;

public class Fire extends Projectile {

    private final Sprite[] spritesArrayFire = Sprite.extractSprites("zelda/fire", 7,1, 1
            ,this , new Vector(.15f,-.15f), 16, 16);
    private final Animation animationOfFire =  new Animation(getMoveDuration(), spritesArrayFire);

    private final Sprite[] spritesArrayExplosion = Sprite.extractSprites("zelda/explosion", 7,1.5f, 1.5f
            ,this , new Vector(.15f,-.15f), 32, 32);
    private final Animation animationOfExplosion =  new Animation(getMoveDuration(), spritesArrayExplosion);

    private boolean isExplosion = false;

    public Fire(Area area, Orientation orientation, DiscreteCoordinates position, int MOVE_DURATION, int damage) {
        super(area, orientation, position, MOVE_DURATION, damage);
    }

    public Fire(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);
    }

    public boolean isExplosion(){return isExplosion;}
    public void throwExplosion(){isExplosion = true;}


    @Override
    public void draw(Canvas canvas) {
        if(isExplosion){
            animationOfExplosion.draw(canvas);
        }else {
            animationOfFire.draw(canvas);
        }
    }
    @Override
    public void update(float deltatime){
        super.update(deltatime);
        throwFireBall();
        animationOfFire.update(deltatime);
        animationOfExplosion.update(deltatime);
    }

    private void throwFireBall(){
        move(getMoveDuration());
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

    private final ICRogueFireInteractionHandler handler = new ICRogueFireInteractionHandler();
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }

    private class ICRogueFireInteractionHandler implements ICRogueInteractionHandler{
        @Override
        public void interactWith(ICRogueBehavior.ICRogueCell cell, boolean isCellInteraction){

            if(doesItStopProjectiles(cell) && !(isConsumed())){
                consume();

            }
        }
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction){
            if (wantsCellInteraction()){
                turret.die();
                consume();
            }
        }
    }
}
