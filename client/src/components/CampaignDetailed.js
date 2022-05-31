import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import { useParams } from "react-router-dom";
import AuthContext from "../AuthContext";

function CampaignDetailed() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [type, setType] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [sessionCount, setSessionCount] = useState('');
  const [playerCount, setPlayerCount] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');

  const [gameMasterName, setGameMasterName] = useState('');

  const [errors, setErrors] = useState([]);
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
          history.update('/not-found');
          return null;
        }
        return Promise.reject('Something went wrong on the server :)');
      })
      .then(body => {
        if (body) {
          setInit(true);
          setName(body.name);
          setDescription(body.description);
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
        <p>Gamemaster: {}</p>
    </>
  )
}

export default CampaignDetailed;