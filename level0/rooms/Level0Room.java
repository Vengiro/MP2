package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.items.Cherry;
import ch.epfl.cs107.play.game.icrogue.actor.items.Staff;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Or;

import java.util.ArrayList;
import java.util.List;

public class Level0Room extends ICRogueRoom {

    public Level0Room(DiscreteCoordinates roomCoordinates){
        super(Level0Connectors.getAllConnectorsPosition(), Level0Connectors.getAllConnectorsOrientation()
                ,"icrogue/Level0Room", roomCoordinates);
    }

    public enum Level0Connectors implements ConnectorInRoom {
        // ordre des attributs: position , destination , orientation
        W(new DiscreteCoordinates(0, 4),
                new DiscreteCoordinates(8, 5), Orientation.LEFT),
        S(new DiscreteCoordinates(4, 0),
                new DiscreteCoordinates(5, 8), Orientation.DOWN),
        E(new DiscreteCoordinates(9, 4),
                new DiscreteCoordinates(1, 5), Orientation.RIGHT),
        N(new DiscreteCoordinates(4, 9),
                new DiscreteCoordinates(5, 1), Orientation.UP);

        private final DiscreteCoordinates position;
        private final DiscreteCoordinates destination;
        private final Orientation orientation;
        Level0Connectors(DiscreteCoordinates position, DiscreteCoordinates destination, Orientation orientation) {
            this.position = position;
            this.destination = destination;
            this.orientation = orientation;
        }

        @Override
        public int getIndex() {
            return this.ordinal();
        }

        @Override
        public DiscreteCoordinates getDestination() {
            return destination;
        }

        static List <Orientation > getAllConnectorsOrientation(){
            List<Orientation> connectorsOrientation = new ArrayList<>();
            for (Level0Connectors connectors: Level0Connectors.values()){
                connectorsOrientation.add(connectors.orientation);
            }
            return connectorsOrientation;
        }
        static List<DiscreteCoordinates > getAllConnectorsPosition(){
            List<DiscreteCoordinates> connectorsPosition = new ArrayList<>();
            for (Level0Connectors connectors: Level0Connectors.values()){
                connectorsPosition.add(connectors.position);
            }
            return connectorsPosition;
        }
    }

        @Override
    protected void createArea() {
        super.createArea();
        registerActor(new Background(this, behaviorName));
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

        return "icrogue/level0"+x+y;
    }


    }
