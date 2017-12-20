/*
 * Copyright (c) 1996, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * (C) Copyright Taligent, Inc. 1996, 1997 - All Rights Reserved
 * (C) Copyright IBM Corp. 1996 - 1998 - All Rights Reserved
 *
 *   The original version of this source code and documentation is copyrighted
 * and owned by Taligent, Inc., a wholly-owned subsidiary of IBM. These
 * materials are provided under terms of a License Agreement between Taligent
 * and Sun. This technology is protected by multiple US and International
 * patents. This notice and attribution to Taligent may not be removed.
 *   Taligent is a registered trademark of Taligent, Inc.
 *
 */

package javax.measure.unit;


import java.text.ParsePosition;

class SubUnit {
    private String subUnit = "";
    private int parsePositionIncrement = 0;

    /**
     * Extract the first subunit from a unit product string. The subunit is the first part of the unit string that
     * should be parsed. The subunit may also be a unit product.
     * For example: <br>
     * Input: kg/s               SubUnit: kg <br>
     * Input: (m/s)/kg           SubUnit: m/s <br>
     * Input: ((m/s)/(kg/h))*N   SubUnit: (m/s)/(kg/h) <br>
     *
     * Parse position increment is also provided which is the count of the characters used up by the subunit.
     * SubUnit is used in conjunction with {@link UnitFormat#parseProductUnit(CharSequence, ParsePosition)} which
     * tracks the parse position.
     *
     * @param unit
     *         The unit string from which to extract a subunit.
     */
    SubUnit(CharSequence unit) {
        if (unit.charAt(0) == '(') {
            // When the input string starts with a '(', find the outside parenthesis pair and return everything
            // inside. The outside parenthesis pair is not included in the returned subunit.
            StringBuilder subUnitBuilder = new StringBuilder("");
            int leftParenCount = 1;
            int rightParenCount = 0;

            // Strip the leading '(' and process the rest of the characters.
            for (char c : unit.subSequence(1, unit.length()).toString().toCharArray()) {
                if (c == '(') {
                    leftParenCount++;
                } else if (c == ')') {
                    rightParenCount++;
                    if (rightParenCount == leftParenCount) {
                        // Everything inside the outside parenthesis pair has been found. We are done.
                        break;
                    }
                }

                subUnitBuilder.append(c);
            }
            this.subUnit = subUnitBuilder.toString();
            this.parsePositionIncrement = this.subUnit.length() + 2; // +2 for the outside paren pair
        } else {
            // When the input string doesn't start with a '(', extract all the characters up until a multiply, divide
            // or closing ')'. (There may be an unmatched closing parenthesis because the leading parenthesis may
            // have already been stripped by UnitFormat#parseProductUnit.)
            this.subUnit = unit.toString().split("[\\/\\)*·]")[0];
            this.parsePositionIncrement = this.subUnit.length();
        }
    }

    /**
     * The count of the characters used up by the subunit. SubUnit is used in conjunction with {@link
     * UnitFormat#parseProductUnit(CharSequence, ParsePosition)} which tracks the
     * parse position.
     *
     * @return The count of the characters used up by the subunit.
     */
    int getParsePositionIncrement() { return parsePositionIncrement; }

    /**
     * The first subunit from a unit product string. The subunit is the first part of the unit string that should
     * be parsed. The subunit may also be a unit product.
     *
     * @return The subunit.
     */
    String getSubUnit() { return subUnit; }
}