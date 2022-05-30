import HostedCampaignList from "./HostedCampaignList";
import JoinedCampaignList from "./JoinedCampaignList";
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

  return (
    <>
      <div>
        <HostedCampaignList />
        <JoinedCampaignList />
      </div>
    </>
  )
}

export default Campaigns;