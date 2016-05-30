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
package org.openo.crossdomain.servicemgr.model.roamo;

/**
 * Parameter type definition.<br/>
 * 
 * @author
 * @version crossdomain 0.5 2016-3-19
 */
public class ServiceParamConstant {

    public static class TEMPLATE_PARAM_VALUE_NUM {

        public static final String HIDDEN_TRUE = "true";

        public static final String TYPE_STRING = "String";

        public static final String TYPE_NUMBER = "Number";

        public static final String TYPE_LIST = "List<Number>";

        public static final String TYPE_LIST_PARAMGROUP = "List<ParamGroup>";

        public static final String TYPE_LIST_RESPARAMGROUP = "List<ResParamGroup>";

        public static final String TYPE_COMMADELIMITEDLIST = "List<String>";
    }

    public static class WIDGET_KEY_NUM {

        public static final String WIDGETTYPE = "WidgetType";
    }

    public static class WIDGET_TYPE_NUM {

        public static final String TEXT = "Text";

        public static final String NOECHO = "Noecho";

        public static final String COMBO = "Combo";

        public static final String MULTICOMBO = "MultiCombo";

        public static final String MULTITEXT = "MultiText";

        public static final String NOECHO_COMBO = "NoechoCombo";

        public static final String NOECHO_MULTICOMBO = "NoechoMultiCombo";

        public static final String NOECHO_MULTITEXT = "NoechoMultiText";

        public static final String TYPE_LIST_GROUP = "ListGroup";

        public static final String TYPE_LIST_RESGROUP = "ResListGroup";
    }

    public static class TEMPLATE_PARAM_KEY_NUM {

        public static final String TYPE = "type";

        public static final String HIDDEN = "hidden";

        public static final String VALIDVALUES = "validValues";

        public static final String MAP = "map";

        public static final String LOGICALNAME = "logicalName";

        public static final String ACTION = "action";
    }

    public static class RESPONSE_KEY_NUM {

        public static final String SVCPARAMETER_KEY = "parameters";
    }
}
