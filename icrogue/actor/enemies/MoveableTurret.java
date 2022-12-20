package ch.epfl.cs107.play.game.icrogue.actor.enemies;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.ICRoguePlayer;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class MoveableTurret extends Turret implements Interactor {

    private final MoveableTurretInteractionHandler handler = new MoveableTurretInteractionHandler();
    private final int mapSizeInCells = 10;
    /**
     * Default MovableAreaEntity constructor
     *
     * @param area                 (Area): Owner area. Not null
     * @param orientation          (Orientation): Initial orientation of the entity. Not null
     * @param position             (Coordinate): Initial position of the entity. Not null
     * @param orientationsOfArrows
     */
    public MoveableTurret(Area area, Orientation orientation, DiscreteCoordinates position, Orientation[] orientationsOfArrows) {
        super(area, orientation, position, orientationsOfArrows);
    }

    @Override
    public void update(float deltatime){
        super.update(deltatime);
        if(!(isDisplacementOccurs())) {

            /*orientate(Orientation.values()[(int) Math.round(Math.random() * 3)]);
            *//*orientate(Orientation.UP);*//*
            move(8);*/
        }
    }

    public void die(){
        if (!(isDisplacementOccurs())){
            super.die();
        }
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        List<DiscreteCoordinates> around = new ArrayList<>();
        for(int x=-mapSizeInCells; x<mapSizeInCells; ++x){
            for (int y=-mapSizeInCells; y<mapSizeInCells; ++y){
                around.add(getCurrentMainCellCoordinates().jump(x,y));
            }
        }
        return around;
    }

    @Override
    public boolean wantsCellInteraction() {
        return false;
    }

    @Override
    public boolean wantsViewInteraction() {
        return !(isDisplacementOccurs());
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler , isCellInteraction);
    }

    private class MoveableTurretInteractionHandler implements ICRogueInteractionHandler {

        @Override
        public void interactWith(ICRoguePlayer player, boolean isCellInteraction){
            if(wantsViewInteraction()){
                DiscreteCoordinates diff = new DiscreteCoordinates
                        (player.getCurrentCells().get(0).x - getCurrentCells().get(0).x,
                        player.getCurrentCells().get(0).y - getCurrentCells().get(0).y);


                if (Math.abs(diff.x) <= Math.abs(diff.y) &&  Math.abs(diff.x)>0){
                    orientate(Orientation.fromVector(new Vector(0, diff.y)));
                    move(24);
                }else if ( Math.abs(diff.y)>0){
                    orientate(Orientation.fromVector(new Vector(diff.x, 0)));
                    move(24);
                }

            }
        }
    }
}
