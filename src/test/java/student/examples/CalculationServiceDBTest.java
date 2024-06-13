package student.examples;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO test_data VALUES (1,?,?,1)");

        //2.Insert test info into database.
        result.executeUpdate("INSERT INTO tests VALUES (1, 'Sum of negatives', false, now())");

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


    @Test
    public void testSumOfLIst(){

        Assert.assertEquals(calculationService.summ(new ArrayList<>(Arrays.asList(1, 2, 3, 4))), 10);
    }
    @AfterClass
    public void tearDown(){
        calculationService = null;
    }
}
