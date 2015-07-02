@pushd "%~dp0build\orientdb-community-2.0.11\bin"

@rem set uid root password to admin
@SET ORIENTDB_ROOT_PASSWORD=admin
server.bat %*

@popd
