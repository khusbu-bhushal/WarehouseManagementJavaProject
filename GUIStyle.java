import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Small helper class for consistent styling across all GUI states.
 */
public class GUIStyle {

    /**
     * Style for main menu buttons (bold text, darker color, rounded edges).
     */
    public static void styleMainButton(JButton b) {
        if (b == null) return;

        // Bold, readable font
        b.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Darker text color
        b.setForeground(Color.BLACK);

        // Slightly nicer button behavior
        b.setFocusPainted(false);          // no ugly focus outline
        b.setContentAreaFilled(true);
        b.setOpaque(false);

        // Rounded border
        b.setBorder(new RoundedBorder(12));   // radius = 12px
    }

    /**
     * Add padding around the entire content area (top/left/bottom/right).
     */
    public static void addDefaultPadding(JComponent c) {
        if (c == null) return;
        c.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        //      top, left, bottom, right
    }

    /**
     * Custom border with rounded corners.
     */
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g,
                                int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            // Border color (soft gray, you can tweak)
            g2.setColor(new Color(180, 180, 190));

            g2.drawRoundRect(
                    x + 1, y + 1,
                    width - 3, height - 3,
                    radius, radius
            );
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(8, 16, 8, 16);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(8, 16, 8, 16);
            return insets;
        }
    }
}

