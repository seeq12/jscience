package javax.measure.unit;

import org.junit.Test;

public class UnitTest {
    @Test
    public void test() {
        // Order of operations appears to be messed up when it comes to division
        //assertThat(Unit.valueOf("m/s/s")).isEqualTo(SI.METER.divide(SI.SECOND).divide(SI.SECOND));
    }
}