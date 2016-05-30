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

package org.openo.nfvo.vimadapter.util;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openo.nfvo.vimadapter.util.constant.Constant;

/**
 * 
* Used for basic string operation, such ip check, etc.<br/>
* <p>
* </p>
* 
* @author
* @version NFVO 0.5 May 15, 2016
 */
public final class StringUtil {
    private StringUtil() {

    }

    public static boolean isValidString(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static boolean isboolIp(String ipAddress) {
        String ip = "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static long ipToLong(String address) {
        long[] ip = new long[4];
        int position1 = address.indexOf('.');
        int position2 = address.indexOf('.', position1 + 1);
        int position3 = address.indexOf('.', position2 + 1);
        ip[0] = Long.parseLong(address.substring(0, position1));
        ip[1] = Long.parseLong(address.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(address.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(address.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String longToIp(long ipLong) {
        return new StringBuilder().append((ipLong >> 24) & 0xff).append('.')
                .append((ipLong >> 16) & 0xff).append('.')
                .append((ipLong >> 8) & 0xff).append('.')
                .append(ipLong & 0xff).toString();
    }

    public static String printBinary(long n, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = size - 1; i >= 0; i--) {
            sb.append(n >>> i & 0x01);
        }
        return sb.toString();
    }

    public static int getCount(String str, String sub) {
        int index = 0;
        int count = 0;
        String localStr = str;

        while ((index = localStr.indexOf(sub)) != -1) {
            localStr = localStr.substring(index + sub.length());
            count++;
        }
        
        return count;
    }

    public static String getCidr(String startIp, String subMask) {
        long ip = StringUtil.ipToLong(startIp);
        long mask = StringUtil.ipToLong(subMask);

        String ipAdress = StringUtil.longToIp(ip & mask);
        return ipAdress + '/'
                + StringUtil.getCount(StringUtil.printBinary(mask, 32), "1");
    }

    public static boolean isSameUrlIgnorePort(String source, String target,
            String sourceType, String targetType) throws MalformedURLException {
        if (source.equals(target)) {
            return true;
        }

        if (validType(sourceType, targetType)) {
            return false;
        }

        URL sourceUrl = new URL(source);
        URL targetUrl = new URL(target);

        if (sourceUrl.getPort() == -1 && targetUrl.getPort() != -1) {
            if (targetUrl.getPort() == Constant.DEFLAUT_SECURE_PORT) {
                return isSameUrlIgnorePort(new URL(source).toString(),
                        new URL(targetUrl.getProtocol(), targetUrl.getHost(),
                                targetUrl.getFile()).toString(), sourceType,
                        targetType);
            }
        } else if (sourceUrl.getPort() != -1 && targetUrl.getPort() == -1) {
            if (sourceUrl.getPort() == Constant.DEFLAUT_SECURE_PORT) {
                return isSameUrlIgnorePort(new URL(sourceUrl.getProtocol(),
                        sourceUrl.getHost(), sourceUrl.getFile()).toString(),
                        new URL(target).toString(), sourceType, targetType);
            }
        } else {
            return source.equals(target);
        }
        return false;
    }
    
    private static boolean validType(String sourceType, String targetType) {
        return Constant.VCLOUD.equalsIgnoreCase(sourceType)
                || Constant.VCENTER.equalsIgnoreCase(sourceType)
                || Constant.VCLOUD.equalsIgnoreCase(targetType)
                || Constant.VCENTER.equalsIgnoreCase(targetType);
    }
}