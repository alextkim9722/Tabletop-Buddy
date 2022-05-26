import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function CampaignDetailed() {

    const [campaigns, setCampaigns] = useState([]);

    const history = useHistory();

    const authManager = useContext(AuthContext);
    
    let getCampaigns = () => {
        return fetch('http://localhost:8080/api/campaign')
        .then(response => {
            if (response.status ===200) {
                return response.json()
            }
            return Promise.reject('Something went wrong on the server :)');
        })
        .then(body => {
            setCampaigns(body);
          })
          .catch(err => console.error(err));
    }


    useEffect(() => {
        getCampaigns();
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
         <h2 className="mt-5">Campaign List</h2>
         {authManager.user ? <button className="btn btn-primary mb-3 mt-4" type="button" onClick={handleAddSelect}>Add Campaign</button> : null}
         <table className="table table-sm">
             <thead>
             <tr>
                 <th scope="col"></th>
                 <th scope="col">#</th>
                 <th scope="col">Name</th>
                 <th scope="col">Description</th>
                 <th scope="col">Type</th>
                 <th scope="col">City</th>
                 <th scope="col">State</th>
                 <th scope="col">Session List</th>
                 <th scope="col">Players</th>
                 <th scope="col">Game Master</th>
            </tr>
        </thead>
        <tbody>
          {campaigns.map((cmp, i) => (
            <tr key={cmp.campaignId}>
              <td>
                {authManager.user ? (<>
                  <button className="btn btn-info" type="button" onClick={() => handleEditSelect(cmp)} >Edit</button>
                  &nbsp;
                  {authManager.hasRole('admin') ? <button className="btn btn-secondary" type="button" onClick={() => handleDeleteSelect(cmp)} >Delete</button> : null }
                </>) : null}
              </td>
              <td>
                &nbsp;
                {i + 1}
              </td>
              <td>
                  {cmp.name}
                </td>
                <td>
                  {cmp.description}
                </td>
                <td>
                  {cmp.type}
                </td>
                <td>
                  {cmp.city}
                </td>
                <td>
                  {cmp.state}
                </td>
                <td>
                  {cmp.nextSession}                     {/*need to check later*/}
                </td>
                <td>
                  {cmp.players}                        {/*need to check later*/}
                </td>
                <td>
                  {cmp.gameMaster}                     {/*need to check later*/}
                </td>
            </tr>

          ))}
        </tbody>
      </table>
    </>
  )
}

export default CampaignDetailed;