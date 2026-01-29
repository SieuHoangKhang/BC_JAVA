package noithat.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Animation helper for smooth UI transitions
 */
public class AnimationHelper {
    
    /**
     * Fade in animation
     */
    public static void fadeIn(final Component component, int durationMs) {
        final Timer timer = new Timer(10, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1f) {
                    opacity = 1f;
                    timer.stop();
                }
                
                if (component instanceof JComponent) {
                    ((JComponent) component).setOpaque(opacity >= 1f);
                }
                component.repaint();
            }
        });
        timer.start();
    }
    
    /**
     * Fade out animation
     */
    public static void fadeOut(final Component component, int durationMs, final Runnable onComplete) {
        final Timer timer = new Timer(10, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 1f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                opacity -= 0.05f;
                if (opacity <= 0f) {
                    opacity = 0f;
                    timer.stop();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                }
                
                component.repaint();
            }
        });
        timer.start();
    }
    
    /**
     * Slide animation
     */
    public static void slideIn(final Component component, final Direction direction, int durationMs) {
        final int steps = durationMs / 10;
        final Point finalPos = component.getLocation();
        
        // Set initial position based on direction
        switch (direction) {
            case LEFT:
                component.setLocation(-component.getWidth(), finalPos.y);
                break;
            case RIGHT:
                component.setLocation(component.getParent().getWidth(), finalPos.y);
                break;
            case UP:
                component.setLocation(finalPos.x, -component.getHeight());
                break;
            case DOWN:
                component.setLocation(finalPos.x, component.getParent().getHeight());
                break;
        }
        
        final Timer timer = new Timer(10, null);
        timer.addActionListener(new ActionListener() {
            int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                float progress = (float) step / steps;
                
                int currentX = (int) (component.getX() + (finalPos.x - component.getX()) * 0.1f);
                int currentY = (int) (component.getY() + (finalPos.y - component.getY()) * 0.1f);
                
                component.setLocation(currentX, currentY);
                
                if (step >= steps || 
                    (Math.abs(currentX - finalPos.x) < 2 && Math.abs(currentY - finalPos.y) < 2)) {
                    component.setLocation(finalPos);
                    timer.stop();
                }
            }
        });
        timer.start();
    }
    
    /**
     * Scale animation (for button click effect)
     */
    public static void scaleEffect(final JComponent component, final float targetScale, int durationMs) {
        final Dimension originalSize = component.getSize();
        final Timer timer = new Timer(10, null);
        
        timer.addActionListener(new ActionListener() {
            float currentScale = 1.0f;
            boolean growing = targetScale > 1.0f;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (growing) {
                    currentScale += 0.02f;
                    if (currentScale >= targetScale) {
                        currentScale = targetScale;
                        growing = false;
                    }
                } else {
                    currentScale -= 0.02f;
                    if (currentScale <= 1.0f) {
                        currentScale = 1.0f;
                        timer.stop();
                    }
                }
                
                int newWidth = (int) (originalSize.width * currentScale);
                int newHeight = (int) (originalSize.height * currentScale);
                component.setSize(newWidth, newHeight);
                component.repaint();
            }
        });
        timer.start();
    }
    
    /**
     * Smooth color transition
     */
    public static void colorTransition(final JComponent component, final Color fromColor, 
                                      final Color toColor, int durationMs) {
        final int steps = durationMs / 10;
        final Timer timer = new Timer(10, null);
        
        timer.addActionListener(new ActionListener() {
            int step = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                step++;
                float progress = (float) step / steps;
                
                int r = (int) (fromColor.getRed() + (toColor.getRed() - fromColor.getRed()) * progress);
                int g = (int) (fromColor.getGreen() + (toColor.getGreen() - fromColor.getGreen()) * progress);
                int b = (int) (fromColor.getBlue() + (toColor.getBlue() - fromColor.getBlue()) * progress);
                
                component.setBackground(new Color(r, g, b));
                
                if (step >= steps) {
                    component.setBackground(toColor);
                    timer.stop();
                }
            }
        });
        timer.start();
    }
    
    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
