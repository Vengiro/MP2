package ch.epfl.cs107.play.game.icrogue;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.icrogue.handler.ICRogueInteractionHandler;
import ch.epfl.cs107.play.game.tutosSolution.Tuto2Behavior;
import ch.epfl.cs107.play.window.Window;

public class ICRogueBehavior extends AreaBehavior {

    /**
     * Default AreaBehavior Constructor
     *
     * @param window (Window): graphic context, not null
     * @param name   (String): name of the behavior image, not null
     */
    public ICRogueBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for(int y = 0; y < height; y++) {
            for (int x = 0; x < width ; x++) {
                ICRogueCell.ICRogueCellType color = ICRogueCell.ICRogueCellType.toType(getRGB(height-1-y, x));
                setCell(x,y, new ICRogueBehavior.ICRogueCell(x,y,color));
            }
        }

    }

    public class ICRogueCell extends Cell {
        public enum ICRogueCellType{
            NONE(0,false), // Should never been used except in the
            GROUND(-16777216, true), // traversable
            WALL(-14112955, false), // non traversable
            HOLE(-65536, true);

            final int type;
            final boolean isWalkable;

            ICRogueCellType(int type, boolean isWalkable){
                this.type = type;
                this.isWalkable = isWalkable;
            }

            public static ICRogueCell.ICRogueCellType toType(int type){
                for(ICRogueCell.ICRogueCellType val : ICRogueCell.ICRogueCellType.values()){
                    if(val.type == type)
                        return val;
                }
                // When you add a new color, you can print the int value here before assign it to a type
                System.out.println(type);
                return NONE;
            }
            public boolean equals(ICRogueCellType cell){
                return ((cell.type == this.type) && (cell.isWalkable == this.isWalkable));
            }
        }

        private final ICRogueCellType cellType;

        /**
         * Default Cell constructor
         *
         * @param x (int): x-coordinate of this cell
         * @param y (int): y-coordinate of this cell
         */
        public ICRogueCell(int x, int y, ICRogueCellType cellType) {
            super(x, y);
            this.cellType = cellType;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            for(Interactable actorOnCell: this.entities){
                if(actorOnCell.takeCellSpace()){
                    return false;
                }
            }
            return cellType.isWalkable;
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
            ((ICRogueInteractionHandler) v).interactWith(this , isCellInteraction);
        }

        public ICRogueCellType getType(){
            return cellType;
        }
    }
}
