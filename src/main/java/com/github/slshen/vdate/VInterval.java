package com.github.slshen.vdate;

import java.io.Serializable;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * A interval consisting of 2 parts:
 * <ol>
 * <li>A number of years, months, and days (the date portion, corresponding to
 * {@link Period}
 * <li>And a number of seconds and nanoseconds (the time portion, corresponding
 * to {@link Duration}
 * </ol>
 * 
 * Either portion may be ZERO.
 * 
 * <p>
 * <ul>
 * <li>Intervals may be added to or subtracted from {@link VDate}, in which case
 * the time part is ignored.
 * <li>Intervals may be added to or subtracted from {@link VTimeOfDay}, in which
 * case the date part is ignored.
 * <li>Interval math on {@link VDateTime} and {@link VTimestamp} consider both
 * parts.
 * </ul>
 */
public class VInterval implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Period period;
	private final Duration duration;

	public static VInterval ZERO = new VInterval();

	public static VInterval of(long n, TimeUnit unit) {
		return ZERO.plus(n, unit);
	}

	/**
	 * Return a new interval with the period portion unchanged and the duration
	 * portion set to the value supplied.
	 * 
	 * Exception: for {@link TimeUnit#DAYS} modifies the period portion.
	 */
	public VInterval plus(long n, TimeUnit unit) {
		Objects.requireNonNull(unit);
		switch (unit) {
		case DAYS:
			return plus(n, ChronoUnit.DAYS);
		case NANOSECONDS:
		case MICROSECONDS:
		case HOURS:
		case MILLISECONDS:
		case MINUTES:
		case SECONDS:
			return new VInterval(period, Duration.ofSeconds(0, unit.toNanos(n)));
		default: // not reached
			throw new IllegalArgumentException();
		}
	}

	public static VInterval of(long n, ChronoUnit unit) {
		return ZERO.plus(n, unit);
	}

	/**
	 * Return a new interval. If the {@link ChronoUnit} is a calendar unit (years,
	 * months, or days) the the period portion is set to the value, and the duration
	 * portion is unused. If the {@link ChronoUnit} is a time unit then set the
	 * duration portion and leave the period part unchanged.
	 */
	public VInterval plus(long n, ChronoUnit unit) {
		Objects.requireNonNull(unit);
		switch (unit) {
		case CENTURIES:
			return new VInterval(period.plusYears(100 * (int) n), duration);
		case DAYS:
			return new VInterval(period.plusDays((int) n), duration);
		case DECADES:
			return new VInterval(period.plusYears(10 * (int) n), duration);
		case ERAS:
		case FOREVER:
			throw new IllegalArgumentException("unsupported unit " + unit);
		case HALF_DAYS:
			return plus(12, TimeUnit.HOURS);
		case HOURS:
			return plus(n, TimeUnit.HOURS);
		case MICROS:
			return plus(n, TimeUnit.MICROSECONDS);
		case MILLENNIA:
			return new VInterval(period.plusYears(1000 * (int) n), duration);
		case MILLIS:
			return plus(n, TimeUnit.MILLISECONDS);
		case MINUTES:
			return plus(n, TimeUnit.MINUTES);
		case MONTHS:
			return new VInterval(period.plusMonths((int) n), duration);
		case NANOS:
			return plus(n, TimeUnit.NANOSECONDS);
		case SECONDS:
			return plus(n, TimeUnit.SECONDS);
		case WEEKS:
			return new VInterval(period.plusDays(7 * (int) n), duration);
		case YEARS:
			return new VInterval(period.plusYears((int) n), duration);
		default: // not reached
			throw new IllegalArgumentException();
		}
	}

	public VInterval(Period period, Duration duration) {
		Objects.requireNonNull(period, "period");
		Objects.requireNonNull(duration, "duration");
		this.period = period;
		this.duration = duration;
	}

	public VInterval(Period period) {
		this(period, Duration.ZERO);
	}

	public VInterval(Duration duration) {
		this(Period.ZERO, duration);
	}

	public VInterval() {
		this(Period.ZERO, Duration.ZERO);
	}

	public Period getPeriod() {
		return period;
	}

	public Duration getDuration() {
		return duration;
	}

	public int getYears() {
		return period.getYears();
	}

	public int getMonths() {
		return period.getMonths();
	}

	public int getDays() {
		return period.getDays();
	}

	public long getSeconds() {
		return duration.getSeconds();
	}

	public int getNano() {
		return duration.getNano();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((period == null) ? 0 : period.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VInterval))
			return false;
		VInterval other = (VInterval) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (period == null) {
			if (other.period != null)
				return false;
		} else if (!period.equals(other.period))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		Period period = getPeriod();
		Duration duration = getDuration();
		if (!period.equals(Period.ZERO)) {
			b.append(period);
		}
		if (b.length() == 0 || !duration.equals(Duration.ZERO)) {
			if (b.length() > 0) {
				b.append(' ');
			}
			b.append(duration);
		}
		return b.toString();
	}

}
