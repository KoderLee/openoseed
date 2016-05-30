package org.openo.ict.drivermgmt.common.exception;

import org.openo.ict.drivermgmt.common.Constant;

import com.huawei.bsp.roa.common.HttpContext;
import com.oss.so.service.common.exception.InvalidParameterValueException;
import com.oss.so.service.common.exception.ServiceException;

/**
 * <br/>
 * <p>
 * Exception Factory
 * </p>
 *
 * @author
 * @version SDNO 0.5 Mar 24, 2016
 */
public final class ExceptionFactory {

    /**
     * Constructor<br/>
     * <p>
     * </p>
     *
     * @since SDNO 0.5
     */
    private ExceptionFactory() {
    }

    /**
     * <br/>
     *
     * @param context   HttpContext
     * @param exception object for which exception class to be check
     * @return ServiceException
     * @since SDNO 0.5
     */
    public static Object getException(HttpContext context, Exception exception) {
        return (exception instanceof ServiceException)
                ? ((ServiceException) exception).setHttpResponse(context).toString()
                : new ServiceException(Constant.MODULE_NM, 500, exception.getMessage()).setHttpResponse(context)
                .toString();
    }

    /**
     * <br/>
     *
     * @return InvalidParameterValueException
     * @since SDNO 0.5
     */
    public static InvalidParameterValueException newInvalidInstanceIdException() {
        return new InvalidParameterValueException(Constant.MODULE_NM, "Invalid instance ID.");
    }

    /**
     * <br/>
     *
     * @return InvalidParameterValueException
     * @since SDNO 0.5
     */
    public static InvalidParameterValueException newInvalidInstanceUrlException() {
        return new InvalidParameterValueException(Constant.MODULE_NM, "Invalid ServiceUrls.");
    }

    /**
     * <br/>
     *
     * @return InvalidParameterValueException
     * @since SDNO 0.5
     */
    public static InvalidParameterValueException newInvalidResourceTypeException() {
        return new InvalidParameterValueException(Constant.MODULE_NM, "Invalid resource type.");
    }

    /**
     * <br/>
     *
     * @return InvalidParameterValueException
     * @since SDNO 0.5
     */
    public static InvalidParameterValueException newInvalidRouteIdException() {
        return new InvalidParameterValueException(Constant.MODULE_NM, "Invalid route id.");
    }

    /**
     * <br/>
     *
     * @return InvalidParameterValueException
     * @since SDNO 0.5
     */
    public static InvalidParameterValueException newInvalidRouteRuleException() {
        return new InvalidParameterValueException(Constant.MODULE_NM, "Invalid route rule.");
    }

}
