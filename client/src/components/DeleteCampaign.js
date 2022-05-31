import { useHistory, useParams } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import AuthContext from '../AuthContext';
import Errors from "./Errors";

function DeleteCampaign() {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [type, setType] = useState('');
  const [city, setCity] = useState('');
  const [state, setState] = useState('');
  const [sessionCount, setSessionCount] = useState('');
  const [maxPlayers, setMaxPlayers] = useState('');

    const [init, setInit] = useState(false);
    const [errors, setErrors] = useState([]);
    const history = useHistory();
    const { campaignId } = useParams();
    const authManager = useContext(AuthContext);
    const [campaignUserId, setCampaignUserId] = useState(authManager.userId);

    useEffect(() => {
        fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/${campaignId}`)
          .then(response => {
            if (response.status === 200) {
              return response.json();
            }
    
            if (response.status === 404) {
              history.replace('/not-found');
              return null;
            }
    
            return Promise.reject('Something went wrong on the server :)');
          })
           .then(body => {
            if (body) {
              setInit(true);
              setName(body.name);
              if (body.description) {
                setDescription(body.description);
              }
              setType(body.type);
              setCity(body.city);
              setState(body.state);
              if (body.sessionCount) {
                setSessionCount(body.sessionCount);
              }
              setMaxPlayers(body.maxPlayers);
            }
          })
          .catch(err => console.error(err));
      });

      const campaignToDelete = {
        name,
        campaignId: campaignId,
        userId: campaignUserId,
        description,
        type,
        city,
        state,
        sessionCount,
        maxPlayers
      };

      const handleSubmit = () => {

        const deleteInit = {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
          },
          body: JSON.stringify(campaignToDelete)
        };

        fetch(`${window.TABLETOPBUDDY_ROOT_URL}/campaign/${campaignId}`, deleteInit)
      .then(response => {
        if (response.status === 204) {
          return;
        } else if (response.status === 400) {
          return response.json();
        }
        else if (response.status === 403) {
          authManager.logout();
          history.push('/login');
        }


        return Promise.reject('Something went wrong :)');
      })
      .then(body => {
        if (!body) {
          history.push('/campaign');
          return;
        }
  
        setErrors(body);
      })
      .catch(err => console.error(err));
  }


  return (
    <>
      {init ? (<>
        <h2>Delete Campaign</h2>
        <div className="alert alert-warning">
          <div>Are you sure you want to delete this campaign?</div>
          <div>Campaign: {name} </div>
        </div>
        <button className="btn btn-primary" type="button" onClick={handleSubmit}>Delete</button>
        &nbsp;
        <button className="btn btn-secondary" type="button" onClick={() => history.push('/campaign')}>Cancel</button>
      </>) : null}
      <Errors errors={errors}/>
    </>
  );


}

export default DeleteCampaign;