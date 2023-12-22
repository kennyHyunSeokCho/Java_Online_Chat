package Online_Chat.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PaintFrame {
    JFrame f = new JFrame("Swing Paint Demo");
    public void PaintFrame() {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PaintPanel paintP = new PaintPanel();
        f.add(paintP, BorderLayout.NORTH);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    class PaintPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

        String shapeString = "";
        JButton resetB, penB, eraseB, saveB, openB, backB;
        Point firstPointer = new Point(0, 0);
        Point secondPointer = new Point(0, 0);
        BufferedImage bufferedImage;
        Color pencolor = Color.black;
        Float stroke = (float) 5;
        JComboBox<String> colorComboBox;
        JComboBox<Float> strokeComboBox;

        int width;
        int height;
        int minPointx;
        int minPointy;

        public PaintPanel() {

            colorComboBox = new JComboBox<String>();
            strokeComboBox = new JComboBox<Float>();
            resetB = new JButton("전체지우기");
            penB = new JButton("펜");
            eraseB = new JButton("지우개");
            saveB = new JButton("파일저장");
            backB = new JButton("그림판닫기");

            colorComboBox.setModel(new DefaultComboBoxModel<String>(new String[]{ "검정색", "빨간색", "파란색","초록색","노란색","핑크색","하늘색"}));

            strokeComboBox.setModel(new DefaultComboBoxModel<Float>(
                    new Float[] { (float) 5, (float) 10, (float) 15, (float) 20, (float) 25 }));

            add(colorComboBox);
            add(strokeComboBox);
            add(penB);
            add(eraseB);
            add(resetB);
            add(saveB);
            add(backB);

            Dimension d = getPreferredSize();
            bufferedImage = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
            setImageBackground(bufferedImage); // save 할 때 배경이 default로 black이여서 흰색으로

            resetB.addActionListener(this);
            penB.addActionListener(this);
            eraseB.addActionListener(this);
            backB.addActionListener(this);
            colorComboBox.addActionListener(this);
            strokeComboBox.addActionListener(this);
            saveB.addActionListener(new SaveL(this, bufferedImage));
            addMouseListener(this);
            addMouseMotionListener(this);

        }

        public void mousePressed(MouseEvent e) {
            firstPointer.setLocation(0, 0);
            secondPointer.setLocation(0, 0);
            firstPointer.setLocation(e.getX(), e.getY());
        }

        public void mouseReleased(MouseEvent e) {


                secondPointer.setLocation(e.getX(), e.getY());
                updatePaint();

        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(backB)){
                f.setVisible(false);
            }else if (e.getSource().getClass().toString().contains("JButton")) {
                shapeString = e.getActionCommand();
                 if (shapeString.equals("전체지우기")) {
                    JOptionPane.showMessageDialog(this, "화면을 클릭하면 전부 지워집니다.");
                }
            }

            else if (e.getSource().equals(colorComboBox)) {
                if (colorComboBox.getSelectedItem().equals("검정색")){
                    pencolor=Color.black;
                }else if (colorComboBox.getSelectedItem().equals("빨간색")){
                    pencolor=Color.red;
                }else if (colorComboBox.getSelectedItem().equals("파란색")){
                    pencolor=Color.blue;
                }else if (colorComboBox.getSelectedItem().equals("초록색")){
                    pencolor=Color.green;
                }else if (colorComboBox.getSelectedItem().equals("노란색")){
                    pencolor=Color.yellow;
                }else if (colorComboBox.getSelectedItem().equals("핑크색")){
                    pencolor=Color.pink;
                }else if (colorComboBox.getSelectedItem().equals("하늘색")){
                    pencolor=Color.cyan;
                }
            }

            else if (e.getSource().equals(strokeComboBox)) {
                stroke = (float) strokeComboBox.getSelectedItem();
            }

        }
        public Dimension getPreferredSize() {
            return new Dimension(900, 700);
        }

        public void updatePaint() {

            width = Math.abs(secondPointer.x - firstPointer.x);
            height = Math.abs(secondPointer.y - firstPointer.y);

            minPointx = Math.min(firstPointer.x, secondPointer.x);
            minPointy = Math.min(firstPointer.y, secondPointer.y);

            Graphics2D g = bufferedImage.createGraphics();

            if (shapeString.equals("펜")) {
                g.setColor(pencolor);
                g.setStroke(new BasicStroke(stroke));
                g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
            } else if (shapeString.equals("지우개")) {
                g.setColor(Color.white);
                g.setStroke(new BasicStroke(stroke));
                g.drawLine(firstPointer.x, firstPointer.y, secondPointer.x, secondPointer.y);
            } else if (shapeString.equals("전체지우기")) {
                setImageBackground(bufferedImage);
                shapeString = "";
            }

            g.dispose();
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(bufferedImage, 0, 0, null);
        }

        public void setImageBackground(BufferedImage bi) {
            this.bufferedImage = bi;
            Graphics2D g = bufferedImage.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, 900, 700);
            g.dispose();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            width = Math.abs(secondPointer.x - firstPointer.x);
            height = Math.abs(secondPointer.y - firstPointer.y);

            minPointx = Math.min(firstPointer.x, secondPointer.x);
            minPointy = Math.min(firstPointer.y, secondPointer.y);

            if (shapeString == "펜" | shapeString == "지우개") {
                if (secondPointer.x != 0 && secondPointer.y != 0) {
                    firstPointer.x = secondPointer.x;
                    firstPointer.y = secondPointer.y;
                }
                secondPointer.setLocation(e.getX(), e.getY());
                updatePaint();
            } 
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {
        }


        class SaveL implements ActionListener {

            PaintPanel rectPanel;
            BufferedImage bufferedImage;
            JFileChooser jFileChooser;

            SaveL(PaintPanel rectPanel, BufferedImage bufferedImage) {
                this.rectPanel = rectPanel;
                this.bufferedImage = bufferedImage;

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("PNG files", "png"));
                int rVal = jFileChooser.showSaveDialog(null);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = jFileChooser.getSelectedFile();
                        String filePath = file.getAbsolutePath();
                        if (!filePath.toLowerCase().endsWith(".png")) {
                            file = new File(filePath + ".png");
                        }
                        ImageIO.write(bufferedImage, "png", file);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (rVal == JFileChooser.CANCEL_OPTION) {
                }

            }
        }
    }
}
