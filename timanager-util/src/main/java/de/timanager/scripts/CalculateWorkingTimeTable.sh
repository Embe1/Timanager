#!/bin/sh

log_file=/home/michael/IdeaProjects/TiManager/Storings/Logs/workingTimeCalculation.log

# cd ../ui/
# echo "Compiling java file..."
# javac -d ../../../../../../target/classes/ Start.java

cd /home/michael/IdeaProjects/TiManager/timanager-util/target/classes/

/etc/alternatives/jre_11_openjdk/bin/java de.timanager.statistics.ConsoleTableCalculation $1
