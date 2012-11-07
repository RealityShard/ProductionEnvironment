echo "Running mvn clean install on all sub-projects..."

cd entitysystem && mvn clean install && cd ..
cd productionhost && mvn clean install && cd ..
