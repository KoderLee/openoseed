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
package org.openo.crossdomain.servicemgr.util.uuid;

import org.apache.commons.lang.StringUtils;
import java.util.UUID;

/**
 * Tools for generating ID of resource.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class UUIDUtils {

    private static final byte[] URL_SAFE_ENCODE_TABLE = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
                    'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};

    private static final int MASK_6BITS = 0x3f;

    /**
     * <br/>
     *
     * @param binaryData
     * @return
     * @since crossdomain 0.5
     */
    public static String encodeBase64URLSafeString(final byte[] binaryData) {
        int length = 2 * binaryData.length;
        byte buffer[] = new byte[length];
        int index = 0;
        for(int i = 0; i < binaryData.length - 2; i += 3) {
            int a = 0;
            for(int j = 0; j < 3; ++j) {
                int b = binaryData[i + j];
                if(b < 0) {
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
        switch(binaryData.length % 3) {
            case 1:
                remains = binaryData[binaryData.length - 1];
                if(remains < 0) {
                    remains += 256;
                }
                buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 2) & MASK_6BITS];
                buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains << 4) & MASK_6BITS];
                break;
            case 2:
                remains = 0;
                for(int i = 2; i >= 1; --i) {
                    int b = binaryData[binaryData.length - i];
                    if(b < 0) {
                        b += 256;
                    }
                    remains = (remains << 8) + b;
                }
                buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 10) & MASK_6BITS];
                buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains >> 4) & MASK_6BITS];
                buffer[index++] = URL_SAFE_ENCODE_TABLE[(remains << 2) & MASK_6BITS];
                break;
            default:
                break;
        }

        return new String(buffer).substring(0, index);
    }

    /**
     * Generate UUID.<br/>
     *
     * @return UUID
     * @since crossdomain 0.5
     */
    public static String createUuid() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.remove(uuid, '-');
    }

    /**
     * Create UUID in the form of Base64.<br/>
     *
     * @return UUID
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

    /**
     * Get byte array form long data.<br/>
     *
     * @param l long date
     * @return byte array
     * @since crossdomain 0.5
     */
    public static byte[] byteArrayFromLong(long l) {
        byte[] out = new byte[8];
        longIntoByteArray(l, 0, out);
        return out;
    }
    
    /**
     * Get the value of byte array.<br/>
     *
     * @param l long data
     * @param offset offset of index
     * @param bytes array
     * @since crossdomain 0.5
     */
    public static void longIntoByteArray(long l, int offset, byte[] bytes) {
        bytes[offset + 0] = (byte)((l >>> 56) & 0xFF);
        bytes[offset + 1] = (byte)((l >>> 48) & 0xFF);
        bytes[offset + 2] = (byte)((l >>> 40) & 0xFF);
        bytes[offset + 3] = (byte)((l >>> 32) & 0xFF);
        bytes[offset + 4] = (byte)((l >>> 24) & 0xFF);
        bytes[offset + 5] = (byte)((l >>> 16) & 0xFF);
        bytes[offset + 6] = (byte)((l >>> 8) & 0xFF);
        bytes[offset + 7] = (byte)((l >>> 0) & 0xFF);
    }

    /**
     * Convert stander UUID to bytes.<br/>
     *
     * @param hex32UUID resource ID
     * @return bytes that UUID is converted to.
     * @throws IllegalArgumentException
     * @since crossdomain 0.5
     */
    public static byte[] getByteFromStanderUUID(String hex32UUID) throws IllegalArgumentException {
        if(hex32UUID == null || hex32UUID.length() != 32) {
            throw new IllegalArgumentException("only accept 32 charactors uuid");
        }

        String[] components =
                {hex32UUID.substring(0, 8), hex32UUID.substring(8, 12), hex32UUID.substring(12, 16),
                                hex32UUID.substring(16, 20), hex32UUID.substring(20, 32)};
        for(int i = 0; i < components.length; i++) {
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
        byte bb = (byte)0xff;
        for(int i = 0; i < 8; i++) {
            b[i] = (byte)((mostSigBits >> ((8 - i - 1) * 8)) & bb);
        }

        for(int i = 0; i < 8; i++) {
            b[i + 8] = (byte)((leastSigBits >> ((8 - i - 1) * 8)) & bb);
        }

        return b;
    }

    /**
     * Get UUID from bytes<br/>
     *
     * @param data bytes
     * @return UUID
     * @since crossdomain 0.5
     */
    public static String getUUIDFrom(byte[] data) {
        long msb = 0;
        long lsb = 0;
        assert data.length == 16;
        for(int i = 0; i < 8; i++) {
            msb = (msb << 8) | (data[i] & 0xff);
        }

        for(int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (data[i] & 0xff);
        }

        UUID id = new UUID(msb, lsb);
        return StringUtils.remove(id.toString(), '-');
    }
}
