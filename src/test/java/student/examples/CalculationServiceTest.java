package student.examples;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalculationServiceTest {
    private CalculationService calculationService;
    @BeforeClass
    public void setup(){
        calculationService =new CalculationService();
    }
    @Test
    public void testSumOfTwo(){
        Assert.assertEquals(calculationService.summ(1,2), 3);
    }
    @Test
    public void testSumOfArray(){
        Assert.assertEquals(calculationService.summ(new int[]{1,2,3}), 6);
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
