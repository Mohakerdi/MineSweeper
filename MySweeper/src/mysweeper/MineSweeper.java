package mysweeper;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class MineSweeper implements ActionListener {

    private class Cell extends JButton {
        boolean revealed = false;
        int row;
        int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
        public void reveal(){
            this.revealed = true;
        }
        
    }

    int cellSize = 55;
    int cols;
    int rows;
    int boardWidth;
    int boardHeight;
    boolean firstTouch = true;

    JFrame frame = new JFrame("Mine Sweeper Game");
    JLabel txtLbl = new JLabel();
    JLabel timeLbl = new JLabel("time : 0");
    JPanel txtPanel = new JPanel();
    JPanel timePanel = new JPanel();
    JButton backBtn = new JButton("back");
    JButton replayBtn = new JButton("replay");
    JPanel boardPanel = new JPanel();

    int mineCnt;
    int time=0;

    Cell[][] board;

    ArrayList<Cell> flags;
    ArrayList<Cell> mines;

    Random rand = new Random();

    int cellsClicked = 0;
    boolean gameEnd = false;

    public MineSweeper(int cols, int mineCnt) {
        
        frame.setBackground(Color.LIGHT_GRAY);
        
        frame.setIconImage(new ImageIcon("src/mysweeper/Icon.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        
        Timer timer = new Timer(1000, e -> {
            if(gameEnd){
                ((Timer) e.getSource()).stop();
            }
            time++;
            timeLbl.setText("time : "+Integer.toString(time));
        });
        timer.start();
        this.cols = cols;
        this.mineCnt = mineCnt;
        rows = cols;
        board = new Cell[rows][cols];
        boardWidth = cols * cellSize;
        boardHeight = cols * cellSize;

        flags = new ArrayList();

        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        txtLbl.setFont(new Font("Arial", Font.BOLD, 25));
        txtLbl.setHorizontalAlignment(JLabel.CENTER);
        txtLbl.setText("Minesweeper: " + Integer.toString(mineCnt));
        txtLbl.setOpaque(true);
        
        timeLbl.setFont(new Font("Arial", Font.BOLD, 25));
        timeLbl.setHorizontalAlignment(JLabel.CENTER);
        timeLbl.setOpaque(true);

        backBtn.setHorizontalAlignment(JButton.LEFT);
        backBtn.setBackground(Color.lightGray);
        backBtn.setVisible(false);
        backBtn.addActionListener(this);

        replayBtn.setHorizontalAlignment(JButton.RIGHT);
        replayBtn.setBackground(Color.lightGray);
        replayBtn.setVisible(false);
        replayBtn.addActionListener(this);

        txtPanel.setLayout(new BorderLayout());
        txtPanel.add(txtLbl, BorderLayout.CENTER);
        txtPanel.add(backBtn, BorderLayout.WEST);
        txtPanel.add(replayBtn, BorderLayout.EAST);
        frame.add(txtPanel, BorderLayout.NORTH);
        
        timePanel.setLayout(new BorderLayout());
        timePanel.add(timeLbl, BorderLayout.CENTER);
        frame.add(timePanel, BorderLayout.SOUTH);

        boardPanel.setLayout(new GridLayout(rows, cols));
        frame.add(boardPanel);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = new Cell(r, c);
                board[r][c] = cell;

                cell.setFocusable(false);
                cell.setMargin(new Insets(0, 0, 0, 0));
                cell.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
//                cell.setText("💣");
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameEnd) {
                            return;
                        }
                        Cell cell = (Cell) e.getSource();
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            SoundEff.playSound("/mysweeper/click.wav");
                            if (cell.getText() == "") {
                                if (mines.contains(cell)) {
                                    if(firstTouch){
                                        firstTouch = false;
                                    }else{gameOver();}
                                } else {
                                    checkMine(cell.row, cell.col);
                                    firstTouch = false;
                                }
                            }
                            else{
                                revealRound(cell.row, cell.col);
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
//                            SoundEff.playSound("/mysweeper/flagSound.wav");
                            if (cell.getText() == "" && cell.isEnabled()) {
                                cell.setText("🚩");
                                flags.add(cell);
                                txtLbl.setText("Minesweeper: " + Integer.toString(mineCnt - flags.size()));
                            } else if (cell.getText() == "🚩") {
                                cell.setText("");
                                flags.remove(cell);
                                txtLbl.setText("Minesweeper: " + Integer.toString(mineCnt - flags.size()));
                            }
                        }
                    }
                });
                boardPanel.add(cell);
            }
        }
        frame.setVisible(true);

        setMines();

    }

    void setMines() {
        mines = new ArrayList<Cell>();

        int mineLeft = mineCnt;
        while (mineLeft > 0) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);

            Cell cell = board[r][c];
            if (!mines.contains(cell)) {
                mines.add(cell);
                mineLeft--;
            }
        }
    }

    void gameOver() {
//        SoundEff.playSound("/mysweeper/bombSound.wav");
        for (int i = 0; i < mines.size(); i++) {
            Cell cell = mines.get(i);
            cell.setText("💣");

        }
        gameEnd = true;
        backBtn.setVisible(true);
        replayBtn.setVisible(true);
        txtLbl.setText("Game Over ! You Lose");
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return;
        }

        Cell cell = board[r][c];
        if (cell.revealed || flags.contains(cell)) {
            return;
        }

        cell.reveal();
        
        cellsClicked++;

        int minesFound = 0;

        minesFound += cntMine(r - 1, c - 1);
        minesFound += cntMine(r - 1, c);
        minesFound += cntMine(r - 1, c + 1);

        minesFound += cntMine(r, c - 1);
        minesFound += cntMine(r, c + 1);

        minesFound += cntMine(r + 1, c - 1);
        minesFound += cntMine(r + 1, c);
        minesFound += cntMine(r + 1, c + 1);
        
        if(minesFound == 0) cell.setBackground(Color.WHITE);
        
        if (minesFound > 0) {
            cell.setBackground(Color.WHITE);
            switch(minesFound){
                case 6:
                    cell.setForeground(Color.YELLOW);break;
                case 5:
                    cell.setForeground(Color.ORANGE);break;
                case 4:
                    cell.setForeground(Color.MAGENTA);break;
                case 3:
                    cell.setForeground(Color.red);break;
                case 2:
                    cell.setForeground(Color.green);break;
                case 1:
                    cell.setForeground(Color.blue);break;
            }
            cell.setText(Integer.toString(minesFound));
        } else {
            cell.setText("");

            //Recurse to neighbours
            checkMine(r - 1, c - 1);
            checkMine(r - 1, c);
            checkMine(r - 1, c + 1);

            checkMine(r, c - 1);
            checkMine(r, c + 1);

            checkMine(r + 1, c - 1);
            checkMine(r + 1, c);
            checkMine(r + 1, c + 1);
        }
        if (cellsClicked == rows * cols - mines.size()) {
            gameEnd = true;
            backBtn.setVisible(true);
            replayBtn.setVisible(true);
            txtLbl.setText("Mines Cleared ! You Win");
        }

    }

    int cntMine(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return 0;
        }
        if (mines.contains(board[r][c])) {
            return 1;
        }
        return 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backBtn) {
            frame.dispose();
            new StartPage();
        }
        if (e.getSource() == replayBtn) {
            frame.dispose();
            new MineSweeper(cols, mineCnt);
        }
    }
    
    
    private void revealRound(int r, int c) {
        revealer(r - 1, c - 1);
        revealer(r - 1, c);
        revealer(r - 1, c + 1);

        revealer(r, c - 1);
        revealer(r, c + 1);

        revealer(r + 1, c - 1);
        revealer(r + 1, c);
        revealer(r + 1, c + 1);
    }
    
    void revealer(int r, int c){
         if (r < 0 || r >= rows || c < 0 || c >= cols) {
            return;
        }

        Cell cell = board[r][c];
        if (cell.revealed || flags.contains(cell)) {
            return;
        }
        cell.reveal();
        if (mines.contains(cell)) {
            gameOver();
        }
        cellsClicked++;
        int minesFound = 0;

        minesFound += cntMine(r - 1, c - 1);
        minesFound += cntMine(r - 1, c);
        minesFound += cntMine(r - 1, c + 1);

        minesFound += cntMine(r, c - 1);
        minesFound += cntMine(r, c + 1);

        minesFound += cntMine(r + 1, c - 1);
        minesFound += cntMine(r + 1, c);
        minesFound += cntMine(r + 1, c + 1);
        
        if(minesFound == 0) cell.setBackground(Color.WHITE);
        
        if (minesFound > 0) {
            cell.setBackground(Color.WHITE);
            switch(minesFound){
                case 6:
                    cell.setForeground(Color.YELLOW);break;
                case 5:
                    cell.setForeground(Color.ORANGE);break;
                case 4:
                    cell.setForeground(Color.MAGENTA);break;
                case 3:
                    cell.setForeground(Color.red);break;
                case 2:
                    cell.setForeground(Color.green);break;
                case 1:
                    cell.setForeground(Color.blue);break;
            }
            cell.setText(Integer.toString(minesFound));
        }
        if (cellsClicked == rows * cols - mines.size()) {
            gameEnd = true;
            backBtn.setVisible(true);
            replayBtn.setVisible(true);
            txtLbl.setText("Mines Cleared ! You Win");
        }
    }
}
