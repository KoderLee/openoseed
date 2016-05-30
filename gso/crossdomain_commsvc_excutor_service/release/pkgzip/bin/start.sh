#!/bin/bash

umask 027

dir_name=`dirname "$0"`
SCRIPT_DIR=`cd "$dir_name"; pwd`
OLD=`pwd`

#check user
if [ "root" = "`/usr/bin/id -u -n`" ];then
    echo "root has been forbidden to execute the shell."
	cd $OLD
    exit 1
fi

cd $APP_ROOT
JAVA=$JAVA_HOME/bin/java
COMPLETE_PROCESS_NAME=$PROCESS_NAME-$NODE_ID-$PROCESS_SLOT
LOG_PATH=$_APP_LOG_DIR/$COMPLETE_PROCESS_NAME


AGENT_ARG="-DNFW=$COMPLETE_PROCESS_NAME -Dprocname=$COMPLETE_PROCESS_NAME"

JVM_OPT="$AGENT_ARG"
JVM_OPT="$JVM_OPT -Xms128m -Xmx512m -XX:PermSize=128m -XX:MaxPermSize=320m -XX:ReservedCodeCacheSize=64m"
JVM_OPT="$JVM_OPT -Dfile.encoding=UTF-8 -XX:+DisableExplicitGC"
#Close largePages
JVM_OPT="$JVM_OPT -XX:-UseLargePages"
JVM_OPT="$JVM_OPT -XX:+UseConcMarkSweepGC -XX:+UseParNewGC"
JVM_OPT="$JVM_OPT -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0"
JVM_OPT="$JVM_OPT -XX:+UseFastAccessorMethods -XX:+CMSClassUnloadingEnabled -XX:+CMSParallelRemarkEnabled"
JVM_OPT="$JVM_OPT -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=62"
JVM_OPT="$JVM_OPT -XX:+HeapDumpOnOutOfMemoryError"
JVM_OPT="$JVM_OPT -javaagent:$KERNEL_RTSP_PATH/lib/org.openo.commonservice.clagent-2.0-SNAPSHOT.jar"
JVM_OPT="$JVM_OPT -Dcrypto.package.file=$RUNTIME_CENTER_PATH/etc/framework/encryptoPackages.properties"
JVM_OPT="$JVM_OPT -Djavax.xml.transform.TransformerFactory=org.apache.xalan.processor.TransformerFactoryImpl"
JVM_OPT="$JVM_OPT -Djava.library.path=$THIRD_PART_PATH/lib:$THIRD_PART_PATH/lib/linux64"
CLASS_PATH="$THIRD_PART_PATH/lib/com.springsource.slf4j.api-1.6.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.slf4j.log4j-1.6.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.log4j-1.2.16.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.framework.appmgr.impl-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.launcher-2.0-SNAPSHOT.jar:\
$KERNEL_RTSP_PATH/lib/org.openo.commonservice.log4j.extend-2.0-SNAPSHOT.jar:\
$RUNTIME_CENTER_PATH/lib/org.openo.commonservice.deploy-2.0-SNAPSHOT.jar:\
$KERNEL_RTSP_PATH/lib/org.openo.commonservice.log-2.0-SNAPSHOT.jar:\
$THIRD_PART_PATH/lib/json-lib-2.4-jdk15.jar:\
$THIRD_PART_PATH/lib/commons-beanutils-1.9.1.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.commons.lang-2.6.0.jar:\
$THIRD_PART_PATH/lib/com.springsource.org.apache.commons.collections-3.2.1.jar:\
$THIRD_PART_PATH/lib/commons-logging-1.1.3.jar:\
$THIRD_PART_PATH/lib/ezmorph-1.0.6.jar:\
$RUNTIME_CENTER_PATH/etc/framework/acl_user"
JVM_OPT="$JVM_OPT -classpath"
JVM_OPT="$JVM_OPT $CLASS_PATH"
export CONFIG_DIR=$RUNTIME_CENTER_PATH/etc
JVM_OPT="$JVM_OPT -DCONFIG_DIR=$RUNTIME_CENTER_PATH/etc -Dlog.dir=$LOG_PATH"

DEFAULT_APPDIR="module"
if [ -z "$APPDIR" ]
then
	APPDIR=$DEFAULT_APPDIR
fi

JVM_OPT="$JVM_OPT -Dappdir=$APPDIR"
JVM_OPT="$JVM_OPT -Djava.awt.headless=true"

# Disable memory mapping in ZipFile for linux and solaris
JVM_OPT="$JVM_OPT -Dsun.zip.disableMemoryMapping=true"


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
$INSTALL_ROOT/manager/tools/shscript/syslogutils.sh "$(basename $0)" "$result" "Excute($#):$0 $@";exit $result
