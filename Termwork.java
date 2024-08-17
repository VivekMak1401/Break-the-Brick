import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Termwork extends JPanel implements ActionListener, MouseMotionListener {
    int ballX = 300;
    int ballY = 200;
    int ballSpeedX = 3;
    int ballSpeedY = 4;
    int paddleWidth = 80;
    int score = 0;
    Timer timer;
    boolean[] bricks;
    int brickWidth = 100;
    int brickHeight = 20;
    int numBricks = 50;
    int brickRows = 5;
    int brickCols = 10;

    public Termwork() {
        bricks = new boolean[numBricks];
        for (int i = 0; i < numBricks; i++) {
            bricks[i] = true;
        }
        timer = new Timer(1000 / 60, this);
        timer.start();
        addMouseMotionListener(this);
        setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw score
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics(); 
        int scoreX = (getWidth() - fm.stringWidth("Score: " + score)) / 2; 
        int scoreY = fm.getAscent(); 
        g.drawString("Score: " + score, scoreX, scoreY);

        // Draw paddle
        int paddleX = Math.min(Math.max(mouseX - paddleWidth / 2, 0), getWidth() - paddleWidth); 
        g.setColor(Color.BLACK);
        g.fillRect(paddleX, getHeight() - 20, paddleWidth, 10);

        // Draw ball
        g.setColor(Color.RED);
        g.fillOval(ballX, ballY, 20, 20);

        // Draw bricks
        int brickIndex = 0;
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < brickCols; col++) {
                if (brickIndex < numBricks && bricks[brickIndex]) {
                    if ((row % 2 == 0 && brickIndex % 2 == 0) || (row % 2 != 0 && brickIndex % 2 != 0)) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.PINK);
                    }
                    g.fillRect(col * brickWidth, row * brickHeight + 30, brickWidth, brickHeight);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * brickWidth + 1, row * brickHeight + 31, brickWidth - 2, brickHeight - 2);

                    // Check for collision with ball
                    if (ballX + 20 >= col * brickWidth && ballX <= (col + 1) * brickWidth &&
                            ballY + 20 >= row * brickHeight + 30 && ballY <= (row + 1) * brickHeight + 30) {
                        bricks[brickIndex] = false;
                        score++;
                        ballSpeedY *= -1;
                    }
                }
                brickIndex++;
            }
        }
    }

    int mouseX;

    public void actionPerformed(ActionEvent e) {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // Ball collision with walls
        if (ballX <= 0 || ballX >= getWidth() - 20) {
            ballSpeedX *= -1;
        }
        if (ballY <= 0) {
            ballSpeedY *= -1;
        }

        // Ball collision with paddle
        if (ballY >= getHeight() - 30 && ballX >= mouseX - paddleWidth / 2 && ballX <= mouseX + paddleWidth / 2 && ballY <= getHeight() - 20) {
            ballSpeedY *= -1;
        }

        // Game over condition
        if (ballY >= getHeight()) {
            timer.stop();
            int result = JOptionPane.showConfirmDialog(this, "Game Over!!!! Score: " + score + "\nDo you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        }

        // Check if all bricks are removed
        boolean allBricksRemoved = true;
        for (boolean brick : bricks) {
            if (brick) {
                allBricksRemoved = false;
                break;
            }
        }

        if (allBricksRemoved && score != 0) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Congratulations! You won the game with a score of " + score);
            int result = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Play Again", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                System.exit(0);
            }
        }

        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
    }

    public void mouseDragged(MouseEvent e) {
    }

    private void restartGame() {
        ballX = 300;
        ballY = 200;
        ballSpeedX = 3;
        ballSpeedY = 4;
        score = 0;
        for (int i = 0; i < numBricks; i++) {
            bricks[i] = true;
        }
        timer.restart();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Termwork game = new Termwork();
        frame.getContentPane().add(game);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }
}
