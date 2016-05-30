package org.openo.ict.drivermgmt.impl;

import com.huawei.bsp.roa.common.HttpContext;
import com.oss.so.service.common.exception.ServiceException;

/**
 * <br/>
 * <p>
 * Driver Manager REST Service for IR.
 * </p>
 *
 * @author
 * @version SDNO 0.5 Mar 24, 2016
 */
public class DriverMgmtServiceImpl {

    /**
     * get instance.
     * <br/>
     *
     * @return driver manager service instance
     * @since SDNO 0.5
     */
    public static DriverMgmtServiceImpl getInstance() {
        // TODO singleton
        return null;
    }

    /**
     * Heartbeat interfaces
     * <br/>
     *
     * @param instanceId is instance id
     * @param context    is Http Context
     * @return heart beat instance
     * @throws ServiceException when invalid parameters of check hearth beat
     * @since SDNO 0.5
     */
    public Object checkBeatHeart(final String instanceId, HttpContext context) throws ServiceException {
        // TODO check heart beat of instance id
        return null;
    }

    /**
     * <br/>
     *
     * @param instanceId is instance id
     * @param context    is Http Context
     * @return Examples of information inquiry
     * @throws ServiceException when query driver execute fail
     * @since SDNO 0.5
     */
    public Object getDriver(String instanceId, HttpContext context) throws ServiceException {
        // TODO query one plugin instance
        return null;
    }

    /**
     * <br/>
     *
     * @param resourceType is Resource type
     * @param ruleBody     is Rule body
     * @param context      is Http Context
     * @return driver list
     * @throws ServiceException when query driver list fail
     * @since SDNO 0.5
     */
    public Object getDriverList(String resourceType, String ruleBody, HttpContext context) throws ServiceException {
        // TODO query plugin instance list
        return null;
    }

    /**
     * Register NE driver widget
     *
     * @param context : HTTP context.
     * @return Object
     * @throws ServiceException : ServiceException
     */
    public Object registerDriver(HttpContext context) throws ServiceException {
        // TODO register plugin instance
        return null;
    }

    /**
     * Logout NE driver widget
     *
     * @param instanceId : plugin instance id.
     * @param context    : HTTP context.
     * @return Object
     * @throws ServiceException : ServiceException
     */
    public Object unregisterDriver(final String instanceId, HttpContext context) throws ServiceException {
        // TODO unregister plugin instance implement
        return null;
    }

    /**
     * Holder for get DriverMgmtServiceImpl instance.
     * <br/>
     * <p>
     * </p>
     *
     * @author
     * @version SDNO 0.5 Mar 24, 2016
     */
    private static class DriverMgrServiceInternalImplHolder {

        private static final DriverMgmtServiceImpl driverServiceImpl = new DriverMgmtServiceImpl();
    }
}