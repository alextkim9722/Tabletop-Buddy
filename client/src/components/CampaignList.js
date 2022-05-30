import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function Campaigns() {

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

    const handleViewSelect = (campaign) => {
      history.push(`/campaign/campaignDetailed/${campaign.campaignId}`);
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
                 <th scope="col">Type</th>
                 <th scope="col">City</th>
                 <th scope="col">State</th>
                 <th scope="col">Next Session</th>
            </tr>
        </thead>
        <tbody>
          {campaigns.map((cmp, i) => (
            <tr key={cmp.campaignId}>
              <td>
                {authManager.userId === cmp.userId ? (<>
                  <button className="btn btn-info" type="button" onClick={() => handleEditSelect(cmp)} >Edit</button>
                  &nbsp;
                  <button className="btn btn-secondary" type="button" onClick={() => handleDeleteSelect(cmp)} >Delete</button>
                  &nbsp;
                  <button className="btn btn-light" type="button" onClick={() => handleViewSelect(cmp)} >View</button>
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
                  {cmp.type}
                </td>
                <td>
                  {cmp.city}
                </td>
                <td>
                  {cmp.state}
                </td>
                <td>
                  {cmp.nextSession}                   {/* what is the name of variable?*/}
                </td>
            </tr>

          ))}
        </tbody>
      </table>
    </>
  )
}

export default Campaigns;