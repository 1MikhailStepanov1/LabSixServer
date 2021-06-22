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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;


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
        this.filePath = filePath;
//        try{
//            File file = new File(filePath);
//            JAXBContext jaxbContext = JAXBContext.newInstance(CollectionManager.class);
//            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//            CollectionManager collectionManagerForParse = (CollectionManager) unmarshaller.unmarshal(file);
//            if (collectionValidator.validateCollection(collectionManagerForParse.getCollection())){
//                collectionFromFile = collectionManagerForParse.getCollection();
//                return collectionFromFile;
//            }
//        } catch (JAXBException e) {
//            e.printStackTrace();
//            System.out.println("File is broken. Collection can't be parsed.");
//        }
        Long tempId = null;
        String tempName = null;
        long tempX = 0;
        Integer tempY = null;
        ZonedDateTime tempCreationDate = null;
        double tempSalary = 0;
        ZonedDateTime tempStartDate = null;
        ZonedDateTime tempEndDate = null;
        Position tempPosition = null;
        Long tempHeight = null;
        Integer tempWeight = null;
        try {
            File input = new File(filePath);
            this.file = input;
            Scanner scanner = new Scanner(input);
            String file = "";
            String backup = "";
            HashMap<Long, String> incorrectNames = new HashMap<>();
            long workerNumberForNames = 1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                backup += line;
                if (line.contains("<worker>")) {
                    workerNumberForNames += 1;
                }
                if (line.contains("<name>")) {
                    String tempLine = line;
                    tempLine = tempLine.replace("<name>", "").replace("</name>", "");
                    incorrectNames.put(workerNumberForNames, tempLine);
                    tempLine = tempLine.replace("<", "");
                    file += "<name>" + tempLine + "</name>";
                } else {
                    file += line;
                }
            }
            scanner.close();
            incorrectNames.replaceAll((s, v) -> v.replace(" ", ""));
            FileWriter tempWriter = new FileWriter(input);
            tempWriter.write(file);
            tempWriter.close();
            long workerNumber = 1;
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("worker");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    try {
                        tempId = Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent());
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong id in worker number " + workerNumber);
                    }
                    tempName = element.getElementsByTagName("name").item(0).getTextContent();

                    NodeList tempNodeListCoord = element.getElementsByTagName("coordinates");
                    Element elementCoord = (Element) tempNodeListCoord.item(0);
                    try {
                        tempX = Long.parseLong(elementCoord.getElementsByTagName("coordinateX").item(0).getTextContent());
                        if (tempX > 768) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong coordinate X in worker number " + workerNumber);
                    }
                    try {
                        tempY = Integer.parseInt(elementCoord.getElementsByTagName("coordinateY").item(0).getTextContent());
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong coordinate Y in worker number " + workerNumber);
                    }

                    try {
                        tempCreationDate = ZonedDateTime.parse(element.getElementsByTagName("creationDate").item(0).getTextContent());
                    } catch (DateTimeParseException exception) {
                        System.out.println("Wrong creation date in worker number " + workerNumber);
                    }
                    try {
                        tempSalary = Double.parseDouble(element.getElementsByTagName("salary").item(0).getTextContent());
                        if (tempSalary <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong salary in worker number " + workerNumber);
                        tempSalary = 0;
                    }
                    try {
                        tempStartDate = ZonedDateTime.parse(element.getElementsByTagName("startDate").item(0).getTextContent());
                    } catch (DateTimeParseException exception) {
                        System.out.println("Wrong start date in worker number " + workerNumber);
                    }
                    if (!element.getElementsByTagName("endDate").item(0).getTextContent().equals("")) {
                        try {
                            tempEndDate = ZonedDateTime.parse(element.getElementsByTagName("endDate").item(0).getTextContent());
                        } catch (DateTimeParseException exception) {
                            System.out.println("Wrong end date in worker number " + workerNumber);
                        }
                    }
                    tempPosition = Position.valueOf(element.getElementsByTagName("position").item(0).getTextContent());

                    NodeList tempNodeListPerson = element.getElementsByTagName("person");
                    Element elementPerson = (Element) tempNodeListPerson.item(0);
                    try {
                        tempHeight = Long.parseLong(elementPerson.getElementsByTagName("height").item(0).getTextContent());
                        if (tempHeight <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong height in worker number " + workerNumber);
                        tempHeight = 0L;
                    }
                    try {
                        tempWeight = Integer.parseInt(elementPerson.getElementsByTagName("weight").item(0).getTextContent());
                        if (tempWeight <= 0) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException exception) {
                        System.out.println("Wrong weight in worker number " + workerNumber);
                        tempWeight = 0;
                    }
                }
                workerNumber += 1;
                if (tempId == null || tempName == null || tempY == null || tempX == 0 || tempCreationDate == null || tempSalary == 0 || tempStartDate == null) {
                    System.out.println("Worker can't be added. Some data is wrong.");
                } else if (incorrectNames.containsKey(workerNumber)) {
                    Worker worker = new Worker(tempId, incorrectNames.get(workerNumber), new Coordinates(tempX, tempY), tempCreationDate, tempSalary, tempStartDate, tempEndDate, tempPosition, new Person(tempHeight, tempWeight));
                    collectionFromFile.add(worker);
                } else {
                    Worker worker = new Worker(tempId, tempName, new Coordinates(tempX, tempY), tempCreationDate, tempSalary, tempStartDate, tempEndDate, tempPosition, new Person(tempHeight, tempWeight));
                    collectionFromFile.add(worker);
                }
            }
            FileWriter tempWriter2 = new FileWriter(input);
            for (String str : backup.split(">")) {
                str.replaceAll("(^ )+", "").replaceAll("( $)+", "");
                if (str.replaceAll(" ", "").equals("<id") || str.replaceAll(" ", "").equals("<name") || str.replaceAll(" ", "").equals("<coordinateX") || str.replaceAll(" ", "").equals("<coordinateY") || str.replaceAll(" ", "").equals("<creationDate") || str.replaceAll(" ", "").equals("<salary") || str.replaceAll(" ", "").equals("<startDate") || str.replaceAll(" ", "").equals("<endDate") || str.replaceAll(" ", "").equals("<position") || str.replaceAll(" ", "").equals("<height") || str.replaceAll(" ", "").equals("<weight")) {
                    tempWriter2.write(str + ">");
                } else {
                    tempWriter2.write(str + ">\n");
                }
            }
            tempWriter2.close();
            System.out.println("Collection was loaded successfully.");
        } catch (FactoryConfigurationError | ParserConfigurationException | IOException | SAXException exception) {
            System.out.println("Something goes wrong. Please correct file and try again. (" + exception.getMessage() + ")");
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
//        try {
//            OutputStream outputStream = new FileOutputStream(new File(filePath));
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
//            JAXBContext jaxbContext = JAXBContext.newInstance(CollectionManager.class);
//            Marshaller marshaller = jaxbContext.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.marshal(collectionManager, outputStreamWriter);
//            outputStreamWriter.close();
//        } catch (FileNotFoundException | JAXBException exception) {
//            System.out.println("File can't be found or was broken.");
//        } catch (IOException exception) {
//            exception.printStackTrace();
//        }
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
                System.out.println("It is impossible to write the collection to file " + file.getName());
                System.out.println("The collection was written to file " + filePath);
            } else {
                BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                StreamResult result = new StreamResult(outputStream);
                tr.transform(dom, result);
            }
        } catch (TransformerException ex) {
            System.out.println("It is impossible to write the collection to the file");
        } catch (FileNotFoundException ex) {
            if (!file.exists()) {
                System.out.println("It is impossible to write collection to the file because file with the specified name does not exist");
            }
        }
    }
}
