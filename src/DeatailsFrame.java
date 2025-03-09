import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeatailsFrame extends JFrame {
    DeatailsFrame(String PKInput, String detailsInput){
        JFrame taskCreator = new JFrame("создание задачи");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(250, 250);
        this.setResizable(false);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);

        String[] detailsOutArray = detailsInput.split("\n");
        String detailsOut = "<html>";
        for (int i = 0; i<detailsOutArray.length; ++i){
            detailsOut += detailsOutArray[i] + "<br>";
        }
        detailsOut+="</html>";

        JLabel details = new JLabel(detailsOut);
        JScrollPane detailsScrl = new JScrollPane(details);
        detailsScrl.setBounds(0,0,235,150);
        details.setBackground(Color.white);
        details.setOpaque(true);
        JButton deleteButton = new JButton("удалить задачу");
        deleteButton.addActionListener(new DeleteTaskListener(PKInput));
        deleteButton.setBounds(0,150,250,75);

        this.add(detailsScrl);
        this.add(deleteButton);
        this.setVisible(true);
    }
    class DeleteTaskListener implements ActionListener {
        String PK;
        DeleteTaskListener(String PKInput){PK = PKInput;}
        public void actionPerformed(ActionEvent e){
            Manager.getInstance().deleteTask(PK);
            MainFrame.getInstance().updateTasks();
            MainFrame.getInstance().updateInfo();
        }
    }
}
