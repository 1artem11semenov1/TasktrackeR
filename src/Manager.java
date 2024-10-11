import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Manager {
    private Document document;
    private static Manager instance;

    Manager(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
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
    }
    public static Manager getInstance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;
    }

    public NodeList getDataFromXML(){
        NodeList tasks = document.getElementsByTagName("task");
        return tasks;
    }
    public void newTask(String PK, String taskName, String taskDescription, String createTime, String deadline){
        NodeList root = document.getElementsByTagName("root");
        Element task = document.createElement("task");
        task.setAttribute("PK", PK);
        task.setAttribute("name", taskName);
        task.setAttribute("description", taskDescription);
        task.setAttribute("createTime", createTime);
        task.setAttribute("deadline", deadline);
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
    public void deleteTask(String PK){
        NodeList root = document.getElementsByTagName("root");
        NodeList tasks = document.getElementsByTagName("task");

        for (int i=0; i<tasks.getLength(); ++i){
            if (tasks.item(i).getAttributes().getNamedItem("PK").getTextContent().equals(PK)){
                tasks.item(i).getParentNode().removeChild(tasks.item(i));
                break;
            }
        }

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
}
