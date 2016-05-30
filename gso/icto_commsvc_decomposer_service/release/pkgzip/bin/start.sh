#!/bin/bash

#check user
CUR_PATH=$(cd `dirname $0`;pwd)
SCRIPT_PATH=$0
IPMC_USER="`stat -c '%U' ${SCRIPT_PATH}`"
export IPMC_USER
CURRENT_USER="`/usr/bin/id -u -n`"
if [ "${IPMC_USER}" != "${CURRENT_USER}" ]
then
    echo "only ${IPMC_USER} can execute this script."
    exit 1
fi

umask 027
cd $APP_ROOT
JAVA=$JAVA_HOME/bin/java
COMPLETE_PROCESS_NAME=$PROCESS_NAME-$NODE_ID-$PROCESS_SLOT
LOG_PATH=$_APP_LOG_DIR/$COMPLETE_PROCESS_NAME

if [ -z "$AGENT_ARG" ]
then
   AGENT_ARG="-DNFW=$COMPLETE_PROCESS_NAME -Dprocname=$COMPLETE_PROCESS_NAME"
fi
JVM_OPT="$AGENT_ARG"

if [ -z "$JVM_MEM_CONF" ] ; then
    JVM_OPT="$JVM_OPT -server -Xms128m -Xmx512m -XX:InitialCodeCacheSize=32m -XX:ReservedCodeCacheSize=64m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m"
else 
    JVM_OPT="$JVM_OPT $JVM_MEM_CONF"
fi
JVM_OPT="$JVM_OPT -XX:+DisableExplicitGC"
#Close largePages
JVM_OPT="$JVM_OPT -XX:-UseLargePages -XX:+UseConcMarkSweepGC -XX:+UseFastAccessorMethods -XX:+UseFastAccessorMethods"
JVM_OPT="$JVM_OPT -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=62"
JVM_OPT="$JVM_OPT -XX:+HeapDumpOnOutOfMemoryError"
JVM_OPT="$JVM_OPT -javaagent:$KERNEL_RTSP_PATH/lib/org.openo.commonservice.clagent-2.0-SNAPSHOT.jar"
JVM_OPT="$JVM_OPT -Dcrypto.package.file=$RUNTIME_CENTER_PATH/etc/framework/encryptoPackages.properties"
JVM_OPT="$JVM_OPT -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl"
JVM_OPT="$JVM_OPT -Djava.library.path=$THIRD_PART_PATH/lib:$THIRD_PART_PATH/lib/linux64"
CLASS_PATH="$THIRD_PART_PATH/lib/com.springsource.slf4j.api-1.6.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.slf4j.log4j-1.6.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.log4j-1.2.16.jar:\
$THIRD_PART_PATH/lib/json-lib-2.4-jdk15.jar:\
$THIRD_PART_PATH/lib/commons-beanutils-1.9.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.commons.lang-2.6.0.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.commons.collections-3.2.1.jar:\
$THIRD_PART_PATH/lib/commons-logging-1.1.3.jar:\
$THIRD_PART_PATH/lib/ezmorph-1.0.6.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.framework.appmgr.impl-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.launcher-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.deploy-2.0-SNAPSHOT.jar:\
$KERNEL_RTSP_PATH/lib/org.openo.commonservice.log-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/etc/framework/acl_user"
JVM_OPT="$JVM_OPT -classpath"
JVM_OPT="$JVM_OPT $CLASS_PATH"
JVM_OPT="$JVM_OPT -DCONFIG_DIR=$RUNTIME_CENTER_PATH/etc"
JVM_OPT="$JVM_OPT -Dlog.dir=$LOG_PATH"
JVM_OPT="$JVM_OPT -Dfile.encoding=UTF-8"

DEFAULT_APPDIR="module"
if [ -z "$APPDIR" ]
then
	APPDIR=$DEFAULT_APPDIR
fi

if [ ! -z "$ROAINSTCONF" ]
then
    JVM_OPT="$JVM_OPT -Droa.instance.conf=$ROAINSTCONF"
fi
JVM_OPT="$JVM_OPT -Dappdir=$APPDIR"
JVM_OPT="$JVM_OPT -Djava.awt.headless=true"
JVM_OPT="$JVM_OPT -Dbsp.app.datasource=servicedecomposerdb"
# Disable memory mapping in ZipFile for linux and solaris


if [ ! -d "$LOG_PATH" ]
then
    mkdir -p $LOG_PATH
fi

if [ ! -z "$AGENTSTART" ]
then
	$JAVA $JVM_OPT $JAVAENV  org.openo.commonservice.base.launcher.Main 1>$LOG_PATH/stack.log | tee $LOG_PATH/stack.log  &
    result=$?
else
	$JAVA $JVM_OPT $JAVAENV  org.openo.commonservice.base.launcher.Main 2>&1 | tee $LOG_PATH/stack.log  &
    result=$?
fi
$CUR_PATH/../../../../manager/agent/tools/shscript/syslogutils.sh "$(basename $0)" "$result" "Execute($#):$CUR_PATH/$0 $@";exit $result