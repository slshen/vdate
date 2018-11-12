package com.github.slshen.vdate;

import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VDateTimeTest {

	@Test
	public void testBasic() {
		VDateTime d = new VDateTime("2018-1-1");
		Assertions.assertThat(d.getDate().toString()).isEqualTo("2018-01-01");
	}

	@Test
	public void testInterval() {
		VInterval interval = new VDateTime("2018-1-1").intervalBetween(new VDateTime("2018-11-7 10:00 PM"));
		Assertions.assertThat(interval.getYears()).isEqualTo(0);
		Assertions.assertThat(interval.getMonths()).isEqualTo(10);
		Assertions.assertThat(interval.getDays()).isEqualTo(6);
		Assertions.assertThat(TimeUnit.SECONDS.toHours(interval.getSeconds())).isEqualTo(22);
	}

}
