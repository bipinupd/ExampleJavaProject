# Java program to read CSV and load to a table in Database

Simple program to read and load the database. To compile the program use `mvn clean install`. 

If you just want to run to get feel. Go to runner folder ad run the bash file `runasDocker.sh`. You don’t need to install `mysql` locally, we pull the `mysql`, build the jar and then run the command with the sample csv file.

The configuration drives the program. The configuration is written in `HACON` format (https://docs.spongepowered.org/stable/en/server/getting-started/configuration/hocon.html). The configuration has two parts `jdbc` (which has all the jdbc related properties)

```
"jdbc" {
	"driver" = "com.mysql.jdbc.Driver"
	"url" = "jdbc:mysql://localhost:6603/"
	"database" = "testdb"
	"username" = "root"
	"password" = "mypassword"
	"useSSL" = "false"
	}
```
The another part is `csv` ash shown below:
```
"csv" {
 		"tableName" =  "tableName"
 		"batchSize" = 20
 		"fieldSeperator" = ","
		"fileLocation"="./resources/ddd"
		"fields" = [ 
			{ 
			  "name" = "name"
			  "dbtype" = "varchar(200)"
			}
			{
			  "name" = "occupation"
			   "dbtype" = "varchar(200)"
			}
			{
			  "name" = "salary"
			  "dbtype" = "double"
			  }
		 ]
	}
```
The `csv` configuration tells more about what file to read, the field separator in csv and the sql table columns names and their data-type. It also has the table name. For simplicity we just few data-types such as varchar, and double datatypes for now.
