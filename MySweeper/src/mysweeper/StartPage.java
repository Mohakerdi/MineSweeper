package mysweeper;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import javax.swing.*;

public class StartPage implements ActionListener {

    JFrame frame = new JFrame("Mine Sweeper Game");
    JButton easyBtn = new JButton("Easy");
    JButton hardBtn = new JButton("Hard");
    JButton extreamBtn = new JButton("Extream");
    JButton customBtn = new JButton("Custom");
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    };
    Image backgroundImage = new ImageIcon("src\\mysweeper\\BG.png").getImage();

    public StartPage() {
        
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setIconImage(new ImageIcon("src/mysweeper/Icon.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        easyBtn.setBounds(185, 100, 140, 50);
        easyBtn.setFont(new Font("Arial", Font.BOLD, 25));
        easyBtn.setBackground(Color.LIGHT_GRAY);
        easyBtn.addActionListener(this);

        hardBtn.setBounds(185, 200, 140, 50);
        hardBtn.setFont(new Font("Arial", Font.BOLD, 25));
        hardBtn.setBackground(Color.LIGHT_GRAY);
        hardBtn.addActionListener(this);

        extreamBtn.setBounds(185, 300, 140, 50);
        extreamBtn.setFont(new Font("Arial", Font.BOLD, 25));
        extreamBtn.setBackground(Color.LIGHT_GRAY);
        extreamBtn.addActionListener(this);

        customBtn.setBounds(185, 400, 140, 50);
        customBtn.setFont(new Font("Arial", Font.BOLD, 25));
        customBtn.setBackground(Color.LIGHT_GRAY);
        customBtn.addActionListener(this);

        panel.setBounds(0, 0, 500, 600);
        panel.setLayout(null);

        panel.add(easyBtn);
        panel.add(hardBtn);
        panel.add(extreamBtn);
        panel.add(customBtn);
        frame.add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == easyBtn) {
            SoundEff.playSound("/mysweeper/click.wav");
            frame.dispose();
            new MineSweeper(8, 8);
        }
        if (e.getSource() == hardBtn) {
            SoundEff.playSound("/mysweeper/click.wav");
            frame.dispose();
            new MineSweeper(10, 20);
        }
        if (e.getSource() == extreamBtn) {
            SoundEff.playSound("/mysweeper/click.wav");
            frame.dispose();
            new MineSweeper(15, 50);
        }
        if (e.getSource() == customBtn) {
            SoundEff.playSound("/mysweeper/click.wav");
            frame.dispose();
            try {
                int col = Integer.parseInt(JOptionPane.showInputDialog("Enter Board Size N x N: ", 1));
                int bmb = Integer.parseInt(JOptionPane.showInputDialog("Enter Bombs Number: ", 1));
                new MineSweeper(col, bmb);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Wrong Input!!!", "Error Message", JOptionPane.ERROR_MESSAGE);
                new StartPage();
            }
        }
    }

}
