import { useContext } from "react";
import { Link, useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function NavBar() {

  const authManager = useContext(AuthContext);
  const history = useHistory();

  const handleLogout = () => {
    authManager.logout();
    history.push("/");
  }

  return (
    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
      <span className="navbar-brand" >TableTop Buddy App</span>
      <div className="navbar-collapse">
        <ul className="navbar-nav mr-auto">
          <li className="nav-item">
            <Link className="nav-link" to="/">Home</Link>
          </li>
          <li className="nav-item" >
            <Link className="nav-link" to="/about">About</Link>
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/campaign">Campaigns</Link>            {/*double check later*/}
          </li>
          <li className="nav-item">
            <Link className="nav-link" to="/userSchedule">Schedule</Link>              {/*double check later*/}
          </li>
          {authManager.hasRole('admin') ? (
              <li className="nav-item">
                <Link className="nav-link" to="/user">Delete User</Link>
              </li>
            ) 
            : null}
          {authManager.user ? (
            <li className="nav-item">
              <button className="btn btn-secondary" type="button" onClick={handleLogout}>Logout</button>
            </li>
          )

            : (
              <>
                <li className="nav-item">
                  <Link className="nav-link" to="/login">Login</Link>
                </li>
                <li className="nav-item">
                  <Link className="nav-link" to="/register">Register</Link>
                </li>
            
              </>
            )}
            


        </ul>
      </div>
      {authManager.user ? <span className="text-light navbar-text">Hello, {authManager.user}</span> : null}
    </nav>
  );
}

export default NavBar;