import { useState } from "react";
import { useHistory } from "react-router-dom";
import Errors from "./Errors";

const DEFAULT_LOGIN = {
    username: '',
    password: '',
    confirmation: '',
    city: '',
    state: '',
    description: ''
}

function Registration() {
    const [credentials, setCredentials] = useState(DEFAULT_LOGIN);
    const [errors, setErrors] = useState([]);

    const history = useHistory();

    const handleChange = (event) => {
        const replacementCredentials = { ...credentials };

        replacementCredentials[event.target.name] = event.target.value;

        setCredentials(replacementCredentials);
    }

    const handleSubmit = (event) => {
        event.preventDefault();

        if (credentials.password !== credentials.confirmation) {
            setErrors(['Password and Confirmation Password do not match']);
            return;
        }

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        }

        fetch('http://localhost:8080/api/create_account', init)
            .then(resp => {
                if (resp.status === 201) {
                    return null;
                }

                if (resp.status === 400) {
                    return resp.json();
                }

                return Promise.reject('Something went wrong on the server :)');
            })
            .then(json => {
                if (json) {
                    setErrors(json)
                } else {
                    history.push('/login');
                }


            })
            .catch(err => console.error(err));
    }
    return (
        <>
            <h2>Registration</h2>
            <form>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input className="form-control" type="text" id="username" name="username" value={credentials.username} onChange={handleChange} ></input>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input className="form-control" type="password" id="password" name="password" value={credentials.password} onChange={handleChange} ></input>
                </div>
                <div className="form-group">
                    <label htmlFor="confirmation">Confirmation Password:</label>
                    <input className="form-control" type="password" id="confirmation" name="confirmation" value={credentials.confirmation} onChange={handleChange} ></input>
                </div>
                <div className="form-group">
                    <label htmlFor="city">City:</label>
                    <input className="form-control" type="text" id="city" name="city" value={credentials.city} onChange={handleChange} ></input>
                </div>
                <div className="form-group">
                    <label htmlFor="state">State:</label>
                    <input className="form-control" type="text" id="state" name="state" value={credentials.state} onChange={handleChange} ></input>
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description:</label>
                    <textarea className="form-control" id="description" name="description" placeholder="Enter a brief description of yourself" value={credentials.description} onChange={handleChange} ></textarea>
                </div>
                <div className="form-group">
                    <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Register</button>
                </div>
            </form>
            <Errors errors={errors}/>
        </>
    )
}

export default Registration; 