/**
 * A command-processor function for our REPL. The function returns a Promise   
 * which resolves to a string, which is the value to print to history when 
 * the command is done executing.
 * 
 * The arguments passed in the input (which need not be named "args") should 
 * *NOT*contain the command-name prefix. 
 */
 export const ERROR_unregistered_command = "Not a registered command, try again!"

 export interface REPLFunction {
    (submittedInput: Array<string>): Promise<string>
  }
  export class REPL {
    Commands: Map<string, REPLFunction>;
  
    constructor() {
      this.Commands = new Map<string, REPLFunction>();
    }
  
    registerCommand(commandName: string, commandFunction: REPLFunction): void {
      this.Commands.set(commandName, commandFunction)
    }
  
    handleInput(input: string): Promise<string> {
      const inputs = input.split(" ")
      const command = this.Commands.get(inputs[0])
      // this happens if the command is not in the map
      if (typeof command === "undefined") {
        return new Promise<string>((reject) => {
          (reject(ERROR_unregistered_command))
        })
      } else {
        const queryParams = inputs.slice(1)
        return command(queryParams) 
      }
    }
  }