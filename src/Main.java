import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IOException, URISyntaxException, TransformerException {
        MySQLConnection mySQLConnection = new MySQLConnection();
        mySQLConnection.setConnectionURL("jdbc:mysql://127.0.0.1:3306/magnit");
        mySQLConnection.setUserName("admin");
        mySQLConnection.setPassword("123");
        mySQLConnection.setN(1000000);
        mySQLConnection.getMySQLConnection();
       // System.out.println(mySQLConnection.hasRecord());
        mySQLConnection.insertIntoDatabase();
        mySQLConnection.readDataFromDatabase();

        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(new File("template.xsl"));
        Transformer transformer = factory.newTransformer(xslt);
        Source xml = new StreamSource(new File("1.xml"));
        transformer.transform(xml, new StreamResult(new File("2.xml")));
        System.out.println(mySQLConnection.getSum());
    }
}
