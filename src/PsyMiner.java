import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import utils.GUI;
import utils.MouseCursor;
import utils.PaintHandler;
import utils.Sleep;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

@ScriptManifest(author = "Psybration", name = "PsyMiner", info = "A mining script", version = 0.1, logo = "https://runescape.wiki/images/Dragon_pickaxe_detail.png?06d83")
public final class PsyMiner extends Script {
    final Area[] BANKS = {Banks.AL_KHARID, Banks.ARDOUGNE_NORTH, Banks.ARDOUGNE_SOUTH, Banks.ARCEUUS_HOUSE, Banks.CAMELOT, Banks.CANIFIS, Banks.CASTLE_WARS, Banks.CATHERBY, Banks.DRAYNOR, Banks.DUEL_ARENA, Banks.EDGEVILLE, Banks.FALADOR_EAST, Banks.FALADOR_WEST, Banks.GNOME_STRONGHOLD, Banks.GRAND_EXCHANGE, Banks.HOSIDIUS_HOUSE, Banks.LOVAKENGJ_HOUSE, Banks.LOVAKITE_MINE, Banks.LUMBRIDGE_LOWER, Banks.LUMBRIDGE_UPPER, Banks.PEST_CONTROL, Banks.PISCARILIUS_HOUSE, Banks.SHAYZIEN_HOUSE, Banks.TZHAAR, Banks.VARROCK_EAST, Banks.VARROCK_WEST, Banks.YANILLE};
    private final String pickaxe = "Bronze Pickaxe";
    private GUI gui = new GUI();
    private GUI.Ore ore;
    private GUI.Location location;
    private GUI.Mode activeMode;
    private final PaintHandler paintHandler = new PaintHandler(this, activeMode);

    @Override
    public void onStart() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                gui = new GUI();
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            stop();
            return;
        }

        // If the user closed the dialog and didn't click the Start button
        if (!gui.isStarted()) {
            stop();
            return;
        }

        ore = gui.getSelectedOre();
        location = gui.getSelectedLocation();
        activeMode = gui.getActiveMode();

        paintHandler.onStart();
    }

    @Override
    public int onLoop() throws InterruptedException {
        if (!getInventory().onlyContains(item -> item.nameContains("pickaxe"))) {
            if (!isAtBank()) {
                paintHandler.status = "Walking to closest bank";
                getWalking().webWalk(BANKS);
            } else {
                paintHandler.status = "Depositing junk";
                bankOres();
            }
        } else if (!getInventory().contains(item -> item.getName().endsWith("pickaxe")) && !getEquipment().isWieldingWeaponThatContains("pickaxe")) {
            bankAndWithdrawPickaxe();
        } else if (!location.getPosition().getArea(5).contains(myPosition())) {
            walkToMine();
        } else if (getInventory().isFull() && activeMode == GUI.Mode.Powermine) {
            dropAllOres();
        } else if (getInventory().isFull() && activeMode == GUI.Mode.Bank) {
            bankOres();
        } else {
            mineClosestRocks();
        }

        return random(200, 300);
    }

    @Override
    public void onExit() {
        if (gui != null) {
            gui.close();
        }
    }

    @Override
    public void onPaint(final Graphics2D g) {
        paintHandler.drawPaint(g);
        MouseCursor.drawMouseCursor(g, mouse);
    }

    private void bankAndWithdrawPickaxe() throws InterruptedException {

        if (!isAtBank()) {
            paintHandler.status = "Walking to closest bank";
            getWalking().webWalk(BANKS);
        } else if (!getBank().isOpen()) {
            paintHandler.status = "Opening bank";
            getBank().open();
        } else if (getInventory().getCapacity() > 1) {
            paintHandler.status = "Depositing all items";
            getBank().depositAllExcept(item -> item.nameContains("pickaxe"));
        } else if (getBank().contains(pickaxe)) {
            paintHandler.status = "Withdrawing pickaxe";
            getBank().withdraw(pickaxe, 1);
        } else if (!hasRequiredMiningLevel()) {
            paintHandler.status = "You don't have the required mining level to use this pickaxe, stopping script...";
            stop();
        }
    }

    private void walkToMine() {
        paintHandler.status = "Walking to " + location + " mine";
        getWalking().webWalk(location.getPosition());
    }

    private RS2Object getClosestRocks() {
        return getObjects().closest(rock -> rock.getName().startsWith(ore.toString()) && myPosition().distance(rock.getPosition()) < 5);
    }

    private void mineClosestRocks() {
        RS2Object closestRock = getClosestRocks();
        if (!myPlayer().isAnimating()) {
            paintHandler.status = "Looking for rocks";
        }

        if (closestRock != null && closestRock.interact("Mine")) {
            paintHandler.status = "Mining";
            Sleep.sleepUntil(() -> !closestRock.exists(), random(10000, 30000));
        }
    }

    private void dropAllOres() {
        paintHandler.status = "Inventory full, dropping ores";
        getInventory().dropAll(item -> item.getName().startsWith(ore.toString()));
    }

    private void bankOres() throws InterruptedException {
        paintHandler.status = "Inventory full, banking ores";

        if (!isAtBank()) {
            paintHandler.status = "Walking to closest bank";
            getWalking().webWalk(BANKS);
        } else if (!getBank().isOpen()) {
            paintHandler.status = "Opening bank";
            getBank().open();
        } else if (getInventory().getCapacity() > 1) {
            paintHandler.status = "Depositing items";
            getBank().depositAllExcept(item -> item.nameContains("pickaxe"));
        }

    }

    private boolean isAtBank() {
        for (Area bank : BANKS) {
            if (bank.contains(myPosition())) {
                log("Arrived at bank");
                return true;
            }
        }
        return false;
    }

    private boolean hasRequiredMiningLevel() {
        int miningLevel = skills.getStatic(Skill.MINING);

        switch (pickaxe) {
            case "Dragon Pickaxe":
                return miningLevel >= 61;
            case "Rune Pickaxe":
                return miningLevel >= 41;
            case "Adamant Pickaxe":
                return miningLevel >= 31;
            case "Mithril Pickaxe":
                return miningLevel >= 21;
            case "Black Pickaxe":
                return miningLevel >= 11;
            case "Steel Pickaxe":
                return miningLevel >= 6;
            case "Iron Pickaxe":
            case "Bronze Pickaxe":
                return miningLevel >= 1;
            default:
                return false;
        }
    }

}

// TODO
// Fix banking
// give user ability to choose powermining or banking
// automatically withdraw best pickaxe user can wield/use
// add more mining locations
// walk to the desired rock user wants to mine, scan the area and walk to it.
// if user walks to far away from the rocks it wont detect them because I have set distance to 5.
// give ability to mine several different rocks
// Enable progression. When user levels up automatically upgrade pickaxe if there is any in bank.
// handle user getting attacked by NPCs
// Hop worlds feature
// Hover over next mineable ore
// Allow user to choose which rocks to mine by clicking on them
// create custom banner img enitre pic: 519x184  inner border: 519x145px