
import { read, Stats } from 'fs';
import internal from 'stream';
import { ERROR_unregistered_command } from './REPL';

export const ERROR_getCSV = "Sorry, we couldn't find your file!"
export const ERROR_weather = "Sorry, we couldn't get the weather for that location!"
export const ERROR_stats = "Sorry, we couldn't get the stats, no CSV found!"
export const ERROR_weather_coordinates = "Sorry, too many coordinates entered!"
export const ERROR_weather_coordinates_not_enough = "Sorry, not enough coordinates entered!"
const load_endpoint = "http://localhost:3000/loadcsv?filepath="
const get_endpoint = "http://localhost:3000/getcsv"

// keep track of last csv parsed to maintain proper stats functionality
let csvStats: Number[];
let csvLoaded: boolean;
csvStats = []
csvLoaded = false


/**
* Allows fetching of the getCSV API, async bc of fetching
* @param submittedInput - the array, originally processed as command (in Command,) only contains
* input that follows the command
* @returns the contents of the CSV file
*/
async function getCSV(submittedInput: string[]): Promise<string> {
    csvStats = []
    csvLoaded = false
    const csvFilePath = load_endpoint + submittedInput[0];
    const retrieveCSV: Promise<string> =
       fetch(csvFilePath)
       .then((res) => res.json())
       .then((res) => {
        const result = res.result
        if (result == "success") {
            csvLoaded = true
            return retrieveContents()
        } else {
            return Promise.reject(ERROR_getCSV)
        }
   })
   return retrieveCSV
}

async function retrieveContents(): Promise<string> {
    const csvContents: Promise<string> = fetch(get_endpoint)
       .then((res) => res.json())
       .then((json) => {
           csvStats = []
           csvStats.push(json.rowCount)
           csvStats.push(json.colCount)
           var csvResults = ""
           json.data.forEach(function (row: string[]) {
            csvResults = csvResults + row.join(", ")
           })
           return csvResults
       })
       return csvContents
}

 
// }
/**
* Allows fetching of the getWeather API
* @param lat - the latitude of the location
* @param lon - the longitude of the location
* @returns an appropriate response to the request
*/
async function getWeather(submittedInput: string[]){
    if (typeof submittedInput === 'undefined' || submittedInput.length > 2) {
        return Promise.reject(ERROR_weather_coordinates)
    } else if (submittedInput.length == 1 || submittedInput.length == 0) {
        return Promise.reject(ERROR_weather_coordinates_not_enough)
    } else {
   let lat = submittedInput[0]
   let lon = submittedInput[1]
   const retrieveWeather = `http://localhost:3000/weather?lat=${lat}&lon=${lon}`;
   const weather: Promise<string> =
       fetch(retrieveWeather)
       .then((res) => res.json())
       .then((res) => {
           if (res.result === 'success') {
               return "It's " + res.temperature.toString() + " degrees Farenheit right now!"
           } else {
               return Promise.reject(ERROR_weather)
           }
       })
   return weather
    }
}
 
/**
* this also has to return a promise of type string, but not fetching so not async
*/
function getStats(submittedInput: string[], csvStatsOverride?: Number[], isCSVLoaded?: boolean): Promise<string> {
    const stats = new Promise<string>( (resolve, reject) => {
        // for testing purposes 
        csvStats = csvStatsOverride ? csvStatsOverride : csvStats
        csvLoaded = isCSVLoaded ? isCSVLoaded : csvLoaded

        const statsLength = csvStats.length

        if (statsLength === 0 || csvLoaded === false || submittedInput.length < 1) {
                return reject(ERROR_stats)
        } else {
                const rows = csvStats[0]
                const cols = csvStats[1]
                if (!rows || !cols) {
                    reject(ERROR_stats)
                } else {
                    resolve("Your CSV has " + rows + " rows and " + cols + " columns!")
                }
            }
        })
        return stats
}
 
async function getJokes() {
   const csvAPI = "https://api.chucknorris.io/jokes/random";
   const retrieveCSV: Promise<string> =  
       fetch(csvAPI)
       .then((res) => res.json())
       .then((res) => {
           return res.value
       })
   return retrieveCSV
}
 
async function getCatFacts() {
   const csvAPI = "https://catfact.ninja/fact?max_length=140";
   const retrieveCSV: Promise<string> =  
       fetch(csvAPI)
       .then((res) => res.json())
       .then((res) => {
           return res.fact
       })
   return retrieveCSV
}
 
export {getJokes, getWeather, getCSV, getStats, getCatFacts}
 

