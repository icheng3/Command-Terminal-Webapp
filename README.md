# PROJECT DETAILS
Project Name: "Sprint 3: Command Terminal Webapp"
Project Description: This is a webapp that integrates front and back-end components previously developed in sprint 1 and 2, respectively, into a command terminal webapp where alll user-interface components are powered by React. Three commands are provided upon launch: get, stats, and weather. In listed order, these commands retrieve and display the contents of a CSV file, provide the number of rows and columns in a previously retrieved CSV file, and provides the current temperature of a location specified by its latitudal and longitudinal coordinates. Developers can register new commands by instantiating a new REPL object and updating the Commands hashmap field with the necessary key-value pairs. 

To run: npm install and then npm start

Team Member(s): 
-Angela Yeung (ayeung9), Iris Cheng (icheng3)

Total time: 16 hours 

Repository Link: https://github.com/cs0320-f2022/sprint-3-ayeung9-icheng3.git

--------------------------------------------------------------------------------
# DESIGN CHOICES

To allow for extensibility of our webapp, we created a REPLFunction interface that takes in an array of strings and returns a Promise of subtype string. This interface also contains a REPL class that has a Commands field which corresponds to a hashmap that maps commands to a REPLFunction interface type. 

#### App component:
- This is the main component of our webapp that renders the overall webpage and executes the Command subtype component. Note that the Command component must be given an object of REPL type in order to register and execute various commands. 
  
#### Command Component:
- This is the secondary component of our webapp that renders the input box, submit button, and displayed command/output history.
- Changes in each element are dynamically handled through React using useState, Dispatch, and SetStateAction.
- A NewCommandProps interface allows for convenient wrapping of fields required to update the DOM upon a new command submission.

#### Fetching functions::
- All functions implement the REPLFunction interface, thereby taking an array of strings as input and outputting a Promise of string subtype,
- Two functions - getWeather and getCSV- involve fetching contents from an API endpoint and are therefore asynchronous.
- One function - getStats- does not involve fetching, instead accessing the number of rows and columns of a previously outputted CSV file through a shared state in the Fetch.tsx file. 

#### REPLFunction Interface:
- This interface allows for extensibility of our webapp. It dictates that a function that implements said interface is to take an input of a string array and output a Promise. 
- REPL class: contains a field called Commands that allows for the registering of new command.
- registerCommand(): registers new commands by updating the Commands field
-handleInput(): returns the output of a submitted command as a Promise.

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
- testCSVInvalidParam: tests for server response when an unsuccessful loadcsv request is made by not providing a filepath- 
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

#### App.test.tsx: tests whether instructions are rendered by checking the screen to see whether appropriate prompt is displayed

#### Command.test.tsx: tests the effects of commands in relation to the REPL. 
- We made a `__mocks__` folder within `src` that contains a "mock" copy of Fetch.tsx with which facilitates testing without having to make calls to our API.
- Rendering: 
  - tests proper display of the submit button and text input field in the REPL 
- Valid inputs: 
  - `entering valid getcsv`: demonstrates that a successful notification should be displayed when user enters `"get"` command with valid CSV filepath
  - `entering valid getcsv with stats`: demonstrates that our `stats` function works in tandem with `get` command
  - `entering valid stats`: demonstrates that our `stats` function works on its own without calling getcsv
  - `entering valid weather coordinates`: demonstrates `getWeather` functionality without having to make an API call to the backend
- Invalid inputs:
  - `inputting only enter key`: demonstrates that appropriate prompt to "enter a command" updates on the page
  - `clicking submit button before typing in text box`: demonstrates that appropriate prompt to "enter a command" updates on the page
  - `inputting only get without filepath`: demonstrates that appropriate error message updates on the page showing that no file was found
  - `entering wrong filename for csv`: demonstrates that appropriate error message updates on the page showing that no file was found
  - `entering stats without getcsv gives error`: demonstrates that appropriate error message updates on the page showing that no file was found
  - `entering extra word after stats gives error`: demonstrates that appropriate error message updates on the page showing that no file was found
  - `entering unsupported weather coordinates`: demonstrates that appropriate error message updates on the page showing that the weather for that location could not be retrieved
  - `entering less than two coordinates for weather`: demonstrates that appropriate prompt to enter the right number of coordinates updates on the page
  - `entering more than two coordinates for weather`: demonstrates that appropriate prompt to enter the right number of coordinates updates on the page
  - `entering unsupported command/command with improper syntax`: demonstrates that appropriate prompt to enter a supported command updates on the page
--------------------------------------------------------------------------------
# REFERENCES
- We used the below repo to help develop an implementation strategy for asynchronous fetching of API endpoint content.
- https://github.com/cs0320-f2022/sprint-3-snallama-solaiya
- We also used Tim's ReactNYT example and java repl live code example to help develop a strategy for transitioning to React and applying our REPLFunction interface.
- https://github.com/cs0320-f2022/class-livecode/tree/main/F22/reactNYT
- https://github.com/cs0320/class-livecode/tree/main/F22/java_repl_sketch
