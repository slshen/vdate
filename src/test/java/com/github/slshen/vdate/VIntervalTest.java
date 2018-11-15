package com.github.slshen.vdate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VIntervalTest {

	@Test
	public void testBasic() {
		VInterval i1 = VInterval.of(1, ChronoUnit.YEARS).plus(1, ChronoUnit.DAYS);
		Assertions.assertThat(i1.getDuration()).isEqualTo(Duration.ZERO);
	}

}
