package ch.epfl.cs107.play.game.icrogue.area.level0.rooms;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Enemy;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.MoveableTurret;
import ch.epfl.cs107.play.game.icrogue.actor.enemies.Turret;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level0BossRoom extends Level0EnemyRoom{

    Turret miniBoss =  new MoveableTurret(this, Orientation.UP, new DiscreteCoordinates(4,4), Orientation.values(), 12);
    private final Enemy[] enemiesList = {miniBoss};
    public Level0BossRoom(DiscreteCoordinates roomCoordinates) {
        super(roomCoordinates);
        setEnemiesList(enemiesList);
    }
}
