@rem UPDATE general SET nick='Luca' UPSERT WHERE nick='Luca'

@rem set hostIp=192.168.1.174

set hostIp=localhost

@rem # 1 optional last part of url /document or /graph default is /document
@rem plocal is a db type other option is memory
curl --user root:admin -X POST "http://%hostIp%:2480/database/general/plocal/graph"

@rem # 2
curl --user admin:admin -d "create class general extends V" "http://%hostIp%:2480/command/general/sql"

@rem # 3
set rnd=%RANDOM%
curl --user admin:admin --header "Accept: text/csv" -d "INSERT INTO general CONTENT {'name': 'John %rnd%', 'surname': 'Doe %rnd%', 'age':%rnd% }" "http://%hostIp%:2480/command/general/sql"

@rem # 4
curl --user admin:admin --header "Accept: text/csv" -d "select from general" "http://%hostIp%:2480/command/general/sql"