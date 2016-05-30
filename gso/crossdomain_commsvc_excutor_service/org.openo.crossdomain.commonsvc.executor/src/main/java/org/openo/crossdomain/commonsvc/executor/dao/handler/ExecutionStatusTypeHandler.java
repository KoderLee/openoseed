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
package org.openo.crossdomain.commonsvc.executor.dao.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.openo.crossdomain.commonsvc.executor.common.enums.ExecutionStatus;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExecutionStatusTypeHandler extends BaseTypeHandler<ExecutionStatus> {

	/**
	 *set NotNull Parameter
	 *@param ps PreparedStatement
	 *@param paramInt Int type parameter
	 *@param paramType parameter type
	 *@param jdbctype jdbctype
	 *@throws SQLException when fai to set
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public void setNonNullParameter(PreparedStatement ps, int paramInt, ExecutionStatus paramType, JdbcType jdbctype)
            throws SQLException {
        ps.setString(paramInt, paramType.toString());
    }

    /**
	 *get NotNull Result
	 *@param ps PreparedStatement
	 *@param param string type parameter
	 *@return ExecutionStatus
	 *@throws SQLException when fai to get
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public ExecutionStatus getNullableResult(ResultSet rs, String param) throws SQLException {
        return ExecutionStatus.enumValueOf(rs.getString(param));
    }

	/**
	 *get NotNull Result
	 *@param ps PreparedStatement
	 *@param param int type parameter
	 *@return ExecutionStatus
	 *@throws SQLException when fai to get
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public ExecutionStatus getNullableResult(ResultSet rs, int param) throws SQLException {
        return ExecutionStatus.enumValueOf(rs.getString(param));
    }

	/**
	 *get NotNull Result
	 *@param cs CallableStatement
	 *@param param string type parameter
	 *@return ExecutionStatus
	 *@throws SQLException when fai to get
	 *@since crossdomain 0.5 2016-3-18
	 */	
    @Override
    public ExecutionStatus getNullableResult(CallableStatement cs, int param) throws SQLException {
        return ExecutionStatus.enumValueOf(cs.getString(param));
    }
}
