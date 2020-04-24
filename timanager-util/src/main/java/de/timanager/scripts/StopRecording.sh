#!/bin/sh

# cd ../ui/
# echo "Compiling java file..."
# javac -d ../../../../../../target/classes/ Start.java

cd /home/michael/IdeaProjects/TiManager/timanager-util/target/classes/
echo "Starting and running TiManager-stop..."
/etc/alternatives/jre_11_openjdk/bin/java de.timanager.ui.gui.StopDialog

echo "TiManager was terminated..."
