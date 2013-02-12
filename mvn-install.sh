echo "Running mvn clean install on all sub-projects..."

cd productionhost && mvn clean install && cd ..
