# 5D Diplomacy Order Log Generator

A simple Java command-line tool that creates a **nicely formatted orders log** from the JSON output provided by the [Oliver Lugg 5D Diplomacy adjudicator](https://github.com/Oliveriver/5d-diplomacy-with-multiverse-time-travel).

The tool reads the adjudicator's JSON results, groups and formats orders by **owner** and **timeline**, and writes them to a Markdown (`.md`) file for easy reading or sharing.

---

## Features

- Reads adjudicator JSON directly from your local filesystem
- Groups orders by:
    1. **Owner**
    2. **Timeline**
- Prints only the **latest phase** per timeline (older phases are omitted)
- Supports all order types:
    - Move
    - Support
    - Hold
    - Convoy
    - Build
    - Disband
- Marks failed orders by italicising
- When the unit-type of a support cannot be inferred, a 'U' is used as the abbreviation
- Outputs clean, human-readable Markdown

---

### Prerequisites
- Java 25+ (or compatible version)
- Maven for building (if running from source)

### Clone and Build
```bash
git clone https://github.com/LudoBC/5DOrdersLogger.git
cd 5DOrdersLogger
mvn package
```

This will produce a runnable JAR in target/.

### Usage

You can run the tool in three ways:

1. Provide JSON file path or http web page as an argument
```Bash
java -jar target/OrdersLogger-1.2.1-jar-with-dependencies.jar /path/to/results.json
```
```Bash
java -jar target/OrdersLogger-1.2.1-jar-with-dependencies.jar http://host:port/jsonsite
```
2. Run without arguments (interactive prompt)
```Bash
java -jar target/OrdersLogger-1.2.1-jar-with-dependencies.jar
```
You will be prompted:
```
Please input the location of the .json file containing the results to be turned into the orders log.
The location must either a path on the local file system, or a web page.
The correct file can be obtained from the 5D diplomacy adjudicator.
```
3. Run with a blank string as argument
```Bash
java -jar target/OrdersLogger-1.2.1-jar-with-dependencies.jar " "
```
In this case the json is expected to be supplied through System.in 
and the markdown with we output on System.out, 
allowing for the use of this program with linux command line pipes.
It can also be used by other programs using this tool as a dependency,
though do note that the program will in this use case close whatever stream
is supplied to it by System.in

### Output

The generated .md file will be created in the same directory as the input JSON,
or if using a web page wherever is being used in a new file named orderlog.md.
In the first case filename will match the JSON filename but with .md extension.

Example:

```results.json  →  results.md```

### Development & Testing
Run Unit Tests
```Bash
mvn test
```
Rum pitest mutation tests
```Bash
mvn test-compile pitest:mutationCoverage
```
The resulting rapports can be read in 
```./target/pit-reports/index.html```.

### License
This project is licensed under the MIT License.

