
# Execute from  out of the target/bin directory

# bin directory
BINDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# execute
java -jar -Xms1024m -Xmx2048m  -XX:PermSize=512m -XX:MaxPermSize=512m  ../target/stock-reader-1.0.jar   
