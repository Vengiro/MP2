package ch.epfl.cs107.play.game.icrogue.area.level0;

import ch.epfl.cs107.play.game.areagame.AreaGame;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.RandomHelper;
import ch.epfl.cs107.play.game.icrogue.area.ConnectorInRoom;
import ch.epfl.cs107.play.game.icrogue.area.ICRogueRoom;
import ch.epfl.cs107.play.game.icrogue.area.Level;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0KeyRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0Room;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0StaffRoom;
import ch.epfl.cs107.play.game.icrogue.area.level0.rooms.Level0TurretRoom;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;
import java.util.List;

public class Level0 extends Level {

    private final int PART_1_KEY_ID = 1;
    private final int BOSS_KEY_ID = 2;

    private final int height = 4;
    private final int width = 4;

    private static DiscreteCoordinates startingRoomPosition = new DiscreteCoordinates(1,1);
    private final int[] roomsDistribution = new int[RoomType.values().length];
    private String roomName;


    public Level0() {
        super(true, startingRoomPosition, 8, 8);
        setRoomsDistribution(fillRoomDistribution());
        generateRandomMap(fillRoomDistribution());
        /*map = new ICRogueRoom[width][height];
        generateFixedMap();*/
    }

    public Level0(boolean randomMap){
        super(randomMap, startingRoomPosition, 4, 2);
        if (!randomMap) {
            setMap(new ICRogueRoom[width][height]);
            generateFixedMap();
        } else {
            generateRandomMap(fillRoomDistribution());
        }

    }

    public enum RoomType {
        TURRET_ROOM(3), // type and number of roon
        STAFF_ROOM(1),
        BOSS_KEY(1),
        SPAWN(1),
        NORMAL(1);
//..
    final int numberOfRooms;
         RoomType(int i) {
            numberOfRooms = i;
        }
        public int getNumberOfRooms(){return numberOfRooms;}
        public static RoomType fromOrder(int i){
             if(0 <= i && i < RoomType.values().length){return RoomType.values()[i];}
             else {return  null;}
        }
    }

    private int[] fillRoomDistribution(){
        for(int i=0; i<roomsDistribution.length; ++i){
            roomsDistribution[i] = RoomType.values()[i].getNumberOfRooms();
        }
        return roomsDistribution;
    }


    public void initArea(AreaGame areaGame){
        for(ICRogueRoom[] roomArray : getMap()){
            for (ICRogueRoom room : roomArray){
                if (room != null){
                    areaGame.addArea(room);
                }
            }
        }
    }

    /**
     * Method that will fill the rooms map.
     */
    public void generateFixedMap(){
        generateMap2();
        /*super.generateRandomRoomPlacement();*/
    }

    private void generateMap1() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0KeyRoom(room00, PART_1_KEY_ID));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);
        lockRoomConnector(room00, Level0Room.Level0Connectors.E,  PART_1_KEY_ID);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1, 0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

    }

    private void generateMap2() {
        DiscreteCoordinates room00 = new DiscreteCoordinates(0, 0);
        setRoom(room00, new Level0TurretRoom(room00));
        setRoomConnector(room00, "icrogue/level010", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room10 = new DiscreteCoordinates(1,0);
        setRoom(room10, new Level0Room(room10));
        setRoomConnector(room10, "icrogue/level011", Level0Room.Level0Connectors.S);
        setRoomConnector(room10, "icrogue/level020", Level0Room.Level0Connectors.E);

        lockRoomConnector(room10, Level0Room.Level0Connectors.W,  BOSS_KEY_ID);
        setRoomConnectorDestination(room10, "icrogue/level000", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room20 = new DiscreteCoordinates(2,0);
        setRoom(room20,  new Level0StaffRoom(room20));
        setRoomConnector(room20, "icrogue/level010", Level0Room.Level0Connectors.W);
        setRoomConnector(room20, "icrogue/level030", Level0Room.Level0Connectors.E);

        DiscreteCoordinates room30 = new DiscreteCoordinates(3,0);
        setRoom(room30, new Level0KeyRoom(room30, BOSS_KEY_ID));
        setRoomConnector(room30, "icrogue/level020", Level0Room.Level0Connectors.W);

        DiscreteCoordinates room11 = new DiscreteCoordinates(1, 1);
        setRoom (room11, new Level0Room(room11));
        setRoomConnector(room11, "icrogue/level010", Level0Room.Level0Connectors.N);
    }
    public static DiscreteCoordinates getStartingRoomPosition(){return startingRoomPosition;}

    /**
     * Method that will randomly create rooms.
     * @param roomsDistribution
     */
    protected void generateRandomMap(int [] roomsDistribution){
        super.generateRandomMap(roomsDistribution);
        MapState[][] carteDesPlacements = generateRandomRoomPlacement(getMap().length);
        roomsPlacement(carteDesPlacements);
    }

    /**
     * A placement plan to create rooms.
     * @param placementOfRooms
     */
    protected void roomsPlacement(MapState[][] placementOfRooms){
        List<Integer> arrayOfRooms = new ArrayList<>();
        for(int i=0; i<getMap().length; ++i){arrayOfRooms.add(i);}

        // For each value in roomsDistribution we randomly add it to the map according to the placementsOfRooms
        for (int k=0; k<roomsDistribution.length; ++k) {
            List<Integer> roomsPlace = RandomHelper.chooseKInList(roomsDistribution[k], arrayOfRooms);
            int counter = 0;

            for (int i = 0; i < placementOfRooms.length; ++i) {
                for (int j = 0; j < placementOfRooms.length; ++j) {
                    if (placementOfRooms[i][j] != MapState.NULL && placementOfRooms[i][j] != MapState.BOSS_ROOM) {
                        for (int room : roomsPlace) {
                            if (room == counter) {
                                setRoom(new DiscreteCoordinates(i, j), room(RoomType.fromOrder(k), i, j));
                                setConnectorsOfRoom(placementOfRooms, new DiscreteCoordinates(i, j));
                                arrayOfRooms.remove((Integer) counter);
                            }
                        }
                        counter += 1;
                    }

                    if(placementOfRooms[i][j] == MapState.BOSS_ROOM){
                        DiscreteCoordinates coords = new DiscreteCoordinates(i, j);
                        setBossPosition(coords);
                        setRoom(coords, new Level0TurretRoom(coords));
                        setConnectorsOfRoom(placementOfRooms, coords);
                    }
                }
            }
        }
    }


    private ICRogueRoom room(RoomType type, int i, int j){
        DiscreteCoordinates position = new DiscreteCoordinates(i, j);
        switch (type){
            case SPAWN -> {
                startingRoomPosition = position;
                return new Level0Room(position);
            }
            case NORMAL -> {
                return new Level0Room(position);
            }
            case STAFF_ROOM -> {
                return new Level0StaffRoom(position);
            }
            case BOSS_KEY -> {
                return new Level0KeyRoom(position, BOSS_KEY_ID);
            }
            case TURRET_ROOM -> {
                return new Level0TurretRoom(position);
            }
        }
        return null;
    }

    private void setConnectorsOfRoom(MapState[][] placementOfRooms, DiscreteCoordinates position){
        if (position.y < getMap().length-1 && placementOfRooms[position.x][position.y + 1] != MapState.NULL) {
            //Right
            setRoomConnector(position, getRoomName(position.x, position.y+1), Level0Room.Level0Connectors.E);
            if(getBossPosition().equals(new DiscreteCoordinates(position.x, position.y+1))){
                lockRoomConnector(position, Level0Room.Level0Connectors.E, BOSS_KEY_ID);
            }
        }
        if (0 < position.x  && placementOfRooms[position.x - 1][position.y] != MapState.NULL) {
            //Up
            setRoomConnector(position, getRoomName(position.x-1, position.y), Level0Room.Level0Connectors.N);
            if(getBossPosition().equals(new DiscreteCoordinates(position.x-1, position.y))){
                lockRoomConnector(position, Level0Room.Level0Connectors.N, BOSS_KEY_ID);
            }
        }
        if (0 < position.y  &&  placementOfRooms[position.x][position.y - 1] != MapState.NULL) {
            //Left
            setRoomConnector(position, getRoomName(position.x, position.y-1), Level0Room.Level0Connectors.W);
            if(getBossPosition().equals(new DiscreteCoordinates(position.x, position.y-1))){
                lockRoomConnector(position, Level0Room.Level0Connectors.W, BOSS_KEY_ID);
            }
        }
        if (position.x < getMap().length-1 && placementOfRooms[position.x +1][position.y] != MapState.NULL) {
            //Down
            setRoomConnector(position, getRoomName(position.x+1, position.y), Level0Room.Level0Connectors.S);
            if(getBossPosition().equals(new DiscreteCoordinates(position.x+1, position.y))){
                lockRoomConnector(position, Level0Room.Level0Connectors.S, BOSS_KEY_ID);
            }
        }
    }

    protected  void setRoomName(DiscreteCoordinates coords){
        roomName = "icrogue/level0"+ coords.x + coords.y;
    }
    private String getRoomName(int x, int y){
        DiscreteCoordinates coords = new DiscreteCoordinates(x,y);
        return "icrogue/level0"+ coords.x + coords.y;}

}
