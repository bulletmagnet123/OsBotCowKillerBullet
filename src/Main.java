import org.osbot.rs07.api.Inventory;
import org.osbot.rs07.api.ai.activity.Location;
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.Player;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(info = "Cow Killer Bullet's script.", logo = "", version = 0.1, author = "Bulletmagnet", name = "CowKiller'Bullet")
public class Main extends Script {
    private final int [] ENEMY_IDS = {2794, 2790, 2791, 2793, 2792};
    private final int FoodID = 379;
    public long startTime = 0L, millis = 0L, hours = 0L;
    Area cows = new Area(3254, 3256, 3264, 3296);
    PaintAPI paint = new PaintAPI();
    private long timeBegan;
    private long timeRan;
    private int beginningXp;
    private int AttackLevel;
    private int DefenceLevel;
    private int StrengthLevel;

    @Override
    public void onStart() throws InterruptedException {

        getCamera().toTop();

        timeBegan = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        AttackLevel = skills.getVirtualLevel(Skill.ATTACK);
        DefenceLevel = skills.getVirtualLevel(Skill.DEFENCE);
        StrengthLevel = skills.getVirtualLevel(Skill.STRENGTH);
        super.onStart();
    }

    @Override
    public int onLoop() throws InterruptedException {
        log("STARTED ON LOOP");
        if (isReadyToAttack()){
            attack();
            log("RUNNING ATTACK IN ON LOOP");
        } else {
            log("RUNNING ELSE BANK METHOD IN ON LOOP");
            bank();
        }
        return 0;
    }

    public void bank() throws InterruptedException {
        log("Running bank method");
        if (!getInventory().contains("Lobster") || getInventory().isFull()) {
            log("has no food or inventory is full");
                walking.webWalk(Banks.LUMBRIDGE_UPPER);
                sleep(random(1400, 2700));
                getBank().open();
                sleep(random(1700, 2300));
                getBank().depositAllExcept(FoodID);
                sleep(random(1700, 2300));
                getBank().withdraw(FoodID, 21);
                sleep(random(1700, 2300));
                getBank().close();
                sleep(random(1700, 2300));
        } else if (!getInventory().contains("Lobster") && !getInventory().isFull()) {
            sleep(random(1700, 2300));
            walking.webWalk(cows.getRandomPosition());
            sleep(random(1700, 2300));
            return;
        }

    }

    private void GetFightingEqipment() {

    }
    public void checkHealth(){

    }

    public void heal() throws InterruptedException {
        sleep(random(1700, 2300));
        getInventory().getItem(FoodID).interact("Eat");
    }

    public void attack() throws InterruptedException {
        if (!isReadyToAttack())
            return;

        if (getHpPercent() < 50){
            heal();
        }

        NPC enemy = getNpcs().closest(ENEMY_IDS);

        if (cows.contains(myPlayer().getPosition())) {
            if (enemy != null && !enemy.isUnderAttack() && cows.contains(enemy))

                if (enemy.interact("Attack"))
                    sleep(random(1700, 2300));
        } else {
            walking.webWalk(cows.getRandomPosition());
        }
    }


    public boolean isReadyToAttack(){
        if (hasFood())
            return false;
        if (getCombat().isFighting())
            return false;
        return !myPlayer().isAnimating() && !myPlayer().isUnderAttack() && !myPlayer().isMoving();
    }

    public int getHpPercent(){
        log("getHpPercent()");

        float staticHp = getSkills().getStatic(Skill.HITPOINTS);
        float dynamicHp = getSkills().getDynamic(Skill.HITPOINTS);

        return (int) (100 * (dynamicHp / staticHp));
    }
    public boolean hasFood(){
        return !getInventory().contains(FoodID);

    }
}
