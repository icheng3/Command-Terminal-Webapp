import React from 'react';

import './App.css';
import Command from './Command'
import {REPL} from './REPL'
import {getJokes, getWeather, getCSV, getStats} from './Fetch'



const replObject = new REPL();
replObject.registerCommand('chuck', getJokes)
replObject.registerCommand('get', getCSV)
replObject.registerCommand('weather', getWeather)
replObject.registerCommand('stats', getStats)

/**
 * Main component of the web app. Renders once when the app is launched.
 * @returns the Command Window app.
 */
function App() {
  return (
    <div className="App">
      <p className="App-header">
        Enter a command here to view the contents of a csv file, its stats or
        get the weather of a current location! add to this later lol
      </p>
      <Command repl={replObject}/>      
    </div>
  );
}

export default App;
