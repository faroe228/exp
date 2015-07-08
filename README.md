# exp

# Notes:
    curl -kOL https://github.com/faroe228/exp/archive/trunk.zip keeps getting older version
    use master.zip instead of trunk.zip
    
    # how to find groovy-#.#.#.jar
    find .  |grep -e "/groovy-[0-9]\+\.[0-9]\+\.[0-9]\+\.jar"
    
# nix
    echo beg
    
    `(find exp-master/orientdb |grep shutdown.sh)`
    
    rm master.zip
    rm -R exp-master
    
    curl -kOL https://github.com/faroe228/exp/archive/master.zip
    unzip master.zip
    cd exp-master/orientdb
    chmod +x gradlew
    ./gradlew installOrientDb
    
    if [ `which nohup` ]
    then
        echo "### running ordb server in bg WITH nohup"
        nohup ./gradlew run &
    else
        # Win Git Bash does not have nohup
        echo "### running ordb server in bg WITHOUT nohup"
        ./gradlew run &
    fi
    
    sleep 10
    ./gradlew setupDb
    
    echo end
        
# nix svn export
    
    svn --non-interactive --trust-server-cert export https://github.com/faroe228/exp/trunk exp-trunk
    cd exp-trunk/orientdb
    chmod +x gradlew
    ./gradlew installOrientDb
    nohup ./gradlew run &
    
    sleep 10
    
    ./gradlew setupDb
    
    echo done


# Windows

# console 1

    call curl -kOL https://github.com/faroe228/exp/archive/trunk.zip
    call unzip trunk.zip
    cd exp-trunk\orientdb
    call gradlew installOrientDb
    call gradlew run

# console 2

    cd exp-trunk\orientdb
    call gradlew setupDb


