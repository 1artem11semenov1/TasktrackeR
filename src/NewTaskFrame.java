import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewTaskFrame extends JFrame{
    private JLabel taskNameLabel = new JLabel("Введите имя задачи:");
    private JTextField taskNameField = new JTextField("", 10);
    private JLabel taskDescrLabel = new JLabel("Введите описание:");
    private JTextArea taskDescriptionField = new JTextArea(5,20);
    private JScrollPane descrScroll = new JScrollPane(taskDescriptionField);
    private JLabel taskDeadlineLabel = new JLabel("Введите срок выполнения в формате");
    private JLabel taskDeadlineLabel1 = new JLabel("день.месяц.год <пробел> часы:минуты:");
    private JTextField taskDeadlineField = new JTextField("", 10);
    private JButton taskCreateButton = new JButton("Добавить задачу");
    private String taskName;
    private String taskDescription;

    NewTaskFrame(){
        JFrame taskCreator = new JFrame("создание задачи");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        taskCreateButton.addActionListener(new isAddListener());

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
        taskNameField.setMaximumSize(new Dimension(400,50));
        taskDeadlineField.setMaximumSize(new Dimension(400,50));

        this.add(taskNameLabel);
        this.add(taskNameField);
        this.add(taskDescrLabel);
        this.add(descrScroll);
        this.add(taskDeadlineLabel);
        this.add(taskDeadlineLabel1);
        this.add(taskDeadlineField);
        this.add(taskCreateButton);

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
            MainFrame.getInstance().updateInfo();
        }
    }

}
