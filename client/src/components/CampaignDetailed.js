import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import { useParams } from "react-router-dom";
import SessionList from "./SessionList";
import AuthContext from "../AuthContext";

function CampaignDetailed() {
  const [campaign, setCampaign] = useState('');
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [type, setType] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [sessionCount, setSessionCount] = useState('');
  const [playerCount, setPlayerCount] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');
  const [userId, setUserId] = useState('');
  const [gameMasterName, setGameMasterName] = useState('');

  const [init, setInit] = useState(false);

  const history = useHistory();

  const authManager = useContext(AuthContext);
  const { campaignId } = useParams();

  useEffect(() => {
    fetch(`http://localhost:8080/api/campaign/${campaignId}`)
    .then(response => {
      if (response.status === 200) {
        return response.json();
      }

      if (response.status === 404) {
        history.push('/not-found');
        return null;
      }
      return Promise.reject('Something went wrong on the server :)');
    })
    .then(body => {
      if (body) {
        setInit(true);
        setCampaign(body);
        setName(body.name);
        setDescription(body.description);
        setUserId(body.userId);
        setType(body.type);
        setCity(body.city);
        setState(body.state);
        setSessionCount(body.sessionCount);
        setMaxPlayers(body.maxPlayers);
        setPlayerCount(body.currentPlayers);
      }
    })
    .catch(err => console.error(err));
  }, []);

  useEffect(() => {
    let url = `http://localhost:8080/api/user/id/${userId}`;

    if(url !== `http://localhost:8080/api/user/id/`){
      fetch(url)
      .then(response => {
        if (response.status === 200) {
          return response.json();
        }

        if (response.status === 404) {
          history.push('/not-found');
          return null;
        }else if(response.status == 500) {
          return response.json();
        }
        return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        if (body) {
          setGameMasterName(body.username);
        }
      })
      .catch(err => console.error(err));
    }
  }, [userId]);

  const handleEditSelect =() => {
      history.push(`/campaign/edit/${campaign.campaignId}`);
  }

  const handleDeleteSelect = () => {
      history.push(`/campaign/delete/${campaign.campaignId}`);
  }

  return (
    <>
      <h1>{name}</h1>
      <table className="table">
        <tbody>
          <tr>
            <th>Gamemaster</th>
            <td>{gameMasterName}</td>
          </tr>
          <tr>
            <th>Type</th>
            <td>{type}</td>
          </tr>
          <tr>
            <th>City</th>
            <td>{city}</td>
          </tr>
          <tr>
            <th>State</th>
            <td>{state}</td>
          </tr>
          <tr>
            <th>Sessions</th>
            <td>{sessionCount}</td>
          </tr>
          <tr>
            <th>Players</th>
            <td>{playerCount}/{maxPlayers}</td>
          </tr>
        </tbody>
      </table>
      <p>{description}</p>
      {authManager.userId == userId ? (<>
        <button className="btn btn-info" type="button" onClick={handleEditSelect} >Edit</button>
        &nbsp;
        <button className="btn btn-secondary" type="button" onClick={handleDeleteSelect} >Delete</button>
        </>) : null}
      <SessionList campaign={campaign}/>
    </>
  )
}

export default CampaignDetailed;