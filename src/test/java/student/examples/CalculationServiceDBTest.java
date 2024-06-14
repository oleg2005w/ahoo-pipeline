package student.examples;

import org.testng.Assert;
import org.testng.annotations.*;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
@Listeners(CalculationServiceTestListener.class)

public class CalculationServiceDBTest {
    private Connection connection;
    private CalculationService calculationService;
    @BeforeClass
    public void setupService(){
        calculationService =new CalculationService();
    }

    @BeforeClass
    public void setupData() throws SQLException {
        //1. Open connection Postgre
        connection =  DriverManager.getConnection("jdbc:postgresql://192.168.222.129/calculation_service_test_data_db?user=postgres&password=qazwsx&ssl=false");
        Statement result = connection.createStatement();
        result.executeUpdate("DELETE FROM test_data");
        result.executeUpdate("DELETE FROM tests");

        //2.Insert test info into database.
        for (int j = 1; j <= 10 ; j++) {
            int authorId = (int) Math.random()*4;
            ResultSet resultSet = result.executeQuery("SELECT id FROM test_authors ORDER BY RANDOM() LIMIT 1");
            resultSet.next();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test_data VALUES (" + j + ",?,?,"+j+")");

            result.executeUpdate("INSERT INTO tests VALUES (" + j + ", 'Sum of negatives', false, now(), "+resultSet.getInt("id")+")");

            //3. Insert test data
            StringBuilder values = new StringBuilder();

            int summ = 0;
            for (int i = 0; i < 10; i++) {
                int value = (int) (Math.random() * (-999) -1);
                summ += value;
                values.append(value).append(",");
            }
            values.deleteCharAt(values.length() - 1);
            String expected = String.format("%d", summ);
//        result.executeUpdate("INSERT INTO test_data VALUES")
            preparedStatement.setString(1, values.toString() );
            preparedStatement.setString(2, expected);
            preparedStatement.executeUpdate();
        }
//        Fetching data
    }
    @DataProvider(name = "dp")
    public Iterator<Map<String, Object>> provideData() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT input,expected,email FROM test_data JOIN tests ON test_data.test_id = tests.id " +
                "JOIN test_authors ON tests.email_id = test_authors.id ORDER BY (test_data.id)");
        List<Map<String, Object>> testData = new ArrayList<>();
        while (resultSet.next()){
            Map<String, Object> sample = new HashMap<>();
            sample.put("input", Arrays.stream(resultSet.getString("input").split(","))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList()));
            sample.put("expected", resultSet.getInt("expected"));
            sample.put("email", resultSet.getString("email"));
            testData.add(sample);
        }
        System.out.println(testData);
        return testData.iterator();
    }

    @Test(dataProvider = "dp")
    public void testSumOfIntegers(Map<String, Object> sample){
        System.out.println("Test:::");

//        Assert.assertEquals(calculationService.summ(new ArrayList<>(Arrays.asList(1, 2, 3, 4))), 10);
        List<Integer> dates = (List<Integer>) sample.get("input");
        Assert.assertEquals(calculationService.summ(dates), sample.get("expected"));
    }
    @AfterClass
    //TODO: CLear table from database
    public void tearDown(){
        calculationService = null;
    }
}
