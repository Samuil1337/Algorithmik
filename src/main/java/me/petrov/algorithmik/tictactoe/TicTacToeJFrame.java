/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.petrov.algorithmik.tictactoe;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author Samuil Petrov
 */
public class TicTacToeJFrame extends javax.swing.JFrame {
    
    private enum Player {
        FIRST, SECOND;
        
        public char getSymbol() {
            return switch(this) {
                case FIRST -> 'X';
                case SECOND -> 'O';
            };
        }
        
        public GameState getWinState() {
            return switch(this) {
                case FIRST -> GameState.FIRST_WIN;
                case SECOND -> GameState.SECOND_WIN;
            };
        }

        public Player nextPlayer() {
            return switch(this) {
                case FIRST -> SECOND;
                case SECOND -> FIRST;
            };
        }
    }
    
    private enum GameState {
        PENDING, RUNNING, STALEMATE, FIRST_WIN, SECOND_WIN;
        
        public boolean isOver() {
            return switch(this) {
                case RUNNING -> false;
                default -> true;
            };
        }
        
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
    private final JButton[][] squares;
    private final JButton[][] transposedSquares;
    private final JButton[] diagTLBRSquares;
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
        
        this.setGameState(GameState.RUNNING);
    }
    
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
        
        switch (gameState) {
            case GameState.RUNNING -> startNewGame();
            default -> endGame(gameState.getMessage());
        }
    }
    
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
    
    private void selectSquare(JButton square) {
        // select button
        square.setEnabled(false);
        square.setText(String.valueOf(currentPlayer.getSymbol()));
        
        // switch player
        currentPlayer = currentPlayer.nextPlayer();
        
        // check if win condition met
        setGameState(checkGameState());
    }
    
    private GameState checkGameState() {
        Player winner;
        GameState state;
        
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
        state = checkLines(squares);
        if (state != null) {
            return state;
        }
        
        // vertical check
        state = checkLines(transposedSquares);
        if (state != null) {
            return state;
        }
        
        // check stalemate
        if (checkStalemate(squares)) {
            return GameState.STALEMATE;
        }
        
        return GameState.RUNNING;
    }
    
    private GameState checkLines(JButton[][] squares) {
        for (JButton[] line : squares) {
            Player winner = checkLine(line);
            if (winner != null) {
                return winner.getWinState();
            }
        }
        
        return null;
    }
    
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(squareA3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareB3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareC3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(squareA1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareB1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareC1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(squareA2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareB2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(squareC2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(squareA1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareB1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareC1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(squareA2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareB2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareC2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(squareA3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareB3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(squareC3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
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
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
