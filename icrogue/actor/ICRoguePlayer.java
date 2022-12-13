package ch.epfl.cs107.play.game.icrogue.actor;

import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.*;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Key;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.actor.projectiles.Fire;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Or;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.game.areagame.actor.Animation.createAnimations;

public class ICRoguePlayer extends ICRogueActor implements Interactor {

    private Sprite sprite;
    final private Sprite[] upSprite = new Sprite[4];
    final private Sprite[] downSprite = new Sprite[4];
    final private Sprite[] rightSprite = new Sprite[4];
    final private Sprite[] leftSprite = new Sprite[4];
    final private Sprite[][] arrayOfSprite = {upSprite, rightSprite, downSprite, leftSprite};
    private int frame;
    private final static int MOVE_DURATION = 6;
    private final Keyboard keyboard = this.getOwnerArea().getKeyboard();
    private final ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    private boolean staffCollected = false;

    private ArrayList<Integer> keysID = new ArrayList<>();

    private boolean wantSwitchRoom = false;
    private String destinationName;
    private DiscreteCoordinates spawnPosition;
    private final Animation[] animation;
    private Animation currentAnimation;


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ICRoguePlayer(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        correctSprite(orientation);
        animation = createAnimations(MOVE_DURATION, arrayOfSprite);
    }

    public void update(float deltatime){
        super.update(deltatime);
        this.moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        this.moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        this.moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        this.moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        throwFireBall(keyboard.get(Keyboard.X));
        if(isDisplacementOccurs()){
            currentAnimation.update(deltatime);
        }
        //If it is not assign to false it keeps being true so switchRoom in ICRogue keeps being called
        wantSwitchRoom = false;
    }

    public boolean getWillOfTransition(){return wantSwitchRoom;}
    public String getDestination(){return destinationName;}
    public DiscreteCoordinates getSpawnPosition(){return spawnPosition;}

    private void dynamicSprite(){
        for (int i = 0; i < 4; ++i) {
            downSprite[i] = new Sprite("zelda/player", .75f, 1.5f, this , new RegionOfInterest(i*16, 0, 16, 32), new Vector(.15f,-.15f));
            rightSprite[i] = new Sprite("zelda/player", .75f, 1.5f, this , new RegionOfInterest(i*16, 32, 16, 32), new Vector(.15f,-.15f));
            upSprite[i] = new Sprite("zelda/player", .75f, 1.5f, this , new RegionOfInterest(i*16, 64, 16, 32), new Vector(.15f,-.15f));
            leftSprite[i] = new Sprite("zelda/player", .75f, 1.5f, this , new RegionOfInterest(i*16, 96, 16, 32), new Vector(.15f,-.15f));
        }
    }
    private void correctSprite(Orientation orientation){
        dynamicSprite();
        switch (orientation) {
            case UP -> {
                sprite = upSprite[0];
            }
            case DOWN -> {
                sprite = downSprite[0];
            }
            case RIGHT -> {
                sprite = rightSprite[0];
            }
            case LEFT -> {
                sprite = leftSprite[0];
            }
        }

    }
    private void setSprite(final Sprite sprite) {
        this.sprite = sprite;
    }
    private void moveIfPressed(final Orientation orientation, final Button b) {
        if (b.isDown()) {
            if (!this.isDisplacementOccurs()) {
                this.orientate(orientation);
                correctSprite(orientation);
                this.move(MOVE_DURATION);
                currentAnimation = animation[orientation.ordinal()];
            }
        }
    }

    private void throwFireBall(Button b){

        if (b.wasDown() && staffCollected){
            // Check if spam fireball
            if(!(b.isDown())){

                // We take the first element of the Discrete Coordinates list provided by getCurrentCells()
                // because it contains only one element

                Fire fireBall = new Fire(getOwnerArea(), getOrientation(),getCurrentCells().get(0));
                fireBall.appear();
            }
        }


    }

    public void enterArea(final Area area, final DiscreteCoordinates position) {
        area.registerActor((Actor)this);
        this.setOwnerArea(area);
        this.setCurrentPosition(position.toVector());
        this.resetMotion();
    }

    public void leaveArea() {
        this.getOwnerArea().unregisterActor((Actor)this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isDisplacementOccurs()){
            this.currentAnimation.draw(canvas);
        }else {
            this.sprite.draw(canvas);
        }
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
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
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList
                (getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return !(wantsViewInteraction());
    }


    @Override
    public boolean wantsViewInteraction() {
        return keyboard.get(Keyboard.W).isDown();
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }
    private class ICRoguePlayerInteractionHandler implements ICRogueInteractionHandler{

        public void interactWith(Cherry c, boolean isCellInteraction){
            if(wantsCellInteraction()){
                c.collect();
            }
        }

        public void interactWith(Staff s, boolean isCellInteraction){
            if (wantsViewInteraction()){
                s.collect();
                staffCollected = s.isCollected();
            }
        }

        public void interactWith(Key key, boolean isCellInteraction){
            if (wantsCellInteraction()){
                key.collect();
                keysID.add(key.getKeyID());
            }
        }

        public void interactWith(Connector connector, boolean isCellInteraction){
            if(wantsViewInteraction()){
                for(int key : keysID){
                    if (connector.getKeyID() == key){
                        connector.openState();
                    }
                }
            }
            if (wantsCellInteraction()){
                destinationName = connector.getDestination();
                spawnPosition = connector.getCoordinatesOfSpawn();
            }

            wantSwitchRoom = (wantsCellInteraction() & !(isDisplacementOccurs()));
        }

        public void interactWith(Turret turret, boolean isCellInteraction){
            if (wantsCellInteraction()){
                turret.die();
            }
        }


    }
}
