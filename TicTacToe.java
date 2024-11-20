
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe {
    private JFrame frame; // Main game window
    private JButton[][] buttons; // Game buttons
    private char[][] board; // Backend board representation
    private char currentPlayer; // Current player (X or O)
    private final int SIZE = 3; // Board size (3x3)

    public TicTacToe() {
        frame = new JFrame("Tic Tac Toe");
        buttons = new JButton[SIZE][SIZE];
        board = new char[SIZE][SIZE];
        currentPlayer = 'X';

        // Initialize the GUI
        initializeGUI();
    }

    private void initializeGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new GridLayout(SIZE, SIZE));

        // Initialize board and buttons
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                buttons[i][j].setFocusPainted(false);

                // Button click event
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> handleMove(row, col));
                frame.add(buttons[i][j]);

                board[i][j] = ' '; // Initialize backend board
            }
        }

        frame.setVisible(true);
    }

    private void handleMove(int row, int col) {
        if (board[row][col] == ' ' && !isGameOver()) {
            board[row][col] = currentPlayer;
            buttons[row][col].setText(String.valueOf(currentPlayer));
            if (checkWinner(row, col)) {
                JOptionPane.showMessageDialog(frame, "Player " + currentPlayer + " wins!");
                resetGame();
            } else if (isDraw()) {
                JOptionPane.showMessageDialog(frame, "It's a draw!");
                resetGame();
            } else {
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
            }
        }
    }

    private boolean checkWinner(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer && board[row][1] == currentPlayer && board[row][2] == currentPlayer)
            return true;
        // Check column
        if (board[0][col] == currentPlayer && board[1][col] == currentPlayer && board[2][col] == currentPlayer)
            return true;
        // Check diagonals
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)
            return true;

        return false;
    }

    private boolean isDraw() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGameOver() {
        return checkWinner(0, 0) || isDraw();
    }

    private void resetGame() {
        currentPlayer = 'X';
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';
                buttons[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeDSA::new);
    }
}
