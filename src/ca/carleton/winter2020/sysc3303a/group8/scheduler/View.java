package ca.carleton.winter2020.sysc3303a.group8.scheduler;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.carleton.winter2020.sysc3303a.group8.utils.Utils;

@SuppressWarnings("serial")
public class View extends JFrame {
    private JPanel panel;
    private JTextField[][] floors = new JTextField[Utils.FLOOR_COUNT][Utils.CAR_COUNT];
    private JTextField[][] directions = new JTextField[2][Utils.CAR_COUNT];

    public View() {
        panel = new JPanel();
        panel.setLayout(new GridLayout(Utils.FLOOR_COUNT + 4, Utils.CAR_COUNT, 20, 0));

        for (int i = 0; i < Utils.CAR_COUNT; i++) {
            panel.add(new JLabel("Car " + (i + 1)));
        }

        for (int j = 0; j < Utils.CAR_COUNT; j++) {
            directions[0][j] = new JTextField("▲");
            directions[1][j] = new JTextField("▼");
            panel.add(directions[0][j]);
            panel.add(directions[1][j]);
            directions[0][j].setEnabled(false);
            directions[1][j].setEnabled(false);
        }

        for (int i = 0; i < Utils.FLOOR_COUNT; i++) {
            for (int j = 0; j < Utils.CAR_COUNT; j++) {
                floors[i][j] = new JTextField("" + (Utils.FLOOR_COUNT - i));
                floors[i][j].setBackground(Color.BLACK);
                panel.add(floors[i][j]);
                floors[i][j].setEnabled(false);
            }
        }

        panel.setVisible(true);
        panel.setBackground(Color.LIGHT_GRAY);
        this.add(panel);

        this.setVisible(true);
        this.setTitle("Elevator");
        this.setSize(400, 750);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public JTextField getFloors(int i, int j) {
        return floors[i][j];
    }

    public void setFloors(JTextField[][] floors) {
        this.floors = floors;
    }

    public JTextField getDirections(int i, int j) {
        return directions[i][j];
    }

    public void setDirections(JTextField[][] directions) {
        this.directions = directions;
    }
}
