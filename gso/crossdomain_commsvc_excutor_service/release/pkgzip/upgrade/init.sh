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

DB_ID="serviceexecutordb"
SCRIPT=$APP_ROOT/init/tables_@{dbtype}.sql

JVM_OPT="$JVM_OPT -Dlog.dir=$_APP_LOG_DIR"
JVM_OPT="$JVM_OPT -Dinit.appconfig=$APP_CONF_FILE"
JVM_OPT="$JVM_OPT -Dinit.approot=$APP_ROOT"
JVM_OPT="$JVM_OPT -Dinit.dataSourceId=$DB_ID"
JVM_OPT="$JVM_OPT -Dinit.script=$SCRIPT"
JVM_OPT="$JVM_OPT -Dinit.connectType=$1"
JVM_OPT="$JVM_OPT -Dinit.class=org.openo.crossdomain.commsvc.inittool.init.impl.DBInitializer"

CLASS_PATH="$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.util-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.deploy-2.0-SNAPSHOT.jar:\
$KERNEL_RTSP_PATH/lib/org.openo.commonservice.log-2.0-SNAPSHOT.jar:\
$KERNEL_RTSP_PATH/lib/org.openo.commonservice.encrypt.cbb-2.0-SNAPSHOT.jar:\
$ICTO_COM_SDK_PATH/org.openo.crossdomain.commsvc.inittool-1.0.0-SNAPSHOT.jar:\
$THIRD_PART_PATH/lib/*"
JVM_OPT="$JVM_OPT -classpath $CLASS_PATH"

$JAVA $JVM_OPT org.openo.crossdomain.commsvc.inittool.InitTool
result=$?
$CUR_PATH/../../../../manager/agent/tools/shscript/syslogutils.sh "$(basename $0)" "$result" "Execute($#):$CUR_PATH/$0 $@";exit $result
