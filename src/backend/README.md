# PROJECT DETAILS
Project Name: "Sprint 2: Server"
Project Description: This is the backend that creates an API server where users can send in 
requests which will be given responses through DataHandlers. Here, three request: loadcsv,
getcsv and weather are beign handled with loadcsv loading the data from a given filepath 
within a restricted directory (only the testCSV directory of the project), getcsv displaying 
the loaded csv data and weather showing the current temperature according to NWS API. 
Appropriate error messaged are seen in the browser when the datasource is invalid, or
parameters are not correctly put in. 
To implement a new datasource/API, a developer can implement the DataHandler interface and 
use appropriate Utility class methods.

Team Member(s): 
-Stephanie Olaiya (solaiya) - 16 hours
-Iris Cheng (icheng3) - 13 hours

Repository Link: https://github.com/cs0320-f2022/sprint-2-icheng3-solaiya.git

--------------------------------------------------------------------------------
# DESIGN CHOICES

To create functionality, we have a DataHandler Interface which implements Route and 
serves as a guideline how to add a new data source or API url and allow them to 
connect to the Server. 

#### DataHandler Interface:
- The interface has two methods that need to be implemented: 
  - handle(): which is the method gotten from the Route interface. In here we call the 
    handleData() method which has the job of specifying how the data source should be
    processed so that it can be sent to the server.
  - handleData(): this is the method that processes the specified file/URL and returns the 
    needed information as a serialized JSON string
  
#### ResponseUtilities:
- This is a class that contains a generic serialize method that can convert a 
<String, Object>HashMap to a serialized JSON string. If another datasource is to be added,
the Handler class for that query would just have to implement the DataHandler interface 
and use the serialize() method in this class to convert a given output to a JSON string
that can be displayed by the server.
- serialize(Map<String, Object> results): contains a generic JSON adapter that converts a
Map<String, Object> to a JSON string to be displayed by the server.

#### APIUtilities:
- This is a Utility class that helps extract a response from a given URL that specifies the
a given API datasource. To a add a new API datasource, this utility class can be used to
extract the responses from a given URL.
- getURLResponse(String url): This takes in a string URL that represents a given API 
datasource and returns the response in the form of a HttpResponse<String>.

#### Project Specific Classes:
- loadCSVHandler: loads the parsed data from a given filepath 
- getCSVHandler: returns the parsed data in a form that is displayed by the API server
- WeatherHandler: returns the current temperature by extracting responses from the 
National Weather Service API.

--------------------------------------------------------------------------------
# ERROR/BUGS

No known errors/bugs

--------------------------------------------------------------------------------
# TESTS

To run, run mvn test in the terminal to run Integration and JUnit tests in the test directory.


## Unit and Integration Tests

#### TestCSVAPIHandlers: tests the CSVHandler classes loadCSVHandler and getCSVHandler
- setup_before_everything: declares a spark port
- setup: establishes connection with the spark server before every test
- teardown: tells spark to stop listening after each test suite
- tryLoadRequest: helper to start a connection to the loadcsv endpoint, providing the necessary query parameter
- tryBadLoadRequest: helper to start a connection to the loadcsv endpoint, does not provide the necessary query parameter
- tryGetRequest: helper to start a connection to the getcsv endpoint
- testCSVLoadSuccessResponse: tests for server response when a successful loadcsv request is made 
- testCSVInvalidFileResponse: tests for server response when an unsuccessful loadcsv request is made by providing an invalid filepath
- testCSVInvalidParam: tests for server response when an unsuccessful loadcsv request is made by not providing a filepath
- testCSVNoFileFailureResponse: tests for server response when a getcsv request is made without loading a csv file prior
- testCSVGetSuccessResponse: tests for server response when a getcsv request is made when a csv file is successfully loaded prior

#### TestWeatherAPIHandler: tests the handler for the WeatherHandler class
- setup_before_everything: declares a spark port
- setup: establishes connection with the spark server before every test
- teardown: tells spark to stop listening after each test suite
- tryRequest: helper to start a connection to the weather endpoint, providing the necessary query parameters
- tryBadJsonRequest: helper to start a connection to the weather endpoint, does not provide the necessary query parameters
- testWeatherResponse: tests for server response when a successful weather request is made 
- testWeatherInvalidParam: tests for server response when an unsuccessful weater request is made by providing invalid query parameters
- testWeatherBadJsonResponse: tests for server response when an unsuccessful weater request is made by not providing the necessary query parameters at all

#### TestWeatherAPIUtilites: tests the utilities for the WeatherHandler class
- testRetrieveGridURL: demonstrates a successful retrieval of the forecast URL when valid parameters are provided as well as one that would throw an Exception when invalid parameters are provided
- testRetrieveForecast: demonstrates a successful retrieval of the temperature when a valid forecast URL is provided as well as one that would throw an Exception when an invalid forecast URL is provided

## REFERNCES: 
- We used the gear up code stencil
- We also used: https://www.baeldung.com/java-json-moshi to gain a better understanding
of how Moshi works
- We also looked at the code for serialization from the repository of:
- https://github.com/cs0320-f2022/sprint-2-sljung-sljung1/blob/916ec0beab024bb284535a8dc634780dacabf4f9/src/main/java/handlers/HandlerUtilities.java
