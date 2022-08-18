vi conf/zoo.cfg
tickTime=2000
dataDir=/path/to/zookeeper/data
clientPort=2181
initLimit=5
syncLimit=2


ports:
      - 2181:2181

      hostname: zoo1



docker run --name some-zookeeper --restart always -d -v $(pwd)/zoo.cfg:/conf/zoo.cfg zookeeper

docker run --name some-zookeeper --restart always -e JVMFLAGS="-Xmx1024m" zookeeper

docker run -e "ZOO_INIT_LIMIT=10" --name some-zookeeper --restart always -d zookeeper

docker run --name some-zookeeper --restart always -e
  JVMFLAGS="-Dzookeeper.serverCnxnFactory=org.apache.zookeeper.server.NettyServerCnxnFactory" zookeeper



