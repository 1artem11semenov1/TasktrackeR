import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.RGBColor;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame {
    private static MainFrame instance;

    private JButton createButton = new JButton("Добавить задачу");
    NodeList tasks;// = getDataFromXML();
    ArrayList<JPanel> taskPanelsList = new ArrayList<>();
    private JPanel tasksPanel;
    private JPanel infoPanel;
    private JScrollPane taskScroll;
    private MainFrame(String title) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(525, 650);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        display();
    }
    public static MainFrame getInstance(){
        if(instance == null){
            instance = new MainFrame("Task trackeR");
        }
        return instance;
    }

    private void display(){
        //основная панель задач
        tasksPanel = new JPanel();
        taskScroll = new JScrollPane(tasksPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        taskScroll.setBounds(0,0,510,450);
        taskScroll.setPreferredSize(new Dimension( 490,450));
        updateTasks();

        //инвормационная панель
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 17));
        Border border = BorderFactory.createMatteBorder(1,0,1,0,Color.black);
        infoPanel.setBackground(Color.gray);
        infoPanel.setBounds(0,450,525,50);
        infoPanel.setBorder(border);
        updateInfo();
        this.add(infoPanel);

        //панель созданиия
        JPanel createPanel = new JPanel();
        createPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 30));
        createPanel.setBackground(Color.gray);
        createPanel.setBounds(0,500,525,150);
            //кнопка добавить задачу
        createPanel.add(createButton);
        createButton.setSize(150, 50);
        createButton.addActionListener(new TaskCreateListener());
        this.add(createPanel);
            //кнопка обновления
        JButton updButton = new JButton("Обновить");
        updButton.setSize(100,50);
        updButton.addActionListener(new UpdListener());
        createPanel.add(updButton);
            //Время
        JPanel datePanel = new JPanel();
        datePanel.setBackground(Color.gray);
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.PAGE_AXIS));
        JLabel timeLabel = new JLabel();
        datePanel.add(timeLabel);
        JLabel dateLabel = new JLabel();
        datePanel.add(dateLabel);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("     HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay(() -> {
            LocalDateTime now = LocalDateTime.now();
            String nowFormattedTime = timeFormatter.format(now);
            String nowFormattedDate = dateFormatter.format(now);
            SwingUtilities.invokeLater(() -> timeLabel.setText(nowFormattedTime));
            SwingUtilities.invokeLater(() -> dateLabel.setText(nowFormattedDate));
        }, 0, 30, TimeUnit.SECONDS);
        createPanel.add(datePanel);
        datePanel.setSize(75,50);

        this.setVisible(true);
    }

    public void updateTasks(){
        if (!taskPanelsList.isEmpty()){
            this.remove(taskScroll);
            tasksPanel.removeAll();
            taskPanelsList.clear();
        }
        tasks = Manager.getInstance().getDataFromXML();
        tasksPanel.setPreferredSize(new Dimension( 500,tasks.getLength()*100));
        tasksPanel.setAutoscrolls(true);
        tasksPanel.setBackground(Color.LIGHT_GRAY);
        tasksPanel.setLayout(null);
        for (int i =0; i<tasks.getLength(); ++i){
            Color taskColor = Color.WHITE;
            NamedNodeMap attributes = tasks.item(i).getAttributes();
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date deadline;
            //Date createTime;
            try {
                deadline = format.parse(attributes.getNamedItem("deadline").getTextContent());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if ((new Date()).after(deadline)){
                taskColor = Color.RED;
            }

            JPanel task = new JPanel();
            task.setBounds(0,i*100,495,100);
            task.setLayout(null);
            Border taskBorder = BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK);
            task.setBorder(taskBorder);

            JPanel seCheck = new JPanel();
            seCheck.setBounds(0,0,80,99);
            Border seCheckBorder = BorderFactory.createMatteBorder(0,0,0,2, Color.BLACK);
            seCheck.setBorder(seCheckBorder);
            seCheck.setLayout(new BoxLayout(seCheck, BoxLayout.PAGE_AXIS));
            JCheckBox startCheck = new JCheckBox("",attributes.getNamedItem("isStart").getTextContent().equals("1"));
            startCheck.addActionListener(new isStartListener(attributes.getNamedItem("PK").getTextContent()));
            if (startCheck.isSelected() && (deadline.getTime() - new Date().getTime())>86400000*2 && (deadline.after(new Date()))){
                taskColor = new Color(173,216,230);
            } else if (startCheck.isSelected() && (deadline.getTime() - new Date().getTime())<86400000*2 && (deadline.after(new Date()))){
                taskColor = Color.YELLOW;
            } else if (!startCheck.isSelected() && (deadline.getTime() - new Date().getTime())<86400000*2 && (deadline.after(new Date()))){
                taskColor = Color.ORANGE;
            }

            JCheckBox endCheck = new JCheckBox("",attributes.getNamedItem("isFinish").getTextContent().equals("1"));
            endCheck.addActionListener(new isFinishListener(attributes.getNamedItem("PK").getTextContent()));
            if (endCheck.isSelected() && (deadline.after(new Date()))){
                taskColor = Color.GREEN;
            } else if (endCheck.isSelected() && (deadline.before(new Date()))){
                taskColor = new Color(0,153,0);
            }

            seCheck.add(new JLabel("Начато:"));
            seCheck.add(startCheck);
            seCheck.add(new JLabel("Выполнено:"));
            seCheck.add(endCheck);

            JPanel taskNamePanel = new JPanel();
            taskNamePanel.setBounds(100,0,300,99);
            taskNamePanel.setLayout(new BoxLayout(taskNamePanel, BoxLayout.PAGE_AXIS));


            taskNamePanel.add(new JLabel("Задача: " + attributes.getNamedItem("name").getTextContent()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy HH:mm");
            taskNamePanel.add(new JLabel("Срок: " + sdf.format(deadline)));

            Button detailsButton = new Button("...");
            detailsButton.addActionListener(new GetDetailsListener(attributes.getNamedItem("PK").getTextContent() ,attributes.getNamedItem("description").getTextContent()));
            detailsButton.setBounds(450,0,50,50);

            task.setBackground(taskColor);
            seCheck.setBackground(taskColor);
            taskNamePanel.setBackground(taskColor);

            task.add(seCheck);
            task.add(taskNamePanel);
            task.add(detailsButton);
            tasksPanel.add(task);
            taskPanelsList.add(task);
        }
        if (!taskPanelsList.isEmpty()){
            this.setVisible(false);
            this.add(taskScroll);
            this.setVisible(true);
        }
    }

    public void updateInfo(){
        int countTasks = 0;
        int countFinish = 0;
        int countOverdue = 0;
        this.remove(infoPanel);
        infoPanel.removeAll();

        countTasks = taskPanelsList.size();
        for(int i=0; i<tasks.getLength(); ++i){
            if (tasks.item(i).getAttributes().getNamedItem("isFinish").getTextContent().equals("1")){
                ++countFinish;
            }
            DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date deadline;
            //Date createTime;
            try {
                deadline = format.parse(tasks.item(i).getAttributes().getNamedItem("deadline").getTextContent());
                //createTime = format.parse(attributes.getNamedItem("createTime").getTextContent());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if ((new Date()).after(deadline)){
                ++countOverdue;
            }
        }

        JLabel Finish = new JLabel("выполнено: " + Integer.toString(countFinish));
        infoPanel.add(Finish);
        JLabel Overdue = new JLabel("просрочено: " + Integer.toString(countOverdue));
        infoPanel.add(Overdue);
        JLabel Count = new JLabel("всего задач: " + Integer.toString(countTasks));
        infoPanel.add(Count);

        this.setVisible(false);
        this.add(infoPanel);
        this.setVisible(true);
    }

    class TaskCreateListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            NewTaskFrame createWindow = new NewTaskFrame();
        }
    }
    class GetDetailsListener implements ActionListener{
        String PK;
        String details;
        GetDetailsListener(String PKInput, String detailsInput){PK = PKInput; details= detailsInput;}
        public void actionPerformed(ActionEvent e){
            DeatailsFrame detailsf = new DeatailsFrame(PK, details);
        }
    }

    class isStartListener implements ActionListener{
        String PK;
        isStartListener(String PKInput){PK = PKInput;}
        public void actionPerformed(ActionEvent e){
            Manager.getInstance().updateStartStatus(PK);
            MainFrame.getInstance().updateTasks();
        }
    }

    class isFinishListener implements ActionListener{
        String PK;
        isFinishListener(String PKInput){PK = PKInput;}
        public void actionPerformed(ActionEvent e){
            Manager.getInstance().updateFinishStatus(PK);
            MainFrame.getInstance().updateTasks();
            MainFrame.getInstance().updateInfo();
        }
    }

    class UpdListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            MainFrame.getInstance().updateTasks();
            MainFrame.getInstance().updateInfo();
        }
    }

}
