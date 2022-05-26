import AuthContext from "../AuthContext";
import Errors from "./Errors";

import { useState, useContext } from "react";
import { useHistory } from "react-router-dom";

function AddCampaign({handleAdd, handleCancel}) {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [type, setType] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [sessionCount, setSessionCount] = useState('');
    const [maxPlayers, setMaxPlayers] = useState('');

    const [errors, setErrors] = useState([]);
    const history = useHistory();
    const authManager = useContext(AuthContext);


    const handleName = (event) => {
        setName(event.target.value);
      } 

      
    const handleDescription = (event) => {
        setDescription(event.target.value);
      } 

      
    const handleType = (event) => {
        setType(event.target.value);
      } 

      
    const handleCity = (event) => {
        setCity(event.target.value);
      } 

      
    const handleState = (event) => {
        setState(event.target.value);
      } 

      
    const handleSessionCount = (event) => {
        setSessionCount(event.target.value);
      } 

      
    const handleMaxPlayers = (event) => {
        setMaxPlayers(event.target.value);
      } 

    
      const handleSubmit = (event) => {
          event.preventDefault();

          const newCampaign = {
              name,
              description,
              type,
              city,
              state,
              sessionCount,
              maxPlayers
          };

          const init = {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
            },
            body: JSON.stringify(newCampaign)
          };

          fetch('http://localhost:8080/api/campaign', init)
      .then(response => {
        switch (response.status) {
          case 201:
          case 400:
            return response.json();
          case 403:
            authManager.logout();
            history.push('/login');
            break;
          default:
            return Promise.reject('Something went wrong on the server :)');
        }
      })
      .then(json => {
       
        if (json.campaignId) {
          
          history.push('/campaign');
        } else {
          
          setErrors(json);
        }
      })
      .catch(err => console.error(err));

      }


      return (
        <>
          <h2 className="mt-5">Add Campaign</h2>
          <form>
            <div className="form-group">
              <label htmlFor="name">Name:</label>
               <input className="form-control" type="text" id="name" name="name" value={name} onChange={handleName} ></input>
              
               <label htmlFor="description">Description:</label>
              <input className="form-control" type="text" id="description" name="description" value={description} onChange={handleDescription} ></input>
              
              <label htmlFor="type">Type:</label>
              <input className="form-control" type="text" id="type" name="type" value={type} onChange={handleType} ></input>
              
              <label htmlFor="city">City:</label>
              <input className="form-control" type="text" id="city" name="city" value={city} onChange={handleCity} ></input>
              
              <label htmlFor="state">State:</label>
              <input className="form-control" type="text" id="state" name="state" value={state} onChange={handleState} ></input>

              <label htmlFor="sessionCount">Session Count:</label>
              <input className="form-control" type="number" id="sessionCount" name="sessionCount" value={sessionCount} onChange={handleSessionCount} ></input>

              <label htmlFor="maxPlayers">Max Players:</label>
              <input className="form-control" type="number" id="maxPlayers" name="maxPlayers" value={maxPlayers} onChange={handleMaxPlayers} ></input>
            </div>
            <div className="form-group">
              <button className="btn btn-primary" type="submit" onClick={handleSubmit}>Add Campaign</button>
              &nbsp;
              <button className="btn btn-secondary" type="button" onClick={() => history.push('/campaign')}>Cancel</button>
            </div>
          </form>
          <Errors errors={errors}/>
        </>
      )
}
    
    export default AddCampaign;