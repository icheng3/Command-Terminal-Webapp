import React from 'react';
import { render, screen } from '@testing-library/react';
import App from './App';

describe('App', () => {
  beforeEach(() => {
    render(<App />)
  })
  test('should render instructions', () => {
    const instructionElement = screen.getByText(/Enter a command here to view the contents of a csv file, its stats or get the weather of a current location!/i);
    expect(instructionElement).toBeInTheDocument();
  });
})
