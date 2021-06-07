package server.utils;

import data.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import java.io.*;
import java.util.LinkedList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


/**
 * This class is used to operate with files
 */
public class FileWorker {
    private final CollectionManager collectionManager;
    private String filePath;
    private CollectionValidator collectionValidator;

    public FileWorker(CollectionManager collectionManager, CollectionValidator collectionValidator) {
        this.collectionManager = collectionManager;
        this.collectionValidator = collectionValidator;
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
    public LinkedList<Worker> parse() throws IllegalArgumentException, NullPointerException, IOException, SAXException, ParserConfigurationException {
        LinkedList<Worker> collectionFromFile = new LinkedList<>();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            JAXBContext jaxbContext = JAXBContext.newInstance(CollectionManager.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            CollectionManager collectionManagerForParse = (CollectionManager) unmarshaller.unmarshal(bufferedReader);
            if (collectionValidator.validateCollection(collectionManagerForParse.getCollection())){
                collectionFromFile = collectionManagerForParse.getCollection();
            }
        } catch (JAXBException e) {
            System.out.println("File is broken.");
        }
        return collectionFromFile;

    }

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String arg){
        filePath = arg;
    }

    /**
     * @param filePath - file in which collection wil be wrote
     */
    public void getToXmlFormat() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            writer.write("<collection>\n");
            for (Worker w : collectionManager.getCollection()) {
                writer.write("<worker>\n");
                writer.write("<id>" + w.getId() + "</id>\n");
                writer.write("<name>" + w.getName() + "</name>\n");
                writer.write("<coordinates>\n");
                writer.write("<coordinateX>" + w.getCoordinates().getX() + "</coordinateX>\n");
                writer.write("<coordinateY>" + w.getCoordinates().getY() + "</coordinateY>\n");
                writer.write("</coordinates>\n");
                writer.write("<creationDate>" + w.getCreationDate() + "</creationDate>\n");
                writer.write("<salary>" + w.getSalary() + "</salary>\n");
                writer.write("<startDate>" + w.getStartDate() + "</startDate>\n");
                if (w.getEndDate() == null) {
                    writer.write("<endDate></endDate>\n");
                } else {
                    writer.write("<endDate>" + w.getEndDate() + "</endDate>\n");
                }
                if (w.getPosition() == null) {
                    writer.write("<position></position>\n");
                } else {
                    writer.write("<position>" + w.getPosition() + "</position>\n");
                }
                writer.write("<person>\n");
                if (w.getPerson().getHeight() == null) {
                    writer.write("<height>" + w.getPerson().getHeight() + "</height>\n");
                } else {
                    writer.write("<height>" + w.getPerson().getHeight() + "</height>\n");
                }
                if (w.getPerson().getWeight() == null) {
                    writer.write("<weight></weight>\n");
                } else {
                    writer.write("<weight>" + w.getPerson().getWeight() + "</weight>\n");
                }
                writer.write("</person>\n");
                writer.write("</worker>\n");
            }
            writer.write("</collection>");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
