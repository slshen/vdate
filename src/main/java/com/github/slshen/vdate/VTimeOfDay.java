package com.github.slshen.vdate;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A time of day corresponding to {@link LocalTime}. This is a time as if read
 * off a wall clock. It does not include a date or timezone.
 */
public class VTimeOfDay implements Comparable<VTimeOfDay>, Serializable {
	private static final long serialVersionUID = 1L;
	private final LocalTime time;

	public static VTimeOfDay MIDNIGHT = new VTimeOfDay(LocalTime.MIDNIGHT);
	public static final Pattern TIME_PATTERN = Pattern
			.compile("\\s*(\\d{1,2}):(\\d{2})(:(\\d{2})(\\.(\\d+))?)?\\s*([AaPp][Mm])?");

	public VTimeOfDay(LocalTime time) {
		Objects.requireNonNull(time, "time");
		this.time = time;
	}

	public VTimeOfDay() {
		this(LocalTime.now());
	}

	public VTimeOfDay(CharSequence text) {
		this(parse(text));
	}

	/**
	 * Parse a time from some text, loosely following the ISO standard.
	 * Can handle the following formats:
	 * <ul>
	 * <li><code>H:MM</code> (seconds can be omitted, the hour does not require a leading 0.
	 * <li><code>H:MM:SS</code> fractional seconds are optional
	 * <li><code>H:MM:SS.sss</code> 
	 * </ul>
	 * 
	 * The time may optionally be followed by an AM or PM indicator.
	 */
	public static LocalTime parse(CharSequence text) {
		Matcher m = TIME_PATTERN.matcher(text);
		return extract(m).orElseThrow(() -> new DateTimeParseException("not a time", text, 0));
	}

	public static Optional<LocalTime> extract(Matcher m) {
		if (m.lookingAt()) {
			try {
				String seconds = m.group(4);
				String secondsFraction = m.group(6);
				int nanos = 0;
				if (secondsFraction != null) {
					if (secondsFraction.length() > 9) {
						// truncate
						secondsFraction = secondsFraction.substring(0, 9);
					}
					nanos = Integer.parseInt(secondsFraction) * (int) Math.pow(10, 9 - secondsFraction.length());
				}
				String amPm = m.group(7);
				int hoursOffset = 0;
				if ("PM".equalsIgnoreCase(amPm)) {
					hoursOffset = 12;
				}
				return Optional.of(LocalTime.of(hoursOffset + Integer.parseInt(m.group(1)),
						Integer.parseInt(m.group(2)), seconds != null ? Integer.parseInt(seconds) : 0, nanos));
			} catch (NumberFormatException e) {
				throw new DateTimeParseException("not a time", m.group(0), 0, e);
			}
		}
		return Optional.empty();
	}

	public int getHour() {
		return time.getHour();
	}

	public int getMinute() {
		return time.getMinute();
	}

	public int getSecond() {
		return time.getSecond();
	}

	public int getNano() {
		return time.getNano();
	}

	public VTimeOfDay plusHours(long hours) {
		return new VTimeOfDay(time.plusHours(hours));
	}

	public VTimeOfDay minusHours(long hours) {
		return new VTimeOfDay(time.minusHours(hours));
	}

	public VTimeOfDay plusMinutes(long minutes) {
		return new VTimeOfDay(time.plusMinutes(minutes));
	}

	public VTimeOfDay minusMinutes(long minutes) {
		return new VTimeOfDay(time.minusMinutes(minutes));
	}

	public VTimeOfDay plusSeconds(long seconds) {
		return new VTimeOfDay(time.plusSeconds(seconds));
	}

	public VTimeOfDay minusSeconds(long seconds) {
		return new VTimeOfDay(time.minusSeconds(seconds));
	}

	public VTimeOfDay plusInterval(VInterval interval) {
		return new VTimeOfDay(time.plus(interval.getDuration()));
	}

	public VTimeOfDay minusInterval(VInterval interval) {
		return new VTimeOfDay(time.minus(interval.getDuration()));
	}

	public long nanosBetween(VTimeOfDay time) {
		return ChronoUnit.NANOS.between(this.time, time.time);
	}
	
	public VInterval intervalBetween(VTimeOfDay time) {
		return new VInterval(Duration.ofSeconds(0, nanosBetween(time)));
	}

	public LocalTime toLocalTime() {
		return time;
	}

	@Override
	public int hashCode() {
		return time.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VTimeOfDay))
			return false;
		VTimeOfDay other = (VTimeOfDay) obj;
		return time.equals(other.time);
	}

	@Override
	public int compareTo(VTimeOfDay o) {
		return time.compareTo(o.time);
	}

	@Override
	public String toString() {
		return time.toString();
	}

}
