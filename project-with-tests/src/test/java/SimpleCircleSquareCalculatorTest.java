import org.junit.jupiter.api.RepeatedTest;
import ru.rsreu.SimpleCircleSquareCalculator;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleCircleSquareCalculatorTest {

    private static final double DELTA = 1e-7;

    @RepeatedTest(50)
    public void testDefaultCalculatorUsage() {
        final double EXPECTED_VALUE = 12867.9635091;
        SimpleCircleSquareCalculator calculator = new SimpleCircleSquareCalculator();

        assertEquals(EXPECTED_VALUE, calculator.calculate(64), DELTA);
    }

    @RepeatedTest(50)
    public void testZeroCalculatorUsage() {
        SimpleCircleSquareCalculator calculator = new SimpleCircleSquareCalculator();

        assertEquals(0.0, calculator.calculate(0.0), DELTA);
    }

    @RepeatedTest(50)
    public void testNegativeRadiusCalculatorUsage() {
        SimpleCircleSquareCalculator calculator = new SimpleCircleSquareCalculator();

        assertThrows(IllegalArgumentException.class, () -> calculator.calculate(-50.0));
    }

}
