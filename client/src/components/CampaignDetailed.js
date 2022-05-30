import { useContext, useEffect, useState } from "react";
import { useHistory } from "react-router-dom";
import AuthContext from "../AuthContext";

function CampaignDetailed() {

    const [campaign, setCampaign] = useState([]);

    const history = useHistory();

    const authManager = useContext(AuthContext);
    
    let getCampaign = () => {
        return fetch('http://localhost:8080/api/campaign')
        .then(response => {
            if (response.status ===200) {
                return response.json()
            }
            return Promise.reject('Something went wrong on the server :)');
        })
        .then(body => {
            setCampaign(body);
          })
          .catch(err => console.error(err));
    }


    useEffect(() => {
        getCampaign();
      }, []);


    const handleEditSelect =(campaign) => {
        history.push(`/campaign/edit/${campaign}`);
    }

    const handleDeleteSelect = (campaign) => {
        history.push(`/campaign/delete/${campaign}`);
    }

    return (
      <>
       <h1 className="mt-4">Campaign List</h1>
          <button className="btn btn-info" type="button" onClick={handleEditSelect} >Edit</button>
          &nbsp;
          <button className="btn btn-secondary" type="button" onClick={handleDeleteSelect} >Delete</button>
       

      {campaign.campaignId}
      
      <h2> Description: {campaign.description}</h2>
      &nbsp;
      <h2> Type:  {campaign.type}</h2>
      &nbsp;
      <h2> City:  {campaign.city}</h2>
      &nbsp;
      <h2> State: {campaign.state}</h2>
      &nbsp;
      <h2> Players: {campaign.players}</h2>
      &nbsp;
      <h2> Game Master: {campaign.gameMaster}</h2>
      
  </>
)
}

export default CampaignDetailed;
   