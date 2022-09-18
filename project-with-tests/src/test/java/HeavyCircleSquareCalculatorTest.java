import org.junit.jupiter.api.Test;
import ru.rsreu.HeavyCircleSquareCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HeavyCircleSquareCalculatorTest {
    private static final double DELTA = 1.0;

    @Test
    public void testHeavyCalculatorUsage() {
        final double EXPECTED_VALUE = 12867.9635091;
        HeavyCircleSquareCalculator calculator = new HeavyCircleSquareCalculator(250_000_000);

        assertEquals(EXPECTED_VALUE, calculator.calculate(64), DELTA);
    }

}
