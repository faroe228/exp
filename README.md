# exp

# console 1

    call curl -kOL https://github.com/faroe228/exp/archive/trunk.zip
    call unzip trunk.zip
    cd exp-trunk\orientdb
    call gradlew installOrientDb
    call gradlew run

# console 2

    cd exp-trunk\orientdb
    call groovy lpt\setup_db.groovy


