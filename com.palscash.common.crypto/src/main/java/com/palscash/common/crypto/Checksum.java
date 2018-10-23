package com.palscash.common.crypto;

public class Checksum {

	public final static int CRC_POLYNOM = 0x9c;

	public final static int CRC_PRESET = 0xFF;

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