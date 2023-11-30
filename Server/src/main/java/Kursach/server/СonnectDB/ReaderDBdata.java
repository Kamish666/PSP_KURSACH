package Kursach.server.СonnectDB;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ReaderDBdata {
    private String url;
    private String name;
    private String password;

    @Override
    public String toString() {
        return "ReaderDBdata{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public ReaderDBdata() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database_data.xml")){

            // Чтение XML файла
//            File xmlFile = new File("src/main/java/Server/database_data.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();

            url = getElementValue(doc, "connection.url");
            name = getElementValue(doc, "name");
            password = getElementValue(doc, "password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getElementValue(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        Node node = nodeList.item(0);
        return node.getTextContent();
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
