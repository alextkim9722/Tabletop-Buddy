import NavBar from "./components/NavBar";
import CampaignList from "./components/CampaignList";
import AddCampaign from "./components/AddCampaign";
import DeleteCampaign from "./components/DeleteCampaign";
import CampaignDetailed from "./components/CampaignDetailed";
import DeleteUser from "./components/DeleteUser";
import Login from "./components/Login";
import NotFound from "./components/NotFound";
import UpdateCampaign from "./components/UpdateCampaign";
import UserScheduleList from "./components/UserScheduleList";
import AuthContext from "./AuthContext";

import { Route, Switch } from "react-router-dom";
import { useEffect, useState } from "react";
import { Redirect } from "react-router-dom";
import jwtDecode from "jwt-decode";

const TOKEN = 'jwt_token';

function App() {
  const [init, setInit] = useState(false);
  const [authManager, setAuthManager] = useState({
    user: null,
    userId: null,
    state: null,
    city: null,
    description: null,
    roles: null,
    login(token) {
      if (!this.user) {
        const userData = jwtDecode(token);
        localStorage.setItem(TOKEN, token);
        setAuthManager((prevState) => ({...prevState, user: userData.sub, userId: userData.userId, state: userData.state, city: userData.city, description: userData.description, roles: userData.authorities}));
      }
    },
    logout() {
      if (this.user) {
        localStorage.removeItem(TOKEN);
        setAuthManager((prevState) => ({...prevState, user: null, userId: null, state: null, city: null, description: null, roles: null}));
      }
    },
    hasRole(role) {
      return this.roles && this.roles.split(',').includes(`ROLE_${role.toUpperCase()}`);
    }
  });

  useEffect(() => {
    const token = localStorage.getItem(TOKEN);
    if (token) {
      authManager.login(token);
    }
    setInit(true);
  }, []);

  return (
    <div className="App">
      {init ? 
      (<AuthContext.Provider value={authManager} >
        <NavBar />
        <div className="container">
          <Switch>
            <Route exact path="/">
              <Home />
            </Route>
            <Route path="/about">
              <About />
            </Route>
            <Route exact path="/campaign" >
              <CampaignList />
            </Route>
            <Route path="/campaign/add" >
              {authManager.user ? <AddCampaign /> : <Redirect to="/login" /> }
            </Route>
            <Route path="/campaign/view/:campaignId" >
              <CampaignDetailed />
            </Route>
            <Route path="/campaign/edit/:campaignId" >
              {authManager.user ? <UpdateCampaign />  : <Redirect to="/login" /> }
            </Route>
            <Route path="/campaign/delete/:campaignId" >
              {authManager.user ? <DeleteCampaign />  : <Redirect to="/login" /> }
            </Route>
            <Route path="/login" >
              {authManager.user ? <Redirect to="/" /> : <Login />}
            </Route>
            <Route path="/register" >
              {authManager.user ? <Redirect to ="/" /> : <Registration /> }
            </Route>
            <Route path="/userSchedule" >
              {authManager.user ? <UserScheduleList /> : <Redirect to="/login" />}
            </Route>
            <Route path="/register" >
              {authManager.user ? <Redirect to="/login" /> : <Registration />}
            </Route>
            <Route path="/user" >
              {authManager.hasRole('admin') ? <DeleteUser /> : <Redirect to="/" /> }
            </Route>
            <Route>
              <NotFound />
            </Route>
          </Switch>   
        </div>
      </AuthContext.Provider>) : null}
    </div>
  );
}

function Home() {
  return (
    <>
      <h2>Home</h2>
      <div>
        <p>Welcome to TableTop Buddy App</p>
      </div>
    </>
  )
}


function About() {
  return (
    <>
      <h2>About</h2>
      <div>
        <p>About the project</p>
        <ul>
          <li>We are a group of board game enthusiasts</li>
          <li>We built this website to facilitate organization between players and people who share our interest in board games </li>
        </ul>
      </div>
    </>
  )
}

export default App;

