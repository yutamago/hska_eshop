CALL mvn -f ./eshop-edge/pom.xml clean package
CALL mvn -f ./eshop-api/pom.xml clean package
CALL mvn -f ./eshop-discovery/pom.xml clean package
CALL mvn -f ./composite-product/pom.xml clean package
CALL mvn -f ./composite-category/pom.xml clean package
CALL mvn -f ./core-product/pom.xml clean package
CALL mvn -f ./core-category/pom.xml clean package
CALL mvn -f ./core-user/pom.xml clean package