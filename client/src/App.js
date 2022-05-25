import NavBar from "./components/NavBar"
import AddSession from './components/AddSession';

import React from 'react';
import { Route, Switch } from "react-router-dom";


function App() {
  return (
    <div className="App">
      <NavBar />
      <Switch>
        <Route exact path="/addsession">
          <AddSession />
        </Route>
      </Switch>
    </div>
  );
}

export default App;