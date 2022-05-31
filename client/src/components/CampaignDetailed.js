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

  const handleAddSelect = () => {
      history.push(`/campaign/add`);
  }

  const handleEditSelect =(campaign) => {
      history.push(`/campaign/edit/${campaign.campaignId}`);
  }

  const handleDeleteSelect = (campaign) => {
      history.push(`/campaign/delete/${campaign.campaignId}`);
  }

  return (
    <>
      <h1>{name}</h1>
      <table className="table">
        <tbody>
          <tr>
            <th>Gamemaster</th>
            <td>{userId}</td>
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
      <SessionList campaign={campaign}/>
    </>
  )
}

export default CampaignDetailed;