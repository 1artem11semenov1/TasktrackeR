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
import java.util.ArrayList;
import java.util.Date;

public class MainFrame extends JFrame {
    private static MainFrame instance;

    private JButton createButton = new JButton("Добавить задачу");
    NodeList tasks;// = getDataFromXML();
    ArrayList<JPanel> taskPanelsList = new ArrayList<>();
    private JPanel tasksPanel;

    private MainFrame(String title) {
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 650);
        ImageIcon image = new ImageIcon("src/logo.png");
        this.setIconImage(image.getImage());
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
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
        tasksPanel.setBounds(0,0,500,450);
        tasksPanel.setBackground(Color.LIGHT_GRAY);
        tasksPanel.setLayout(null);
        updateTasks();

        //панель созданиия
        JPanel createPanel = new JPanel();
        createPanel.setLayout(null);
        createPanel.setBackground(Color.gray);
        createPanel.setBounds(0,500,500,150);
        createPanel.add(createButton);
        createButton.setBounds(40,30, 125, 50);
        createButton.addActionListener(new TaskCreateListener());
        this.add(createPanel);

        //инвормационная панель
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(null);
        Border border = BorderFactory.createMatteBorder(1,0,1,0,Color.black);
        infoPanel.setBackground(Color.gray);
        infoPanel.setBounds(0,450,500,50);
        infoPanel.setBorder(border);
        this.add(infoPanel);

        this.setVisible(true);
    }

    public void updateTasks(){
        if (!taskPanelsList.isEmpty()){
            this.remove(tasksPanel);
            tasksPanel.removeAll();
            taskPanelsList.clear();
        }
        tasks = Manager.getInstance().getDataFromXML();
        for (int i =0; i<tasks.getLength(); ++i){
            JPanel task = new JPanel();
            task.setBounds(0,i*100,500,100);
            task.setBackground(new Color(173,216,230));
            task.setLayout(null);
            Border taskBorder = BorderFactory.createMatteBorder(0,0,1,0, Color.BLACK);
            task.setBorder(taskBorder);

            JPanel seCheck = new JPanel();
            seCheck.setBounds(0,0,50,99);
            seCheck.setLayout(new BoxLayout(seCheck, BoxLayout.PAGE_AXIS));
            seCheck.setBackground(new Color(173,216,230));
            JCheckBox startCheck = new JCheckBox("",false);
            JCheckBox endCheck = new JCheckBox("",false);
            seCheck.add(startCheck);
            seCheck.add(endCheck);

            JPanel taskNamePanel = new JPanel();
            taskNamePanel.setBounds(50,0,400,99);
            taskNamePanel.setBackground(new Color(173,216,230));
            taskNamePanel.setLayout(new BoxLayout(taskNamePanel, BoxLayout.PAGE_AXIS));

            NamedNodeMap attributes = tasks.item(i).getAttributes();
            taskNamePanel.add(new JLabel(attributes.getNamedItem("name").getTextContent()));
            /*taskNamePanel.add(new JLabel(attributes.item(3).getTextContent()));
            taskNamePanel.add(new JLabel(attributes.item(2).getTextContent()));
            taskNamePanel.add(new JLabel(attributes.item(1).getTextContent()));
            taskNamePanel.add(new JLabel(attributes.item(0).getTextContent()));*/

            Button detailsButton = new Button("...");
            detailsButton.addActionListener(new GetDetailsListener(attributes.getNamedItem("PK").getTextContent() ,attributes.getNamedItem("description").getTextContent()));
            detailsButton.setBounds(450,0,50,50);

            task.add(seCheck);
            task.add(taskNamePanel);
            task.add(detailsButton);
            tasksPanel.add(task);
            taskPanelsList.add(task);
        }
        if (!taskPanelsList.isEmpty()){
            this.setVisible(false);
            this.add(tasksPanel);
            this.setVisible(true);
        }
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


}
