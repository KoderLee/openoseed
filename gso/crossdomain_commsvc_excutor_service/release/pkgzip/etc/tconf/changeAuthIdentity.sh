#!/bin/bash +x
TARGET_DIR=/

## if tconf not exists then create one
if [ ! -f /var/log/oss/tconf ]; then
	mkdir /var/log/oss/tconf
fi

authIdentityLog=/var/log/oss/tconf/changeAuthIdentity.log
authIdentityConf=./identity.properties
authIdentityConfBak=/tmp/identity.properties.$RANDOM

##log who and when call this method
echo "   ">>$authIdentityLog
whoami >> $authIdentityLog
date >> $authIdentityLog
echo "changeAuthIdentity.sh called!">>$authIdentityLog
echo `pwd`>>$authIdentityLog

##check password	
CheckPassWord()
	{
		echo "check password start..."
		bash /opt/oss/manager/bin/engr_profile.sh -f
		echo "do encrypting su -ossadm">>$authIdentityLog
		bash /opt/oss/manager/agent/bin/osskey -cmd encryptpasswd -passwd $1
		return $?
	}

##wait for user make sure
echo "----------------------------warning---------------------------"
echo "Change authorization Identity may cause service break down,"
echo "Before you run this bash, Must make sure your accessName and identity are correct with IAM Manager."
read -t 40 -p "Are you sure to run this bash? (Y/N) :" CHOOSE
if [ $CHOOSE != "Y" ] ; then
	echo "exit without any change"
	echo "exit without any change" >> $authIdentityLog
	exit 0
fi

##wait for input accessName
ret=1
while (( 1 == $ret ))
do 
read -t 40 -p "Enter authIdentity accessName ( 6 charactor at least and 32 charactor at most) :" TYPE
if [ ! -n "$TYPE" ] ; then
       echo "Error"
   else
      lengthaccessname=`expr length $TYPE`
      if [ $lengthaccessname -gt 32 ] || [ $lengthaccessname -lt 6 ] ; then
           echo "invalid! length error!!!"
      else
          ret=0
          echo "good!"
      fi
   fi
done

##wait for input identity and do check
ret=1
while (( 1 == $ret ))
do
read -s -t 10 -p "Enter authIdentity identity( 8 charactor at least and 32 charactor at most):" TYPE1
echo " "
   if [ ! -n "$TYPE1" ] ; then
       echo "Error"
       # continue 
   else
		CheckPassWord $TYPE1
		result=$?
		if [ $result != 0 ] ; then
			echo $result
			echo $result>>$authIdentityLog
		else
			ret=0
			echo "good!"
		fi
   fi
done

##wait for input identity again and do check
ret=1
while (( 1 == $ret ))
do
read -s -t 10 -p "Enter authIdentity identity again:" TYPE2
echo " "
    if [ ! -n "$TYPE2" ] ; then
        echo "Error"    
    else
        if [ $TYPE1 != $TYPE2 ]; then
            echo "They are Different! Please try again!"
        else 
			echo "do encrypting identity start ...">>$authIdentityLog
			line1=`bash /opt/oss/manager/agent/bin/osskey -cmd encryptpasswd -passwd $TYPE1`
			echo $line1
			echo "do encrypting line1">>$authIdentityLog
			ret=0
        fi
    fi
done

if [ -f $authIdentityConf ] ; then
	##update data into configuration file
	echo "update data into file">>$authIdentityLog
	sed -i "s/\(name.*=\).*\$/\1 $TYPE/" $authIdentityConf
	sed -i "s/\(value.*=\).*\$/\1 $line1/" $authIdentityConf
else
    ##update data into configuration file
	echo "write date into file">>$authIdentityLog
	echo "name = "$TYPE >>$authIdentityConf
	echo "value = "$line1 >>$authIdentityConf
fi



chmod -R 600 $authIdentityLog
chown ossadm:ossgroup $authIdentityLog

echo "changeAuthIdentity.sh complete!!"
echo "changeAuthIdentity.sh complete!!">>$authIdentityLog

exit 0
