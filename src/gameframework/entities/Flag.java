package gameframework.entities;

import gameframework.BattleMode;
import gameframework.Framework;
import gameframework.libs.Animation;
import gameframework.libs.other.VectorLight;

import java.awt.Color;
import java.awt.Graphics2D;

public class Flag extends Entity {

    public Animation mainAnimation;
    public String country = "none";
    public int percents = 100;
    public int team = 1;

    public Flag(String country, int team) {
        this.team = team;
        if (country == null || country.equals("none")) {
            mainAnimation = new Animation("FLAG_NONE", Framework.activeGame().getGameResources().getTexture("flag/flag_empty.png"), 128,
                    128, 1, 400, false, 0, 0, 0, 0, false);
            percents = 0;
            this.country = "none";
        } else {
            this.country = country;
            percents = 100;
            mainAnimation = new Animation("FLAG_IDLE", Framework.activeGame().getGameResources()
                    .getTexture("flag/" + country + "_flag_idle.png"), 128, 128, 2, 550 + (int) (Math.random() * 100), true, 0, 0, 0, 0,
                    false);
        }
    }

    long nextThink = 0;

    public void Update() {
        if (Framework.CurGameTime() > nextThink) {
            boolean enemyHere = false;
            for (Entity ent : Framework.activeGame().getEntsArray()) {
                if (VectorLight.distanceApproximation2D(ent.GetPos(), GetPos()) <= 180) {
                    if (ent.getClass().getSimpleName().equals("Human")) {
                        if (((Human) ent).getAI().getTeam() != team) {
                            if (country.equals("none")) {
                                team = ((Human) ent).getAI().getTeam();
                                country = BattleMode.getCountryID(team);
                                mainAnimation = new Animation("FLAG_IDLE_HALF", Framework.activeGame().getGameResources()
                                        .getTexture("flag/" + country + "_flag_idle_half.png"), 128, 128, 2,
                                        550 + (int) (Math.random() * 100), true, 0, 0, 0, 0, false);
                                percents = 0;
                            } else {
                                percents = percents - 15;
                                if (percents <= 0) {
                                    team = ((Human) ent).getAI().getTeam();
                                    country = BattleMode.getCountryID(team);
                                    mainAnimation = new Animation("FLAG_IDLE_HALF", Framework.activeGame().getGameResources()
                                            .getTexture("flag/" + country + "_flag_idle_half.png"), 128, 128, 2,
                                            550 + (int) (Math.random() * 100), true, 0, 0, 0, 0, false);
                                    percents = 0;
                                } else if (percents <= 50 && !mainAnimation.getAnimName().equals("FLAG_IDLE_HALF")) {
                                    mainAnimation = new Animation("FLAG_IDLE_HALF", Framework.activeGame().getGameResources()
                                            .getTexture("flag/" + country + "_flag_idle_half.png"), 128, 128, 2,
                                            550 + (int) (Math.random() * 100), true, 0, 0, 0, 0, false);
                                }
                            }
                            enemyHere = true;
                            break;
                        }
                    }
                }
            }
            if (!enemyHere && !country.equals("none")) {
                percents = Math.min(percents + 5, 100);
            }
            if (mainAnimation.getAnimName().equals("FLAG_IDLE_HALF") && percents >= 50) {
                mainAnimation = new Animation("FLAG_IDLE", Framework.activeGame().getGameResources()
                        .getTexture("flag/" + country + "_flag_idle.png"), 128, 128, 2, 550 + (int) (Math.random() * 100), true, 0, 0, 0,
                        0, false);
            }
            nextThink = Framework.CurGameTime() + 3000;
        }
    }

    public void Draw(Graphics2D g2d) {
        mainAnimation.changeCoordinates(pos.x - 51, pos.y - 85);
        mainAnimation.Draw(g2d);
        if (Framework.activeGame().getClass().getSimpleName().equals("DemoBattle")) {
            return;
        }
        g2d.setColor(Color.WHITE);
        g2d.drawString(percents + "%", pos.x, pos.y - 52);

    }

}
