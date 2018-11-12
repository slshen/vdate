package com.github.slshen.vdate;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class VTimeOfDayTest {

	@Test
	public void testParseHM() {
		VTimeOfDay t = new VTimeOfDay("8:00");
		Assertions.assertThat(t.getHour()).isEqualTo(8);
		Assertions.assertThat(t.getMinute()).isEqualTo(0);
	}
	
	@Test
	public void testParseHMS() {
		VTimeOfDay t = new VTimeOfDay("9:05:31");
		Assertions.assertThat(t.getHour()).isEqualTo(9);
		Assertions.assertThat(t.getMinute()).isEqualTo(5);
		Assertions.assertThat(t.getSecond()).isEqualTo(31);
	}
	
	@Test
	public void testParseHMSF() {
		VTimeOfDay t = new VTimeOfDay("13:12:45.123");
		Assertions.assertThat(t.getHour()).isEqualTo(13);
		Assertions.assertThat(t.getMinute()).isEqualTo(12);
		Assertions.assertThat(t.getSecond()).isEqualTo(45);
		Assertions.assertThat(t.getNano()).isEqualTo(123_000_000);
	}
	
	@Test
	public void testAMPM() {
		VTimeOfDay t = new VTimeOfDay("10:00 PM");
		Assertions.assertThat(t.getHour()).isEqualTo(22);
	}

}
