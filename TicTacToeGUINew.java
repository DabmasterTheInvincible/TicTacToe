
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeGUINew extends JFrame {
    private char[][] board = {{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
    private JButton[][] buttons = new JButton[3][3];
    private boolean playerTurn = true; // true for player 'X', false for AI 'O'
    private JLabel statusLabel;

    public TicTacToeGUINew() {
        setTitle("Tic-Tac-Toe");
        setSize(400, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        statusLabel = new JLabel("Your Turn (X)", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        initializeButtons(boardPanel);
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void initializeButtons(JPanel boardPanel) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 60));
                buttons[i][j].setFocusPainted(false);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setOpaque(true);
                buttons[i][j].setBorder(BorderFactory.createLineBorder(Color.BLACK));
                buttons[i][j].addActionListener(new ButtonClickListener(i, j));
                boardPanel.add(buttons[i][j]);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int x, y;

        public ButtonClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (board[x][y] == ' ' && playerTurn) {
                board[x][y] = 'X';
                buttons[x][y].setText("X");
                buttons[x][y].setForeground(Color.BLUE);
                playerTurn = false;
                statusLabel.setText("AI's Turn (O)");
                if (isGameFinished()) {
                    return;
                }
                aiMove();
                if (isGameFinished()) {
                    return;
                }
                playerTurn = true;
                statusLabel.setText("Your Turn (X)");
            }
        }
    }

    private void aiMove() {
        int[] bestMove = minimax(board, 'O');
        board[bestMove[0]][bestMove[1]] = 'O';
        buttons[bestMove[0]][bestMove[1]].setText("O");
        buttons[bestMove[0]][bestMove[1]].setForeground(Color.RED);
    }

    private boolean isGameFinished() {
        if (hasContestantWon(board, 'X')) {
            JOptionPane.showMessageDialog(this, "Player wins!");
            resetBoard();
            return true;
        }
        if (hasContestantWon(board, 'O')) {
            JOptionPane.showMessageDialog(this, "AI wins!");
            resetBoard();
            return true;
        }
        if (isBoardFull(board)) {
            JOptionPane.showMessageDialog(this, "The game ended in a tie!");
            resetBoard();
            return true;
        }
        return false;
    }

    private boolean hasContestantWon(char[][] board, char symbol) {
        if ((board[0][0] == symbol && board[0][1] == symbol && board[0][2] == symbol) ||
            (board[1][0] == symbol && board[1][1] == symbol && board[1][2] == symbol) ||
            (board[2][0] == symbol && board[2][1] == symbol && board[2][2] == symbol) ||
            (board[0][0] == symbol && board[1][0] == symbol && board[2][0] == symbol) ||
            (board[0][1] == symbol && board[1][1] == symbol && board[2][1] == symbol) ||
            (board[0][2] == symbol && board[1][2] == symbol && board[2][2] == symbol) ||
            (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
            (board[2][0] == symbol && board[1][1] == symbol && board[0][2] == symbol)) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull(char[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetBoard() {
        board = new char[][]{{' ', ' ', ' '}, {' ', ' ', ' '}, {' ', ' ', ' '}};
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText(" ");
                buttons[i][j].setForeground(Color.BLACK);
            }
        }
        playerTurn = true;
        statusLabel.setText("Your Turn (X)");
    }

    private int[] minimax(char[][] board, char currentPlayer) {
        char opponent = (currentPlayer == 'O') ? 'X' : 'O';

        int[] bestMove = {-1, -1};
        int bestScore = (currentPlayer == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        if (hasContestantWon(board, 'X')) {
            return new int[]{-1, -1, -10};
        } else if (hasContestantWon(board, 'O')) {
            return new int[]{-1, -1, 10};
        } else if (isBoardFull(board)) {
            return new int[]{-1, -1, 0};
        }

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = currentPlayer;
                    int[] currentMove = minimax(board, opponent);
                    board[i][j] = ' ';
                    currentMove[0] = i;
                    currentMove[1] = j;

                    if (currentPlayer == 'O') {
                        if (currentMove[2] > bestScore) {
                            bestScore = currentMove[2];
                            bestMove = currentMove;
                        }
                    } else {
                        if (currentMove[2] < bestScore) {
                            bestScore = currentMove[2];
                            bestMove = currentMove;
                        }
                    }
                }
            }
        }

        return new int[]{bestMove[0], bestMove[1], bestScore};
    }

    public static void main(String[] args) {
        new TicTacToeGUINew();
    }
}
