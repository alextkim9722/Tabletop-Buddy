import { Link } from "react-router-dom";

function NavBar() {
    return (
        <nav className="navbar navbar-expand-md navbar-dark bg-dark">
            <span className="navbar-brand">Tabletop Buddy</span>
            <div className="navbar-collapse">
            <ul className="navbar-nav mr-auto">
                <li className="nav-item">
                    <Link className="nav-link" to="/addsession">Add Session</Link>
                </li>
            </ul>                
            </div>
        </nav>
    )
}

export default NavBar; 