import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

class GraphicPanel extends JPanel{
    //size of the panel
    private int width, heigth;
    //size of each cell
    private int gridWidth, gridHeight;
    //current GridWorld that we are painting at the moment
    private GridWorld world;
    //storing of colors for each type of cell
    private HashMap<String, Color> colors;
    GraphicPanel(int width, int height, GridWorld world){
        this.width = width;
        this.heigth = height;
        repaint(world);
        colors = new HashMap<>();
        colors.put(world.obstacleTree, Color.GREEN);
        colors.put(world.obstacleBuilding, Color.GRAY);
        colors.put(world.freeCell, Color.WHITE);
        colors.put(world.selectedCell, Color.cyan);
        setSize(width, height);
    }
    //mark with red rectangles cells which are not allowed by the rules to be selected
    public void markDisallowed(){
        Graphics g = getGraphics();
        int[][] matrix = world.computing;
        int pointerY = 0;
        g.setColor(Color.RED);
        for(int i = 0; i < matrix.length; i++){
            int pointerX = 0;
            for(int j = 0; j < matrix[i].length; j++){
                if(matrix[i][j]==0)
                    g.fillRect(pointerX+10, pointerY+10, gridWidth-20, gridHeight-20);
                pointerX += gridWidth;
            }
            pointerY += gridHeight;
        }
    }

    public void paintComponent(Graphics g){
        g.clearRect(0, 0, width, heigth);
        //matrix of our marks about each cell
        String[][] matrix = world.matrix;
        //painting each cell
        int pointerY = 0;
        for(int i = 0; i < matrix.length; i++){
            int pointerX = 0;
            for(int j = 0; j < matrix[i].length; j++){
                //get color from our colors table
                g.setColor(colors.get(matrix[i][j]));
                //draw a cell as rectangle area
                g.fillRect(pointerX, pointerY, gridWidth, gridHeight);
                pointerX += gridWidth;
            }
            pointerY += gridHeight;
        }
        //if there are not too many cells paint a grid
        if(world.M < 160 && world.N < 90) {
            pointerY = 0;

            g.setColor(Color.BLACK);
            for (int i = 0; i < matrix.length; i++) {
                int pointerX = 0;
                for (int j = 0; j < matrix[i].length; j++) {
                    g.drawRect(pointerX, pointerY, gridWidth, gridHeight);
                    pointerX += gridWidth;
                }
                pointerY += gridHeight;
            }
        }
    }
    //custom repaint with creating new GridWorld
    public void repaint(GridWorld newWorld){
        world = newWorld;
        gridWidth = width/world.N;
        gridHeight = heigth/world.M;
        repaint();
    }
}

public class Visualize extends JFrame {
    //size of the window
    private int width = 1280, height = 720;
    private JTextField MField, NField, TreeFrequencyField, BuildingFrequencyField, FreeCellFrequencyField;
    private JLabel MLabel, NLabel, TreeFrequencyLabel, BuildingFrequencyLabel, FreeCellFrequencyLabel;
    private JButton generate, solve, mark;
    private GridWorld world;
    private GraphicPanel gp;
    Visualize(){
        world = new GridWorld(9, 16, 10, 10, 100);
        gp = new GraphicPanel(4*width/5, 3*height/5, world);
        gp.setPreferredSize(new Dimension(width, 4*height/5));
        MField = new JTextField(Integer.toString(9));
        NField = new JTextField(Integer.toString(16));
        TreeFrequencyField = new JTextField(Integer.toString(10));
        BuildingFrequencyField = new JTextField(Integer.toString(10));
        FreeCellFrequencyField = new JTextField(Integer.toString(100));
        MField.setPreferredSize(new Dimension(100, 20));
        NField.setPreferredSize(new Dimension(100, 20));
        MLabel = new JLabel("M = ");
        NLabel = new JLabel("N = ");
        TreeFrequencyLabel = new JLabel("Tree frequency = ");
        BuildingFrequencyLabel = new JLabel("Building frequency = ");
        FreeCellFrequencyLabel = new JLabel("Free cell frequency = ");
        generate = new JButton("Generate");
        solve = new JButton("Solve");
        mark = new JButton("Mark Disallowed Cells");

        //create new GridWorld when pressed "generate"
        generate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world = new GridWorld(Integer.parseInt(MField.getText()), Integer.parseInt(NField.getText()),
                        Integer.parseInt(TreeFrequencyField.getText()), Integer.parseInt(BuildingFrequencyField.getText()),
                        Integer.parseInt(FreeCellFrequencyField.getText()));
                gp.repaint(world);
                repaint();
            }
        });
        //paint the answer when pressed "solve"
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world.calculate();
                repaint();
            }
        });
        //mark disallowed cells when "mark" pressed
        mark.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gp.markDisallowed();
            }
        });

        JPanel MN = new JPanel();
        MN.setLayout(new BoxLayout(MN, BoxLayout.Y_AXIS));
        MN.add(MLabel);
        MN.add(MField);
        MN.add(NLabel);
        MN.add(NField);

        JPanel frequencies = new JPanel();
        frequencies.setLayout(new BoxLayout(frequencies, BoxLayout.Y_AXIS));
        frequencies.add(TreeFrequencyLabel);
        frequencies.add(TreeFrequencyField);
        frequencies.add(BuildingFrequencyLabel);
        frequencies.add(BuildingFrequencyField);
        frequencies.add(FreeCellFrequencyLabel);
        frequencies.add(FreeCellFrequencyField);


        JPanel Buttons = new JPanel();
        Buttons.setLayout(new BoxLayout(Buttons, BoxLayout.Y_AXIS));
        Buttons.add(generate);
        Buttons.add(solve);
        Buttons.add(mark);

        JPanel wrapper = new JPanel();
        wrapper.setLayout(new GridLayout(1, 3));
        wrapper.add(MN);
        wrapper.add(MN);
        wrapper.add(frequencies);
        wrapper.add(Buttons);
        wrapper.setPreferredSize(new Dimension(width, height/5));

        JPanel fine = new JPanel();
        fine.setLayout(new BoxLayout(fine, BoxLayout.Y_AXIS));
        fine.add(wrapper);
        fine.add(gp);

        add(fine);
        pack();
        setVisible(true);
        setSize(width, height);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


}
