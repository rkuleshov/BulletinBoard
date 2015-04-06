package bulletinboard.dao;

import bulletinboard.model.Advert;
import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AdvertDAO {
    private static File fXmlFile;
    private static File advertsJsonFile;

    static {
        URL xmlUrl = AdvertDAO.class.getClassLoader().getResource("adverts.xml");
        URL jsonUrl = AdvertDAO.class.getClassLoader().getResource("adverts.json");
        if (xmlUrl != null && jsonUrl != null) {
            try {
                fXmlFile = new File(xmlUrl.toURI());
                advertsJsonFile = new File(jsonUrl.toURI());
            } catch (URISyntaxException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Could not find adverts.xml or adverts.json files");
        }
    }

    public List<Advert> getAdvertsFromXML() {
        List<Advert> adverts = new ArrayList<Advert>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("advert");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    adverts.add(getAdvertFromXML((Element) nNode));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adverts;
    }

    public void saveAllAdvertsToXML(List<Advert> adverts) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element root = doc.createElement("adverts");
            doc.appendChild(root);

            for (Advert adv : adverts) {

                Element advert = doc.createElement("advert");
                advert.setAttribute("id", String.valueOf(adv.getId()));
                root.appendChild(advert);

                Element userId = doc.createElement("userId");
                userId.appendChild(doc.createTextNode(String.valueOf(adv.getUserId())));
                advert.appendChild(userId);

                Element publicationDate = doc.createElement("publicationDate");
                publicationDate.appendChild(doc.createTextNode(String.valueOf(adv.getPublicationDate())));
                advert.appendChild(publicationDate);

                Element rubricId = doc.createElement("rubricId");
                rubricId.appendChild(doc.createTextNode(String.valueOf(adv.getRubricId())));
                advert.appendChild(rubricId);

                Element title = doc.createElement("title");
                title.appendChild(doc.createTextNode(String.valueOf(adv.getTitle())));
                advert.appendChild(title);

                Element text = doc.createElement("text");
                text.appendChild(doc.createTextNode(String.valueOf(adv.getText())));
                advert.appendChild(text);
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
                e.printStackTrace();
            }

        } catch (TransformerException ex) {
            System.err.println("Error outputting document");

        } catch (ParserConfigurationException ex) {
            System.err.println("Error building document");
        }
    }

    public void saveAllAdvertsToJSON(List<Advert> adverts) {
        Gson gson = new Gson();
        String jsonAdverts = gson.toJson(adverts);
        FileOutputStream outputStream;

        try {
            outputStream = new FileOutputStream(advertsJsonFile);
            outputStream.write(jsonAdverts.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Advert> getAllAdvertsFromJSON() {

        List<Advert> adverts = new ArrayList<Advert>();

        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(advertsJsonFile));

            for (Object object : jsonArray) {
                adverts.add(getAdvertFromJSON((JSONObject) object));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adverts;

    }

    private Advert getAdvertFromJSON(JSONObject object) {
        Advert advert = new Advert();

        String stringId = String.valueOf(object.get("id"));

        advert.setId(Integer.parseInt(stringId));

        String stringUserId = String.valueOf(object.get("userId"));
        advert.setUserId(Integer.parseInt(stringUserId));

        advert.setPublicationDate(String.valueOf(object.get("publicationDate")));

        String stringRubricId = String.valueOf(object.get("rubricId"));
        advert.setRubricId(Integer.parseInt(stringRubricId));

        advert.setText(String.valueOf(object.get("text")));

        advert.setTitle(String.valueOf(object.get("title")));
        return advert;
    }

    private Advert getAdvertFromXML(Element nNode) {
        Advert advert = new Advert();
        advert.setId(Integer.parseInt(nNode.getAttribute("id")));
        advert.setUserId(Integer.parseInt(nNode.getElementsByTagName("userId").item(0).getTextContent()));
        advert.setPublicationDate(nNode.getElementsByTagName("publicationDate").item(0).getTextContent());
        advert.setRubricId(Integer.parseInt(nNode.getElementsByTagName("rubricId").item(0).getTextContent()));
        advert.setTitle(nNode.getElementsByTagName("title").item(0).getTextContent());
        advert.setText(nNode.getElementsByTagName("text").item(0).getTextContent());
        return advert;
    }
}
