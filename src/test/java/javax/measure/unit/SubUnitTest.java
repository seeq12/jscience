package javax.measure.unit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;


public class SubUnitTest {
    @Test
    public void testSubUnit() {
        SubUnit subUnit = new SubUnit("kg/s");
        assertThat(subUnit.getSubUnit()).isEqualTo("kg");
        assertThat(subUnit.getParsePositionIncrement()).isEqualTo(2);

        subUnit = new SubUnit("(m/s)/kg ");
        assertThat(subUnit.getSubUnit()).isEqualTo("m/s");
        assertThat(subUnit.getParsePositionIncrement()).isEqualTo(5);

        subUnit = new SubUnit("((m/s)/(kg/h))*N");
        assertThat(subUnit.getSubUnit()).isEqualTo("(m/s)/(kg/h)");
        assertThat(subUnit.getParsePositionIncrement()).isEqualTo(14);
    }
}