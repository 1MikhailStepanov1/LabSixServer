package utility;

import data.Coordinates;
import data.Person;
import data.Position;
import data.Worker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;


/**
 * This class is used to operate with files
 */
public class FileWorker {
    private CollectionManager collectionManager;
    private String filePath;
    private File file;
    private final CollectionValidator collectionValidator = new CollectionValidator(collectionManager);


    public FileWorker(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
        filePath = "";
    }


    /**
     * Read collection from indicated file
     *
     * @return collection from indicated file
     * @throws IllegalArgumentException     if some methods have incorrect argument
     * @throws NullPointerException         if some of the fields is null
     * @throws IOException                  if can't read collection from file
     * @throws SAXException                 if can't match XML format in file
     * @throws ParserConfigurationException if document builder can't be created
     */
    public LinkedList<Worker> parse(String filePath) throws IllegalArgumentException, NullPointerException, IOException, SAXException, ParserConfigurationException {
        LinkedList<Worker> collectionFromFile = new LinkedList<>();
        try {
            Long tempId = null;
            String tempName = null;
            long tempX = 0;
            Integer tempY = null;
            ZonedDateTime tempCreationDate = null;
            double tempSalary = 0;
            ZonedDateTime tempStartDate = null;
            ZonedDateTime tempEndDate = null;
            Position tempPosition = null;
            Long tempHeight = 0L;
            Integer tempWeight = null;
            file = new File(filePath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("worker");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    try {
                        Element element = (Element) node;
                        try {
                            tempId = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong id in worker number " + (temp + 1));
                        }
                        tempName = element.getElementsByTagName("name").item(0).getTextContent();

                        NodeList tempNodeListCoord = element.getElementsByTagName("coordinates");
                        Element elementCoord = (Element) tempNodeListCoord.item(0);
                        try {
                            tempX = Long.parseLong(elementCoord.getElementsByTagName("coordinateX").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong coordinate X in worker number " + (temp + 1));
                        }
                        try {
                            tempY = Integer.parseInt(elementCoord.getElementsByTagName("coordinateY").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong coordinate Y in worker number " + (temp + 1));
                        }

                        try {
                            tempCreationDate = ZonedDateTime.parse(element.getElementsByTagName("creationDate").item(0).getTextContent());
                        } catch (DateTimeParseException exception) {
                            System.out.println("Wrong creation date in worker number " + (temp + 1));
                        }
                        try {
                            tempSalary = Double.parseDouble(element.getElementsByTagName("salary").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong salary in worker number " + (temp + 1));
                            tempSalary = 0;
                        }
                        try {
                            tempStartDate = ZonedDateTime.parse(element.getElementsByTagName("startDate").item(0).getTextContent());
                        } catch (DateTimeParseException exception) {
                            System.out.println("Wrong start date in worker number " + (temp + 1));
                        }
                        if (!element.getElementsByTagName("endDate").item(0).getTextContent().equals("")) {
                            if (element.getElementsByTagName("endDate").item(0).getTextContent().equals("null")) {
                                tempEndDate = null;
                            } else {
                                try {
                                    tempEndDate = ZonedDateTime.parse(element.getElementsByTagName("endDate").item(0).getTextContent());
                                } catch (DateTimeParseException exception) {
                                    System.out.println("Wrong end date in worker number " + (temp + 1));
                                }
                            }
                        }
                        if (element.getElementsByTagName("position").item(0).getTextContent().equals("null")) {
                            tempPosition = null;
                        } else {
                            tempPosition = Position.valueOf(element.getElementsByTagName("position").item(0).getTextContent());
                        }
                        NodeList tempNodeListPerson = element.getElementsByTagName("person");
                        Element elementPerson = (Element) tempNodeListPerson.item(0);
                        try {
                            tempHeight = Long.parseLong(elementPerson.getElementsByTagName("height").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong height in worker number " + (temp + 1));
                            tempHeight = 0L;
                        }
                        try {
                            tempWeight = Integer.parseInt(elementPerson.getElementsByTagName("weight").item(0).getTextContent());
                        } catch (NumberFormatException exception) {
                            System.out.println("Wrong weight in worker number " + (temp + 1));
                            tempWeight = 0;
                        }
                    } catch (NullPointerException ex) {
                        continue;
                    }
                }

                if (collectionValidator.validateObject(collectionFromFile, new Worker(tempId, tempName, new Coordinates(tempX, tempY), tempCreationDate, tempSalary, tempStartDate, tempEndDate, tempPosition, new Person(tempHeight, tempWeight)))) {
                    collectionFromFile.add(new Worker(tempId, tempName, new Coordinates(tempX, tempY), tempCreationDate, tempSalary, tempStartDate, tempEndDate, tempPosition, new Person(tempHeight, tempWeight)));
                } else {
                    System.out.println("Worker number "+ (temp+1) + " has wrong id.");
                }
            }

        } catch (ParserConfigurationException | SAXException |
                IOException e) {
            System.out.println("File can't be parsed. Something went wrong.");
        }
        if (!collectionFromFile.isEmpty()) {
            System.out.println("Collection was loaded successfully.");
        } else {
            System.out.println("Collection wasn't loaded. Please, check errors above.");
        }
        return collectionFromFile;
    }


    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath - file in which collection wil be wrote
     */
    public void getToXmlFormat(String filePath) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.newDocument();
            if (!collectionManager.getCollection().isEmpty()) {
                Element root = document.createElement("collection");
                document.appendChild(root);
                for (Worker w : collectionManager.getCollection()) {
                    Element worker = document.createElement("worker");
                    root.appendChild(worker);
                    Element id = document.createElement("id");
                    id.appendChild(document.createTextNode(String.valueOf(w.getId())));
                    worker.appendChild(id);
                    Element name = document.createElement("name");
                    name.appendChild(document.createTextNode(w.getName()));
                    worker.appendChild(name);
                    Element coordinates = document.createElement("coordinates");
                    worker.appendChild(coordinates);
                    Element x = document.createElement("coordinateX");
                    x.appendChild(document.createTextNode(String.valueOf(w.getCoordinates().getCoordinateX())));
                    coordinates.appendChild(x);
                    Element y = document.createElement("coordinateY");
                    y.appendChild(document.createTextNode(String.valueOf(w.getCoordinates().getCoordinateY())));
                    coordinates.appendChild(y);
                    Element creationDate = document.createElement("creationDate");
                    creationDate.appendChild(document.createTextNode(String.valueOf(w.getCreationDate())));
                    worker.appendChild(creationDate);
                    Element salary = document.createElement("salary");
                    salary.appendChild(document.createTextNode(String.valueOf(w.getSalary())));
                    worker.appendChild(salary);
                    Element startDate = document.createElement("startDate");
                    startDate.appendChild(document.createTextNode(String.valueOf(w.getStartDate())));
                    worker.appendChild(startDate);
                    Element endDate = document.createElement("endDate");
                    endDate.appendChild(document.createTextNode(String.valueOf(w.getEndDate())));
                    worker.appendChild(endDate);
                    Element position = document.createElement("position");
                    position.appendChild(document.createTextNode(String.valueOf(w.getPosition())));
                    worker.appendChild(position);
                    Element person = document.createElement("person");
                    worker.appendChild(person);
                    Element height = document.createElement("height");
                    height.appendChild(document.createTextNode(String.valueOf(w.getPerson().getHeight())));
                    person.appendChild(height);
                    Element weight = document.createElement("weight");
                    weight.appendChild(document.createTextNode(String.valueOf(w.getPerson().getWeight())));
                    person.appendChild(weight);
                }
                writeDocument(document);
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void writeDocument(Document document) {
        try {
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource dom = new DOMSource(document);
            if (!file.canWrite() || !file.exists()) {
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamResult result = new StreamResult(outputStream);
                tr.transform(dom, result);
                System.out.println("Collection can't be written to the file " + file.getName());
                System.out.println("Collection was written to file " + filePath);
            } else {
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                StreamResult result = new StreamResult(outputStream);
                tr.transform(dom, result);
            }
        } catch (TransformerException ex) {
            System.out.println("Collection can't be written to the file.");
        } catch (FileNotFoundException ex) {
            if (!file.exists()) {
                System.out.println("Collection can't be written to the file with identified name. (write or exist roots)");
            }
        }
    }

    public void setFilePath(String path) {
        filePath = path;
    }
}
