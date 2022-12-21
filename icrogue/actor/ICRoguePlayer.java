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

    private int HP = 3;
    private TextGraphics message = new TextGraphics(Integer.toString(HP), 0.4f, Color.WHITE);
    private boolean hasLost = false;
    private Sprite sprite;
    private final static int MOVE_DURATION = 6;
    private final Keyboard keyboard = this.getOwnerArea().getKeyboard();
    private final ICRoguePlayerInteractionHandler handler = new ICRoguePlayerInteractionHandler();

    private boolean staffCollected = false;

    private ArrayList<Integer> keysID = new ArrayList<>();

    private boolean wantSwitchRoom = false;
    private String destinationName;
    private DiscreteCoordinates spawnPosition;
    // Build an 2d array containing all useful sprites to build an animation in every orientation
    private final Sprite[][] spritesArrayPlayer = RPGSprite.extractSprites("zelda/player", 4,.75f, 1.5f
            ,this , 16, 32, new Vector(.15f,-.15f),
            new Orientation[]{Orientation.DOWN, Orientation.RIGHT, Orientation.UP, Orientation.LEFT});
    private final Animation[] animationOfPlayer =  createAnimations(MOVE_DURATION, spritesArrayPlayer);
    private Animation currentAnimationOfPlayer;


    private final Sprite[][] spritesArrayStaff = RPGSprite.extractSprites("zelda/player.staff_water", 4,1.5f, 1.5f
            ,this , 32, 32, new Vector(.15f,-.15f),
            new Orientation[]{Orientation.DOWN, Orientation.UP, Orientation.RIGHT, Orientation.LEFT});

    private final Animation[] animationOfPlayerStaff =  createAnimations(MOVE_DURATION, spritesArrayStaff);
    private Animation currentAnimationOfPlayerStaff;


    /**
     * Default MovableAreaEntity constructor
     *
     * @param area        (Area): Owner area. Not null
     * @param orientation (Orientation): Initial orientation of the entity. Not null
     * @param position    (Coordinate): Initial position of the entity. Not null
     */
    public ICRoguePlayer(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        correctOrientationSprite(orientation);
        message.setParent(this);
        message.setAnchor(new Vector(-0.3f, 0.1f));
    }

    private final static float COOLDOWN = 1.5f;
    private final static float dt = .1f;
    private float counter = COOLDOWN;

    private final static float CHARGETIMEFOREXPLOSION = 3.f;

    private float currentCharge = 0;

    @Override
    public void update(float deltatime){
        super.update(deltatime);
        this.moveIfPressed(Orientation.UP, keyboard.get(Keyboard.UP));
        this.moveIfPressed(Orientation.DOWN, keyboard.get(Keyboard.DOWN));
        this.moveIfPressed(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
        this.moveIfPressed(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
        counter += dt;
        if (keyboard.get(Keyboard.X).isDown()){ currentCharge += dt;}
        if (counter >= COOLDOWN && keyboard.get(Keyboard.X).isReleased()) {
            counter = 0;
            Fire fireball = throwFireBall();
            if (currentCharge >= CHARGETIMEFOREXPLOSION && fireball != null){
                fireball.throwExplosion();
                fireball.appear();
            }else if (fireball != null){
                fireball.appear();
            }
            currentCharge = 0;
        }
        if (keyboard.get(Keyboard.X).isDown() & staffCollected){
            currentAnimationOfPlayerStaff.update(deltatime);
        }
        if(isDisplacementOccurs()){
            currentAnimationOfPlayer.update(deltatime);
        }
        if (this.isDead() && !(hasLost)){
            hasLost = true;
            System.out.println("Game over");
        }
        message.setText(Integer.toString(HP));
        //If it is not assign to false it keeps being true so switchRoom in ICRogue keeps being called
        wantSwitchRoom = false;
    }

    public void takeDamage(int damage){
        HP = HP - damage;
        if (HP < 0){HP = 0;}
    }
    public boolean isDead(){
        return HP == 0;
    }
    public boolean getWillOfTransition(){return wantSwitchRoom;}
    public String getDestination(){return destinationName;}
    public DiscreteCoordinates getSpawnPosition(){return spawnPosition;}


    private void correctOrientationSprite(Orientation orientation){
        sprite = spritesArrayPlayer[orientation.ordinal()][0];
    }
    private void moveIfPressed(final Orientation orientation, final Button b) {
        if (b.isDown()) {
            if (!this.isDisplacementOccurs()) {
                this.orientate(orientation);
                correctOrientationSprite(orientation);
                this.move(MOVE_DURATION);
                currentAnimationOfPlayer = animationOfPlayer[orientation.ordinal()];
                currentAnimationOfPlayerStaff = animationOfPlayerStaff[orientation.ordinal()];
            }
        }
    }

    private Fire throwFireBall(){
        if (staffCollected){
            // We take the first element of the Discrete Coordinates list provided by getCurrentCells()
            // because it contains only one element
            // The fireball should spawn in front of his staff, so we jump to the cell in front of him
            return new Fire(getOwnerArea(), getOrientation(),getCurrentCells().get(0).jump(getOrientation().toVector()));
        }
        return null;
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
            this.currentAnimationOfPlayer.draw(canvas);
        }else if(keyboard.get(Keyboard.X).isDown() & staffCollected){
            this.currentAnimationOfPlayerStaff.draw(canvas);
        }else {
            this.sprite.draw(canvas);
        }
        message.draw(canvas);
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
        return true;
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
        @Override
        public void interactWith(Cherry c, boolean isCellInteraction){
            if(wantsCellInteraction()){
                c.collect();
            }
        }
        @Override
        public void interactWith(Staff s, boolean isCellInteraction){
            if (wantsViewInteraction()){
                s.collect();
                staffCollected = s.isCollected();
            }
        }
        @Override
        public void interactWith(Key key, boolean isCellInteraction){
            if (wantsCellInteraction()){
                key.collect();
                keysID.add(key.getKeyID());
            }
        }
        @Override
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
        @Override
        public void interactWith(Turret turret, boolean isCellInteraction){
            if (wantsCellInteraction() && !(turret.isDead())){
                turret.die();
            }
        }


    }
}
