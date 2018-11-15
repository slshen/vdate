package com.github.slshen.vdate;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;

public class VTimestamp implements Comparable<VTimestamp>, Serializable {
	private static final long serialVersionUID = 1L;
	private final Instant instant;

	public VTimestamp() {
		this(Instant.now());
	}

	public VTimestamp(CharSequence text) {
		this(Instant.parse(text));
	}

	public VTimestamp(long epochMilli) {
		this(Instant.ofEpochMilli(epochMilli));
	}

	public VTimestamp(Instant instant) {
		this.instant = instant;
	}

	public Instant toInstant() {
		return instant;
	}

	public VDate toDate(ZoneId zoneId) {
		return new VDate(toInstant().atZone(zoneId).toLocalDate());
	}

	public VDate toDate() {
		return toDate(VDateTime.DEFAULT_ZONE);
	}

	public VTimeOfDay toTimeOfDay(ZoneId zoneId) {
		return new VTimeOfDay(toInstant().atZone(zoneId).toLocalTime());
	}

	public VTimeOfDay toTimeOfDay() {
		return toTimeOfDay(VDateTime.DEFAULT_ZONE);
	}

	public VDateTime toDateTime(ZoneId zoneId) {
		return new VDateTime(instant.atZone(zoneId));
	}

	public VDateTime toDateTime() {
		return toDateTime(VDateTime.DEFAULT_ZONE);
	}

	public VTimestamp plusInterval(VInterval interval) {
		return toDateTime(VDateTime.UTC).plusInterval(interval).toTimestamp();
	}

	public VTimestamp minusInterval(VInterval interval) {
		return toDateTime(VDateTime.UTC).minusInterval(interval).toTimestamp();
	}

	public VInterval intervalBetween(VTimestamp ts) {
		return toDateTime(VDateTime.UTC).intervalBetween(ts.toDateTime(VDateTime.UTC));
	}

	@Override
	public String toString() {
		return instant.toString();
	}

	@Override
	public int hashCode() {
		return instant.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VTimestamp))
			return false;
		VTimestamp other = (VTimestamp) obj;
		return instant.equals(other.instant);
	}

	@Override
	public int compareTo(VTimestamp o) {
		return instant.compareTo(o.instant);
	}

}
