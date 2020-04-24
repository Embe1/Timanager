#!/bin/sh

log_file=/home/michael/IdeaProjects/TiManager/Storings/Logs/start-recording.log

# cd ../ui/
# echo "Compiling java file..."
# javac -d ../../../../../../target/classes/ Start.java

cd /home/michael/IdeaProjects/TiManager/timanager-util/target/classes/

echo "Starting and running TiManager-start..." >> $log_file
/etc/alternatives/jre_11_openjdk/bin/java de.timanager.recording.Start >> $log_file
echo "TiManager start was terminated..." >> $log_file
echo "------------------!------------------!------------------!-------------------" >> $log_file
