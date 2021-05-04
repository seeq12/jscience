package javax.measure.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.measure.quantity.Miscellaneous;

import org.assertj.core.api.SoftAssertions;
import org.junit.Test;


public class UnitTest {
    @Test
    public void testBasic() {
        assertThat(Unit.valueOf("g/m/s")).isEqualTo(SI.GRAM.divide(SI.METER).divide(SI.SECOND));
        assertThat(Unit.valueOf("(g/m)/s")).isEqualTo(SI.GRAM.divide(SI.METER).divide(SI.SECOND));
    }

    @Test
    public void testUnmatchedParentheses() {
        assertThatThrownBy(() -> Unit.valueOf("N/(g/s")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unmatched parenthesis").hasMessageContaining("index 6");

        assertThatThrownBy(() -> Unit.valueOf("N/g)/s")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unmatched parenthesis").hasMessageContaining("index 3");

        assertThatThrownBy(() -> Unit.valueOf("((N/g)/s")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unmatched parenthesis").hasMessageContaining("index 8");

        assertThatThrownBy(() -> Unit.valueOf("(N/g))/s")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unmatched parenthesis").hasMessageContaining("index 5");
    }


    // The jscience library 4.3.1 did not follow the order of operations when division was involved.  These tests
    // all failed before the fix was made.
    @Test
    public void testParseUnitsWithDivisions() throws Exception {

        SoftAssertions softly = new SoftAssertions();

        for (Map.Entry<String, String> divisionUnit : DIVISION_UNITS.entrySet()) {
            String parsedUnit = Unit.valueOf(divisionUnit.getKey()).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue());

            // Test it again but replace * with ·.  Example: m/kg*h -> m/kg·h
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("*", "·")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue());

            // Test it again but replace N with N^2.  Example: N/kg*h -> N^2/kg*h
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("N", "N^2")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue().replace("N", "N²"));

            // Test it again but replace kg with kg^2.  Example: m/kg*h -> m/kg^2*h
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("kg", "kg^2")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue().replace("kg", "kg²"));

            // Test it again but replace kg with kg².  Example: m/kg*h -> m/kg²*h
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("kg", "kg²")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue().replace("kg", "kg²"));

            // Test it again but replace h with h^2.  Example: m/kg*h -> m/kg*h^2
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("h", "h^2")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue().replace("h", "h²"));

            // Test it again but replace m with m^2.  Example: m/kg*h -> m^2/kg*h
            parsedUnit = Unit.valueOf(divisionUnit.getKey().replace("m", "m^2")).toString();
            softly.assertThat(parsedUnit).isEqualTo(divisionUnit.getValue().replace("m", "m²"));

        }

        softly.assertAll();
    }

    @Test
    public void testPressureChanges() {
        assertThat(NonSI.BAR.getConverterTo(SI.PASCAL).convert(1.0)).isEqualTo(100_000.0);
        assertThat(SI.PASCAL.getConverterTo(NonSI.BAR).convert(100_000.0)).isEqualTo(1.0);
        assertThat(NonSI.BAR.toString()).isEqualTo("bar");
        assertThat(Unit.valueOf("bar")).isEqualTo(NonSI.BAR);
        assertThat(Unit.valueOf("daµbar")).as("Regrettable that we need to expose this odd unit").isNotNull();
    }

    @Test
    public void testCubicCentiMetreChanges() {
        assertThat(NonSI.CUBIC_CENTI_METER.getConverterTo(SI.CUBIC_METRE).convert(1_000_000.0)).isEqualTo(1.0);
        assertThat(SI.CUBIC_METRE.getConverterTo(NonSI.CUBIC_CENTI_METER).convert(1)).isEqualTo(1_000_000.0);
        assertThat(NonSI.CUBIC_CENTI_METER.toString()).isEqualTo("cc");
        assertThat(Unit.valueOf("cc")).isEqualTo(NonSI.CUBIC_CENTI_METER);
        assertThat(Unit.valueOf("cc")).isNotNull();
    }

    @Test
    public void testMicronChanges() {
        assertThat(NonSI.MICRON.getConverterTo(SI.CENTIMETER).convert(10_000.0)).isEqualTo(1.0);
        assertThat(SI.CENTIMETER.getConverterTo(NonSI.MICRON).convert(1)).isEqualTo(10_000.0);
        assertThat(NonSI.MICRON.toString()).isEqualTo("micron");
        assertThat(Unit.valueOf("micron")).isEqualTo(NonSI.MICRON);
        assertThat(Unit.valueOf("micron")).isNotNull();
    }

    @Test
    public void testPartsPerMillionVolumeChanges() {
        assertThat(NonSI.PPMV.getConverterTo(Miscellaneous.ppm).convert(1.0)).isEqualTo(1.0);
        assertThat(Miscellaneous.ppm.getConverterTo(NonSI.PPMV).convert(1.0)).isEqualTo(1.0);
        assertThat(NonSI.PPMV.toString()).isEqualTo("ppmv");
        assertThat(Unit.valueOf("ppmv")).isEqualTo(NonSI.PPMV);
        assertThat(Unit.valueOf("ppmv")).isNotNull();

        assertThat(NonSI.PPMW.getConverterTo(Miscellaneous.ppm).convert(1.0)).isEqualTo(1.0);
        assertThat(Miscellaneous.ppm.getConverterTo(NonSI.PPMW).convert(1.0)).isEqualTo(1.0);
        assertThat(NonSI.PPMW.toString()).isEqualTo("ppmw");
        assertThat(Unit.valueOf("ppmw")).isEqualTo(NonSI.PPMW);
        assertThat(Unit.valueOf("ppmw")).isNotNull();
    }

    private static final Map<String, String> DIVISION_UNITS;

    static {
        Map<String, String> map = new LinkedHashMap<String, String>();

        // 1 Divide
        map.put("N/kg", "N/kg");

        // 2 Divides
        map.put("N/kg/h", "N/(kg·h)");
        map.put("(N/kg)/h", "N/(kg·h)");
        map.put("N/(kg/h)", "N·h/kg");
        map.put("N/(kg)/h", "N/(kg·h)");

        // 1 Divide+ 1 Multiply
        map.put("N*kg/h", "N·kg/h");
        map.put("(N*kg)/h", "N·kg/h");
        map.put("N*(kg/h)", "N·kg/h");
        map.put("N*(kg)/h", "N·kg/h");

        map.put("N/kg*h", "N·h/kg");
        map.put("(N/kg)*h", "N·h/kg");
        map.put("N/(kg*h)", "N/(kg·h)");
        map.put("N/(kg)*h", "N·h/kg");

        // 3 Divides
        map.put("N/kg/m/h", "N/(kg·m·h)");
        map.put("((N/kg)/m)/h", "N/(kg·m·h)");
        map.put("(N/kg)/(m/h)", "N·h/(kg·m)");
        map.put("N/(kg/(m/h))", "N·m/(kg·h)");

        map.put("(N/kg)/m/h", "N/(kg·m·h)");
        map.put("N/(kg/m)/h", "N·m/(kg·h)");
        map.put("N/kg/(m/h)", "N·h/(kg·m)");

        // 2 Divides + 1 Multiply
        map.put("N*kg/m/h", "N·kg/(m·h)");
        map.put("N/kg*m/h", "N·m/(kg·h)");
        map.put("N/kg/m*h", "N·h/(kg·m)");

        map.put("(N*kg)/m/h", "N·kg/(m·h)");
        map.put("(N/kg)*m/h", "N·m/(kg·h)");
        map.put("(N/kg)/m*h", "N·h/(kg·m)");

        map.put("N*(kg/m)/h", "N·kg/(m·h)");
        map.put("N/(kg*m)/h", "N/(kg·m·h)");
        map.put("N/(kg/m)*h", "N·m·h/kg");

        map.put("N*kg/(m/h)", "N·kg·h/m");
        map.put("N/kg*(m/h)", "N·m/(kg·h)");
        map.put("N/kg/(m*h)", "N/(kg·m·h)");

        // 1 Divide + 2 Multiplies
        map.put("N/kg*m*h", "N·m·h/kg");
        map.put("N*kg/m*h", "N·kg·h/m");
        map.put("N*kg*m/h", "N·kg·m/h");

        map.put("(N/kg)*m*h", "N·m·h/kg");
        map.put("(N*kg)/m*h", "N·kg·h/m");
        map.put("(N*kg)*m/h", "N·kg·m/h");

        map.put("N/(kg*m)*h", "N·h/(kg·m)");
        map.put("N*(kg/m)*h", "N·kg·h/m");
        map.put("N*(kg*m)/h", "N·kg·m/h");

        map.put("N/kg*(m*h)", "N·m·h/kg");
        map.put("N*kg/(m*h)", "N·kg/(m·h)");
        map.put("N*kg*(m/h)", "N·kg·m/h");

        // 3 Divides Total, 2 within ()
        map.put("(N/kg/m)/h", "N/(kg·m·h)");
        map.put("N/(kg/m/h)", "N·m·h/kg");

        // 4 Divides
        map.put("N/kg/m/h/K", "N/(kg·m·h·K)");

        DIVISION_UNITS = Collections.unmodifiableMap(map);
    }
}