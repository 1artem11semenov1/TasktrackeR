import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTaskFrame extends JFrame{
    private JTextField taskNameField = new JTextField("", 10);
    private JTextField taskDescriptionField = new JTextField("", 10);
    private JTextField taskDeadlineField = new JTextField("", 10);
    private JButton taskCreateButton = new JButton("Добавить задачу");
    private String taskName;
    private String taskDescription;

    NewTaskFrame(){
        JFrame taskCreator = new JFrame("создание задачи");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(250, 250);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        taskCreateButton.addActionListener(new isAddListener());

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3,2,2,2));

        container.add(taskNameField);
        container.add(taskDescriptionField);
        container.add(taskDeadlineField);
        container.add(taskCreateButton);

        this.setVisible(true);
    }

    class isAddListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            taskName = taskNameField.getText();
            taskDescription = taskDescriptionField.getText();
            String dateStr = taskDeadlineField.getText();
            DateFormat inputDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date deadline;
            try{
                deadline = inputDateFormat.parse(dateStr);
            } catch (ParseException ex){
                throw new RuntimeException(ex);
            }
            Task newTask = new Task(taskName, taskDescription, deadline);
            //вызов обновления окна задач
            MainFrame.getInstance().updateTasks();
        }
    }

}
