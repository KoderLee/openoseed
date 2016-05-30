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
package org.openo.crossdomain.commonsvc.executor.common.util;

import java.util.Calendar;
import java.util.UUID;

import org.openo.commonservice.encrypt.cbb.CipherException;
import org.openo.commonservice.encrypt.cbb.CipherManager;
import org.openo.commonservice.encrypt.cbb.KeyType;
import org.openo.commonservice.log.OssLog;
import org.openo.commonservice.log.OssLogFactory;
import org.openo.commonservice.remoteservice.exception.ServiceException;

import org.openo.crossdomain.commonsvc.executor.common.constant.Constants;
import org.openo.crossdomain.commonsvc.executor.common.constant.ErrorMessage;
import org.springframework.util.StringUtils;

/**
 * Common Util Class<br/>
 *
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class CommonUtil {

    private static final OssLog logger = OssLogFactory.getLogger(CommonUtil.class);

    private static final int ENCRYPT_TIME = 10;

    /**
     * Get UTC time in millisecond<br/>
     *
     * @return
     * @since crossdomain 0.5
     */
    public static long getTimeInMillis() {
        // get local time
        Calendar cal = Calendar.getInstance();

        // get time zone offset
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

        // get daylight saving offset in milliseconds
        int dstOffset = cal.get(Calendar.DST_OFFSET);

        // get UTC time in millisecond
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        return cal.getTimeInMillis();
    }

    /**
     * Convert UTC time to local time<br/>
     *
     * @param utcTime UTC time
     * @return local time
     * @since crossdomain 0.5
     */
    public static long getLocalTimeFromUtc(long utcTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(utcTime);

        // get time zone offset
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

        // get daylight saving offset in milliseconds
        int dstOffset = cal.get(Calendar.DST_OFFSET);

        cal.add(Calendar.MILLISECOND, (zoneOffset + dstOffset));

        return cal.getTimeInMillis();
    }

    /**
     * Generate UUID<br/>
     *
     * @return UUID string
     * @since crossdomain 0.5
     */
    public static String generateUuid() {
        return StringUtils.delete(UUID.randomUUID().toString(), "-");
    }

    /**
     * cipher input plaintext into ciphertext <br/>
     *
     * @param cipher input plaintext
     * @return ciphertext
     * @throws ServiceException when fail to cipher
     * @since crossdomain 0.5
     */
    public static String encrypt(String cipher) throws ServiceException {
        if(!StringUtils.hasLength(cipher)) {
            logger.error("encrypt cipher is empty!");
            return cipher;
        }

        try {
            long start = System.currentTimeMillis();

            char[] encrypted =
                    CipherManager.getInstance().encrypt(cipher.toCharArray(), KeyType.SHARE, "common_shared");

            // record the end time
            long end = System.currentTimeMillis();
            if(end - start > ENCRYPT_TIME) {
                logger.warn("encrypt time:{}", (end - start));
            }

            if(encrypted == null) {
                logger.error("The encrypted cipher is null!");
                return Constants.NULL_STR;
            }

            return new String(encrypted);
        } catch(CipherException e) {
            logger.error(e.toString());
            throw new ServiceException(ErrorMessage.SE_DATA_ENCRYPT_FAIL, e.toString());
        }
    }

    /**
     * decrypt ciphertext into plaintext<br/>
     *
     * @param cipher input ciphertext
     * @return plaintext after decrypting
     * @throws ServiceException when fail to decrypt
     * @since crossdomain 0.5
     */
    public static String decrypt(String cipher) throws ServiceException {
        if(!StringUtils.hasLength(cipher)) {
            logger.error("decrypt cipher is empty!");
            return cipher;
        }

        try {
            long start = System.currentTimeMillis();

            char[] decrypted =
                    CipherManager.getInstance().decrypt(cipher.toCharArray(), KeyType.SHARE, "common_shared");

            // record the end time
            long end = System.currentTimeMillis();
            if(end - start > ENCRYPT_TIME) {
                logger.warn("decrypt time:{}", end, (end - start));
            }

            if(decrypted == null) {
                logger.error("The decrypted cipher is null!");
                return Constants.NULL_STR;
            }

            return new String(decrypted);
        } catch(CipherException e) {
            logger.error(e.toString());
            throw new ServiceException(ErrorMessage.SE_DATA_DECRYPT_FAIL, e.toString());
        }
    }
}
