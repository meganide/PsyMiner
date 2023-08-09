package utils;

import org.osbot.rs07.api.Mouse;
import org.osbot.rs07.script.MethodProvider;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class MouseCursor{
    private static double rotationAngle = 0;

    public static void drawMouseCursor(Graphics2D g, Mouse mouse) {
        int x = mouse.getMouse().getPosition().x;
        int y = mouse.getMouse().getPosition().y;
//        int outerCircleSize = 30;
        int innerCircleSize = 16;
        int dotSize = 5;
        float dashLength = 10.0f;
//        Color outerCircleColor = new Color(154, 95, 183); // Purple color for outer circle
        Color innerCircleColor = new Color(196, 147, 213); // Light purple color for inner circle
        Color dotColor = new Color(230, 128, 255); // Lighter purple color for dot

        // Update the rotation angle for spinning effect
        rotationAngle += 0.05;

        // Save the original transformation
        AffineTransform originalTransform = g.getTransform();

        // Apply rotation transformation
        g.rotate(rotationAngle, x, y);

        // Draw the spinning outer dashed circle
//        g.setColor(outerCircleColor);
        Stroke outerDashedStroke = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[]{dashLength}, 0);
        g.setStroke(outerDashedStroke);
//        g.drawOval(x - outerCircleSize / 2, y - outerCircleSize / 2, outerCircleSize, outerCircleSize);

        // Draw the spinning inner dashed circle
        g.setColor(innerCircleColor);
        Stroke innerDashedStroke = new BasicStroke(1.25f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, new float[]{7.5f}, 0);
        g.setStroke(innerDashedStroke);
        g.drawOval(x - innerCircleSize / 2, y - innerCircleSize / 2, innerCircleSize, innerCircleSize);

        // Restore the original transformation
        g.setTransform(originalTransform);

        // Draw the dot inside the circles
        int dotX = x - dotSize / 2;
        int dotY = y - dotSize / 2;
        g.setColor(dotColor);
        g.fillOval(dotX, dotY, dotSize, dotSize);
    }
}
