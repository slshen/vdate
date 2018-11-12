package com.github.slshen.vdate;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VDate implements Comparable<VDate>, Serializable {
	private static final long serialVersionUID = 1L;
	private final LocalDate localDate;

	public static final Pattern DATE_PATTERN = Pattern.compile("\\s*((\\d{4})-(\\d{1,2})-(\\d{1,2}))");

	public VDate(LocalDate localDate) {
		Objects.requireNonNull(localDate, "localDate");
		this.localDate = localDate;
	}

	public VDate(int year, int month, int day) {
		this(LocalDate.of(year, month, day));
	}

	public VDate(String ymd) {
		this(parse(ymd));
	}

	public static LocalDate parse(CharSequence text) {
		Matcher m = DATE_PATTERN.matcher(text);
		return extract(m).orElseThrow(() -> new DateTimeParseException("not a date", text, 0));
	}

	public static Optional<LocalDate> extract(Matcher m) {
		if (m.lookingAt()) {
			try {
				return Optional.of(LocalDate.of(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)),
						Integer.parseInt(m.group(4))));
			} catch (NumberFormatException e) {
				throw new DateTimeParseException("not a date", m.group(0), 0, e);
			}
		}
		return Optional.empty();
	}

	public VDate() {
		localDate = LocalDate.now();
	}

	public int getYear() {
		return localDate.getYear();
	}

	public int getMonth() {
		return localDate.getMonthValue();
	}

	public int getDay() {
		return localDate.getDayOfMonth();
	}

	public LocalDate toLocalDate() {
		return localDate;
	}

	public Date toDate() {
		return new Date(localDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
	}

	public VDate plusDays(long days) {
		return new VDate(localDate.plusDays(days));
	}

	public VDate minusDays(long days) {
		return new VDate(localDate.minusDays(days));
	}

	public VDate plusWeeks(long weeks) {
		return new VDate(localDate.plusWeeks(weeks));
	}

	public VDate minusWeeks(long weeks) {
		return new VDate(localDate.minusWeeks(weeks));
	}

	public VDate plusMonths(long months) {
		return new VDate(localDate.plusMonths(months));
	}

	public VDate minusMonths(long months) {
		return new VDate(localDate.minusMonths(months));
	}

	public VDate plusYears(long years) {
		return new VDate(localDate.plusYears(years));
	}

	public VDate minusYears(long years) {
		return new VDate(localDate.minusYears(years));
	}

	public long daysBetween(VDate d) {
		return ChronoUnit.DAYS.between(localDate, d.localDate);
	}

	public long monthsBetween(VDate d) {
		return ChronoUnit.MONTHS.between(localDate, d.localDate);
	}

	public long weeksBetween(VDate d) {
		return ChronoUnit.WEEKS.between(localDate, d.localDate);
	}

	public long yearsBetween(VDate d) {
		return ChronoUnit.YEARS.between(localDate, d.localDate);
	}

	public VInterval intervalBetween(VDate d) {
		LocalDate ld = d.localDate;
		int years = (int) ChronoUnit.YEARS.between(localDate, ld);
		ld = ld.minusYears(years);
		int months = (int) ChronoUnit.MONTHS.between(localDate, ld);
		ld = ld.minusMonths(months);
		int days = (int) ChronoUnit.DAYS.between(localDate, ld);
		return new VInterval(Period.of(years, months, days));
	}

	/**
	 * Returns a new date by adding the {@link Period} (year, month, days) portion
	 * of the interval. (The seconds based portion of the interval is ignored.)
	 */
	public VDate plusInterval(VInterval interval) {
		return new VDate(localDate.plus(interval.getPeriod()));
	}

	public VDate minusInterval(VInterval interval) {
		return new VDate(localDate.minus(interval.getPeriod()));
	}

	@Override
	public int hashCode() {
		return localDate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VDate))
			return false;
		VDate other = (VDate) obj;
		return localDate.equals(other.localDate);
	}

	@Override
	public String toString() {
		return localDate.toString();
	}

	@Override
	public int compareTo(VDate o) {
		return localDate.compareTo(o.localDate);
	}

}
