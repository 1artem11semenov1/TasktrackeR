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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Task {
    private String taskName;
    private String taskDescription;
    private Date createTime;
    private Date deadline;

    Task (String name, String description, Date dl){
        taskName = name;
        taskDescription = description;
        createTime = new Date();
        deadline = dl;

        addToXML();

    }

    private void addToXML(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document document;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File("tasks.xml"));
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        NodeList root = document.getElementsByTagName("root");
        Element task = document.createElement("task");
        task.setAttribute("name", taskName);
        task.setAttribute("description", taskDescription);
        String createTimeStr = createTime.toString();
        String deadlineStr = deadline.toString();
        task.setAttribute("createTime", createTimeStr);
        task.setAttribute("deadline", deadlineStr);
        root.item(0).appendChild(task);
        Transformer t;
        try {
            t = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            t.transform(new DOMSource(document), new StreamResult("tasks.xml"));
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }

    public void info(){
        System.out.println(taskName);
        System.out.println(taskDescription);
        System.out.println(createTime);
        System.out.println(deadline);
    }
}
