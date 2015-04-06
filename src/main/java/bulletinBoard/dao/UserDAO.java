package bulletinboard.dao;

import bulletinboard.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static File fXmlFile;
    static {
        URL xmlUrl = AdvertDAO.class.getClassLoader().getResource("users.xml");
        if (xmlUrl != null) {
            try {
                fXmlFile = new File(xmlUrl.toURI());
            } catch (URISyntaxException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Could not find users.xml file");
        }
    }

    public List<User> getAllUsers() {

        List<User> users = new ArrayList<User>();
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    User user = new User();
                    user.setId(Integer.parseInt(eElement.getAttribute("id")));
                    user.setUserName(eElement.getElementsByTagName("userName").item(0).getTextContent());

                    users.add(user);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void saveAllUsers(List<User> users) {

        try {
            DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dFact.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("users");
            doc.appendChild(root);

            for (User user : users) {

                Element advert = doc.createElement("user");
                advert.setAttribute("id", String.valueOf(user.getId()));
                root.appendChild(advert);

                Element userName = doc.createElement("userName");
                userName.appendChild(doc.createTextNode(user.getUserName()));
                advert.appendChild(userName);
            }

            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();

            aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            aTransformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            try {
                FileWriter fos = new FileWriter(fXmlFile);
                StreamResult result = new StreamResult(fos);
                aTransformer.transform(source, result);

            } catch (IOException e) {
                System.err.print(e.getMessage());
                e.printStackTrace();
            }

        } catch (TransformerException ex) {
            System.err.println("Error outputting document");

        } catch (ParserConfigurationException ex) {
            System.err.println("Error building document");
        }
    }
}
