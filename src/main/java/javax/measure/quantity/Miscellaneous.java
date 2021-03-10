package javax.measure.quantity;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.BaseUnit;
import javax.measure.unit.Unit;

/**
 * This interface represents a measure of miscellaneous quantities.
 * In general units of this interface cannot be freely converted. This quantity is dimensionless.
 *
 * @author  <a href="mailto:ryan.smith@seeq.com">Ryan Smith</a>
 * @version 4.3.4, March 2021
 */
public interface Miscellaneous extends Quantity {
    public final static Unit<Miscellaneous> SG = new BaseUnit<>("SG");

    public final static Unit<Miscellaneous> API = SG.transform(new SpecificGravityConverter(false));

    public final static Unit<Miscellaneous> ppm = new BaseUnit<>("ppm");

    public final static Unit<Miscellaneous> ppb = ppm.divide(1000);
    /**
     * This class represents the Specific Gravity Converter. See https://en.wikipedia.org/wiki/API_gravity.
     */
    class SpecificGravityConverter extends UnitConverter {
        boolean _invert;

        private SpecificGravityConverter(boolean invert) {
            _invert = invert;
        }

        @Override
        public UnitConverter inverse() {
            return new SpecificGravityConverter(!_invert);
        }

        @Override
        public double convert(double x) {
            return _invert ? (141.5) / (x + 131.5) : (141.5 / x) - 131.5;
        }

        @Override
        public boolean isLinear() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof SpecificGravityConverter))
                return false;
            SpecificGravityConverter that = (SpecificGravityConverter) obj;
            return this._invert == that._invert;
        }

        @Override
        public int hashCode() {
            return _invert ? 1 : 0;
        }
    }
}
