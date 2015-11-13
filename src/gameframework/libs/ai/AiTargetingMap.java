package gameframework.libs.ai;

import gameframework.Framework;
import gameframework.entities.Entity;
import gameframework.entities.Human;
import gameframework.libs.other.VectorLight;

import java.util.ArrayList;

public class AiTargetingMap {

    private AiTargetingMapSector[][] sectors;
    private int size;

    public AiTargetingMapSector[][] getSectors() {
        return sectors;
    }

    public AiTargetingMap(int size) {
        this.size = size;
        int sizeX = Framework.activeGame().getMapSizeX() / size;
        int sizeY = Framework.activeGame().getMapSizeY() / size;
        sectors = new AiTargetingMapSector[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                AiTargetingMapSector newSector = new AiTargetingMapSector();
                sectors[i][j] = newSector;
            }
        }

    }

    public Entity getOneTargetInSectors(Entity human, int dist) {
        VectorLight humanPos = human.GetPos();

        int minX = (humanPos.x - dist) / size;
        int maxX = (humanPos.x + dist) / size;
        int minY = (humanPos.y - dist) / size;
        int maxY = (humanPos.y + dist) / size;

        minX = Math.min(Math.max(minX, 0), sectors.length);
        maxX = Math.max(Math.min(maxX, sectors.length), 0);
        minY = Math.min(Math.max(minY, 0), sectors[0].length);
        maxY = Math.max(Math.min(maxY, sectors[0].length), 0);

        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {
                for (Entity ent : sectors[i][j].getSectorEnts()) {
                    if (VectorLight.distanceApproximation2D(ent.GetPos(), humanPos) <= dist) {
                        if (ent.getClass().getSimpleName().equals("Human")) {
                            if (((Human) ent).getAI().getTeam() != ((Human) human).getAI().getTeam()) {
                                return ent;
                            }
                        }
                    }
                }
            }
        }

        return null;

    }

    public void updateSectors() {
        for (int i = 0; i < sectors.length; i++) {
            for (int j = 0; j < sectors[0].length; j++) {
                sectors[i][j].getSectorEnts().clear();
            }
        }

        for (Entity curEnt : Framework.activeGame().getEntsArray()) {
            int x = curEnt.GetPos().x / size;
            int y = curEnt.GetPos().y / size;
            if ((x >= 0 && x < sectors.length) && (y >= 0 && y < sectors[0].length)) {
                sectors[x][y].getSectorEnts().add(curEnt);
            }
        }
    }

    public void setSectors(AiTargetingMapSector[][] sectors) {
        this.sectors = sectors;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private class AiTargetingMapSector {
        public AiTargetingMapSector() {
            sectorEnts = new ArrayList<Entity>();
        }

        private ArrayList<Entity> sectorEnts;

        public ArrayList<Entity> getSectorEnts() {
            return sectorEnts;
        }

    }

    public ArrayList<Entity> getTargetsInSectors(Entity human, int dist) {
        VectorLight humanPos = human.GetPos();
        ArrayList<Entity> victims = new ArrayList<Entity>();

        int minX = (humanPos.x - dist) / size;
        int maxX = (humanPos.x + dist) / size;
        int minY = (humanPos.y - dist) / size;
        int maxY = (humanPos.y + dist) / size;

        minX = Math.min(Math.max(minX, 0), sectors.length);
        maxX = Math.max(Math.min(maxX, sectors.length), 0);
        minY = Math.min(Math.max(minY, 0), sectors[0].length);
        maxY = Math.max(Math.min(maxY, sectors[0].length), 0);

        for (int i = minX; i < maxX; i++) {
            for (int j = minY; j < maxY; j++) {

                for (Entity ent : sectors[i][j].getSectorEnts()) {
                    if (VectorLight.distanceApproximation2D(ent.GetPos(), humanPos) <= dist) {
                        if (ent.getClass().getSimpleName().equals("Human")) {
                            if (((Human) ent).getAI().getTeam() != ((Human) human).getAI().getTeam()) {
                                victims.add(ent);
                            }
                        }
                    }
                }
            }
        }

        return victims;
    }

}
