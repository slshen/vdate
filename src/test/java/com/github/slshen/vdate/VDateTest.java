package com.github.slshen.vdate;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VDateTest {

	@Test
	public void testBasic() {
		VDate jan1 = new VDate("2018-01-01");
		Assertions.assertThat(jan1.toString()).isEqualTo("2018-01-01");
		Assertions.assertThat(jan1.plusDays(1).toString()).isEqualTo("2018-01-02");
		Assertions.assertThat(jan1.minusDays(1).toString()).isEqualTo("2017-12-31");
		Assertions.assertThat(jan1.plusMonths(1L).toString()).isEqualTo("2018-02-01");
		Assertions.assertThat(jan1.minusMonths(1L).toString()).isEqualTo("2017-12-01");
		Assertions.assertThat(jan1.plusYears(1L).toString()).isEqualTo("2019-01-01");
		Assertions.assertThat(jan1.minusYears(1L).toString()).isEqualTo("2017-01-01");
	}

	@Test
	public void testBetween() {
		Assertions.assertThat(new VDate("2018-01-01").daysBetween(new VDate("2018-03-01"))).isEqualTo(30 + 28 + 1);
	}

	@Test
	public void testInterval() {
		Assertions.assertThat(new VDate("2018-03-15").plusInterval(VInterval.of(1, ChronoUnit.MONTHS)).toString())
				.isEqualTo("2018-04-15");
		Assertions.assertThat(new VDate("2018-01-30").plusInterval(VInterval.of(1, ChronoUnit.MONTHS)).toString())
				.isEqualTo("2018-02-28");
		Assertions
				.assertThat(new VDate("2018-01-30")
						.plusInterval(VInterval.of(1, ChronoUnit.MONTHS).plus(3, TimeUnit.DAYS)).toString())
				.isEqualTo("2018-03-03");
	}

}
