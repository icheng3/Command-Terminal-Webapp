import React from 'react';
import {render, screen, waitFor, act} from '@testing-library/react';
import Command, {TEXT_submit_button_accessible_name, TEXT_input_box_accessible_name,
                 TEXT_submit_button_text, TEXT_empty_input_notif, TEXT_place_holder,
                ID_input_box, ERROR_output_message, RESULT_input_output_accessible_name, 
                REPLHISTORY_accessible_name, } from './Command';
import userEvent from '@testing-library/user-event';
// import { UserEvent } from '@testing-library/user-event/dist/types/setup/setup';
import {REPL, ERROR_unregistered_command} from './REPL';
import {ERROR_getCSV, ERROR_weather, ERROR_stats, getStats, 
    ERROR_weather_coordinates_not_enough, ERROR_weather_coordinates} from './Fetch';


const replObject: REPL = new REPL();

/**
 * Note that the effects of REPL are also tested within this test file
 */

describe('Command', () => {
    beforeEach(() => {
        render(<Command repl={replObject}/>);
    })

    test('renders submit button', () => {
        const buttonElement = screen.getByText(new RegExp(TEXT_submit_button_text));
        expect(buttonElement).toBeInTheDocument();
    })
    
    test('renders repl input field', () => {
        // using accessibility tags
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name})
        expect(input).toBeInTheDocument()
    })
    
    /* Valid inputs */
    // testing whether csv file contents are updated on page
    test('entering valid getcsv', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name})
        // simulating user input
        input.focus();
        userEvent.keyboard("get csv1");
        userEvent.keyboard("{enter}");
        // output should appear (UI should be updated accordingly)
        // a success notification should be displayed
        waitFor(() => {
            const output = screen.getByText(/getcsv worked!/i)
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })

    test('entering valid getcsv with stats', async () => {
        // executing get command
        const myCommand = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name})
        userEvent.type(myCommand, "get data/fruits.csv")
        userEvent.keyboard("{enter}")
        waitFor(() => {
            const output = screen.getByText(/getcsv worked!/i)
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
        // calling stats
        userEvent.type(myCommand, 'stats')
        userEvent.keyboard("{enter}")
        const response = await getStats(["1", "2"], [1, 2], true)
        waitFor(() => {
            const outputStats = screen.getByText(/Your CSV has 1 rows and 2 columns!/i)
            expect(outputStats).toBeInTheDocument()
        }, {timeout:1000})
    })

    test('testing load state', async () => {
        // executing our first get command
        const myCommand = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name})
        userEvent.type(myCommand, "get data/fruits.csv")
        userEvent.keyboard("{enter}")
        waitFor(() => {
            const output = screen.getByText(/getcsv worked!/i)
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
        // executing second get command 
        userEvent.type(myCommand, "get data/oneLine.csv")
        userEvent.keyboard("{enter}")
        waitFor(() => {
            const output = screen.getByText(/getcsv worked!/i)
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
        // calling stats - should output that of second get command
        // in this case we are mocking the stats function by the second get command
        const response = await getStats(["1", "5"], [1, 5], true) 
        waitFor(() => {
            const outputStats = screen.getByText(/Your CSV has 1 rows and 2 columns!/i)
            expect(outputStats).toBeInTheDocument()
        }, {timeout:1000})
    })
    

    test('entering valid stats', async () => {
        const response = await getStats(["1", "2"], [1, 2], true)
        waitFor(() => {
            const outputStats = screen.getByText(/Your CSV has 1 rows and 2 columns!/i)
            expect(outputStats).toBeInTheDocument()
        }, {timeout:1000})
    })

    test('entering valid weather coordinates', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name})
        // simulating user input by enter key
        input.focus();
        userEvent.keyboard("weather 41 -81");
        userEvent.keyboard("{enter}");
        // output should appear (UI should be updated accordingly)
        // wait for state to load
        waitFor(() => {
            const output = screen.getByText(/weather worked!/i)
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })
    
    /* Invalid inputs */
    test('inputting only enter key', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input as enter
        input.focus();
        userEvent.keyboard("{enter}");
        // prompt to add input should appear
        const prompt = screen.getByText(new RegExp(TEXT_empty_input_notif));
        expect(prompt).toBeInTheDocument();
    })
    
    test('clicking submit button before typing in text box', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        const buttonElement = screen.getByText(new RegExp(TEXT_submit_button_text));
        // simulating user input click button
        input.focus();
        userEvent.click(buttonElement);
        // prompt to add input should appear
        const prompt = screen.getByText(new RegExp(TEXT_empty_input_notif));
        expect(prompt).toBeInTheDocument();
    })

    test('inputting only get without filepath', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        const buttonElement = screen.getByText(new RegExp(TEXT_submit_button_text));
        // simulating user input click button
        input.focus();
        userEvent.keyboard("get");
        userEvent.click(buttonElement);
        // output - wait for state to load
        waitFor(() => {
            const response = screen.findByText(/Sorry, we couldn't find your file!/i);
            expect(response).toBeInTheDocument(); 
        }, {timeout:1000})
    })

    test('entering wrong filename for getcsv', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input
        input.focus();
        userEvent.keyboard("get badcsv");
        userEvent.keyboard("{enter}");
        // Waiting for the state to load
        waitFor(() => {
            const output = screen.getByRole(/.*/, {name: ERROR_getCSV});
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })

    test('entering stats without getcsv gives error', async () => {
        // here we are assuming that no CSV has been loaded
        try {
            const response = await getStats([], [], false)
        } catch (e) {
            expect(e).toBe("Sorry, we couldn't get the stats, no CSV found!")
        } 
    })
    
    test('entering extra word after stats gives error', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input
        input.focus();
        userEvent.keyboard("stats hello");
        userEvent.keyboard("{enter}");
        // Waiting for the state to load
        waitFor(() => {
            const output = screen.getByRole(/.*/, {name: ERROR_stats});
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })
    
    test('entering unsupported weather coordinates', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input
        input.focus();
        userEvent.keyboard("weather");
        userEvent.keyboard("4112312");
        userEvent.keyboard("4112313");
        // output should appear 
        waitFor(() => {
            const output = screen.getByText(new RegExp(ERROR_weather));
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })
    
    test('entering less than two coordinates for weather', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input
        input.focus();
        userEvent.keyboard("weather");
        userEvent.keyboard("41");
        // output should appear 
        waitFor(() => {
            const output = screen.getByText(new RegExp(ERROR_weather_coordinates_not_enough));
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })

    test('entering more than two coordinates for weather', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        // simulating user input
        input.focus();
        userEvent.keyboard("weather");
        userEvent.keyboard("41");
        userEvent.keyboard("-81");
        userEvent.keyboard("21");
        // output should appear 
        waitFor(() => {
            const output = screen.getByText(new RegExp(ERROR_weather_coordinates));
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })

    test('entering unsupported command/command with improper syntax', () => {
        const input = screen.getByRole("textbox", {name: TEXT_input_box_accessible_name});
        input.focus();
        // simulating user input
        userEvent.keyboard("getcsvfilepath");
        userEvent.keyboard("{enter}");
        const error = {ERROR_unregistered_command}
        // Waiting for the state to load
        waitFor(() => {
            const output = screen.getByText(new RegExp(ERROR_unregistered_command));
            expect(output).toBeInTheDocument();
        }, {timeout:1000})
    })
})





