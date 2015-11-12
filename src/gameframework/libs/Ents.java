package gameframework.libs;

import gameframework.BattleMode;
import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Human;
import gameframework.libs.other.VectorLight;
import gnu.trove.TIntProcedure;

import java.util.ArrayList;

import com.infomatiq.jsi.Point;

public class Ents {
    public static void addNewEntityInGame(Entity newEnt) {
        gameframework.Framework.activeGame().entCreate(newEnt);
    }

    public static void removeEntityFromGame(Entity ent) {
        gameframework.Framework.activeGame().entRemove(ent);
    }

    public static ArrayList<Entity> getAllEnts() {
        return gameframework.Framework.activeGame().getEntsArray();
    }

    public static ArrayList<Entity> getAllEntsInSphere(VectorLight vectorLight, int dist) {
        ArrayList<Entity> newArray = new ArrayList<Entity>();
        for (Entity ent : getAllEnts()) {
            if (VectorLight.distance2D(ent.GetPos(), vectorLight) <= dist) {
                newArray.add(ent);
            }
        }
        return newArray;
    }

    public static Entity getOneEntInSphere(VectorLight vectorLight, int dist) {
        for (Entity ent : getAllEnts()) {
            if (ent.getClass().getSimpleName().equals("Human")) {
                if (VectorLight.distance2D(ent.GetPos(), vectorLight) <= dist) {
                    return ent;
                }
            }
        }
        return null;
    }

    public static ArrayList<Entity> getAllTargetsInSphere(Entity human, int dist) {
        ArrayList<Entity> newArray = new ArrayList<Entity>();
        for (Entity ent : getAllEnts()) {
            if (ent.getClass().getSimpleName().equals("Human")) {
                if (((Human) ent).getAI().getTeam() != ((Human) human).getAI().getTeam()) {

                    if (VectorLight.distanceApproximation2D(ent.GetPos(), human.GetPos()) <= dist) {
                        newArray.add(ent);
                    }
                }
            }
        }
        return newArray;
    }

    public static Entity getNearestTargetInSphere(Entity human, int dist) {
        // int shortestDist = dist;
        Entity nearestHuman = null;
        final int[] ret = new int[1];
        ret[0] = -1;

        if (Framework.activeGame().getClass().getSimpleName().equals("BattleMode")) {
            if (((Human) human).getAI().getTeam() == 1) {
                ((BattleMode) Framework.activeGame()).getSiTeam1().nearest(new Point(human.GetPos().x, human.GetPos().y),
                        new TIntProcedure() {
                            public boolean execute(int i) {
                                ret[0] = i;
                                return true;
                            }
                        }, dist);
            } else if (((Human) human).getAI().getTeam() == 0) {
                ((BattleMode) Framework.activeGame()).getSiTeam2().nearest(new Point(human.GetPos().x, human.GetPos().y),
                        new TIntProcedure() {
                            public boolean execute(int i) {
                                ret[0] = i;
                                return true;
                            }
                        }, dist);

            }
            if (ret[0] != -1) {
                return Framework.activeGame().getEntsArray().get(ret[0]);
            }
        }
        /*
         * for (Entity ent : getAllEnts()) {
         * 
         * 
         * if (ent.getClass().getSimpleName().equals("Human")) { if (((Human)
         * ent).getAI().getTeam() != ((Human) human).getAI().getTeam()) {
         * 
         * int distance = VectorLight.distanceApproximation2D(ent.GetPos(),
         * human.GetPos()); if ((distance <= dist) && (distance <=
         * shortestDist)) { nearestHuman = ent; shortestDist = distance; } } } }
         */
        return nearestHuman;
    }

    public static Entity getOneTargetInSphere(Entity human, int dist) {
        for (Entity ent : getAllEnts()) {
            if (ent.getClass().getSimpleName().equals("Human")) {
                if (((Human) ent).getAI().getTeam() != ((Human) human).getAI().getTeam()) {

                    if (VectorLight.distanceApproximation2D(ent.GetPos(), human.GetPos()) <= dist) {
                        return ent;
                    }
                }
            }
        }
        return null;
    }

}
