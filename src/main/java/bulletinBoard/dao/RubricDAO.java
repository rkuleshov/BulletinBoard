package bulletinBoard.dao;

import bulletinBoard.model.Rubric;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RubricDAO {

    public List<Rubric> getRubrics() {

        List<Rubric> rubrics = new ArrayList<Rubric>();
        try {

            File fXmlFile = new File("src/main/resources/rubrics.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("rubric");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    Rubric rubric = new Rubric();
                    rubric.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    rubric.setId(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()));
                    rubrics.add(rubric);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rubrics;
    }
}
