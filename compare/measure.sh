#!/bin/bash
COMPILE_TIMES=4
STARTUP_TIMES=4


function compileTime(){
    pushd ../$1
    for (( i=0; i<COMPILE_TIMES; i++))
    do
        #make a clean first as we want to measure a full rebuild
        eval $2

	#Build the application and store the time needed to results
        /usr/bin/time -o times -f "%e" $3
	echo "Compile time $1:  $(cat times)" >> ../compare/results
    done
    popd
}

function startup(){
    for (( i=0; i<STARTUP_TIMES; i++))
    do
        #Rebuild the container to always have a startup from null
        docker-compose stop $1
        docker-compose rm -f $1
        docker-compose build $1

	#Start the container and measure how long it takes untill we get a valid result
        startNS=$(date +"%s%N")
        docker-compose up -d $1
        curl http://localhost:8080/issue/550e8400-e29b-11d4-a716-446655440000/
        while [ $? -ne 0 ]; do
            sleep 0.3
            curl http://localhost:8080/issue/550e8400-e29b-11d4-a716-446655440000/
        done
        endNS=$(date +"%s%N")
        startuptime=$(echo "scale=2;($endNS-$startNS)/1000000000" | bc)
        echo "Startup with $1 took $startuptime" >> results
        #Measure memory
        docker stats --no-stream "compare_$2_1"  >> results
    done

    #Stop contains as they might influence ohter startup times
    docker-compose stop $1
}

#Prepare
rm -f results
docker-compose stop
docker-compose rm -f postgres
docker-compose build postgres
docker-compose up -d postgres
sleep 3

#measure individually
compileTime "spring" "./mvnw clean" "./mvnw package"
startup "spring"
compileTime "micronaut" "./gradlew clean" "./gradlew assemble"
startup "micronaut"

#Output all results
cat results;
