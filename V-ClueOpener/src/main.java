import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import simple.api.script.Category;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;

@ScriptManifest(author = "Vainiven", category = Category.OTHER, description = "Will open all your clue scrolls in your bank and then bank the items.", discord = "Vainven#6986", name = "V-ClueOpener", servers = {
		"Xeros" }, version = "0.1")

public class main extends Script implements SimplePaintable {

	int cluesOpened = 0;
	public String status;
	public long startTime;
	private final Color color1 = new Color(255, 255, 255);
	private final Font font1 = new Font("Gadugi", 1, 13);
	private final Font font2 = new Font("Gadugi", 0, 13);
	private final Image img1 = ctx.paint.getImage("https://i.imgur.com/jSK1we2.png");

	@Override
	public boolean onExecute() {
		System.out.println("Started V-ClueOpener!");
		startTime = System.currentTimeMillis();
		status = "Preparing Clue's";
		if (ctx.players.getLocal().getLocation().getRegionID() != 12342) {
			ctx.onCondition(() -> ctx.magic.castHomeTeleport(), 10, 300);
		}
		return true;
	}

	@Override
	public void onProcess() {
		if (ctx.inventory.populate().filterContains("clue").isEmpty()
				&& (ctx.inventory.populate().filterContains("Casket").isEmpty())) {
			if (!ctx.bank.bankOpen()) {
				ctx.bank.openBank();
				status = "Opening Bank";
			} else {
				ctx.bank.depositInventory();
				status = "Depositing Clue's";
				if (ctx.bank.populate().filterContains("clue", "Casket").isEmpty()
						&& (ctx.inventory.populate().filterContains("clue", "Casket").isEmpty())) {
					ctx.stopScript();
				}
				ctx.bank.withdraw(ctx.bank.populate().filterContains("clue", "Casket").next().getId(), 5);
				ctx.bank.closeBank();
			}
		} else if (ctx.inventory.populate().filterContains("clue", "Casket").next() != null) {
			ctx.inventory.populate().filterContains("clue", "Casket").next().interact(74);
			status = "Opening Clue's";
			cluesOpened++;
		}
	}

	@Override
	public void onTerminate() {

	}

	@Override
	public void onPaint(Graphics2D g1) {
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(img1, 7, 345, null);
		g.setFont(font1);
		g.setColor(color1);
		g.drawString("V-ClueOpener", 108, 362);
		g.setFont(font2);
		g.drawString("TIME: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 132, 380);
		g.drawString("STATUS: " + status, 133, 395);
		g.drawString("CLUES OPENED: " + cluesOpened, 230, 380);
	}
}