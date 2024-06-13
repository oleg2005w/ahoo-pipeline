package student.examples;

import java.util.List;
import java.util.stream.Stream;

public class CalculationService {
    public int summ(int value1, int value2){
        return value1 + value2;
    }
    public int summ (int[] values){
        int res = 0;
        for (int i = 0; i < values.length; i++) {
              res += values[i];
        }
        return res;
    }
    public int summ (List<Integer> values){
        Stream<Integer> soi = values.stream();
        return values.stream().reduce(0,(acc, current) -> acc + current);
    }
}
