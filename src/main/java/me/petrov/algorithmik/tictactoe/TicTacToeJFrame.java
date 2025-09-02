/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.petrov.algorithmik.tictactoe;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *
 * @author Samuil Petrov
 */
public class TicTacToeJFrame extends javax.swing.JFrame {
    
    /**
     * Represents the players of the game.
     * Provides useful methods to standardize the game's flow.
     */
    private enum Player {
        /** The player that comes first and uses X */
        FIRST('X', GameState.FIRST_WIN),
        /** The player that comes second and uses O */
        SECOND('O', GameState.SECOND_WIN);
        
        /** The symbol the Player draws on the board */
        private final char symbol;
        /** The game state that represent the victory of the Player */
        private final GameState winState;
        /** The games the Player has won */
        private int wins = 0;
        
        Player(char symbol, GameState winState) {
            this.symbol = symbol;
            this.winState = winState;
        }
        
        /**
         * Returns the Player that would be on next turn relative to this Player.
         */
        public Player nextPlayer() {
            return switch(this) {
                case FIRST -> SECOND;
                case SECOND -> FIRST;
            };
        }
        
        /**
         * Returns the char each Player writes on the board.
         */
        public char getSymbol() {
            return this.symbol;
        }
        
        /**
         * Returns the GameState representing the victory of the Player.
         */
        public GameState getWinState() {
            return this.winState;
        }
        
        /**
         * Returns the current win count of the player.
         */
        public int getWins() {
            return this.wins;
        }
        
        /**
         * Adds 1 to the current win count of the player.
         */
        public void incrementWins() {
            this.wins += 1;
        }
    }
    
    /**
     * Represents each phase of the game for a consistent game flow.
     */
    private enum GameState {
        /**
         * The GameState only set on initialization of the global gameState.
         * This is more of a placeholder so that the attribute doesn't have to be null.
         * Therefore, you should not use it after the constructor.
         */
        PENDING,
        /** Represents an unfinished game. */
        RUNNING,
        /** Represents a draw where no line is connect and no fields are free. */
        STALEMATE,
        /** Represents the victory of the first player. */
        FIRST_WIN,
        /** Represents the victory of the second player. */
        SECOND_WIN;
        
        /**
         * A GameState is over when it is not a running game.
         * Useful as this Enum replaces the gameOver Boolean.
         * @return Whether the game is running or not
         */
        public boolean isOver() {
            return switch(this) {
                case RUNNING -> false;
                default -> true;
            };
        }
        
        /**
         * Provides a message that describes the current game state.
         * Useful for declaring the outcome.
         */
        public String getMessage() {
            return switch(this) {
                case PENDING -> "Game has not been started";
                case RUNNING -> "Game is still running";
                case STALEMATE -> "It's a draw";
                case FIRST_WIN -> Player.FIRST.getSymbol() + " wins!";
                case SECOND_WIN -> Player.SECOND.getSymbol() + " wins!";
            };
        }
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TicTacToeJFrame.class.getName());
    private Player currentPlayer = Player.FIRST;
    private GameState gameState = GameState.PENDING;
    /** A matrix of fields representing the board */
    private final JButton[][] squares;
    /** A matrix representing the board with rows and columns switched */
    private final JButton[][] transposedSquares;
    /** The top-left to bottom-right diagonal fields */
    private final JButton[] diagTLBRSquares;
    /** The top-right to bottom-left diagonal fields */
    private final JButton[] diagTRBLSquares;

    /**
     * Creates new form TicTacToeJFrame
     */
    public TicTacToeJFrame() {
        initComponents();
        
        this.squares = new JButton[][] {
            { squareA1, squareB1, squareC1 },
            { squareA2, squareB2, squareC2 },
            { squareA3, squareB3, squareC3 },
        };
        this.transposedSquares = transposeSquares(squares);
        this.diagTLBRSquares = getTopLeftToBottomRightSquares(squares);
        this.diagTRBLSquares = getTopRightToBottomLeftSquares(squares);
        
        // configure UI
        installKeyboardControls();
        
        this.setGameState(GameState.RUNNING);
    }
    
    /**
     * Switches the rows and columns of an board.
     * @param squares The input to transpose
     * @return 2D array with rows and columns switched from input
     */
    private JButton[][] transposeSquares(JButton[][] squares) {
        // TODO(Samuil): This assumes that the array is not null or empty
        int rows = squares.length;
        int cols = squares[0].length;
        // TODO(Samuil): This assumes the 2D array is not jagged
        JButton[][] result = new JButton[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = squares[i][j];
            }
        }
        return result;
    }
    
    /**
     * Creates an array of squares
     * that run diagonally from top left to bottom right on the given board.
     * @param squares The input matrix
     * @return The JButtons on the diagonal
     */
    private JButton[] getTopLeftToBottomRightSquares(JButton[][] squares) {
        // TODO(Samuil): This assumes that the array is not null
        JButton[] result = new JButton[squares.length];
        for (int i = 0; i < squares.length; i++) {
            // do not allow jagged 2D arrays for diag check
            if (squares[i].length != squares.length) {
                return null;
            }
            result[i] = squares[i][i];
        }
        return result;
    }
    
    /**
     * Creates an array of squares
     * that run diagonally from top right to bottom left on the given board.
     * @param squares The input matrix
     * @return The JButtons on the diagonal
     */
    private JButton[] getTopRightToBottomLeftSquares(JButton[][] squares) {
        // TODO(Samuil): This assumes that the array is not null
        JButton[] result = new JButton[squares.length];
        for (int i = 0; i < squares.length; i++) {
            // do not allow jagged 2D arrays for diag check
            if (squares[i].length != squares.length) {
                return null;
            }
            result[i] = squares[i][squares[i].length - i - 1];
        }
        return result;
    }
    
    /**
     * Sets the #gameState variable and manipulates the game accordingly.
     * @param gameState The new GameState
     */
    private void setGameState(GameState gameState) {
        // prevent NPE
        if (gameState == null) {
            return;
        }
        // makes sure you can't end the game again while its ended and the other way around
        if (this.gameState.isOver() == gameState.isOver()) {
            return;
        }
        
        this.gameState = gameState;
        
        // execute action according to new GameState
        switch (gameState) {
            case GameState.RUNNING -> {
                startNewGame();
                return;
            }
            case GameState.FIRST_WIN -> Player.FIRST.incrementWins();
            case GameState.SECOND_WIN -> Player.SECOND.incrementWins();
        }
        endGame(gameState.getMessage());
    }
    
    /**
     * Resets the board and prepares the game for playing.
     */
    private void startNewGame() {
        // reset fields
        for (JButton[] squareRow : this.squares) {
            for (JButton square : squareRow) {
                square.setEnabled(true);
                square.setText("");
            }
        }
        
        // reset game state
        this.currentPlayer = Player.FIRST;
    }
    
    /**
     * Discloses game over in pop-up and restarts game after acknowledgment.
     * @param message The message to print in the Dialog
     */
    private void endGame(String message) {
        // disable all buttons to prevent modification after ending
        for (JButton[] squareRow : this.squares) {
            for (JButton square : squareRow) {
                square.setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(rootPane, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
        // state is only set after dialog is closed
        setGameState(GameState.RUNNING);
    }
    
    /**
     * Selects the square in the UI while also progressing the game.
     * @param square The square to block and draw the players symbol on
     */
    private void selectSquare(JButton square) {
        if (!square.getText().isBlank()) {
            return;
        }
        
        // select button
        square.setText(String.valueOf(currentPlayer.getSymbol()));
        
        // switch player
        currentPlayer = currentPlayer.nextPlayer();
        
        // check if win condition met
        setGameState(checkGameState());
    }
    
    /**
     * Checks the current state of the Tic Tac Toe game.
     * @return The current GameState
     */
    private GameState checkGameState() {
        Player winner;
        
        // top-left to bottom-right check
        winner = checkLine(diagTLBRSquares);
        if (winner != null) {
            return winner.getWinState();
        }
        
        // top-right to bottom-left check
        winner = checkLine(diagTRBLSquares);
        if (winner != null) {
            return winner.getWinState();
        }
        
        // horizontal check
        winner = checkLines(squares);
        if (winner != null) {
            return winner.getWinState();
        }
        
        // vertical check
        winner = checkLines(transposedSquares);
        if (winner != null) {
            return winner.getWinState();
        }
        
        // check stalemate
        if (checkStalemate(squares)) {
            return GameState.STALEMATE;
        }
        
        return GameState.RUNNING;
    }
    
    /**
     * Checks the rows of a 2D array for a full line by one player.
     * @param squares The matrix to scan
     * @return Either a Player or null if no continuous line was detected
     */
    private Player checkLines(JButton[][] squares) {
        for (JButton[] line : squares) {
            Player winner = checkLine(line);
            if (winner != null) {
                return winner;
            }
        }
        
        return null;
    }
    
    /**
     * Checks if all buttons are marked by the same player.
     * @param squares The line to check
     * @return Either a Player or null if no continuous line was detected
     */
    private Player checkLine(JButton[] squares) {
        // catch if diag lines were disabled
        if (squares == null || squares.length == 0) {
            return null;
        }
        
        for (Player player : Player.values()) {
            boolean win = true;
            for (JButton square : squares) {
                if (!square.getText().equals(String.valueOf(player.getSymbol()))) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return player;
            }
        }
        
        // if no-one occupies the line, null is returned
        return null;
    }
    
    /**
     * Checks whether all fields are occupied.
     * @param squares The matrix to scan
     * @return True if it's a draw
     */
    private boolean checkStalemate(JButton[][] squares) {
        boolean stalemate = true;
        for (JButton[] fieldRow : squares) {
            for (JButton field : fieldRow ) {
                if (field.getText().isBlank()) {
                    stalemate = false;
                    break;
                }
            }
        }
        return stalemate;
    }
    
    /**
     * Creates bindings for moving between the buttons with the arrow keys
     * and selecting a square with the enter key.
     */
    private void installKeyboardControls() {
        int rows = squares.length;
        int cols = squares[0].length;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton square = squares[r][c];
                
                InputMap im = square.getInputMap(JComponent.WHEN_FOCUSED);
                ActionMap am = square.getActionMap();
                
                // arrow keys move focus
                im.put(KeyStroke.getKeyStroke("UP"), "moveUp");
                im.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
                im.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
                im.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
                // enter performs click
                im.put(KeyStroke.getKeyStroke("ENTER"), "click");
                
                // arrow keys move focus
                am.put("moveUp", moveAction(r - 1, c));
                am.put("moveDown", moveAction(r + 1, c));
                am.put("moveLeft", moveAction(r, c - 1));
                am.put("moveRight", moveAction(r, c + 1));
                // enter performs click
                am.put("click", new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        square.doClick();
                    }
                });
            }
        }
    }
    
    /**
     * Creates a javax.swing.AbstractAction
     * that focuses the button in squares at the indices given.
     * @param nr The new row
     * @param nc The new column
     * @return Action that focuses button at given position
     */
    private Action moveAction(int nr, int nc) {
        final int rows = squares.length;
        final int cols = squares[0].length;
        
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    squares[nr][nc].requestFocus();
                }
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        squaresPanel = new javax.swing.JPanel();
        squareA1 = new javax.swing.JButton();
        squareA2 = new javax.swing.JButton();
        squareA3 = new javax.swing.JButton();
        squareB1 = new javax.swing.JButton();
        squareB2 = new javax.swing.JButton();
        squareB3 = new javax.swing.JButton();
        squareC1 = new javax.swing.JButton();
        squareC2 = new javax.swing.JButton();
        squareC3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setAlwaysOnTop(true);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Tic Tac Toe");
        titleLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        squareA1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareA1ActionPerformed(evt);
            }
        });

        squareA2.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareA2ActionPerformed(evt);
            }
        });

        squareA3.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareA3ActionPerformed(evt);
            }
        });

        squareB1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareB1ActionPerformed(evt);
            }
        });

        squareB2.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareB2ActionPerformed(evt);
            }
        });

        squareB3.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareB3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareB3ActionPerformed(evt);
            }
        });

        squareC1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareC1ActionPerformed(evt);
            }
        });

        squareC2.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareC2ActionPerformed(evt);
            }
        });

        squareC3.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        squareC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                squareC3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout squaresPanelLayout = new javax.swing.GroupLayout(squaresPanel);
        squaresPanel.setLayout(squaresPanelLayout);
        squaresPanelLayout.setHorizontalGroup(
            squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 249, Short.MAX_VALUE)
            .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(squaresPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(squaresPanelLayout.createSequentialGroup()
                            .addComponent(squareA3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareB3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareC3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(squaresPanelLayout.createSequentialGroup()
                            .addComponent(squareA1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareB1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareC1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(squaresPanelLayout.createSequentialGroup()
                            .addComponent(squareA2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareB2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(squareC2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        squaresPanelLayout.setVerticalGroup(
            squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 198, Short.MAX_VALUE)
            .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(squaresPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(squareA1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareB1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareC1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(squareA2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareB2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareC2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(squaresPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(squareA3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareB3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(squareC3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(squaresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(squaresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void squareA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareA1ActionPerformed
        selectSquare(squareA1);
    }//GEN-LAST:event_squareA1ActionPerformed

    private void squareA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareA2ActionPerformed
        selectSquare(squareA2);
    }//GEN-LAST:event_squareA2ActionPerformed

    private void squareA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareA3ActionPerformed
        selectSquare(squareA3);
    }//GEN-LAST:event_squareA3ActionPerformed

    private void squareB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareB1ActionPerformed
        selectSquare(squareB1);
    }//GEN-LAST:event_squareB1ActionPerformed

    private void squareB2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareB2ActionPerformed
        selectSquare(squareB2);
    }//GEN-LAST:event_squareB2ActionPerformed

    private void squareB3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareB3ActionPerformed
        selectSquare(squareB3);
    }//GEN-LAST:event_squareB3ActionPerformed

    private void squareC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareC1ActionPerformed
        selectSquare(squareC1);
    }//GEN-LAST:event_squareC1ActionPerformed

    private void squareC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareC2ActionPerformed
        selectSquare(squareC2);
    }//GEN-LAST:event_squareC2ActionPerformed

    private void squareC3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_squareC3ActionPerformed
        selectSquare(squareC3);
    }//GEN-LAST:event_squareC3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new TicTacToeJFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton squareA1;
    private javax.swing.JButton squareA2;
    private javax.swing.JButton squareA3;
    private javax.swing.JButton squareB1;
    private javax.swing.JButton squareB2;
    private javax.swing.JButton squareB3;
    private javax.swing.JButton squareC1;
    private javax.swing.JButton squareC2;
    private javax.swing.JButton squareC3;
    private javax.swing.JPanel squaresPanel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
