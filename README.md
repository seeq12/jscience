jscience
========

A stripped down fork of JScience's unit library (version 4.3.1) with various bug fixes.

* 4.3.2: Fixed an error with division order of operations.
* 4.3.3: Removed currency exchange support (all currencies on own dimension).
* 5.0.0: Added new dimensions to previously dimensionless concepts (radian, ppm, dB, and a few others).
    * Technically not a major version per semver, but the last one should have been.
* 5.1.0: Reworked bar to be an AlternateUnit and not a TransformedUnit to avoid mbar/hPa collisions.
* 5.2.1: Created alternate names for some units (in the future, these can be done within CRAB).
* 5.2.2: Improved efficiency of UnitConverter.Equals in some cases.
