import org.junit.Assert;
import org.junit.Test;
import ru.rsreu.CircleSquareCalculator;

public class CircleSquareCalculatorTest {

    private static final double DELTA = 1e-7;

    @Test
    public void testDefaultCalculatorUsage() {
        CircleSquareCalculator calculator = new CircleSquareCalculator();

        Assert.assertEquals(calculator.calculate(64), 12867.9635091, DELTA);
    }

    @Test
    public void testZeroCalculatorUsage() {
        CircleSquareCalculator calculator = new CircleSquareCalculator();

        Assert.assertEquals(calculator.calculate(0.0), 0.0, DELTA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeRadiusCalculatorUsage() {
        CircleSquareCalculator calculator = new CircleSquareCalculator();

        Assert.assertEquals(calculator.calculate(-50.0), 7853.98163397, DELTA);
    }
}
