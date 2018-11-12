package com.github.slshen.vdate;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A wrapper class for {@link ZonedDateTime}.
 * 
 * @author sshen
 */
public class VDateTime implements Comparable<VDateTime>, Serializable {
	private static final long serialVersionUID = 1L;
	private final ZonedDateTime dateTime;

	public static final ZoneId UTC = ZoneOffset.UTC;
	public static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
	public static final Pattern ZONE_ID_PATTERN = Pattern.compile("\\s*(\\S+)");

	public VDateTime(ZonedDateTime dateTime) {
		Objects.requireNonNull(dateTime, "dateTime");
		this.dateTime = dateTime;
	}

	public VDateTime(VDate date, VTimeOfDay time, ZoneId zoneId) {
		this(ZonedDateTime.of(date.toLocalDate(), time.toLocalTime(), zoneId));
	}

	public VDateTime(VDate date, VTimeOfDay time) {
		this(date, time, DEFAULT_ZONE);
	}

	/**
	 * Construct a datetime by parsing a string. Unlike
	 * {@link ZonedDateTime#parse(CharSequence)} neither the date, nor time, nor
	 * timezone portions are required:
	 * <ul>
	 * <li>If the date is missing, then {@link LocalDate#now()} is used.
	 * <li>If the time is missing, then {@link LocalTime#MIDNIGHT} is used.
	 * <li>If the timezone is missing, then {@link #DEFAULT_ZONE} is used.
	 * </ul>
	 * In addition, dates in the form "2018-1-1" are acceptable (leading 0's are not
	 * required.)
	 * 
a	 * @param text
	 */
	public VDateTime(CharSequence text) {
		this(parse(text));
	}

	public static ZonedDateTime parse(CharSequence text) {
		// ZonedDateTime is picky about ISO datetime formats
		Matcher d = VDate.DATE_PATTERN.matcher(text);
		LocalDate date = VDate.extract(d).orElse(null);
		if (date != null) {
			text = text.subSequence(d.end(), text.length());
		}
		Matcher t = VTimeOfDay.TIME_PATTERN.matcher(text);
		LocalTime time = VTimeOfDay.extract(t).orElse(null);
		if (time != null) {
			text = text.subSequence(t.end(), text.length());
		}
		Matcher z = ZONE_ID_PATTERN.matcher(text);
		ZoneId zoneId = DEFAULT_ZONE;
		if (z.lookingAt()) {
			zoneId = ZoneId.of(z.group(1));
		}
		return ZonedDateTime.of(date != null ? date : LocalDate.now(), time != null ? time : LocalTime.MIDNIGHT,
				zoneId);
	}

	public VDateTime withDate(VDate date) {
		return new VDateTime(date, getTime(), getZoneId());
	}

	public VDateTime withTime(VTimeOfDay time) {
		return new VDateTime(getDate(), time, getZoneId());
	}

	public VDateTime withZoneId(ZoneId zoneId) {
		return new VDateTime(getDate(), getTime(), zoneId);
	}

	public VDate getDate() {
		return new VDate(dateTime.toLocalDate());
	}

	public VTimeOfDay getTime() {
		return new VTimeOfDay(dateTime.toLocalTime());
	}

	public ZoneId getZoneId() {
		return dateTime.getZone();
	}

	public ZonedDateTime toZonedDateTime() {
		return dateTime;
	}

	public VDateTime plusInterval(VInterval i) {
		return new VDateTime(toZonedDateTime().plus(i.getPeriod()).plus(i.getDuration()));
	}

	public VDateTime minusInterval(VInterval i) {
		return new VDateTime(toZonedDateTime().minus(i.getPeriod()).minus(i.getDuration()));
	}

	public VInterval intervalBetween(VDateTime dateTime) {
		ZonedDateTime d1 = this.dateTime;
		ZonedDateTime d2 = dateTime.dateTime;
		int years = (int) d1.until(d2, ChronoUnit.YEARS);
		d2 = d2.minusYears(years);
		int months = (int) d1.until(d2, ChronoUnit.MONTHS);
		d2 = d2.minusMonths(months);
		int days = (int) d1.until(d2, ChronoUnit.DAYS);
		d2 = d2.minusDays(days);
		long seconds = d1.until(d2, ChronoUnit.SECONDS);
		d2 = d2.minusSeconds(seconds);
		long nanos = d1.until(d2, ChronoUnit.NANOS);
		return new VInterval(Period.of(years, months, days), Duration.ofSeconds(seconds, nanos));
	}

	@Override
	public int compareTo(VDateTime o) {
		return toZonedDateTime().compareTo(o.toZonedDateTime());
	}

	@Override
	public String toString() {
		return toZonedDateTime().toString();
	}

	public VTimestamp toTimestamp() {
		return new VTimestamp(dateTime.toInstant());
	}

}
