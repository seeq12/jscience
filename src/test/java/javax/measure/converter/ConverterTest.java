package javax.measure.converter;

import java.text.ParseException;
import java.text.ParsePosition;

import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ConverterTest {

    @Test
    public void equalsTest() throws ParseException {
        UnitFormat format = UnitFormat.getInstance();
        {
            // BaseUnit test
            Unit<?> a = format.parseProductUnit("m", new ParsePosition(0));
            Unit<?> b = format.parseProductUnit("m", new ParsePosition(0));
            Unit<?> c = format.parseProductUnit("km", new ParsePosition(0));

            assertThat(a.getConverterTo(b)).isEqualTo(b.getConverterTo(a));
            assertThat(a.getConverterTo(c)).isEqualTo(b.getConverterTo(c));
            assertThat(c.getConverterTo(a)).isEqualTo(c.getConverterTo(b));

            assertThat(a.getConverterTo(b)).isNotEqualTo(a.getConverterTo(c));
        }
        {
            // CompoundUnit test
            Unit<?> a = format.parseProductUnit("N", new ParsePosition(0));
            Unit<?> b = format.parseProductUnit("kg*m/s^2", new ParsePosition(0));
            Unit<?> c = format.parseProductUnit("kN", new ParsePosition(0));

            assertThat(a.getConverterTo(b)).isEqualTo(b.getConverterTo(a));
            assertThat(a.getConverterTo(c)).isEqualTo(b.getConverterTo(c));
            assertThat(c.getConverterTo(a)).isEqualTo(c.getConverterTo(b));

            assertThat(a.getConverterTo(b)).isNotEqualTo(a.getConverterTo(c));
        }
    }
}
