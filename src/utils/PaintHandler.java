package utils;

import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.input.mouse.BotMouseListener;
import org.osbot.rs07.script.MethodProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;

public class PaintHandler {
    private final GUI.Mode activeMode;
    private final Image bg = getImage("https://i.imgur.com/arkC8nn.png");
    public String status;
    MethodProvider methods;
    Rectangle clickableXArea = new Rectangle(502, 338, 16, 10);
    private long startTime;
    private boolean showPaint = true;


    public PaintHandler(MethodProvider methods, GUI.Mode activeMode) {
        this.methods = methods;
        this.activeMode = activeMode;
    }

    public void onStart() {
        startTime = System.currentTimeMillis();
        methods.getExperienceTracker().start(Skill.MINING);
        methods.bot.addMouseListener(new BotMouseListener() {
            @Override
            public void checkMouseEvent(MouseEvent mouseEvent) {
                Point point = mouseEvent.getPoint();
                if (clickableXArea.contains(point)) {
                    showPaint = !showPaint;
                }
            }
        });
    }

    public final String formatTime(final long ms) {
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60;
        m %= 60;
        h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    public void drawPaint(final Graphics2D g) {
        long timeElapsed = System.currentTimeMillis() - startTime;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        Font font = new Font("Verdana", Font.BOLD, 26);
        g.setFont(font);


        if (showPaint) {

            g.drawImage(bg, 0, 295, null);

//            // Paint background
//            g.setColor(new Color(18, 10, 17)); // dark purple bg
//            g.fillRect(0, 339, 519, 141);
//
//            // Paint foreground
//            g.setColor(new Color(27, 15, 26)); // light purple bg
//            g.fillRoundRect(10, 349, 498, 120, 5, 5);

            // Paint text
//            g.setColor(Color.WHITE); // White text color
//            g.drawString("PsyMiner", 200, 360);

            g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
            g.drawString("Status: " + status, 15, 380);
            g.drawString("Runtime: " + formatTime(timeElapsed), 15, 400);
//            g.drawString("Mode: " + activeMode.toString(), 15, 420);
            g.setFont(g.getFont().deriveFont(Font.PLAIN, 10f));
            g.drawString("Version: 1.0", 15, 468);

            g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
            g.drawString("Mining Exp Gained: " + decimalFormat.format(methods.getExperienceTracker().getGainedXP(Skill.MINING) / 1000.0) + "K", 325, 380);
            g.drawString("Mining Levels Gained: " + methods.getExperienceTracker().getGainedLevels(Skill.MINING), 325, 400);
            g.drawString("Mining xp/h: " + methods.getExperienceTracker().getGainedXPPerHour(Skill.MINING), 325, 420);
            g.drawString("XP to next level: " + methods.getSkills().experienceToLevel(Skill.MINING), 325, 440);
            g.drawString("TTL: " + formatTime(methods.getExperienceTracker().getTimeToLevel(Skill.MINING)), 325, 460);
        }

        // Draw the clickable X to close the paint
        g.setFont(g.getFont().deriveFont(Font.BOLD, 16f));
        g.setColor(Color.WHITE);
        g.drawString("X", 505, 352);
    }

    public Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
        }
        return null;
    }
}
