package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0StaffRoom extends  Level0ItemRoom{
    private Staff staff;
    public Level0StaffRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        staff = new Staff(this, Orientation.DOWN, new DiscreteCoordinates(4,3));
        items.add(staff);
    }

    /**
     * Method that creates an area.
     */
    public void createArea(){
        super.createArea();
        registerActor(staff);
    }
}
