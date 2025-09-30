package ui;

import model.Leaderboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A custom panel that displays leaderboard information as a podium visualization.
 */
public class LeaderboardPodiumPanel extends JPanel {
    private Leaderboard leaderboardMain;

    private final Color firstPlaceColour = new Color(255, 215, 0);    // Gold
    private final Color secondPlaceColour = new Color(192, 192, 192); // Silver
    private final Color thirdPlaceColour = new Color(205, 127, 50);   // Bronze
    private final Color textColour = new Color(0, 0, 0);              // Black
    
    // EFFECTS: Constructs a new LeaderboardPodiumPanel
    public LeaderboardPodiumPanel(Leaderboard leaderboard) {
        this.leaderboardMain = leaderboard;
        setPreferredSize(new Dimension(500, 300));
    }
    
    // EFFECTS: updates the leaderboard
    public void updateLeaderboard(Leaderboard leaderboard) {
        this.leaderboardMain = leaderboard;
        repaint();
    }
    
    // EFFECTS: Overrides the paintComponent method to render the leaderboard podium
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawPodium(g2d, this.leaderboardMain);
    }
        
    // EFFECTS: Draws the podium with the top three difficulty levels
    private void drawPodium(Graphics2D g2d, Leaderboard leaderboard) {
        int width = getWidth();
        int height = getHeight();

        drawTitle(g2d, width);
        drawPodiumBlocks(g2d, width, height);
        drawScoreLabels(g2d, leaderboard, width, height);
    }

    // EFFECTS: Draws the leaderboard title
    private void drawTitle(Graphics2D g2d, int width) {
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(textColour);
        String title = "Leaderboard Podium";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (width - titleWidth) / 2, 30);
    }

    // EFFECTS: Draws the podium blocks
    private void drawPodiumBlocks(Graphics2D g2d, int width, int height) {
        int blockWidth = width / 5;
        int maxBlockHeight = height / 2;

        int firstPlaceX = width / 2 - blockWidth / 2;
        int secondPlaceX = firstPlaceX - blockWidth - 10;
        int thirdPlaceX = firstPlaceX + blockWidth + 10;

        int firstPlaceHeight = maxBlockHeight;
        int secondPlaceHeight = (int) (maxBlockHeight * 0.8);
        int thirdPlaceHeight = (int) (maxBlockHeight * 0.6);

        int firstPlaceY = height - firstPlaceHeight - 20;
        int secondPlaceY = height - secondPlaceHeight - 20;
        int thirdPlaceY = height - thirdPlaceHeight - 20;

        drawPodiumBlock(g2d, firstPlaceX, firstPlaceY, blockWidth, firstPlaceHeight, firstPlaceColour, "1");
        drawPodiumBlock(g2d, secondPlaceX, secondPlaceY, blockWidth, secondPlaceHeight, secondPlaceColour, "2");
        drawPodiumBlock(g2d, thirdPlaceX, thirdPlaceY, blockWidth, thirdPlaceHeight, thirdPlaceColour, "3");
    }

    // EFFECTS: Draws an individual podium block
    private void drawPodiumBlock(Graphics2D g2d, int x, int y, int width, int height, Color color, String position) {
        g2d.setColor(color);
        g2d.fillRect(x, y, width, height);
        g2d.setColor(textColour);
        g2d.drawRect(x, y, width, height);
        drawCenteredNumber(g2d, position, x, y, width, 20);
    }

    // EFFECTS: Draws the score labels for each podium block
    @SuppressWarnings("methodlength")
    private void drawScoreLabels(Graphics2D g2d, Leaderboard leaderboard, int width, int height) {
        int blockWidth = width / 5;
        int maxBlockHeight = height / 2;
        int firstPlaceX = width / 2 - blockWidth / 2;
        int secondPlaceX = firstPlaceX - blockWidth - 10;
        int thirdPlaceX = firstPlaceX + blockWidth + 10;

        int firstPlaceHeight = maxBlockHeight;
        int secondPlaceHeight = (int) (maxBlockHeight * 0.8);
        int thirdPlaceHeight = (int) (maxBlockHeight * 0.6);

        int firstPlaceY = height - firstPlaceHeight - 20;
        int secondPlaceY = height - secondPlaceHeight - 20;
        int thirdPlaceY = height - thirdPlaceHeight - 20;

        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        ArrayList<String> scores = leaderboard.sortedList();

        if (scores.size() >= 1) {
            String first = scores.get(0);
            drawCenteredText(g2d, 
                    first + ": " + leaderboard.getHighScore(first), 
                    firstPlaceX, firstPlaceY - 25, blockWidth);
        }
        if (scores.size() >= 2) {
            String second = scores.get(1);
            drawCenteredText(g2d, 
                    second + ": " + leaderboard.getHighScore(second), 
                    secondPlaceX, secondPlaceY - 25, blockWidth);
        }
        if (scores.size() >= 3) {
            String third = scores.get(2);
            drawCenteredText(g2d, 
                    third + ": " + leaderboard.getHighScore(third), 
                    thirdPlaceX, thirdPlaceY - 25, blockWidth);
        }
    }

    
    // EFFECTS: Draws a centered number on the podium block
    private void drawCenteredNumber(Graphics2D g2d, String number, int x, int y, int width, int height) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(number);
        int textHeight = fm.getHeight();
        g2d.drawString(number, x + (width - textWidth) / 2, y + height - (height - textHeight) / 2);
    }
    
    // EFFECTS: Draws centered text above the podium block
    private void drawCenteredText(Graphics2D g2d, String text, int x, int y, int width) {
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        g2d.drawString(text, x + (width - textWidth) / 2, y);
    }
}