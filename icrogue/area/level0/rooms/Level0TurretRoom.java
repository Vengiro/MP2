package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.MoveableTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

import java.util.ArrayList;

public class Level0TurretRoom extends Level0EnemyRoom{

    private final Turret turret1 = new Turret(this, Orientation.UP, new DiscreteCoordinates(1,8), new Orientation[]{Orientation.DOWN, Orientation.RIGHT});
    private final Turret turret2 = new Turret(this, Orientation.UP, new DiscreteCoordinates(8,1), new Orientation[]{Orientation.UP, Orientation.LEFT});

    private final MoveableTurret turret3 = new MoveableTurret(this, Orientation.UP, new DiscreteCoordinates(4,4), new Orientation[]{Orientation.UP, Orientation.LEFT});
    private final Turret[] enemiesList = {turret1, turret2, turret3};
    public Level0TurretRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        setEnemiesList(enemiesList);

    }

}
