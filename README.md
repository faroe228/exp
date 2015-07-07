# exp

# Notes:
    curl -kOL https://github.com/faroe228/exp/archive/trunk.zip keeps getting older version
    use master.zip instead
    
# nix
    rm master.zip
    rm -R exp-master
    
    curl -kOL https://github.com/faroe228/exp/archive/master.zip
    unzip master.zip
    cd exp-master/orientdb
    chmod +x gradlew
    ./gradlew installOrientDb
    nohup ./gradlew run &
    
    sleep 10
    
    groovy lpt/setup_db.groovy
    
    echo done
        
# nix svn export
    
    svn --non-interactive --trust-server-cert export https://github.com/faroe228/exp/trunk exp-trunk
    cd exp-trunk/orientdb
    chmod +x gradlew
    ./gradlew installOrientDb
    nohup ./gradlew run &
    
    sleep 10
    
    groovy lpt/setup_db.groovy
    
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
    call groovy lpt\setup_db.groovy


