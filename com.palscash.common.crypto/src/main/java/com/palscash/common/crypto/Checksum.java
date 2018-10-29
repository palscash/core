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

/**
 * Calculates a CRC8 checksum for a byte array
 */
public class Checksum {

	public final static int CRC_POLYNOM = 0x9c;

	public final static int CRC_PRESET = 0xFF;

	/**
	 * Calculate
	 */
	public static int calculate(byte[] frame_U) {

		int crc_U = CRC_PRESET;
		for (int i = 0; i < frame_U.length; i++) {
			crc_U ^= Byte.toUnsignedInt(frame_U[i]);
			for (int j = 0; j < 8; j++) {
				if ((crc_U & 0x01) != 0) {
					crc_U = (crc_U >>> 1) ^ CRC_POLYNOM;
				} else {
					crc_U = (crc_U >>> 1);
				}
			}
		}

		return crc_U;
	}

}