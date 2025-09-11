/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package me.petrov.algorithmik.tictactoe;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
        /** Whether the AI was toggled for this instance */
        private boolean ai = false;
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
        
        public boolean isAI() {
            return this.ai;
        }
        
        public void setAI(boolean ai) {
            this.ai = ai;
            if (this == currentPlayer) {
                
            }
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
            switch(this) {
                case FIRST -> xWinsCounter.setText(String.valueOf(getWins()));
                case SECOND -> oWinsCounter.setText(String.valueOf(getWins()));
            }
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
        PENDING(true),
        /** Represents an unfinished game. */
        RUNNING(false),
        /** Represents a game ended by the reset button. */
        CANCELED(true),
        /** Represents a draw where no line is connect and no fields are free. */
        STALEMATE(true),
        /** Represents the victory of the first player. */
        FIRST_WIN(true),
        /** Represents the victory of the second player. */
        SECOND_WIN(true);
        
        private final boolean gameOver;
        
        GameState(boolean over) {
            this.gameOver = over;
        }
        
        /**
         * A GameState is over when it is not a running game.
         * Useful as this Enum replaces the gameOver Boolean.
         * @return Whether the game is running or not
         */
        public boolean isGameOver() {
            return this.gameOver;
        }
        
        /**
         * Provides a message that describes the current game state.
         * Useful for declaring the outcome.
         */
        public String getMessage() {
            return switch(this) {
                case PENDING -> "Game has not been started";
                case RUNNING -> "Game is still running";
                case CANCELED -> "Game has been canceled";
                case STALEMATE -> "It's a draw";
                case FIRST_WIN -> Player.FIRST.getSymbol() + " wins!";
                case SECOND_WIN -> Player.SECOND.getSymbol() + " wins!";
            };
        }
    }
    
    /**
     * Represents the intelligence of the AI.
     */
    private enum Difficulty {
        EASY(0, "Easy", "The AI plays randomly"),
        MEDIUM(1, "Medium", "The AI plays randomly and takes/prevents immediate win"),
        HARD(2, "Hard", "The AI generates the optimal plays");
        
        /** The description used as a tool tip in the slider */
        private final int index;
        private final String name;
        private final String description;

        Difficulty(int index, String name, String description) {
            this.index = index;
            this.name = name;
            this.description = description;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public String getName() {
            return this.name;
        }
        
        public String getDescription() {
            return this.description;
        }
        
    }
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(TicTacToeJFrame.class.getName());
    private static Player currentPlayer = Player.FIRST;
    private static GameState gameState = GameState.PENDING;
    private static Difficulty aiDifficulty = Difficulty.EASY;
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
        installAISliderText();
        
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
    private void setGameState(GameState newState) {
        // prevent NPE
        if (newState == null) {
            return;
        }
        
        if (gameState == GameState.RUNNING && newState == GameState.RUNNING) {
            setCurrentPlayer(currentPlayer.nextPlayer());
            return;
        }
        
        // makes sure you can't end the game again while its ended and the other way around
        if (gameState.isGameOver() == newState.isGameOver()) {
            return;
        }
        
        gameState = newState;
        
        // execute action according to new GameState
        switch (newState) {
            case RUNNING -> {
                startNewGame();
                return;
            }
            case FIRST_WIN -> Player.FIRST.incrementWins();
            case SECOND_WIN -> Player.SECOND.incrementWins();
        }
        endGame(newState.getMessage());
    }
    
    /**
     * Resets the board and prepares the game for playing.
     */
    private void startNewGame() {
        // reset fields
        freeFields();
        for (JButton[] squareRow : this.squares) {
            for (JButton square : squareRow) {
                square.setText("");
            }
        }
        
        // reset game state
        setCurrentPlayer(Player.FIRST);
    }
    
    private void setCurrentPlayer(Player newCurrentPlayer) {
        currentPlayer = newCurrentPlayer;
        
        if(!newCurrentPlayer.isAI()) {
            return;
        }
        
        blockFields();
        
        JButton aiAction = generateAIAction(); 
        aiAction.setEnabled(true);
        aiAction.doClick();
        
        freeFields();
    }
    
    private void blockFields() {
        for (JButton[] squareRow : this.squares) {
            for (JButton square : squareRow) {
                square.setEnabled(false);
            }
        }
    }
    
    private void freeFields() {
        for (JButton[] squareRow : this.squares) {
            for (JButton square : squareRow) {
                square.setEnabled(true);
            }
        }
    }
    
    /**
     * Discloses game over in pop-up and restarts game after acknowledgment.
     * @param message The message to print in the Dialog
     */
    private void endGame(String message) {
        // disable all buttons to prevent modification after ending
        blockFields();
        JOptionPane.showMessageDialog(rootPane, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
        // state is only set after dialog is closed
        setGameState(GameState.RUNNING);
    }
    
    private JButton generateAIAction() {
        return switch(aiDifficulty) {
            case EASY -> easyAIAction();
            case MEDIUM -> mediumAIAction();
            case HARD -> hardAIAction();
        };
    }
    
    private JButton easyAIAction() {
        JButton[] clickableButtons = Arrays.stream(squares)
                .flatMap(Arrays::stream)
                .filter(b -> b.getText().isEmpty())
                .toArray(JButton[]::new);
        
        int randomIndex = ThreadLocalRandom.current().nextInt(clickableButtons.length);
        return clickableButtons[randomIndex];
    }
    
    private JButton mediumAIAction() {
        return easyAIAction();
    }
    
    private JButton hardAIAction() {
        return easyAIAction();
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
                    squares[nr][nc].requestFocusInWindow();
                }
            }
        };
    }
    
    private void installAISliderText() {
        aiDifficultySlider.setMajorTickSpacing(1);
        
        aiDifficultySlider.setValue(aiDifficulty.getIndex());
        
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        for (Difficulty difficulty : Difficulty.values()) {
            JLabel label = new JLabel(difficulty.getName());
            label.setFont(new Font("Comic Sans MS", 0, 10));
            labels.put(difficulty.getIndex(), label);
        }
            
        aiDifficultySlider.setLabelTable(labels);
        
        aiDifficultySlider.setToolTipText(Difficulty
                .values()[aiDifficultySlider.getValue()]
                .getDescription()
        );
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
        aiPanel = new javax.swing.JPanel();
        aiToggle = new javax.swing.JCheckBox();
        aiDifficultySlider = new javax.swing.JSlider();
        winsPanel = new javax.swing.JPanel();
        xWinsHint = new javax.swing.JLabel();
        xWinsCounter = new javax.swing.JLabel();
        oWinsHint = new javax.swing.JLabel();
        oWinsCounter = new javax.swing.JLabel();
        resetButton = new javax.swing.JButton();

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

        aiToggle.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        aiToggle.setText("Enable AI");
        aiToggle.setFocusable(false);
        aiToggle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        aiToggle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aiToggleActionPerformed(evt);
            }
        });

        aiDifficultySlider.setMaximum(2);
        aiDifficultySlider.setPaintLabels(true);
        aiDifficultySlider.setPaintTicks(true);
        aiDifficultySlider.setSnapToTicks(true);
        aiDifficultySlider.setToolTipText("AI difficulty");
        aiDifficultySlider.setValue(0);
        aiDifficultySlider.setFocusable(false);
        aiDifficultySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                aiDifficultySliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout aiPanelLayout = new javax.swing.GroupLayout(aiPanel);
        aiPanel.setLayout(aiPanelLayout);
        aiPanelLayout.setHorizontalGroup(
            aiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aiToggle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, aiPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(aiDifficultySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );
        aiPanelLayout.setVerticalGroup(
            aiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aiPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aiToggle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aiDifficultySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                .addContainerGap())
        );

        xWinsHint.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        xWinsHint.setText("X wins:");

        xWinsCounter.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        xWinsCounter.setText("0");

        oWinsHint.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        oWinsHint.setText("O wins:");

        oWinsCounter.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        oWinsCounter.setText("0");

        javax.swing.GroupLayout winsPanelLayout = new javax.swing.GroupLayout(winsPanel);
        winsPanel.setLayout(winsPanelLayout);
        winsPanelLayout.setHorizontalGroup(
            winsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, winsPanelLayout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(xWinsHint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(xWinsCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(oWinsHint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oWinsCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53))
        );
        winsPanelLayout.setVerticalGroup(
            winsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, winsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(winsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oWinsHint, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xWinsHint, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(xWinsCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oWinsCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        resetButton.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        resetButton.setText("Reset");
        resetButton.setFocusable(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .addComponent(resetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(68, 68, 68)
                        .addComponent(squaresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(winsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(aiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(squaresPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(winsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resetButton)
                .addContainerGap())
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

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        setGameState(GameState.CANCELED);
    }//GEN-LAST:event_resetButtonActionPerformed

    private void aiDifficultySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_aiDifficultySliderStateChanged
        aiDifficulty = Difficulty.values()[aiDifficultySlider.getValue()];
        aiDifficultySlider.setToolTipText(aiDifficulty.getDescription());
    }//GEN-LAST:event_aiDifficultySliderStateChanged

    private void aiToggleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aiToggleActionPerformed
        //Player aiPlayer = Player.values()[Player.values().length - 1];
        Player aiPlayer = Player.FIRST;
        aiPlayer.setAI(!aiPlayer.isAI());
        setCurrentPlayer(currentPlayer);
    }//GEN-LAST:event_aiToggleActionPerformed

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
    private javax.swing.JSlider aiDifficultySlider;
    private javax.swing.JPanel aiPanel;
    private javax.swing.JCheckBox aiToggle;
    private static javax.swing.JLabel oWinsCounter;
    private javax.swing.JLabel oWinsHint;
    private javax.swing.JButton resetButton;
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
    private javax.swing.JPanel winsPanel;
    private static javax.swing.JLabel xWinsCounter;
    private javax.swing.JLabel xWinsHint;
    // End of variables declaration//GEN-END:variables
}
