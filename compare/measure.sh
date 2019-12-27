#!/bin/bash
COMPILE_TIMES=1
STARTUP_TIMES=1
CLEAN=true

function compileTime(){
    for (( i=0; i<COMPILE_TIMES; i++))
    do
        pushd ../$1
        #make a clean first as we want to measure a full rebuild
	if ( $CLEAN )
        then
            if [ "$3" == "mvn" ]
            then
                ./mvnw clean
            else
                ./gradlew clean
	    fi
        fi

	#Build the application and store the time needed to results
        startNS=$(date +"%s%N")
        if [ "$3" == "mvn" ]
        then
            ./mvnw package
        else
            ./gradlew assemble
        fi
        popd
        docker-compose build $2
        endNS=$(date +"%s%N")
        compiletime=$(echo "scale=2;($endNS-$startNS)/1000000000" | bc)
	echo "Compile time $2: $compiletime" >> results
    done
}

function startup(){
    for (( i=0; i<STARTUP_TIMES; i++))
    do
        #Rebuild the container to always have a startup from null
        docker-compose stop $2
        docker-compose rm -f $2

	#Start the container and measure how long it takes untill we get a valid result
        startNS=$(date +"%s%N")
        docker-compose up -d $2
        curl http://localhost:8080/issue/550e8400-e29b-11d4-a716-446655440000/
        while [ $? -ne 0 ]; do
            sleep 0.3
            curl http://localhost:8080/issue/550e8400-e29b-11d4-a716-446655440000/
        done
        endNS=$(date +"%s%N")
        startuptime=$(echo "scale=2;($endNS-$startNS)/1000000000" | bc)
        echo "Startup with $2 took $startuptime" >> results

        #Measure memory
	memory=$(docker stats --format "{{.MemUsage}}" --no-stream "compare_$2_1")
        echo "Memory usage after startup $2 $memory" >> results
    done

    #Stop contains as they might influence ohter startup times
    docker-compose stop $2
}

#Prepare
rm -f results
docker-compose stop
docker-compose rm -f postgres
docker-compose build postgres
docker-compose up -d postgres
sleep 3

#measure individually
compileTime "micronaut" "micronaut" "gradle"
startup     "micronaut" "micronaut"
compileTime "micronaut" "micronaut-graal" "gradle"
startup     "micronaut" "micronaut-graal"

#compileTime "quarkus"   "quarkus"   "mvn"
#startup     "quarkus"   "quarkus"
compileTime "spring"    "spring"    "mvn"
startup     "spring"    "spring"

#Output all results
cat results;
