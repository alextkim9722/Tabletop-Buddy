import { useHistory, useParams } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import AuthContext from '../AuthContext';

function DeleteCampaign({campaign}) {
    const [name, setName] = useState('');
    const [type, setType] = useState('');

    const [init, setInit] = useState(false);
    const history = useHistory();
    const { campaignId } = useParams();
    const authManager = useContext(AuthContext);


    useEffect(() => {
        fetch(`http://localhost:8080/api/campaign/${campaignId}`)
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
              setType(body.type);
            }
          })
    
          .catch(err => console.error(err));
      });


      const handleSubmit = () => {

        const init = {
          method: 'DELETE',
          headers: {
            Authorization: `Bearer ${localStorage.getItem('jwt_token')}`
          }
        }


        fetch(`http://localhost:8080/api/campaign/${campaignId}`, init)
      .then(response => {
        if (response.status === 204) {
          history.push('/campaign');
          return;
        } else if (response.status === 403) {
          authManager.logout();
          history.push('/login');
        }

        return Promise.reject('Something went wrong :)');
      })
      .catch(err => console.error(err));
  }


  return (
    <>
      {init ? (<>
        <h2>Delete Campaign</h2>
        <div className="alert alert-warning">
          <div>Are you sure you want to delete this campaign?</div>
          <div>Campaign: {name} {type} </div>
        </div>
        <button className="btn btn-primary" type="button" onClick={handleSubmit}>Delete</button>
        &nbsp;
        <button className="btn btn-secondary" type="button" onClick={() => history.push('/campaign')}>Cancel</button>
      </>) : null}
    </>
  );


}

export default DeleteCampaign;