package javax.measure.converter;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    // @Test
    // Execute this manually to test the concurrency of the cache converters.
    // Set CONVERTER_CACHE_SIZE_LIMIT to 3 (a value lower than data.length to force many cache clear actions)
    public void multiThreadTest() throws InterruptedException {
        final int THREAD_COUNT = 1_000;
        final int DATA_ITERATIONS = 10_000;
        final AtomicInteger errorCount = new AtomicInteger(0);
        final UnitFormat format = UnitFormat.getUCUMInstance();
        Object[][] data = {
                {2.0, "m", 200.0, "cm"}, {2.0, "m", 2000.0, "mm"}, {2.0, "m", 0.002, "km"}, {2.0, "m", 20.0, "dm"},
                {2.0, "cm", 0.02, "m"}, {2.0, "cm", 20.0, "mm"}, {2.0, "cm", 0.00002, "km"}, {2.0, "cm", 0.2, "dm"},
                {2000.0, "mm", 2.0, "m"},
                {0.002, "km", 2.0, "m"},
                {20.0, "dm", 2.0, "m"},
                {1.0, "mm^2", 0.01, "cm^2"},
                {0.01, "cm^2", 0.000001, "m^2"},
                {0.0001, "m^2", 0.0000000001, "km^2"},
                {1_000_123.0, "T·g·s/(h·kg)", 277.81194444444446, "T·g/kg"},
                {1.0, "g/mL", 1000.0, "kg/m^3"}, {1.0, "g/mL", 62.42796057614461, "lb/ft^3"},
                {1.0, "kPa", 0.001, "MPa"},
                {1.0, "L/s", 60.0, "L/min"},
                {60.0, "L/min", 3600.0, "L/h"}
        };



        List<Thread> threads = new ArrayList<>(THREAD_COUNT);
        for(int i = 0; i < THREAD_COUNT; i++) {
            threads.add(new Thread(() -> {
                for(int j = 0; j < DATA_ITERATIONS; j++){
                    for (Object[] datum : data) {
                        try {
                            Unit<?> fromUnit = format.parseProductUnit((String) datum[1], new ParsePosition(0));
                            Unit<?> toUnit = format.parseProductUnit((String) datum[3], new ParsePosition(0));
                            double inputValue = (double) datum[0];
                            double outputValue = (double) datum[2];
                            double output = fromUnit.getConverterTo(toUnit).convert(inputValue);
                            if(output != outputValue) {
                                System.out.println("ERROR: Expected " + outputValue + " but got " + output);
                                errorCount.incrementAndGet();
                            }
                        } catch (ParseException e) {
                            errorCount.incrementAndGet();
                            throw new RuntimeException(e);
                        }
                    }
                }}));
        }

        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }

        assertThat(errorCount.get()).isEqualTo(0);
    }
}
