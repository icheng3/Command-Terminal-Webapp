
import { read, Stats } from 'fs';
import React, { useState, Dispatch, SetStateAction, useEffect } from 'react';
import internal from 'stream';
import { ERROR_getCSV, ERROR_weather, ERROR_stats } from '../Fetch';

/* This file is constructed to mock Fetch functions for unit testing purposes,
 * hence why it is in the __mocks__ folder */
// Source: https://www.leighhalliday.com/mock-fetch-jest 

// keep track of last csv parsed to maintain proper stats functionality
let csvStats: Number[];
let csvLoaded: boolean;
// make some fake stats
csvStats = [1, 2];
csvLoaded = true

/**
 * A mock function for getCSV that allows us to get results for unit testing
 * @param submittedInput - hypothetical input of user
 * @returns - a validation or error message, depending on validity of input
 */
async function getCSV(submittedInput: string[]): Promise<string> {
    
    // mocking fetch request for getCSV
    const csvContents: Promise<string> = new Promise((resolve, reject) => {
    
        if (submittedInput.includes("badcsv")) {
            const error = ERROR_getCSV;
            reject(ERROR_getCSV);
            return error;
        }
        resolve("getcsv worked!")
        return "getcsv worked!"
    })

    return csvContents 
}

/**
 * A mock function for weather that allows us to get results for unit testing
 * @param submittedInput - hypothetical input of user
 * @returns - a validation or error message, depending on validity of input
 */
async function getWeather(submittedInput: string[]): Promise<string> {

    // mocking fetch request for weather
    const weatherContents: Promise<string> = new Promise((resolve, reject) => {
        if (submittedInput.length !== 2) {
            const error = new Error("fetch request for weather failed")
            reject(error);
            return error;
        }
        resolve("weather worked!")
        return "weather worked!"
        
    })

    return weatherContents
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


export {getWeather, getCSV, getStats} 
  

