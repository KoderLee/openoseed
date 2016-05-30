/*******************************************************************************
 * Copyright (c) 2016, Huawei Technologies Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.openo.crossdomain.commonsvc.decomposer.util;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;

/**
 * UUID util class
 * 
 * @since crossdomain 0.5
 */
public class UUIDUtils {

	/**
	 * encode table
	 */
	private static final byte[] URL_SAFE_ENCODE_TABLE = { 'A', 'B', 'C', 'D',
			'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
			'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9', '-', '_' };

	/**
	 * mask
	 */
	private static final int MASK_6BITS = 0x3f;

	/**
	 * Encodes binary data using a URL-safe variation of the base64 algorithm
	 * but does not chunk the output. The url-safe variation emits - and _
	 * instead of + and / characters. <b>Note: no padding is added.</b>
	 * 
	 * @param binaryData binary data to encode
	 * @return String containing Base64 characters
	 */
	public static String encodeBase64URLSafeString(final byte[] binaryData) {
		int length = 2 * binaryData.length;
		byte buffer[] = new byte[length];
		int index = 0;
		for (int i = 0; i < binaryData.length - 2; i += 3) {
			int a = 0;
			for (int j = 0; j < 3; ++j) {
				int b = binaryData[i + j];
				if (b < 0) {
					b += 256;
				}
				a = (a << 8) + b;
			}
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(a >> 18) & MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(a >> 12) & MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(a >> 6) & MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[a & MASK_6BITS];
		}
		int remains = 0;
		switch (binaryData.length % 3) {
		case 1:
			remains = binaryData[binaryData.length - 1];
			if (remains < 0) {
				remains += 256;
			}
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 2) & MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains << 4) & MASK_6BITS];
			break;
		case 2:
			remains = 0;
			for (int i = 2; i >= 1; --i) {
				int b = binaryData[binaryData.length - i];
				if (b < 0) {
					b += 256;
				}
				remains = (remains << 8) + b;
			}
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 10)
					& MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 4) & MASK_6BITS];
			buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains << 2) & MASK_6BITS];
			break;
		default:
			break;
		}

		return new String(buffer).substring(0, index);
	}

	/**
	 * create uuid
	 * 
	 * @return uuid
	 * @since crossdomain 0.5
	 */
	public static String createUuid() {
		String uuid = UUID.randomUUID().toString();
		return StringUtils.remove(uuid, '-');
	}

	/**
	 * Create 64 bit uuid
	 * 
	 * @return uuid
	 * @since crossdomain 0.5
	 */
	public static String createBase64Uuid() {
		UUID uuid = UUID.randomUUID();
		byte[] most = byteArrayFromLong(uuid.getMostSignificantBits());
		byte[] least = byteArrayFromLong(uuid.getLeastSignificantBits());

		byte[] uuidBytes = new byte[16];
		System.arraycopy(most, 0, uuidBytes, 0, 8);
		System.arraycopy(least, 0, uuidBytes, 8, 8);

		return encodeBase64URLSafeString(uuidBytes);
	}

	private static byte[] byteArrayFromLong(long l) {
		byte[] out = new byte[8];
		longIntoByteArray(l, 0, out);
		return out;
	}

	/**
	 * @param l
	 * @param offset
	 * @param bytes
	 */
	private static void longIntoByteArray(long l, int offset, byte[] bytes) {
		bytes[offset + 0] = (byte) ((l >>> 56) & 0xFF);
		bytes[offset + 1] = (byte) ((l >>> 48) & 0xFF);
		bytes[offset + 2] = (byte) ((l >>> 40) & 0xFF);
		bytes[offset + 3] = (byte) ((l >>> 32) & 0xFF);
		bytes[offset + 4] = (byte) ((l >>> 24) & 0xFF);
		bytes[offset + 5] = (byte) ((l >>> 16) & 0xFF);
		bytes[offset + 6] = (byte) ((l >>> 8) & 0xFF);
		bytes[offset + 7] = (byte) ((l >>> 0) & 0xFF);
	}

	private static byte[] getByteFromStanderUUID(String hex32UUID)
			throws IllegalArgumentException {
		if (hex32UUID == null || hex32UUID.length() != 32) {
			throw new IllegalArgumentException("only accept 32 charactors uuid");
		}

		String[] components = { hex32UUID.substring(0, 8),
				hex32UUID.substring(8, 12), hex32UUID.substring(12, 16),
				hex32UUID.substring(16, 20), hex32UUID.substring(20, 32) };
		for (int i = 0; i < components.length; i++) {
			components[i] = "0x" + components[i];
		}
		long mostSigBits = Long.decode(components[0]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[1]).longValue();
		mostSigBits <<= 16;
		mostSigBits |= Long.decode(components[2]).longValue();

		long leastSigBits = Long.decode(components[3]).longValue();
		leastSigBits <<= 48;
		leastSigBits |= Long.decode(components[4]).longValue();

		byte[] b = new byte[16];
		byte bb = (byte) 0xff;
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) ((mostSigBits >> ((8 - i - 1) * 8)) & bb);
		}

		for (int i = 0; i < 8; i++) {
			b[i + 8] = (byte) ((leastSigBits >> ((8 - i - 1) * 8)) & bb);
		}

		return b;
	}
}
