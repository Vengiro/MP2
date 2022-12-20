package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.icrogue.actor.items.Item;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public abstract class Level0ItemRoom extends Level0Room{
    protected ArrayList<Item> items = new ArrayList<>();
    public Level0ItemRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
    }

    @Override
    public boolean isOn() {
        for(Item item : items){
            if(!(item.isCollected())){
                return false;
            }
        }
        return true;
    }
}
