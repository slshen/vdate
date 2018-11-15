Vernacular Date and Time
====
[![Build Status](https://travis-ci.com/slshen/vdate.svg?branch=master)](https://travis-ci.com/slshen/vdate)

The `java.time` package has classes with non-obvious names and methods
in non-obvious places.  These small wrapper classes remedy that.

V-Class | `java.time` Class | Description
------- | ----------------- | ------------
`VDate` | `LocalDate` | A pure date in the form year-month-day.
`VTimeOfDay` | `LocalTime` | A nanosecond-accurate time of day as one might read off a clock.
`VDateTime` | `ZonedDateTime` | A date and time and time zone, e.g. April 1, 1982, America/New_York
`VTimestamp` | `Instant` | A nanonsecond-accurate point in time, without timezone.
`VInterval` | `Period` and `Duration` | An interval of time, measured in years, months, and days (`Period`) and seconds and nanoseconds (`Duration`)

Style notes:

* The V-Classes mostly have constructors instead of static methods

* The string constructors are less picky about exactly conforming to
  ISO standards (following the "be liberal in what you accept"
  principle)

* The `java.time` classes expose operations that are documented not to
work, e.g.  `LocalDate.of(2018, 1, 28).plus(Duration.ofHours(24))`
will throw an exception (but you have to read the documentation carefully to
know this.)

