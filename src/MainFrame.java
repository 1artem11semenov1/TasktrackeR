import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class MainFrame extends JFrame {
    private JButton createButton = new JButton("Добавить задачу");

    MainFrame(String title) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 500);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        display();
        /*ImageIcon pict = new ImageIcon("src/1.jpg");
        Border border = BorderFactory.createLineBorder(Color.BLACK,3);

        JLabel label = new JLabel();
        label.setText("выйди на улицу, потрогай траву");
        label.setIcon(pict);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.TOP);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.GREEN);
        label.setOpaque(true); //display bg color
        label.setBorder(border);
        label.setVerticalAlignment(JLabel.TOP);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBounds(52,169,200,130);*/
        //this.setLayout(null);
        //this.add(label);
    }

    private void display(){
        this.add(createButton);
        createButton.addActionListener(new TaskCreateListener());
        this.setVisible(true);
    }

    class TaskCreateListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            NewTaskFrame createWindow = new NewTaskFrame();
        }
    }
}
