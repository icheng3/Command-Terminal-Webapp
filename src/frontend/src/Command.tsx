
import { read } from 'fs';
import './ReplHistory.css';
import React, { useState, Dispatch, SetStateAction, useEffect } from 'react';
import {ERROR_unregistered_command, REPL} from './REPL'

export const TEXT_submit_button_accessible_name = 'submit your command'
export const TEXT_input_box_accessible_name = 'enter your command here'
export const TEXT_submit_button_text =  'Submit!'
export const TEXT_empty_input_notif = 'Please provide a command!'
export const TEXT_place_holder = 'Enter command here!'
export const ID_input_box = 'input-box'
export const ERROR_output_message = 'See error message below!'
export const RESULT_input_output_accessible_name = 'Command and output'
export const REPLHISTORY_accessible_name = 'Your commands and output results are printed here'


/**
 * 
 */
interface NewCommandProps {
  value: string
  setValue: Dispatch<SetStateAction<string>>
  addCommand: (command: string[]) => any
  setNotification: Dispatch<SetStateAction<string>>
  repl: REPL
}

/**
 * handles commands, calls on the REPL object to do so
 * @param param0 
 */
function handleCommand({value, setValue, addCommand, setNotification, repl}: NewCommandProps) {
  const maybeOutput = repl.handleInput(value) // returns a promise 
  maybeOutput.then(r => {
    addCommand([value, r])
  })  // entering possible errors --> design choice, do we want to still display the command but with an empty output and then print the error message 
    .catch(e => {
        addCommand([value, e.toString()])
      })
  setValue('')
  setNotification('')
}



/**
 * Adds a new entry to the repl history
 * @param param0 
 * @returns 
 */
function ReplHistory({command} : {command: string[]}) {
  return (
    <div className = "command-and-output"
    aria-label= {RESULT_input_output_accessible_name}>
      Command: {command[0]}<p>
        Output: {command[1]}</p>
    </div>
  )
}

/**
 * Updates input box as well as button
 * @param param0 
 * @returns 
 */
function CommandLine({value, setValue, addCommand, setNotification, repl}: NewCommandProps) {
  function handleKey(event: React.KeyboardEvent<HTMLInputElement>) {
    if (event.key === "Enter") {
      if (value.length > 0) {
        handleCommand({value, setValue, addCommand, setNotification, repl})
      } else {
        setNotification(TEXT_empty_input_notif)
      }
    } 
  }

  function handleClick() {
    if(value.length > 0) {
      handleCommand({value, setValue, addCommand, setNotification, repl})
    } else {
      setNotification(TEXT_empty_input_notif)
    }
  }
   // possible loading setting where we disable the button while we are retrieving output?
  return (
    <div className="new-command">
      <div className="command-current"> 
        <input
          value={value}
          id={ID_input_box}
          placeholder= {TEXT_place_holder}
          onChange={(e) => setValue(e.target.value)}
          onKeyDown={handleKey}
          aria-label={TEXT_input_box_accessible_name}
        ></input>
      </div>
      <div>
        <button onClick={handleClick}
          aria-label={TEXT_submit_button_accessible_name} id = "button">
          {TEXT_submit_button_text}
        </button>
      </div>
    </div>
  )
}



/**
 * Function that is executed when App is run
 * Responsible for updating the input box and repl history
 * @param param0 - a REPL object
 * @returns an updated CommandWindow
 */
export default function CommandWindow({repl}:{repl:REPL}) {
  // the commands --> main thing that is being processed and 
  // where first entry is the command and second entry is the result
  const [commands, setCommands] = useState<string[][]>([]);
  // message that gets updated if user tries to input an empty command
  const [notification, setNotification] = useState('');
  const [input, setInput] = useState<string>('')
  
  function addToHistory(command: string[]) {
    const newInputs = commands.slice();
    newInputs.push(command)
    setCommands(newInputs)
  }
  return (
    <div className="App"> 
    <div className="placeholder">
      <div className= "repl-history"
      aria-label = {REPLHISTORY_accessible_name}>
        {commands.map( (command,commandNumber) => 
          <ReplHistory           
            command={command} 
            key={commandNumber}  
            />)}
      </div>
      </div>
      <CommandLine 
        value={input}
        setValue={setInput}
        addCommand={addToHistory}
        setNotification={setNotification}
        repl={repl}/>
      {notification}   
    </div>
  );
}
