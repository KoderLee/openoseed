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
package org.openo.crossdomain.commonsvc.executor.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.openo.crossdomain.commonsvc.executor.common.exception.PreprocessException;
import org.openo.crossdomain.commonsvc.executor.model.Resource;
import org.openo.crossdomain.commonsvc.executor.model.ServiceJob;
import org.openo.crossdomain.commonsvc.executor.service.inf.IPreprocessor;

public class CycleDependencyCheck implements IPreprocessor<ServiceJob> {

    /**
	 *Resource Preprocess
	 *@param job ServiceJob to be checked
	 *@throws PreprocessException when Resource Preprocess is failed
	 *@since crossdomain 0.5 2016-3-18
	 */
    @Override
    public void preprocess(ServiceJob serviceJob) throws PreprocessException {

        Map<String, DAGNode> nodeMap = initNodeMap(serviceJob);

        Stack<DAGNode> visitingNodeStatck = new Stack<DAGNode>();

        for(DAGNode node : nodeMap.values()) {

            if(node.isNodeUnvisited()) {
                recursiveTraversal(nodeMap, visitingNodeStatck, node);
            }
        }
    }

    private void recursiveTraversal(Map<String, DAGNode> nodeMap, Stack<DAGNode> visitingNodeStatck, DAGNode node)
            throws PreprocessException {

        node.setNodeVisiting();

        visitingNodeStatck.push(node);
        if(node.getDependResources() != null) {
            for(String dependKey : node.getDependResources()) {
                DAGNode dependNode = nodeMap.get(dependKey);

                if((dependNode != null)) {
                    if(dependNode.isNodeUnvisited()) {
                        recursiveTraversal(nodeMap, visitingNodeStatck, dependNode);
                    }

                    else if(dependNode.isNodeVisiting()) {

                        StringBuilder strBuilder = new StringBuilder();
                        for(int i = visitingNodeStatck.indexOf(dependNode), size = visitingNodeStatck.size(); i < size; i++) {
                            DAGNode iterNode = visitingNodeStatck.get(i);
                            strBuilder.append(iterNode.getResource().getName());
                            strBuilder.append(" --> ");
                        }
                        strBuilder.append(dependNode.getResource().getName());

                        throw new PreprocessException(strBuilder.toString());
                    }
                }
            }
        }

        node.setNodeVisited();

        visitingNodeStatck.pop();
    }

    private Map<String, DAGNode> initNodeMap(ServiceJob serviceJob) {
        Map<String, DAGNode> nodeMap = new HashMap<String, DAGNode>();
        for(Entry<String, Resource> entry : serviceJob.getService().getResources().entrySet()) {
            nodeMap.put(entry.getKey(), new DAGNode(entry.getValue()));
        }

        return nodeMap;
    }

    private class DAGNode {

        private static final int STATUS_UNVISITED = 0;

        private static final int STATUS_VISITING = 1;

        private static final int STATUS_VISITED = 2;

        Resource resource;

        int visitStatus = STATUS_UNVISITED;

        DAGNode(Resource resource) {
            this.resource = resource;
        }

        public Resource getResource() {
            return resource;
        }

        List<String> getDependResources() {
            return resource.getDependon();
        }

        void setNodeVisiting() {
            visitStatus = STATUS_VISITING;
        }

        void setNodeVisited() {
            visitStatus = STATUS_VISITED;
        }

        boolean isNodeUnvisited() {
            return visitStatus == STATUS_UNVISITED;
        }

        boolean isNodeVisiting() {
            return visitStatus == STATUS_VISITING;
        }
    }

}
