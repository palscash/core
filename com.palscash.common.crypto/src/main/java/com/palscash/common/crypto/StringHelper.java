/*

MIT License

Copyright (c) 2017 ZDP Developers
Copyright (c) 2018 PalsCash Team

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.palscash.common.crypto;

import org.apache.commons.lang3.StringUtils;

/**
 * Utilities for converting data from/to strings.
 */
public class StringHelper {

	public static String cleanUpMemo(final String memo) {
		final StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(memo)) {
			for (Character c : memo.toCharArray()) {
				if (Character.isLetterOrDigit(c)) {
					sb.append(c);
				} else if (c == ' ') {
					sb.append(c);
				} else if (c == '-') {
					sb.append(c);
				} else if (c == '_') {
					sb.append(c);
				}
			}
		}

		String result = sb.toString().trim();

		if (result.length() > 64) {
			result = result.substring(0, 63);
		}

		return result;
	}

}
