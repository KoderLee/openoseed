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

package org.openo.nfvo.nsservice.utils;

import org.openo.baseservice.encrypt.cbb.CipherException;
import org.openo.baseservice.encrypt.cbb.CipherManager;
import org.openo.baseservice.encrypt.cbb.KeyType;
import org.openo.baseservice.log.OssLog;
import org.openo.baseservice.log.OssLogFactory;

/**
 * 
* The utilty of encrypt/decrypt for other module use<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class EncryptionUtil {

    private static final OssLog LOG = OssLogFactory.getLogger(EncryptionUtil.class);

    private EncryptionUtil() {

    }

    /**
     * Encrypt the input string or password<br/>
     * 
     * @param enCryptPwd
     * The enCryptPwd to be encrypted
     * @return the encrypted data
     * @since NFVO 0.5
     */
    public static String enCrypt(String enCryptPwd) {
        try {
            CipherManager cipher = CipherManager.getInstance();
            return String.valueOf(cipher.encrypt(enCryptPwd.toCharArray(), KeyType.SHARE, "common_shared"));
        } catch(CipherException e) {
            LOG.error("loc=enCrypt, msg=enCrypt fail,e=" + e);
        }
        return pwd;
    }

    /**
     * Encrypt the input string or password<br/>
     * 
     * @param deCryptPwd
     * The deCryptPwd to be decrypted
     * @return the decrypted data
     * @since NFVO 0.5
     */
    public static String deCrypt(String deCryptPwd) {
        try {
            CipherManager cipher = CipherManager.getInstance();
            return String.valueOf(cipher.decrypt(deCryptPwd.toCharArray(), KeyType.SHARE, "common_shared"));
        } catch(CipherException e) {
            LOG.error("loc=deCrypt, msg=deCrypt fail,e=" + e);
        }
        return pwd;
    }
}
