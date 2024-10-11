/*import javax.swing.text.Document;
import javax.swing.text.Element;*/
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Task {
    public String PK;
    private String taskName;
    private String taskDescription;
    private Date createTime;
    private Date deadline;

    Task (String name, String description, Date dl){
        PK = UUID.randomUUID().toString();
        taskName = name;
        taskDescription = description;
        createTime = new Date();
        deadline = dl;

        addToXML();
    }

    private void addToXML(){
        Manager.getInstance().newTask(PK, taskName, taskDescription, createTime.toString(), deadline.toString());
    }

    public Date getCreateTime(){
        return createTime;
    }
    public void info(){
        System.out.println(taskName);
        System.out.println(taskDescription);
        System.out.println(createTime);
        System.out.println(deadline);
    }

   /* public void deleteTask(){
        MainFrame.getInstance().deleteTask(this);

    }*/
}
