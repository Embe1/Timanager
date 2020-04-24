#!/bin/sh

log_file=/home/michael/IdeaProjects/TiManager/Storings/Logs/stop-recording.log

# cd ../ui/
# echo "Compiling java file..."
# javac -d ../../../../../../target/classes/ Start.java

cd /home/michael/IdeaProjects/TiManager/timanager-util/target/classes/

echo "Starting and running TiManager-stop..." >> $log_file
/etc/alternatives/jre_11_openjdk/bin/java de.timanager.recording.Stop false >> $log_file

echo "TiManager stop was terminated..." >> $log_file
echo "------------------!------------------!------------------!-------------------" >> $log_file

poweroff
