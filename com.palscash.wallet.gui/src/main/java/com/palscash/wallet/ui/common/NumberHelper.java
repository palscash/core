package com.palscash.wallet.ui.common;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberHelper {

	public static String toGroupedAmount(String amount) {
		return toGroupedAmount(new BigDecimal(amount));
	}

	public static String toGroupedAmount(BigDecimal amount) {
		DecimalFormat df = new DecimalFormat("###,###.########");
		return df.format(amount);

	}

}
