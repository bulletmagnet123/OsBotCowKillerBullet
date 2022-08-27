import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.NPCS;
import org.osbot.rs07.api.ai.activity.Location;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;
import java.util.concurrent.TimeUnit;

@ScriptManifest(info = "Multi Killer Script.", logo = "", version = 0.1, author = "Bulletmagnet", name = "Multi Killer Bullet")
public class Main extends Script {
    private final int[] ENEMY_IDS_COWS = {2794, 2790, 2791, 2793, 2792};
    private final int[] ENEMY_IDS_Hill_Giants = {2100, 2098, 2099, 2102, 2101, 2103};
    private final int FoodID = 379;
    public long startTime = 0L, millis = 0L, hours = 0L;
    Area cows = new Area(3254, 3256, 3264, 3296);
    Area HillGiants = new Area(3097, 9829, 3124, 9853);

    private long timeRan;
    private int beginningXp;
    private int AttackLevel;
    private int DefenceLevel;
    private int StrengthLevel;
    public int SattackLevelint;
    public int SstrengthLevel;
    public int SdefenceLevel;
    private long timeBegan;

    public void onMessage(Message m) {
        if (m.getMessage().contains("")) {

        }
    }

    @Override
    public void onStart() throws InterruptedException {
        SattackLevelint  = getSkills().getStatic(Skill.ATTACK);
        SstrengthLevel = getSkills().getStatic(Skill.STRENGTH);
        SdefenceLevel = getSkills().getStatic(Skill.DEFENCE);
        timeBegan = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        super.onStart();
    }

    @Override
    public int onLoop() throws InterruptedException {
        log("STARTED ON LOOP");
        if (isReadyToAttack()) {
            sleep(random(1700, 2300));
            log("RUNNING ATTACK IN ON LOOP");
            attack();
            sleep(random(1700, 2300));
        } else {
            sleep(random(1700, 2300));
            log("RUNNING ELSE BANK METHOD IN ON LOOP");
            bank();
            sleep(random(1700, 2300));
        }
        return 2000;
    }

    public void bank() throws InterruptedException {
        log("Running bank method");
        if (!getInventory().contains("Lobster")) {
            log("has no food or inventory is full");
            walking.webWalk(Banks.GRAND_EXCHANGE);
            sleep(random(1400, 2700));
            getBank().open();
            sleep(random(1700, 2300));
            getBank().depositAllExcept("Lobster", "Brass key");
            sleep(random(1700, 2300));
            getBank().withdraw(FoodID, 21);
            sleep(random(1700, 2300));
            //if (!getInventory().contains("Brass key")) {
            //    getBank().withdraw("Brass key", 1);
            //} else
                getBank().close();
            sleep(random(1700, 2300));
        }

    }

    private void GetFightingEqipment() {

    }

    public void checkHealth() {

    }

    public void heal() {
        getInventory().getItem(FoodID).interact("Eat");
    }

    public void attack() throws InterruptedException {
        if (!isReadyToAttack())
            return;

        if (getHpPercent() < 30) {
            heal();
        } else {

            NPC enemy = getNpcs().closest(ENEMY_IDS_COWS);

            if (cows.contains(myPlayer().getPosition())) {
                if (enemy != null && !enemy.isUnderAttack() && cows.contains(enemy))

                    if (enemy.interact("Attack"))
                        sleep(random(1700, 2300));
            } else {
                walking.webWalk(cows.getRandomPosition());
            }
        }
    }


    public boolean isReadyToAttack() {
        if (!hasFood())
            return false;
        if (getCombat().isFighting())
            return false;
        return !myPlayer().isAnimating() && !myPlayer().isUnderAttack() && !myPlayer().isMoving();
    }

    public int getHpPercent() {
        log("getHpPercent");
        int hpPercent = 0;

        int staticHp = getSkills().getStatic(Skill.HITPOINTS);
        int dynamicHp = getSkills().getDynamic(Skill.HITPOINTS);

        hpPercent = (100 * (dynamicHp / staticHp));

        return hpPercent;
    }

    public boolean hasFood() {
        if (getInventory().contains(FoodID)) {
            return  true;
        } else {
            return false;
        }

    }

    @Override
    public void onPaint(Graphics2D g) {

        Point mP = getMouse().getPosition();
        g.setColor(Color.RED);
        g.drawString("Bullets MultiKiller: 1.0", 15, 265);
        // Draw a line from top of screen (0), to bottom (500), with mouse x coordinate
        g.drawLine(mP.x, 0, mP.x, 500);

        // Draw a line from left of screen (0), to right (800), with mouse y coordinate
        g.drawLine(0, mP.y, 800, mP.y);

        g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
        g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);

        g.setColor(new Color(51, 51, 51, 140));
        g.drawRect(10, 250, 300, 400);
        g.fillRect(10, 250, 300, 400);
        g.setColor(Color.CYAN);
        g.drawString("Starting Attack Level: " + SattackLevelint, 130, 330);
        g.drawString("Starting Strength Level: " + SstrengthLevel, 130, 345);
        g.drawString("Starting Defence Level: " + SdefenceLevel, 130, 360);
        g.drawString("Current Attack Level: " + getSkills().getDynamic(Skill.ATTACK), 130, 375);
        g.drawString("Current Strength Level: " + getSkills().getDynamic(Skill.STRENGTH), 130, 390);
        g.drawString("Current Defence Level: " + getSkills().getDynamic(Skill.DEFENCE), 130, 405);
        g.drawString("Time ran: " + ft(timeRan), 130, 300);


        g.setColor(Color.CYAN);
        timeRan = System.currentTimeMillis() - this.timeBegan;



    }

    private String ft(long duration)
    {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0)
        {
            res = (hours + ":" + minutes + ":" + seconds);
        }
        else
        {
            res = (days + ":" + hours + ":" + minutes + ":" + seconds);
        }
        return res;
    }

}

