package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0Room extends ICRogueRoom {

    public Level0Room(DiscreteCoordinates roomCoordinates){
        super("icrogue/Level0Room", roomCoordinates);

    }
    @Override
    protected void createArea() {
        registerActor(new Background(this, behaviorName));
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates (4,3)));
        registerActor(new Cherry(this, Orientation.DOWN, new DiscreteCoordinates(6,3)));
    }

    @Override
    public DiscreteCoordinates getPlayerSpawnPosition() {
        return new DiscreteCoordinates(2,2);
    }

    @Override
    public String getTitle() {
        String x =  String.valueOf(roomCoordinates.x);
        String y =  String.valueOf(roomCoordinates.y);

        return "icrogue/Level0"+x+y;
    }
}
