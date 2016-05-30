package org.openo.ict.drivermgmt.rest;

import org.openo.ict.drivermgmt.common.exception.ExceptionFactory;
import org.openo.ict.drivermgmt.impl.DriverMgmtServiceImpl;

import com.huawei.bsp.log.OssLog;
import com.huawei.bsp.log.OssLogFactory;
import com.huawei.bsp.roa.annotation.Consumes;
import com.huawei.bsp.roa.annotation.DEL;
import com.huawei.bsp.roa.annotation.GET;
import com.huawei.bsp.roa.annotation.POST;
import com.huawei.bsp.roa.annotation.PUT;
import com.huawei.bsp.roa.annotation.Path;
import com.huawei.bsp.roa.annotation.PathParam;
import com.huawei.bsp.roa.annotation.QueryParam;
import com.huawei.bsp.roa.annotation.Target;
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
@Path("/plat/drivermgmtservice/v1/drivers")
@Target("driver_management_service")
public class DriverMgmtService {

    private static final OssLog LOGGER = OssLogFactory.getLog(DriverMgmtService.class);

    /**
     * Depending on the type of query to the corresponding service resource address
     * Back to the list and remove urlType
     * U2000 returns a list , CF side in accordance with the priority to get one from the list
     * inside
     * <br/>
     *
     * @param context    is Http context
     * @param instanceId is instance id
     * @return Object driver
     * @since SDNO 0.5
     */
    @GET
    @Path("/{instance-id}")
    @Consumes("application/json")
    // @validate_ok @audit_ok @access_ok @encode_ok
    public Object getDriver(HttpContext context,
                            @PathParam("instance-id") String instanceId) {
        try {
            return DriverMgmtServiceImpl.getInstance().getDriver(instanceId, context);
        } catch (ServiceException exception) {
            LOGGER.error("query route id throw unexpected exception.", exception);
            return ExceptionFactory.getException(context, exception);
        }
    }

    /**
     * Depending on the type of query to the corresponding service resource address
     * This interface is used to provide plug-in information there U2000 inquiry
     * <br/>
     *
     * @param context      is Http context
     * @param resourceType is resource type
     * @param ruleBody     is rule body
     * @return Object driver list
     * @since SDNO 0.5
     */
    @GET
    @Consumes("application/json")
    // @validate_ok @audit_ok @access_ok @encode_ok
    public Object getDriverList(HttpContext context,
                                @QueryParam("resource-type") String resourceType,
                                @QueryParam("rule-body") String ruleBody) {
        try {
            return DriverMgmtServiceImpl.getInstance().getDriverList(resourceType, ruleBody, context);
        } catch (ServiceException exception) {
            LOGGER.error("query route id throw unexpected exception.", exception);
            return ExceptionFactory.getException(context, exception);
        }
    }

    /**
     * Register NE driver widget
     * <br/>
     *
     * @param context is Http context
     * @return Object
     * @since SDNO 0.5
     */
    @POST
    @Consumes("application/json")
    // @validate_ok @audit_ok @access_ok @encode_ok
    public Object registerDriver(HttpContext context) {
        try {
            return DriverMgmtServiceImpl.getInstance().registerDriver(context);
        } catch (ServiceException exception) {
            LOGGER.error("register driver(internal) throw unexpected exception.", exception);
            return ExceptionFactory.getException(context, exception);
        }
    }

    /**
     * Logout NE driver widget
     * <br/>
     *
     * @param context    is Http context
     * @param instanceId is instance id
     * @return Object
     * @since SDNO 0.5
     */
    @DEL
    @Path("/{instance-id}")
    @Consumes("application/json")
    // @validate_ok @audit_ok @access_ok @encode_ok
    public Object unregisterDriver(HttpContext context,
                                   @PathParam("instance-id") String instanceId) {
        try {
            return DriverMgmtServiceImpl.getInstance().unregisterDriver(instanceId, context);
        } catch (ServiceException exception) {
            LOGGER.error("unregister driver(internal) throw unexpected exception.", exception);
            return ExceptionFactory.getException(context, exception);
        }
    }

    /**
     * Heartbeat interfaces
     * <br/>
     *
     * @param context    is Http context
     * @param instanceId is instance id
     * @return Object
     * @since SDNO 0.5
     */
    @PUT
    @Path("/{instance-id}")
    @Consumes("application/json")
    // @validate_ok @audit_ok @access_ok @encode_ok
    public Object updateDriver(HttpContext context,
                               @PathParam("instance-id") String instanceId) {
        try {
            return DriverMgmtServiceImpl.getInstance().checkBeatHeart(instanceId, context);
        } catch (ServiceException exception) {
            LOGGER.error("check beatHeart(internal) throw unexpected exception.", exception);
            return ExceptionFactory.getException(context, exception);
        }
    }
}
