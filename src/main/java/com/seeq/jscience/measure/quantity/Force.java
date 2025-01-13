/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2006 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package com.seeq.jscience.measure.quantity;
import com.seeq.jscience.measure.unit.SI;
import com.seeq.jscience.measure.unit.Unit;

/**
 * This interface represents a quantity that tends to produce an acceleration
 * of a body in the direction of its application. The system unit for
 * this quantity is "N" (Newton).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, January 14, 2006
 */
public interface Force extends Quantity {

    /**
     * Holds the SI unit (Système International d'Unités) for this quantity.
     */
    public final static Unit<Force> UNIT = SI.NEWTON;

}