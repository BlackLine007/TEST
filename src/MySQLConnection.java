import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.*;
import java.sql.*;
import java.util.Objects;

public class MySQLConnection {
    private String userName;
    private String password;
    private String connectionURL;
    private int N;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConnectionURL(String connectionURL) {
        this.connectionURL = connectionURL;
    }

    public void setN(int n) {
        N = n;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MySQLConnection)) return false;
        MySQLConnection that = (MySQLConnection) o;
        return getN() == that.getN() && Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getConnectionURL(), that.getConnectionURL());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword(), getConnectionURL(), getN());
    }

    public String getPassword() {
        return password;
    }

    public String getConnectionURL() {
        return connectionURL;
    }

    public int getN() {
        return N;
    }

    public MySQLConnection() {
    }
    public Connection getMySQLConnection() throws ClassNotFoundException {
        Connection connection = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
            try {
                connection = DriverManager.getConnection(this.connectionURL, this.userName, this.password);
        if (connection != null)
            System.out.println("Successful connected to the database");
        else System.out.println("Connection is failed");
            }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }
    public boolean hasRecord() {
        String sql = "SELECT value from test";
        try {
            PreparedStatement ps = this.getMySQLConnection().prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) return true;
            else return false;
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
    public void insertIntoDatabase() {
        if (this.hasRecord()) {
            String sql = "TRUNCATE TABLE test";
            try {
                PreparedStatement st = this.getMySQLConnection().prepareStatement(sql);
                st.executeUpdate();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }
      //  String sql = "INSERT INTO test (value) VALUES ";
        StringBuilder sql = new StringBuilder("INSERT INTO message (value) VALUES ");

        for (int i = 1; i <= this.getN(); i++) {
            if (i!=this.getN())
            sql.append("("+i+"), ");
            else sql.append("("+i+")");
        }
            try {
                PreparedStatement statement = this.getMySQLConnection().prepareStatement(sql.toString());
              //  statement.setInt(1, i);
                statement.executeUpdate();
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

    }
 //   insert into table values (1),(2), (3)
    public void readDataFromDatabase() {
        String SQL_select = "SELECT value FROM test";
            try {
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("D:\\TEST\\1.xml"));
                    PreparedStatement pst = this.getMySQLConnection().prepareStatement(SQL_select);
                    ResultSet resultSet = pst.executeQuery();
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("<entries>");
                    while (resultSet.next()){
                        int value = resultSet.getInt("value");
                        stringBuilder.append("\n"+"\t").append("<entry>").append("\n\t\t").append("<field>").append(value).append("</field>").append("\n\t").append("</entry>");
                    }
                    stringBuilder.append("\n").append("</entries>");
                    bufferedWriter.write(stringBuilder.toString());
                    bufferedWriter.close();
            } catch (SQLException | ClassNotFoundException | IOException throwables) {
                throwables.printStackTrace();
            }
    }
    public int getSum() {
        int sum = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\TEST\\2.xml"));
            StringBuilder stringBuilder = new StringBuilder();
            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }
            bufferedReader.close();
            Document document = Jsoup.parse(stringBuilder.toString(),"", Parser.xmlParser());
            Elements elem = document.getElementsByAttribute("field");
            for (Element element : elem) {
                sum += Integer.parseInt(element.attr("field"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sum;
    }
}
